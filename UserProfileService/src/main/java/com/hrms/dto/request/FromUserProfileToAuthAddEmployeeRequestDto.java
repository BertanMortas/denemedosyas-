package com.hrms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FromUserProfileToAuthAddEmployeeRequestDto {

    private String email;
    private String password;
    private String name;
    private String surname;
}
