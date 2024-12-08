package com.example.rqchallenge.employees.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor()
@AllArgsConstructor()
public class Employee {
    public Employee(String employeeName){
        this.name = employeeName;
    }

    public Employee(int id, String employeeName){
        this.name = employeeName;
        this.id = id;
    }

    public Employee(double salary, String employeeName){
        this.name = employeeName;
        this.salary = salary;
    }

    @JsonProperty("id")
    private int id;

    @JsonProperty("employee_name")
    private String name;

    @JsonProperty("employee_salary")
    private double salary;

    @JsonProperty("employee_age")
    private int age;

    @JsonProperty("profile_image")
    private String profileImage;
}

