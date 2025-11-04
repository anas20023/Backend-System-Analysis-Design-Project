package com.cseresourcesharingplatform.CSERShP.DTOs;

import com.cseresourcesharingplatform.CSERShP.Entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String avatarUrl;
    private String joinedDate;

    // Constructor for mapping a single User entity
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getFullName();
        this.email = user.getEmail();
        this.avatarUrl=user.getProfileImageLink();
        this.joinedDate = user.getCreatedAt().toString();
    }
}
