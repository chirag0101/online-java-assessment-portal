package com.iris.OnlineCompilerBackend.repositories;

import com.iris.OnlineCompilerBackend.dtos.CodeResDTO;
import com.iris.OnlineCompilerBackend.models.CodeSnippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeSnippetRepo extends JpaRepository<CodeSnippet, Long> {
    Optional<CodeSnippet> findByCodeId(Long codeId);

    @Query("SELECT new com.iris.OnlineCompilerBackend.dtos.CodeResDTO(cs.code,cs.status,cs.output,cs.action) " +
            "FROM CodeSnippet cs " +
            "WHERE cs.userIdFk.userId=:userId")
    List<CodeResDTO> findSubmissionByUserId(@Param("userId") Long userId);

}