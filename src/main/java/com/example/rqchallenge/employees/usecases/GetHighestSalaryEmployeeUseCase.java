package com.example.rqchallenge.employees.usecases;

import com.example.rqchallenge.employees.application.EmployeeService;
import com.example.rqchallenge.employees.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class GetHighestSalaryEmployeeUseCase {

    private static final Logger logger = LoggerFactory.getLogger(GetHighestSalaryEmployeeUseCase.class);

    @Autowired
    private EmployeeService service;

    public GetHighestSalaryEmployeeUseCase(EmployeeService service){
        this.service = service;
    }

    public Optional<Employee> execute(){
        logger.info("Executing useCase rules");
        List<Employee> employeeList = this.service.fetchEmployees();
        return employeeList.stream()
                .max(Comparator.comparingDouble(Employee::getSalary));
    }

}
