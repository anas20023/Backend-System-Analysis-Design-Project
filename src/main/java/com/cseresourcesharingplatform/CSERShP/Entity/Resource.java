package com.cseresourcesharingplatform.CSERShP.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @Column(nullable = false, length = 150)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private String fileUrl;

    @Setter
    @Enumerated(EnumType.STRING)
    private ResourceStatus status = ResourceStatus.PENDING;

    @Column(nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime approvedAt;

    // Relations
    @OneToMany(mappedBy = "resource")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "resource")
    private List<DownloadLog> downloads;

    @OneToMany(mappedBy = "resource")
    private List<ApprovalLog> approvals;

    public void setTitle(Object title) {
        this.title = (String)title;
    }

    public void setDescription(Object description) {
        this.description = (String)description;
    }

    public void setFileUrl(Object fileUrl) {
        this.fileUrl = (String)fileUrl;
    }
}
