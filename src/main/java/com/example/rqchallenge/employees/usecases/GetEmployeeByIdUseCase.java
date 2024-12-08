package com.example.rqchallenge.employees.usecases;

import com.example.rqchallenge.employees.application.EmployeeService;
import com.example.rqchallenge.employees.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetEmployeeByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(GetEmployeeByIdUseCase.class);

    @Autowired
    private EmployeeService service;

    public GetEmployeeByIdUseCase(EmployeeService service){
        this.service = service;
    }

    public Optional<Employee> execute(String id) {
        logger.info("Executing useCase rules");
        return this.service.fetchEmployeeById(id);
    }

}
