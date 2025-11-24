package com.cseresourcesharingplatform.CSERShP.controller;

import com.cseresourcesharingplatform.CSERShP.DTOs.ResourceDetailedResponseDTO;
import com.cseresourcesharingplatform.CSERShP.DTOs.ResourceResponseDTO;
import com.cseresourcesharingplatform.CSERShP.DTOs.ResourceUploadDTO;
import com.cseresourcesharingplatform.CSERShP.Repository.ResourceRepository;
import com.cseresourcesharingplatform.CSERShP.Services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cseresourcesharingplatform.CSERShP.Services.ResourceService;
import com.cseresourcesharingplatform.CSERShP.Entity.Resource;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final AuthService authService;

    public ResourceController(ResourceService resourceService, AuthService authService, ResourceRepository resourceRepository, ObjectMapper objectMapper) {
        this.resourceService = resourceService;
        this.authService = authService;
    }

    // ✅ Get all resources
    @GetMapping
    public ResponseEntity<List<ResourceDetailedResponseDTO>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }
    @GetMapping("/admin")
    public ResponseEntity<List<ResourceResponseDTO>> getAllResourcesAdmin() {
        return ResponseEntity.ok(resourceService.getAllResourcesAdmin());
    }

    // ✅ Get resource by ID
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResourceById(@PathVariable Long id) {
        return resourceService.getResourceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody ResourceUploadDTO dto) {
        Resource saved = resourceService.upload(dto);
        return ResponseEntity.ok(saved);
    }

    // ✅ Approve a resource
    @PutMapping("/{id}/approve")
    public ResponseEntity<Resource> approveResource(@PathVariable Long id) {
        return ResponseEntity.ok(resourceService.approveResource(id));
    }
    @PutMapping("/{id}/pending")
    public ResponseEntity<Resource> pendingResource(@PathVariable Long id) {
        return ResponseEntity.ok(resourceService.pendingResource(id));
    }
    @PutMapping("/{id}/decline")
    public ResponseEntity<Resource> declineResource(@PathVariable Long id) {
        return ResponseEntity.ok(resourceService.declineResource(id));
    }


    // ✅ Delete a resource
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}
