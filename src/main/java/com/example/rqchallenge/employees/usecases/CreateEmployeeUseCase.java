package com.example.rqchallenge.employees.usecases;

import com.example.rqchallenge.employees.application.EmployeeService;
import com.example.rqchallenge.employees.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateEmployeeUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateEmployeeUseCase.class);

    @Autowired
    private EmployeeService service;

    public CreateEmployeeUseCase(EmployeeService service){
        this.service = service;
    }

    public Employee execute(Employee employee) {
        logger.info("Executing useCase rules");
        return this.service.createEmployee(employee);
    }
}
