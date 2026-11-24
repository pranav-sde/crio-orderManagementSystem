package com.cafe.ordermanagementsystem.service;

import com.cafe.ordermanagementsystem.exception.ResourceNotFoundException;
import com.cafe.ordermanagementsystem.exchange.OrderRequestDTO;
import com.cafe.ordermanagementsystem.exchange.OrderResponseDTO;
import com.cafe.ordermanagementsystem.entity.CustomerEntity;
import com.cafe.ordermanagementsystem.entity.GroceryItemEntity;
import com.cafe.ordermanagementsystem.entity.OrderEntity;
import com.cafe.ordermanagementsystem.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final GroceryItemService groceryItemService;

    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        CustomerEntity customer = customerService.findCustomerOrThrow(request.getCustomerId());
        List<GroceryItemEntity> items = resolveItems(request.getItemIds());

        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setItems(items);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(calculateTotalPrice(items));

        OrderEntity saved = orderRepository.save(order);
        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long id) {
        OrderEntity order = findOrderOrThrow(id);
        return toResponseDTO(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByCustomerId(Long customerId) {
        customerService.findCustomerOrThrow(customerId);
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO request) {
        OrderEntity order = findOrderOrThrow(id);
        CustomerEntity customer = customerService.findCustomerOrThrow(request.getCustomerId());
        List<GroceryItemEntity> items = resolveItems(request.getItemIds());

        order.setCustomer(customer);
        order.setItems(items);
        order.setTotalPrice(calculateTotalPrice(items));

        OrderEntity updated = orderRepository.save(order);
        return toResponseDTO(updated);
    }

    public void deleteOrder(Long id) {
        OrderEntity order = findOrderOrThrow(id);
        orderRepository.delete(order);
    }

    private List<GroceryItemEntity> resolveItems(List<Long> itemIds) {
        return itemIds.stream()
                .map(groceryItemService::findItemOrThrow)
                .toList();
    }

    private double calculateTotalPrice(List<GroceryItemEntity> items) {
        return items.stream()
                .mapToDouble(item -> item.getPrice() != null ? item.getPrice() : 0.0)
                .sum();
    }

    private OrderEntity findOrderOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    private OrderResponseDTO toResponseDTO(OrderEntity order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .customer(customerService.toResponseDTO(order.getCustomer()))
                .items(order.getItems().stream()
                        .map(groceryItemService::toResponseDTO)
                        .toList())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
