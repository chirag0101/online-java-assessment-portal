package com.iris.OnlineCompilerBackend.controllers;

import com.iris.OnlineCompilerBackend.dtos.AdminLoginCredsDTO;
import com.iris.OnlineCompilerBackend.dtos.NewAdminReqDTO;
import com.iris.OnlineCompilerBackend.dtos.ResetAdminPasswordReqDTO;
import com.iris.OnlineCompilerBackend.models.ApiResponse;
import com.iris.OnlineCompilerBackend.services.AdminService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("AdminService")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private AdminService adminService;

    @PostMapping("/new-admin")
    public ApiResponse newAdmin(@RequestBody NewAdminReqDTO newAdminReqDTO) {
        return adminService.createAdmin(newAdminReqDTO);
    }

    @PostMapping("/authenticate-admin")
    public ApiResponse verifyAdmin(@Valid @RequestBody AdminLoginCredsDTO adminLoginReqDto) {
        return adminService.verifyAdminCreds(adminLoginReqDto);
    }

    @GetMapping("/all-admins")
    public ApiResponse getAllAdmins(@RequestHeader("adminId") String adminId,@RequestHeader("accessToken") String accessToken) {
        return adminService.fetchAllAdmins();
    }

    @PostMapping("/reset-password")
    public ApiResponse resetPassword(@Valid @RequestBody ResetAdminPasswordReqDTO resetAdminPasswordReqDTO) {
        return adminService.resetAdminPassword(resetAdminPasswordReqDTO);
    }

}
