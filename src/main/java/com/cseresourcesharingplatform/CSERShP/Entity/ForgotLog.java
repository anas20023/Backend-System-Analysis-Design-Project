package com.cseresourcesharingplatform.CSERShP.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "forgot_log")
public class ForgotLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String f_email;

    @Column(nullable = false, length = 8)
    private String code;

    @CreationTimestamp
    @Column(
            name = "timestamp",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime timestamp;




}
