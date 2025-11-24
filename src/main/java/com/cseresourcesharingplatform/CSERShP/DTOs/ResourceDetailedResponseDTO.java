package com.cseresourcesharingplatform.CSERShP.DTOs;

import com.cseresourcesharingplatform.CSERShP.Entity.Resource;
import lombok.*;

@Data
@NoArgsConstructor
public class ResourceDetailedResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String status;
    private String createdAt;
    private String approvedAt;

    // added fields
    private String uploaderName;
    private int downloadCount;

    public ResourceDetailedResponseDTO(Resource resource) {
        this.id = resource.getId();
        this.title = resource.getTitle();
        this.description = resource.getDescription();
        this.fileUrl = resource.getFileUrl();
        this.status = resource.getStatus().name();

        this.createdAt = resource.getCreatedAt() != null ? resource.getCreatedAt().toString() : null;
        this.approvedAt = resource.getApprovedAt() != null ? resource.getApprovedAt().toString() : null;

        // uploader full name
        this.uploaderName = resource.getUploader() != null ? resource.getUploader().getUsername() : null;

        // download count from relation list
        this.downloadCount = resource.getDownloads() != null ? resource.getDownloads().size() : 0;
    }
}
