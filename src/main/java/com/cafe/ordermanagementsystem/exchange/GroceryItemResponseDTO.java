package com.cafe.ordermanagementsystem.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroceryItemResponseDTO {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private Integer quantity;
}
