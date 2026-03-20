package com.assettracking.dto;

import com.assettracking.model.Asset;
import com.assettracking.model.Asset.AssetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponse {

    private Long id;
    private String name;
    private String description;
    private String category;
    private String serialNumber;
    private String location;
    private AssetStatus status;
    private String assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AssetResponse from(Asset asset) {
        return AssetResponse.builder()
                .id(asset.getId())
                .name(asset.getName())
                .description(asset.getDescription())
                .category(asset.getCategory())
                .serialNumber(asset.getSerialNumber())
                .location(asset.getLocation())
                .status(asset.getStatus())
                .assignedTo(asset.getAssignedTo())
                .createdAt(asset.getCreatedAt())
                .updatedAt(asset.getUpdatedAt())
                .build();
    }
}
