package com.cafe.ordermanagementsystem.service;

import com.cafe.ordermanagementsystem.exchange.GroceryItemRequestDTO;
import com.cafe.ordermanagementsystem.exchange.GroceryItemResponseDTO;
import com.cafe.ordermanagementsystem.entity.GroceryItemEntity;
import com.cafe.ordermanagementsystem.repository.GroceryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GroceryItemService {

    private final GroceryItemRepository groceryItemRepository;

    public GroceryItemResponseDTO createItem(GroceryItemRequestDTO request) {
        GroceryItemEntity item = new GroceryItemEntity();
        item.setName(request.getName());
        item.setCategory(request.getCategory());
        item.setPrice(request.getPrice());
        item.setQuantity(request.getQuantity());

        GroceryItemEntity saved = groceryItemRepository.save(item);
        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<GroceryItemResponseDTO> getAllItems() {
        return groceryItemRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public GroceryItemResponseDTO getItemById(Long id) {
        GroceryItemEntity item = findItemOrThrow(id);
        return toResponseDTO(item);
    }

    public GroceryItemResponseDTO updateItem(Long id, GroceryItemRequestDTO request) {
        GroceryItemEntity item = findItemOrThrow(id);
        item.setName(request.getName());
        item.setCategory(request.getCategory());
        item.setPrice(request.getPrice());
        item.setQuantity(request.getQuantity());

        GroceryItemEntity updated = groceryItemRepository.save(item);
        return toResponseDTO(updated);
    }

    public void deleteItem(Long id) {
        GroceryItemEntity item = findItemOrThrow(id);
        groceryItemRepository.delete(item);
    }

    public GroceryItemEntity findItemOrThrow(Long id) {
        return groceryItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grocery item not found with id: " + id));
    }

    public GroceryItemResponseDTO toResponseDTO(GroceryItemEntity item) {
        return GroceryItemResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .category(item.getCategory())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build();
    }
}