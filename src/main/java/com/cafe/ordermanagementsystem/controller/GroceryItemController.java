package com.cafe.ordermanagementsystem.controller;

import com.cafe.ordermanagementsystem.exchange.GroceryItemRequestDTO;
import com.cafe.ordermanagementsystem.exchange.GroceryItemResponseDTO;
import com.cafe.ordermanagementsystem.service.GroceryItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class GroceryItemController {

    private final GroceryItemService groceryItemService;

    @PostMapping
    public ResponseEntity<GroceryItemResponseDTO> createItem(@Valid @RequestBody GroceryItemRequestDTO request) {
        return new ResponseEntity<>(groceryItemService.createItem(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GroceryItemResponseDTO>> getAllItems() {
        return ResponseEntity.ok(groceryItemService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroceryItemResponseDTO> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(groceryItemService.getItemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroceryItemResponseDTO> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody GroceryItemRequestDTO request) {
        return ResponseEntity.ok(groceryItemService.updateItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        groceryItemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
