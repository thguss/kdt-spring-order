package org.prammers.kdt.customer.service;

import org.prammers.kdt.customer.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    public void createCustomers(List<Customer> customers);
    public Customer createCustomer(String email, String name);

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomer(UUID customerId);
}
