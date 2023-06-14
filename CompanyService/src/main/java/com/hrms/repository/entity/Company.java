package com.hrms.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;
    private String companyName;
    private String taxNumber;
    private String logo;
    private String street;
    private String neighborhood;
    private String district;
    private String province;
    private String country;
    private String buildingNumber;
    private String apartmentNumber;
    private String postCode;
    private Long profit; // todo bunlar kaldırılacak başka entity e aktarıldı
    private Long loss; // todo bunlar kaldırılacak başka entity e aktarıldı
    private String department; //TODO: List olarak tutulup entitysi oluşturulabilir
    private String publicHolidays; //TODO: Lİst olarak tutulup entitysi oluşturulabilir

}
