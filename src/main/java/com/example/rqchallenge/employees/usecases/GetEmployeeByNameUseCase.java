package com.example.rqchallenge.employees.usecases;

import com.example.rqchallenge.employees.application.EmployeeService;
import com.example.rqchallenge.employees.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetEmployeeByNameUseCase {

    private static final Logger logger = LoggerFactory.getLogger(GetEmployeeByNameUseCase.class);

    @Autowired
    private EmployeeService service;

    public GetEmployeeByNameUseCase(EmployeeService service){
        this.service = service;
    }

    public List<Employee> execute(String name) {
        logger.info("Executing useCase rules");
        List<Employee> employees = this.service.fetchEmployees();
        return employees.stream()
                .filter(employee -> employee.getName().contains(name))
                .collect(Collectors.toList());
    }
}
