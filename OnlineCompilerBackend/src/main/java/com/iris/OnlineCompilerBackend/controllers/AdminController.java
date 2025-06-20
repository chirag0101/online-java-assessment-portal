package com.iris.OnlineCompilerBackend.controllers;

import com.iris.OnlineCompilerBackend.dtos.AdminLoginCredsDTO;
import com.iris.OnlineCompilerBackend.dtos.NewAdminReqDTO;
import com.iris.OnlineCompilerBackend.dtos.ResetAdminPasswordReqDTO;
import com.iris.OnlineCompilerBackend.models.ApiResponse;
import com.iris.OnlineCompilerBackend.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("AdminService")
@CrossOrigin("http://localhost:4200")
public class AdminController {
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
    public ApiResponse getAllAdmins() {
        return adminService.fetchAllAdmins();
    }

    @PostMapping("/reset-password")
    public ApiResponse resetPassword(@Valid @RequestBody ResetAdminPasswordReqDTO resetAdminPasswordReqDTO) {
        return adminService.resetAdminPassword(resetAdminPasswordReqDTO);
    }

}
