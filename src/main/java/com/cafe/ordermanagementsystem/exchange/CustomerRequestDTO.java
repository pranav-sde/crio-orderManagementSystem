package com.cafe.ordermanagementsystem.exchange;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRequestDTO {

    @NotBlank(message = "Customer name is required")
    private String name;

    private String email;

    @NotBlank(message = "Phone number is required")
    private String phone;
}