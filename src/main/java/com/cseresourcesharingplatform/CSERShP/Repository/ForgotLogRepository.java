package com.cseresourcesharingplatform.CSERShP.Repository;

import com.cseresourcesharingplatform.CSERShP.Entity.ForgotLog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ForgotLogRepository extends Repository<ForgotLog, Long> {
    @Query("SELECT f FROM ForgotLog f WHERE f.f_email = :email ORDER BY f.timestamp DESC limit 1")
    ForgotLog findTopByEmailOrderByCreatedAtDesc(@Param("email") String email);


    @Modifying
    @Transactional
    @Query("DELETE FROM ForgotLog f WHERE f.f_email = :email")
    void deleteByEmail(String email);
}
