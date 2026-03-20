package com.assettracking.controller;

import com.assettracking.dto.*;
import com.assettracking.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    public ResponseEntity<ApiResponse<AssetResponse>> createAsset(@Valid @RequestBody CreateAssetRequest request) {
        AssetResponse asset = assetService.createAsset(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Asset created successfully", asset));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<AssetResponse>>> getAllAssets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false) String search) {
        PagedResponse<AssetResponse> assets = assetService.getAllAssets(page, size, sortBy, search);
        return ResponseEntity.ok(ApiResponse.success("Assets fetched successfully", assets));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetResponse>> getAssetById(@PathVariable Long id) {
        AssetResponse asset = assetService.getAssetById(id);
        return ResponseEntity.ok(ApiResponse.success("Asset fetched successfully", asset));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetResponse>> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAssetRequest request) {
        AssetResponse asset = assetService.updateAsset(id, request);
        return ResponseEntity.ok(ApiResponse.success("Asset updated successfully", asset));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAsset(@PathVariable Long id) {
        assetService.softDeleteAsset(id);
        return ResponseEntity.ok(ApiResponse.success("Asset deleted successfully"));
    }
}
