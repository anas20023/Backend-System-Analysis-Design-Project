package com.cseresourcesharingplatform.CSERShP.Repository;

import com.cseresourcesharingplatform.CSERShP.Entity.ForgotLog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cseresourcesharingplatform.CSERShP.Entity.User;

import java.util.List;
import java.util.Optional;



public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u where u.id=:id")
    Optional<User> seeAllUsers(@Param("id") Long id);
    @Query("select u from User u")
    List<User> findAllUsers();
    // Save the OTP in User Entity
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO forgot_log(f_email, code) VALUES (:email, :code)", nativeQuery = true)
    void insertRecoveryCode(@Param("email") String email, @Param("code") String code);


    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);


    @Modifying
    @Transactional
    @Query(value = "select l.code from ForgotLog l where l.f_email=:email")
    String getCodebyEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.pwhash = :hashpw WHERE u.email = :email")
    void setNewPassword(@Param("email") String email, @Param("hashpw") String hashpw);

}
