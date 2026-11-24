package com.cafe.ordermanagementsystem.controller;

import com.cafe.ordermanagementsystem.exchange.OrderRequestDTO;
import com.cafe.ordermanagementsystem.exchange.OrderResponseDTO;
import com.cafe.ordermanagementsystem.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    void createOrder_validRequest_returns201() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setCustomerId(1L);
        request.setItemIds(List.of(10L));

        OrderResponseDTO response = OrderResponseDTO.builder()
                .id(100L)
                .items(List.of())
                .orderDate(LocalDateTime.now())
                .totalPrice(60.0)
                .build();

        when(orderService.createOrder(any(OrderRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.totalPrice").value(60.0));
    }

    @Test
    void createOrder_emptyItems_returns400() throws Exception {
        OrderRequestDTO invalidRequest = new OrderRequestDTO();
        invalidRequest.setCustomerId(1L);
        invalidRequest.setItemIds(List.of());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllOrders_returns200() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of());

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}