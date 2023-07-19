package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.dto.customer.*;
import org.springframework.stereotype.Service;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface ICustomer extends IGeneralService<Customer,Long> {
    Boolean existsByEmail(String email);

    CustomerUpResDTO update(long customerId, CustomerUpReqDTO customerUpReqDTO);

    Boolean existsByEmailAndIdNot(String email, Long id);
    void incrementBalance(Long id, BigDecimal amount);
    List<Customer> findAllByDeletedIs(Boolean boo);

    List<CustomerResDTO> findAllCustomerResDTOS();

    CustomerCreResDTO create(CustomerCreReqDTO customerCreReqDTO);

}
