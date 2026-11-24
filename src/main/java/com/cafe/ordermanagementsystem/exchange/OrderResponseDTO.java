package com.cafe.ordermanagementsystem.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private CustomerResponseDTO customer;
    private List<GroceryItemResponseDTO> items;
    private LocalDateTime orderDate;
    private Double totalPrice;
}