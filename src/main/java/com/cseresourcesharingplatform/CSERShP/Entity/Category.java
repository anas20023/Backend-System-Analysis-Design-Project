package com.cseresourcesharingplatform.CSERShP.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "catagory") // matches your DB naming
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "catagoryName", nullable = false)
    private String catagoryName;

    @OneToMany(mappedBy = "category")
    private List<ResourceCategory> resourceCategories = new ArrayList<>();
}
