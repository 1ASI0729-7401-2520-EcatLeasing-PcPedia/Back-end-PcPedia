package com.pcpedia.api.inventory.domain.repository;

import com.pcpedia.api.inventory.domain.model.aggregate.Equipment;
import com.pcpedia.api.inventory.domain.model.enums.EquipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    boolean existsBySerialNumber(String serialNumber);

    Page<Equipment> findByStatus(EquipmentStatus status, Pageable pageable);

    Page<Equipment> findByCategory(String category, Pageable pageable);

    @Query("SELECT e FROM Equipment e WHERE e.status = :status AND e.category = :category")
    Page<Equipment> findByStatusAndCategory(@Param("status") EquipmentStatus status,
                                            @Param("category") String category,
                                            Pageable pageable);

    @Query("SELECT e FROM Equipment e WHERE " +
            "(LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.brand) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.model) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Equipment> searchEquipment(@Param("search") String search, Pageable pageable);

    @Query("SELECT e FROM Equipment e WHERE e.status = :status AND " +
            "(LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.brand) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.model) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Equipment> searchByStatusAndKeyword(@Param("status") EquipmentStatus status,
                                              @Param("search") String search,
                                              Pageable pageable);

    long countByStatus(EquipmentStatus status);

    @Query("SELECT DISTINCT e.category FROM Equipment e WHERE e.category IS NOT NULL ORDER BY e.category")
    List<String> findAllCategories();

    // Methods for ProductModel stock
    long countByProductModelId(Long productModelId);

    long countByProductModelIdAndStatus(Long productModelId, EquipmentStatus status);

    Page<Equipment> findByProductModelId(Long productModelId, Pageable pageable);

    Page<Equipment> findByProductModelIdAndStatus(Long productModelId, EquipmentStatus status, Pageable pageable);
}
