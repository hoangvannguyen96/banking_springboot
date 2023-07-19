package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.LocationRegion;
import com.cg.model.dto.LocationRegion.LocationRegionCreReqDTO;
import com.cg.model.dto.customer.*;
import com.cg.repository.LocationRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cg.repository.CustomerRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService implements ICustomer {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LocationRegionRepository locationRegionRepository;

    public List<Customer> findAllByDeletedIs(Boolean boo) {
        return customerRepository.findAllByDeletedIs(boo);
    }

    @Override
    public List<CustomerResDTO> findAllCustomerResDTOS(){
        return customerRepository.findAllCustomerResDTOS();
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public CustomerCreResDTO create(CustomerCreReqDTO customerCreReqDTO) {
        LocationRegionCreReqDTO locationRegionCreReqDTO = customerCreReqDTO.getLocationRegion();
        LocationRegion locationRegion = locationRegionCreReqDTO.toLocationRegion();
        locationRegionRepository.save(locationRegion);

        Customer customer = customerCreReqDTO.toCustomer();
        customer.setLocationRegion(locationRegion);
        customerRepository.save(customer);

        CustomerCreResDTO customerCreResDTO = customer.toCustomerCreResDTO();

        return customerCreResDTO;
    }


    @Override
    public CustomerUpResDTO update(long customerId,CustomerUpReqDTO customerUpReqDTO) {

        LocationRegion locationRegion = customerUpReqDTO.getLocationRegion().toLocationRegion();
        locationRegionRepository.save(locationRegion);

        Customer customer = customerUpReqDTO.toCustomer(customerId);

        customer.setFullName(customerUpReqDTO.getFullName());
        customer.setEmail(customerUpReqDTO.getEmail());
        customer.setPhone(customerUpReqDTO.getPhone());
        customer.setLocationRegion(locationRegion);
        customerRepository.save(customer);

        CustomerUpResDTO customerUpResDTO = customer.toCustomerUpResDTO();

        return customerUpResDTO;
    }

    @Override
    public Boolean existsByEmailAndIdNot(String email, Long id){
        return customerRepository.existsByEmailAndIdNot(email,id);
    }

    @Override
    public void incrementBalance(Long id, BigDecimal amount) {
        customerRepository.incrementBalance(id, amount);
    }
}
