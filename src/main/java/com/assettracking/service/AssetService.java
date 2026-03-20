package com.assettracking.service;

import com.assettracking.dto.*;
import com.assettracking.exception.BadRequestException;
import com.assettracking.exception.ResourceNotFoundException;
import com.assettracking.model.Asset;
import com.assettracking.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetResponse createAsset(CreateAssetRequest request) {
        if (StringUtils.hasText(request.getSerialNumber()) &&
                assetRepository.existsBySerialNumberAndIsDeletedFalse(request.getSerialNumber())) {
            throw new BadRequestException("Asset with serial number '" + request.getSerialNumber() + "' already exists");
        }

        Asset asset = Asset.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .serialNumber(request.getSerialNumber())
                .location(request.getLocation())
                .status(request.getStatus() != null ? request.getStatus() : Asset.AssetStatus.ACTIVE)
                .assignedTo(request.getAssignedTo())
                .isDeleted(false)
                .build();

        return AssetResponse.from(assetRepository.save(asset));
    }

    public PagedResponse<AssetResponse> getAllAssets(int page, int size, String sortBy, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Asset> assetPage;

        if (StringUtils.hasText(search)) {
            assetPage = assetRepository.searchAssets(search.trim(), pageable);
        } else {
            assetPage = assetRepository.findAllByIsDeletedFalse(pageable);
        }

        List<AssetResponse> content = assetPage.getContent()
                .stream()
                .map(AssetResponse::from)
                .toList();

        return PagedResponse.<AssetResponse>builder()
                .content(content)
                .page(assetPage.getNumber())
                .size(assetPage.getSize())
                .totalElements(assetPage.getTotalElements())
                .totalPages(assetPage.getTotalPages())
                .last(assetPage.isLast())
                .build();
    }

    public AssetResponse getAssetById(Long id) {
        Asset asset = assetRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        return AssetResponse.from(asset);
    }

    public AssetResponse updateAsset(Long id, UpdateAssetRequest request) {
        Asset asset = assetRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        if (StringUtils.hasText(request.getSerialNumber()) &&
                !request.getSerialNumber().equals(asset.getSerialNumber()) &&
                assetRepository.existsBySerialNumberAndIsDeletedFalse(request.getSerialNumber())) {
            throw new BadRequestException("Asset with serial number '" + request.getSerialNumber() + "' already exists");
        }

        if (StringUtils.hasText(request.getName()))         asset.setName(request.getName());
        if (StringUtils.hasText(request.getDescription()))  asset.setDescription(request.getDescription());
        if (StringUtils.hasText(request.getCategory()))     asset.setCategory(request.getCategory());
        if (StringUtils.hasText(request.getSerialNumber())) asset.setSerialNumber(request.getSerialNumber());
        if (StringUtils.hasText(request.getLocation()))     asset.setLocation(request.getLocation());
        if (StringUtils.hasText(request.getAssignedTo()))   asset.setAssignedTo(request.getAssignedTo());
        if (request.getStatus() != null)                    asset.setStatus(request.getStatus());

        return AssetResponse.from(assetRepository.save(asset));
    }

    public void softDeleteAsset(Long id) {
        Asset asset = assetRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        asset.setIsDeleted(true);
        asset.setDeletedAt(LocalDateTime.now());
        assetRepository.save(asset);
    }
}
