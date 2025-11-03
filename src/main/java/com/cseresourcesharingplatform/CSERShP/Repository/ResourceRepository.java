package com.cseresourcesharingplatform.CSERShP.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cseresourcesharingplatform.CSERShP.Entity.Resource;
import com.cseresourcesharingplatform.CSERShP.Entity.ResourceStatus;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByUploaderId(Long uploaderId);
    List<Resource> findByStatus(ResourceStatus status);
}
