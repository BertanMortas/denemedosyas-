package com.hrms.repository.entity;

import com.hrms.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document
public class UserProfile extends Base{
    @Id
    private String userId;
    private Long authId;
    private String addressId;
    private String name;
    private String surname;
    private String identityNumber;
    private String password;
    @Indexed(unique = true)
    private String email;
    private String phoneNumber;
    private Long birthDate;
    private String avatar;;
    private String salary;
    private String jobEntryDate;
    private Long companyId;
    private List<String> daysOffList=new ArrayList<>();
    @Builder.Default
    private Long offDaysRemaining=20L;
    private Double annualLeave;
    @Builder.Default
    private EStatus status = EStatus.PENDING;
    //estatus eklencek


}
