package com.cseresourcesharingplatform.CSERShP.DTOs;

import com.cseresourcesharingplatform.CSERShP.Entity.Resource;
import com.cseresourcesharingplatform.CSERShP.Entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ResourceResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String status;      // ResourceStatus as String
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    String  uploader;
    public ResourceResponseDTO(Resource resource) {
        this.id = resource.getId();
        this.title = resource.getTitle();
        this.description = resource.getDescription();
        this.fileUrl = resource.getFileUrl();
        this.status = resource.getStatus() != null ? resource.getStatus().name() : null;
        this.uploader=resource.getUploader().getUsername();
        this.createdAt = resource.getCreatedAt();
        this.approvedAt = resource.getApprovedAt();
    }
}
