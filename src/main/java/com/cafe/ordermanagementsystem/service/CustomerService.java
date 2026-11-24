package com.cafe.ordermanagementsystem.service;

import com.cafe.ordermanagementsystem.exception.ResourceNotFoundException;
import com.cafe.ordermanagementsystem.exchange.CustomerRequestDTO;
import com.cafe.ordermanagementsystem.exchange.CustomerResponseDTO;
import com.cafe.ordermanagementsystem.entity.CustomerEntity;
import com.cafe.ordermanagementsystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {
        CustomerEntity customer = new CustomerEntity();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        CustomerEntity saved = customerRepository.save(customer);
        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(Long id) {
        CustomerEntity customer = findCustomerOrThrow(id);
        return toResponseDTO(customer);
    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request) {
        CustomerEntity customer = findCustomerOrThrow(id);
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        CustomerEntity updated = customerRepository.save(customer);
        return toResponseDTO(updated);
    }

    public void deleteCustomer(Long id) {
        CustomerEntity customer = findCustomerOrThrow(id);
        customerRepository.delete(customer);
    }

    public CustomerEntity findCustomerOrThrow(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public CustomerResponseDTO toResponseDTO(CustomerEntity customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();
    }
}