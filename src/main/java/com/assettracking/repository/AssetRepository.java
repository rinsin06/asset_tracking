package com.assettracking.repository;

import com.assettracking.model.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    Page<Asset> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Asset> findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT a FROM Asset a WHERE a.isDeleted = false AND " +
           "(LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.category) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.location) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.serialNumber) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Asset> searchAssets(@Param("query") String query, Pageable pageable);

    boolean existsBySerialNumberAndIsDeletedFalse(String serialNumber);
}
