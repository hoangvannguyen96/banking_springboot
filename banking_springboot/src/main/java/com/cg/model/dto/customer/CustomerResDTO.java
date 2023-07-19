package com.cg.model.dto.customer;

import com.cg.model.Customer;
import com.cg.model.LocationRegion;
import com.cg.model.dto.LocationRegion.LocationRegionDTO;
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
public class CustomerResDTO implements Validator {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private BigDecimal balance;

    private LocationRegionDTO locationRegion;

    public CustomerResDTO (Long id, String fullName, String email, String phone, BigDecimal balance, LocationRegion locationRegion) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
        this.locationRegion = locationRegion.toLocationRegionDTO();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CustomerResDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerResDTO customerResDTO = (CustomerResDTO) target;

        String fullName = customerResDTO.fullName;

        if (fullName.length() == 0) {
            errors.rejectValue("fullName", "fullName.empty");
        } else {
            if (fullName.length() < 5 || fullName.length() > 20) {
                errors.rejectValue("fullName", "fullName.length");
            }
        }
    }
}
