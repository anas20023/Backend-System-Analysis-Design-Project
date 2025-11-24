package com.cseresourcesharingplatform.CSERShP.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ResourceCatagory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "resourceId", nullable = false)
    private Resource resource;

    @ManyToOne(optional = false)
    @JoinColumn(name = "catagoryId", nullable = false)
    private Category category;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
