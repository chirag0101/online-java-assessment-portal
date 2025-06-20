package com.iris.OnlineCompilerBackend.services;

import com.iris.OnlineCompilerBackend.config.PasswordHashing;
import com.iris.OnlineCompilerBackend.constants.PasswordSuffix;
import com.iris.OnlineCompilerBackend.dtos.*;
import com.iris.OnlineCompilerBackend.models.Admin;
import com.iris.OnlineCompilerBackend.models.ApiResponse;
import com.iris.OnlineCompilerBackend.repositories.AdminRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AdminService {
    private static final Logger log = LoggerFactory.getLogger(AdminService.class);
    @Autowired
    private AdminRepo adminRepo;

    public ApiResponse createAdmin(NewAdminReqDTO adminCredsReqDTO) {
        try {

            if (adminRepo.findByAdminId(adminCredsReqDTO.getAdminId()) != null) {
                return new ApiResponse.Builder().status(false).statusMessage("ADMIN already exists!").build();
            }

            Admin admin = new Admin();
            admin.setAdminId(adminCredsReqDTO.getAdminId());

            String defaultPassword = adminCredsReqDTO.getAdminId() + PasswordSuffix.DefaultPassword;

            String salt = PasswordHashing.getSalt();
            String hashPassword = PasswordHashing.createHash(defaultPassword, salt, Boolean.TRUE);

            admin.setSalt(salt);
            admin.setAdminPassword(hashPassword);

            admin.setFullName(adminCredsReqDTO.getAdminFullName());
            admin.setIsAdmin(adminCredsReqDTO.getIsAdmin());

            admin.setLastLogin(null);

            adminRepo.save(admin);

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(null).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage("FAILURE").build();
        }
    }

    public ApiResponse verifyAdminCreds(AdminLoginCredsDTO adminLoginReqDto) {
        try {
            Admin admin = adminRepo.findByAdminId(adminLoginReqDto.getAdminId());

            if (admin == null) {
                throw new Exception("ADMIN NOT FOUND!");
            }

            if (PasswordHashing.validatePassword(admin.getSalt(), admin.getAdminPassword(), PasswordHashing.createHash(adminLoginReqDto.getAdminPassword(), admin.getSalt(), Boolean.TRUE))) {
                admin.setLastLogin(new Date());
                adminRepo.save(admin);

                return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(new AdminAuthenticationResDTO(admin.getAdminId(), admin.getIsAdmin(), admin.getFullName())).build();
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
            if(!(resetAdminPasswordReqDTO.getAdminNewPassword().equals(resetAdminPasswordReqDTO.getAdminNewConfirmPassword()))){
                throw new Exception("PASSWORDS DON'T MATCH!");
            }

            //checking if admin exists
            Admin admin = adminRepo.findByAdminId(resetAdminPasswordReqDTO.getAdminId());
            if (admin==null) {
                throw new Exception("ADMIN DOESN'T EXISTS!");
            }

            //verifying old passwords match
            if (!(PasswordHashing.validatePassword(admin.getSalt(), admin.getAdminPassword(), PasswordHashing.createHash(resetAdminPasswordReqDTO.getAdminOldPassword(), admin.getSalt(), Boolean.TRUE)))) {
                throw new Exception("INVALID CREDENTIALS!");
            }

            //storing new password & salt

            String newSalt=PasswordHashing.getSalt();
            String newHashPassword=PasswordHashing.createHash(resetAdminPasswordReqDTO.getAdminNewPassword(),newSalt,Boolean.TRUE);

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
}
