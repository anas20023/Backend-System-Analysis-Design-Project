package com.cseresourcesharingplatform.CSERShP.Repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cseresourcesharingplatform.CSERShP.Entity.Resource;
import com.cseresourcesharingplatform.CSERShP.Entity.ResourceStatus;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    @NotNull
    @Query("""
    SELECT DISTINCT r
    FROM Resource r
    LEFT JOIN FETCH r.uploader
    LEFT JOIN FETCH r.downloads
    WHERE r.status = com.cseresourcesharingplatform.CSERShP.Entity.ResourceStatus.APPROVED
""")
    List<Resource> findAllResources();
    List<Resource> findByUploaderId(Long uploaderId);
    List<Resource> findByStatus(ResourceStatus status);
}
