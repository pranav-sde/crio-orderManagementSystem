package com.cafe.ordermanagementsystem.exception;

import com.cafe.ordermanagementsystem.controller.OrderController;
import com.cafe.ordermanagementsystem.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void getOrderById_whenNotFound_returns404WithErrorResponse() throws Exception {
        when(orderService.getOrderById(999L))
                .thenThrow(new ResourceNotFoundException("Order not found with id: 999"));

        mockMvc.perform(get("/api/orders/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Order not found with id: 999"))
                .andExpect(jsonPath("$.path").value("/api/orders/999"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void createOrder_whenInvalidFields_returns400WithFieldValidationErrors() throws Exception {
        // customerId is null (@NotNull) and itemIds is empty (@NotEmpty)
        String invalidJson = """
                {
                  "customerId": null,
                  "itemIds": []
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed for one or more fields"))
                .andExpect(jsonPath("$.path").value("/api/orders"))
                .andExpect(jsonPath("$.validationErrors.customerId").value("Customer ID is required"))
                .andExpect(jsonPath("$.validationErrors.itemIds").value("Order must contain at least one grocery item ID"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }
}