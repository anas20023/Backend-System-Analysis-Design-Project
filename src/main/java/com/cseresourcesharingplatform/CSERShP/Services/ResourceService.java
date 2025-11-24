package com.cseresourcesharingplatform.CSERShP.Services;

import com.cseresourcesharingplatform.CSERShP.DTOs.ResourceUploadDTO;
import com.cseresourcesharingplatform.CSERShP.Entity.Resource;
import com.cseresourcesharingplatform.CSERShP.Entity.ResourceStatus;
import com.cseresourcesharingplatform.CSERShP.Entity.User;
import com.cseresourcesharingplatform.CSERShP.Repository.ResourceRepository;
import com.cseresourcesharingplatform.CSERShP.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    public ResourceService(ResourceRepository resourceRepository,
                           UserRepository userRepository) {
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public Optional<Resource> getResourceById(Long id) {
        return resourceRepository.findById(id);
    }

    public List<Resource> getResourcesByUploader(Long uploaderId) {
        return resourceRepository.findByUploaderId(uploaderId);
    }

    public List<Resource> getPendingResources() {
        return resourceRepository.findByStatus(ResourceStatus.PENDING);
    }

    /**
     * Save a new resource. Ensures status is PENDING and approvedAt is null.
     */
    public Resource uploadResource(Resource resource) {
        resource.setStatus(ResourceStatus.PENDING);
        resource.setApprovedAt(null); // ensure not approved yet
        return resourceRepository.save(resource);
    }

    /**
     * Approve a resource (set status to APPROVED and approvedAt to now).
     * Marked transactional to avoid race conditions if concurrent updates happen.
     */
    @Transactional
    public Resource approveResource(Long id) {
        return resourceRepository.findById(id)
                .map(r -> {
                    r.setStatus(ResourceStatus.APPROVED);
                    r.setApprovedAt(LocalDateTime.now());
                    return resourceRepository.save(r);
                })
                .orElseThrow(() -> new IllegalArgumentException("Resource not found with id: " + id));
    }

    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }

    /**
     * Create and save a Resource from DTO.
     * - Validates uploader exists
     * - Ensures default PENDING status and no approvedAt
     */
    public Resource upload(ResourceUploadDTO dto) {
        // Basic null-checks (you can replace with javax.validation or custom validation)
        if (dto == null) {
            throw new IllegalArgumentException("ResourceUploadDTO must not be null");
        }
        if (dto.getUploaderId() == null) {
            throw new IllegalArgumentException("uploaderId is required");
        }

        User uploader = userRepository.findByUsername(dto.getUploaderId())
                .orElseThrow(() -> new IllegalArgumentException("Uploader not found with id: " + dto.getUploaderId()));

        Resource resource = new Resource();
        resource.setUploader(uploader);
        resource.setTitle(dto.getTitle());
        resource.setDescription(dto.getDescription());
        resource.setFileUrl(dto.getFileUrl());

        // enforce defaults
        resource.setStatus(ResourceStatus.PENDING);
        resource.setApprovedAt(null);

        // createdAt is expected to be set by DB via columnDefinition default
        return resourceRepository.save(resource);
    }
}
