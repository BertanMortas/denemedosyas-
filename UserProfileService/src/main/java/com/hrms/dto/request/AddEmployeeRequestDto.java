package com.hrms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddEmployeeRequestDto {
    private String name;
    private String surname;
    private String identityNumber;
    private String password;
    @Indexed(unique = true)
    private String email;
    private String phoneNumber;
    private String salary;
    private String jobEntryDate;
}
