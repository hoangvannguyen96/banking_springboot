package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.dto.customer.*;
import com.cg.service.customer.ICustomer;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/customers")

public class CustomerAPI {
    @Autowired
    private ICustomer customerService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ValidateUtils validateUtils;

    @GetMapping
    public ResponseEntity<?> getAllCustomers() {

        List<CustomerResDTO> customerResDTOS = customerService.findAllCustomerResDTOS();

        return new ResponseEntity<>(customerResDTOS, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getById(@PathVariable Long customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("message", "Khách hàng không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerOptional.get();
        CustomerResDTO customerResDTO = customer.toCustomerResDTO();

        return new ResponseEntity<>(customerResDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerCreReqDTO customerCreReqDTO, BindingResult bindingResult) {
        new CustomerCreReqDTO().validate(customerCreReqDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Map<String, String> data = new HashMap<>();

        String email = customerCreReqDTO.getEmail().trim();

        if (customerService.existsByEmail(email)) {
            data.put("message", "Email đã tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        CustomerCreResDTO customerCreResDTO = customerService.create(customerCreReqDTO);

        return new ResponseEntity<>(customerCreResDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<?> edit(@PathVariable("customerId") Long customerId,
                                  @RequestBody CustomerUpReqDTO customerUpReqDTO,
                                  BindingResult bindingResult) {

        new CustomerUpReqDTO().validate(customerUpReqDTO, bindingResult);

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã khách hàng không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Map<String, String> data = new HashMap<>();

        String email = customerUpReqDTO.getEmail().trim();

        if (customerService.existsByEmailAndIdNot(email, customerId)) {
            data.put("message", "Email đã tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        CustomerUpResDTO customerCreResDTO = customerService.update(customerId,customerUpReqDTO);

        return new ResponseEntity<>(customerCreResDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> delete(@PathVariable("customerId") Long customerId) {
        try {
            Optional<Customer> customerOptional = customerService.findById(customerId);

            if (customerOptional.isEmpty()) {
                Map<String, String> data = new HashMap<>();
                data.put("message", "Khách hàng không tồn tại");
                return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            }
            Customer customer = customerOptional.get();
            customer.setDeleted(true);
            customerService.save(customer);
            List<Customer> customers = customerService.findAllByDeletedIs(false);

            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> data = new HashMap<>();
            data.put("message", "Lỗi xóa khách hàng");
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
