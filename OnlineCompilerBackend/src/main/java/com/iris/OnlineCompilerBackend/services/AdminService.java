package com.iris.OnlineCompilerBackend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iris.OnlineCompilerBackend.config.AESV2;
import com.iris.OnlineCompilerBackend.config.PasswordHashing;
import com.iris.OnlineCompilerBackend.dtos.AdminLoginCredsDTO;
import com.iris.OnlineCompilerBackend.dtos.request.NewAdminReqDTO;
import com.iris.OnlineCompilerBackend.dtos.request.ResetAdminPasswordReqDTO;
import com.iris.OnlineCompilerBackend.dtos.response.AdminAuthenticationResDTO;
import com.iris.OnlineCompilerBackend.dtos.response.AllAdminResDTO;
import com.iris.OnlineCompilerBackend.models.AccessTokenJSON;
import com.iris.OnlineCompilerBackend.models.Admin;
import com.iris.OnlineCompilerBackend.models.ApiResponse;
import com.iris.OnlineCompilerBackend.repositories.AdminRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {
    private static final Logger log = LoggerFactory.getLogger(AdminService.class);
    @Autowired
    private AdminRepo adminRepo;

    @Value("${password.suffix}")
    private String passwordSuffix;

    @Value("${zone.id}")
    private String timeZone;

    public ApiResponse createAdmin(String existingAdminId, NewAdminReqDTO req) {
        try {

            //if admin exists
            if (adminRepo.findByAdminId(req.getAdminId()) != null) {
                return new ApiResponse.Builder().status(false).statusMessage("ADMIN already exists!").build();
            }

            Admin admin = new Admin();
            admin.setAdminId(req.getAdminId());

            String defaultPassword = req.getAdminId() + passwordSuffix;

            String salt = PasswordHashing.getSalt();
            String hashPassword = PasswordHashing.createHash(defaultPassword, salt, Boolean.TRUE);

            admin.setSalt(salt);
            admin.setAdminPassword(hashPassword);

            admin.setFullName(req.getAdminFullName());
            admin.setIsAdmin(req.getIsAdmin());

            admin.setCreatedByFk(adminRepo.findByAdminId(existingAdminId));

            admin.setLastLogin(null);

            admin.setLastAccesstoken(null);
            admin.setAccessTokenIsExpired(true);
            admin.setAccessTokenCreatedOn(null);
            admin.setAccessTokenLastAccessedOn(null);

            adminRepo.save(admin);

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(null).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage("FAILURE").build();
        }
    }

    public ApiResponse verifyAdminCreds(AdminLoginCredsDTO adminLoginReqDto) {
        try {

            //check if admin exists
            Admin admin = adminRepo.findByAdminId(adminLoginReqDto.getAdminId());

            if (admin == null) {
                throw new Exception("ADMIN NOT FOUND!");
            }

            if (PasswordHashing.validatePassword(admin.getSalt(), admin.getAdminPassword(), PasswordHashing.createHash(adminLoginReqDto.getAdminPassword(), admin.getSalt(), Boolean.TRUE))) {
                admin.setLastLogin(new Date());

                UUID uuid = UUID.randomUUID();

                AccessTokenJSON accessTokenJSON = new AccessTokenJSON(admin.getFullName(), admin.getIsAdmin(), new Date(), uuid);

                ObjectMapper objectMapper = new ObjectMapper();

                String jsonString = objectMapper.writeValueAsString(accessTokenJSON);

                String encryptedString = AESV2.getInstance().encrypt(jsonString);

                String accessToken = PasswordHashing.createHash(encryptedString, admin.getSalt(), Boolean.TRUE);

                admin.setLastAccesstoken(accessToken);

                admin.setAccessTokenIsExpired(false);

                OffsetDateTime offsetDateTime = Instant.now().atZone(ZoneId.of(timeZone)).toOffsetDateTime();
                String formattedTimeWithOffset = offsetDateTime.toString();

                admin.setAccessTokenCreatedOn(formattedTimeWithOffset);

                adminRepo.save(admin);

                return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(new AdminAuthenticationResDTO(admin.getAdminId(), admin.getIsAdmin(), admin.getFullName(), admin.getLastAccesstoken(),admin.getLastLogin())).build();
            }

            throw new Exception("INVALID CREDENTIALS!");
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse resetAdminPassword(ResetAdminPasswordReqDTO resetAdminPasswordReqDTO) {
        try {
            //checking if new & confirm password match
            if (!(resetAdminPasswordReqDTO.getAdminNewPassword().equals(resetAdminPasswordReqDTO.getAdminNewConfirmPassword()))) {
                throw new Exception("PASSWORDS DON'T MATCH!");
            }

            //checking if admin exists
            Admin admin = adminRepo.findByAdminId(resetAdminPasswordReqDTO.getAdminId());
            if (admin == null) {
                throw new Exception("ADMIN DOESN'T EXISTS!");
            }

            //verifying if old & new passwords match
            if (!(PasswordHashing.validatePassword(admin.getSalt(), admin.getAdminPassword(), PasswordHashing.createHash(resetAdminPasswordReqDTO.getAdminOldPassword(), admin.getSalt(), Boolean.TRUE)))) {
                throw new Exception("INVALID CREDENTIALS!");
            }

            //storing new password & salt
            String newSalt = PasswordHashing.getSalt();
            String newHashPassword = PasswordHashing.createHash(resetAdminPasswordReqDTO.getAdminNewPassword(), newSalt, Boolean.TRUE);

            admin.setSalt(newSalt);
            admin.setAdminPassword(newHashPassword);
            adminRepo.save(admin);

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(null).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse fetchAllAdmins() {
        try {
            List<AllAdminResDTO> allAdmins = adminRepo.findAllAdminsDet();
            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(allAdmins).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    @Scheduled(cron = "${admin.session.scheduler.cron}")
    private void processAdminSessions() {
        try {
            List<Admin> admins = adminRepo.findAllActiveAdmins();
            for (Admin admin : admins) {
                if (admin.getAccessTokenLastAccessedOn() != null) {
                    //checking if user is idle for 15 mins from last api call
                    if (Duration.between(OffsetDateTime.parse(admin.getAccessTokenLastAccessedOn()), OffsetDateTime.now(ZoneId.of(timeZone))).toMinutes() >= 15) {
                        admin.setAccessTokenIsExpired(true);
                    }
                }
            }
            adminRepo.saveAll(admins);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
