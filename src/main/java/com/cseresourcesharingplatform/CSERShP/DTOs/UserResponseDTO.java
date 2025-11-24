package com.cseresourcesharingplatform.CSERShP.DTOs;

import com.cseresourcesharingplatform.CSERShP.Entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String avatarUrl;
    private String joinedDate;

    private List<ResourceResponseDTO> resources; // <- added

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getFullName();
        this.email = user.getEmail();
        this.avatarUrl = user.getProfileImageLink();
        this.joinedDate = user.getCreatedAt() != null ? user.getCreatedAt().toString() : null;

        if (user.getResources() != null) {
            this.resources = user.getResources()
                    .stream()
                    .map(ResourceResponseDTO::new)
                    .collect(Collectors.toList());
        }
    }
}
