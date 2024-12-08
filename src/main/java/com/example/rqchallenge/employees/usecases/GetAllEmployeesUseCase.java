package com.example.rqchallenge.employees.usecases;

import com.example.rqchallenge.employees.application.EmployeeService;
import com.example.rqchallenge.employees.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class GetAllEmployeesUseCase {

    private static final Logger logger = LoggerFactory.getLogger(GetAllEmployeesUseCase.class);

    @Autowired
    private EmployeeService service;

    public GetAllEmployeesUseCase(EmployeeService service){
        this.service = service;
    }

    public List<Employee> execute() {
        logger.info("Executing useCase rules");
        return this.service.fetchEmployees();
    }
}
