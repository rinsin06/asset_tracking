package com.assettracking.dto;

import com.assettracking.model.Asset.AssetStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAssetRequest {

    @Size(max = 150, message = "Name must not exceed 150 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @Size(max = 100, message = "Serial number must not exceed 100 characters")
    private String serialNumber;

    @Size(max = 150, message = "Location must not exceed 150 characters")
    private String location;

    private AssetStatus status;

    @Size(max = 100, message = "Assigned to must not exceed 100 characters")
    private String assignedTo;
}
