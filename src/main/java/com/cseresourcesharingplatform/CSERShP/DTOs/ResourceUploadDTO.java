package com.cseresourcesharingplatform.CSERShP.DTOs;

import lombok.Getter;

public class ResourceUploadDTO {
    @Getter
    private String uploaderId;
    private String title;
    private String description;
    private String fileUrl;

    public Object getTitle() {
        return title;
    }

    public Object getDescription() {
        return description;
    }

    public Object getFileUrl() {
        return fileUrl;
    }
}
