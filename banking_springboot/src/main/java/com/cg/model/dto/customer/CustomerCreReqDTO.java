package com.cg.model.dto.customer;

import com.cg.model.Customer;
import com.cg.model.dto.LocationRegion.LocationRegionCreReqDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CustomerCreReqDTO implements Validator {

    private String fullName;
    private String email;
    private String phone;
    private LocationRegionCreReqDTO locationRegion;

    public Customer toCustomer() {
        return new Customer()
                .setId(null)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(BigDecimal.ZERO)
                .setLocationRegion(locationRegion.toLocationRegion())
                ;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CustomerCreReqDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerCreReqDTO customerCreReqDTO = (CustomerCreReqDTO) target;

        String fullName = customerCreReqDTO.fullName;
        String email=customerCreReqDTO.email;

        if (fullName.length() == 0||fullName==null) {
            errors.rejectValue("fullName", "fullName.empty");
        } else {
            if (fullName.length() < 5 || fullName.length() > 20) {
                errors.rejectValue("fullName", "fullName.length");
            }
        }
        if (email.length() == 0||email==null) {
            errors.rejectValue("fullName", "fullName.empty");
        }
    }
}
