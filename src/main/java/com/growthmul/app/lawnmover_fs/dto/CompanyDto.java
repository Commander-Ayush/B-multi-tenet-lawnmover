package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.Company;

public class CompanyDto {
    private String name;
    private String city;
    private Integer foundedYear;
    private Integer yardsServed;
    private Integer yearsExperience;
    private Long phone;
    private String email;

    public static CompanyDto from(Company c) {
        CompanyDto dto = new CompanyDto();
        dto.name = c.getName();
        dto.city = c.getCity();
        dto.foundedYear = c.getFoundedYear();
        dto.yardsServed = c.getYardsServed();
        dto.yearsExperience = c.getYearsExperience();
        dto.phone = c.getPhone();
        dto.email = c.getEmail();
        return dto;
    }

    public String getName() { return name; }
    public String getCity() { return city; }
    public Integer getFoundedYear() { return foundedYear; }
    public Integer getYardsServed() { return yardsServed; }
    public Integer getYearsExperience() { return yearsExperience; }
    public Long getPhone() { return phone; }
    public String getEmail() { return email; }
}
