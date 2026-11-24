package com.cafe.ordermanagementsystem.service;

import com.cafe.ordermanagementsystem.exchange.CustomerRequestDTO;
import com.cafe.ordermanagementsystem.exchange.GroceryItemRequestDTO;
import com.cafe.ordermanagementsystem.exchange.OrderRequestDTO;
import com.cafe.ordermanagementsystem.exchange.CustomerResponseDTO;
import com.cafe.ordermanagementsystem.exchange.GroceryItemResponseDTO;
import com.cafe.ordermanagementsystem.exchange.OrderResponseDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderManagementIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private GroceryItemService groceryItemService;

    @Autowired
    private OrderService orderService;

    @Test
    void createCustomerItemsAndOrder_persistsInH2AndCalculatesTotal() {
        CustomerRequestDTO customerReq = new CustomerRequestDTO();
        customerReq.setName("Rohan Deshmukh");
        customerReq.setEmail("rohan@example.com");
        customerReq.setPhone("9822001122");
        CustomerResponseDTO customer = customerService.createCustomer(customerReq);

        GroceryItemRequestDTO milkReq = new GroceryItemRequestDTO();
        milkReq.setName("Fresh Milk");
        milkReq.setCategory("Dairy");
        milkReq.setPrice(55.0);
        milkReq.setQuantity(10);
        GroceryItemResponseDTO milk = groceryItemService.createItem(milkReq);

        GroceryItemRequestDTO breadReq = new GroceryItemRequestDTO();
        breadReq.setName("Brown Bread");
        breadReq.setCategory("Bakery");
        breadReq.setPrice(45.0);
        breadReq.setQuantity(15);
        GroceryItemResponseDTO bread = groceryItemService.createItem(breadReq);

        OrderRequestDTO orderReq = new OrderRequestDTO();
        orderReq.setCustomerId(customer.getId());
        orderReq.setItemIds(List.of(milk.getId(), bread.getId()));

        OrderResponseDTO order = orderService.createOrder(orderReq);

        assertNotNull(order.getId());
        assertEquals(100.0, order.getTotalPrice());
        assertEquals(2, order.getItems().size());
        assertEquals(1, orderService.getOrdersByCustomerId(customer.getId()).size());
    }
}