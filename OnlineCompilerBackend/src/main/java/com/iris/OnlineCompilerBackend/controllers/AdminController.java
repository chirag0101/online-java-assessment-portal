package com.iris.OnlineCompilerBackend.controllers;

import com.iris.OnlineCompilerBackend.dtos.AdminLoginCredsDTO;
import com.iris.OnlineCompilerBackend.dtos.request.NewAdminReqDTO;
import com.iris.OnlineCompilerBackend.dtos.request.ResetAdminPasswordReqDTO;
import com.iris.OnlineCompilerBackend.models.ApiResponse;
import com.iris.OnlineCompilerBackend.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/AdminService")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/newAdmin")
    public ApiResponse newAdmin(@RequestHeader("adminId") String existingAdminId, @RequestBody @Valid NewAdminReqDTO newAdminReqDTO) {
        return adminService.createAdmin(existingAdminId, newAdminReqDTO);
    }

    @PostMapping("/authenticateAdmin")
    public ApiResponse verifyAdmin(@Valid @RequestBody AdminLoginCredsDTO adminLoginReqDto) {
        return adminService.verifyAdminCreds(adminLoginReqDto);
    }

    @GetMapping("/allAdmins")
    public ApiResponse getAllAdmins() {
        return adminService.fetchAllAdmins();
    }

    @PostMapping("/resetPassword")
    public ApiResponse resetPassword(@Valid @RequestBody ResetAdminPasswordReqDTO resetAdminPasswordReqDTO) {
        return adminService.resetAdminPassword(resetAdminPasswordReqDTO);
    }

}
