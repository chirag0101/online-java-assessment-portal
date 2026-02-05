package com.iris.OnlineCompilerBackend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iris.OnlineCompilerBackend.config.AESV2;
import com.iris.OnlineCompilerBackend.config.PasswordHashing;
import com.iris.OnlineCompilerBackend.constants.ResponseStatus;
import com.iris.OnlineCompilerBackend.dtos.AdminLoginCredsDTO;
import com.iris.OnlineCompilerBackend.dtos.request.NewAdminReqDTO;
import com.iris.OnlineCompilerBackend.dtos.request.ResetAdminPasswordReqDTO;
import com.iris.OnlineCompilerBackend.dtos.response.AdminAuthenticationResDTO;
import com.iris.OnlineCompilerBackend.dtos.response.AllAdminResDTO;
import com.iris.OnlineCompilerBackend.exceptions.GlobalException;
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

            Admin createdByAdmin = adminRepo.findByAdminId(existingAdminId);
            if (createdByAdmin == null) {
                return new ApiResponse.Builder().status(false).statusMessage("Admin Not Found!").build();
            }
            admin.setCreatedByFk(createdByAdmin);

            admin.setLastLogin(null);

            admin.setLastAccesstoken(null);
            admin.setAccessTokenIsExpired(true);
            admin.setAccessTokenCreatedOn(null);
            admin.setAccessTokenLastAccessedOn(null);

            adminRepo.save(admin);

            return new ApiResponse.Builder().status(true).statusMessage(ResponseStatus.SUCCESS.getStatus()).response(null).build();
        } catch (Exception e) {
            log.error("Error creating admin for adminId: {}", req.getAdminId(), e);
            return new ApiResponse.Builder().status(false).statusMessage("Failed to create admin").build();
        }
    }

    public ApiResponse verifyAdminCredentials(AdminLoginCredsDTO adminLoginReqDto) {
        try {

            //check if admin exists
            Admin admin = adminRepo.findByAdminId(adminLoginReqDto.getAdminId());

            if (admin == null) {
                throw new GlobalException("ADMIN NOT FOUND!");
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

                return new ApiResponse.Builder().status(true).statusMessage(ResponseStatus.SUCCESS.getStatus()).response(new AdminAuthenticationResDTO(admin.getAdminId(), admin.getIsAdmin(), admin.getFullName(), admin.getLastAccesstoken(), admin.getLastLogin())).build();
            }

            throw new GlobalException("INVALID CREDENTIALS!");
        } catch (GlobalException e) {
            log.warn("Authentication error for adminId: {}", adminLoginReqDto.getAdminId());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        } catch (Exception e) {
            log.error("Error verifying admin credentials for adminId: {}", adminLoginReqDto.getAdminId(), e);
            return new ApiResponse.Builder().status(false).statusMessage("Authentication failed").build();
        }
    }

    public ApiResponse resetAdminPassword(ResetAdminPasswordReqDTO resetAdminPasswordReqDTO) {
        try {
            //checking if new & confirm password match
            if (!(resetAdminPasswordReqDTO.getAdminNewPassword().equals(resetAdminPasswordReqDTO.getAdminNewConfirmPassword()))) {
                throw new GlobalException("PASSWORDS DON'T MATCH!");
            }

            //checking if admin exists
            Admin admin = adminRepo.findByAdminId(resetAdminPasswordReqDTO.getAdminId());
            if (admin == null) {
                throw new GlobalException("ADMIN DOESN'T EXISTS!");
            }

            //verifying if old & new passwords match
            if (!(PasswordHashing.validatePassword(admin.getSalt(), admin.getAdminPassword(), PasswordHashing.createHash(resetAdminPasswordReqDTO.getAdminOldPassword(), admin.getSalt(), Boolean.TRUE)))) {
                throw new GlobalException("INVALID CREDENTIALS!");
            }

            //storing new password & salt
            String newSalt = PasswordHashing.getSalt();
            String newHashPassword = PasswordHashing.createHash(resetAdminPasswordReqDTO.getAdminNewPassword(), newSalt, Boolean.TRUE);

            admin.setSalt(newSalt);
            admin.setAdminPassword(newHashPassword);
            adminRepo.save(admin);

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(null).build();
        } catch (GlobalException e) {
            log.warn("Password reset validation error for adminId: {}", resetAdminPasswordReqDTO.getAdminId());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        } catch (org.springframework.dao.DataAccessException e) {
            log.error("Database error resetting password for adminId: {}", resetAdminPasswordReqDTO.getAdminId(), e);
            return new ApiResponse.Builder().status(false).statusMessage("Database operation failed").build();
        } catch (Exception e) {
            log.error("Unexpected error resetting password for adminId: {}", resetAdminPasswordReqDTO.getAdminId(), e);
            return new ApiResponse.Builder().status(false).statusMessage("Internal server error").build();
        }
    }

    public ApiResponse fetchAllAdmins() {
        try {
            List<AllAdminResDTO> allAdmins = adminRepo.findAllAdminsDet();
            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(allAdmins).build();
        } catch (org.springframework.dao.DataAccessException e) {
            log.error("Database error fetching all admins", e);
            return new ApiResponse.Builder().status(false).statusMessage("Database operation failed").build();
        } catch (Exception e) {
            log.error("Unexpected error fetching all admins", e);
            return new ApiResponse.Builder().status(false).statusMessage("Internal server error").build();
        }
    }

    @Scheduled(cron = "${admin.session.scheduler.cron}")
    private void processAdminSessions() {
        try {
            List<Admin> admins = adminRepo.findAllActiveAdmins();
            for (Admin admin : admins) {
                if (admin.getAccessTokenLastAccessedOn() != null && (Duration.between(OffsetDateTime.parse(admin.getAccessTokenLastAccessedOn()), OffsetDateTime.now(ZoneId.of(timeZone))).toMinutes() >= 15)) {
                    //checking if user is idle for 15 mins from last api call
                    admin.setAccessTokenIsExpired(true);
                    adminRepo.save(admin);
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
