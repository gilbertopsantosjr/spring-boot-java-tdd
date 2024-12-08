package com.example.rqchallenge.employees.model;

import lombok.Data;

@Data
public class EmployeeDto {
    private int id;
    private String name;
    private double salary;
    private int age;
    private String profileImage;
}
