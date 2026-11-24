package com.cafe.ordermanagementsystem.repository;

import com.cafe.ordermanagementsystem.entity.GroceryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroceryItemRepository extends JpaRepository<GroceryItemEntity, Long> {
    List<GroceryItemEntity> findByCategoryIgnoreCase(String category);
}
