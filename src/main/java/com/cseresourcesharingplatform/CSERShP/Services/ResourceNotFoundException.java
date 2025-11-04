package com.cseresourcesharingplatform.CSERShP.Services;

import org.jetbrains.annotations.NotNull;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String userNotFound) {
        super(userNotFound);
    }
}
