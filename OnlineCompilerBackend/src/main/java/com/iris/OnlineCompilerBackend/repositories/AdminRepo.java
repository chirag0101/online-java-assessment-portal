package com.iris.OnlineCompilerBackend.repositories;

import com.iris.OnlineCompilerBackend.dtos.AllAdminResDTO;
import com.iris.OnlineCompilerBackend.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {

    @Query("SELECT a FROM Admin a WHERE a.adminId=:adminId")
    Admin findByAdminId(@Param("adminId") String adminId);

    @Query("SELECT new com.iris.OnlineCompilerBackend.dtos.AllAdminResDTO(a.fullName, a.adminId) FROM Admin a ")
    List<AllAdminResDTO> findAllAdminsDet();

    @Query("FROM Admin a WHERE a.accessTokenIsExpired=false")
    List<Admin> findAllActiveAdmins();
}
