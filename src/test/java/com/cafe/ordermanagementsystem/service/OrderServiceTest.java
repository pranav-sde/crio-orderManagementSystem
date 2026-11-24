package com.cafe.ordermanagementsystem.service;

import com.cafe.ordermanagementsystem.exchange.OrderRequestDTO;
import com.cafe.ordermanagementsystem.exchange.CustomerResponseDTO;
import com.cafe.ordermanagementsystem.exchange.GroceryItemResponseDTO;
import com.cafe.ordermanagementsystem.exchange.OrderResponseDTO;
import com.cafe.ordermanagementsystem.entity.CustomerEntity;
import com.cafe.ordermanagementsystem.entity.GroceryItemEntity;
import com.cafe.ordermanagementsystem.entity.OrderEntity;
import com.cafe.ordermanagementsystem.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private GroceryItemService groceryItemService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_shouldCalculateTotalPriceAndSaveOrder() {
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setName("Aarav");
        customer.setEmail("aarav@example.com");
        customer.setPhone("9876543210");

        GroceryItemEntity item1 = new GroceryItemEntity();
        item1.setId(10L);
        item1.setName("Organic Milk");
        item1.setCategory("Dairy");
        item1.setPrice(60.0);
        item1.setQuantity(20);

        GroceryItemEntity item2 = new GroceryItemEntity();
        item2.setId(11L);
        item2.setName("Sourdough Bread");
        item2.setCategory("Bakery");
        item2.setPrice(90.0);
        item2.setQuantity(15);

        OrderRequestDTO request = new OrderRequestDTO();
        request.setCustomerId(1L);
        request.setItemIds(List.of(10L, 11L));

        when(customerService.findCustomerOrThrow(1L)).thenReturn(customer);
        when(groceryItemService.findItemOrThrow(10L)).thenReturn(item1);
        when(groceryItemService.findItemOrThrow(11L)).thenReturn(item2);
        when(customerService.toResponseDTO(customer)).thenReturn(
                CustomerResponseDTO.builder()
                        .id(1L).name("Aarav").email("aarav@example.com").phone("9876543210").build()
        );
        when(groceryItemService.toResponseDTO(item1)).thenReturn(
                GroceryItemResponseDTO.builder()
                        .id(10L).name("Organic Milk").category("Dairy").price(60.0).quantity(20).build()
        );
        when(groceryItemService.toResponseDTO(item2)).thenReturn(
                GroceryItemResponseDTO.builder()
                        .id(11L).name("Sourdough Bread").category("Bakery").price(90.0).quantity(15).build()
        );
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> {
            OrderEntity saved = invocation.getArgument(0);
            saved.setId(100L);
            if (saved.getOrderDate() == null) {
                saved.setOrderDate(LocalDateTime.now());
            }
            return saved;
        });

        OrderResponseDTO response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(150.0, response.getTotalPrice());
        assertEquals(2, response.getItems().size());
        assertNotNull(response.getOrderDate());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void getOrderById_whenMissing_shouldThrowException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.getOrderById(999L));
    }
}