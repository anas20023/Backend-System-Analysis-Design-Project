package com.cseresourcesharingplatform.CSERShP.model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "forgot_log")
public class ForgotLog {

    @Id
    @Column(nullable = false, unique = true, length = 100)
    private String f_email;

    @Column(nullable = false, length = 6)
    private String code;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp timestamp;
}
