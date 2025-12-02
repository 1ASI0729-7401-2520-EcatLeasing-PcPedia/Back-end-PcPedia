package com.pcpedia.api.inventory.domain.repository;

import com.pcpedia.api.inventory.domain.model.aggregate.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductModelRepository extends JpaRepository<ProductModel, Long> {

    Page<ProductModel> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT pm FROM ProductModel pm WHERE pm.isActive = true " +
           "AND (LOWER(pm.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(pm.brand) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(pm.model) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ProductModel> searchActive(@Param("search") String search, Pageable pageable);

    @Query("SELECT pm FROM ProductModel pm WHERE pm.isActive = true AND pm.category = :category")
    Page<ProductModel> findByCategory(@Param("category") String category, Pageable pageable);

    List<ProductModel> findByIsActiveTrueOrderByNameAsc();

    Optional<ProductModel> findByIdAndIsActiveTrue(Long id);

    // Catalog queries - only return models with available stock
    @Query("SELECT pm FROM ProductModel pm WHERE pm.isActive = true " +
           "AND EXISTS (SELECT e FROM Equipment e WHERE e.productModel = pm AND e.status = 'AVAILABLE')")
    Page<ProductModel> findActiveWithStock(Pageable pageable);

    @Query("SELECT pm FROM ProductModel pm WHERE pm.isActive = true " +
           "AND EXISTS (SELECT e FROM Equipment e WHERE e.productModel = pm AND e.status = 'AVAILABLE') " +
           "AND (LOWER(pm.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(pm.brand) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(pm.model) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ProductModel> searchActiveWithStock(@Param("search") String search, Pageable pageable);

    @Query("SELECT pm FROM ProductModel pm WHERE pm.isActive = true AND pm.category = :category " +
           "AND EXISTS (SELECT e FROM Equipment e WHERE e.productModel = pm AND e.status = 'AVAILABLE')")
    Page<ProductModel> findByCategoryWithStock(@Param("category") String category, Pageable pageable);
}
