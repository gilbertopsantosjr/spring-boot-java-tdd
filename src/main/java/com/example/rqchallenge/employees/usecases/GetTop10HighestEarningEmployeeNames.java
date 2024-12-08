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
public class GetTop10HighestEarningEmployeeNames {

    private static final Logger logger = LoggerFactory.getLogger(GetTop10HighestEarningEmployeeNames.class);

    @Autowired
    private EmployeeService service;

    public GetTop10HighestEarningEmployeeNames(EmployeeService service){
        this.service = service;
    }

    public List<String> execute(){
        logger.info("Executing useCase rules");
        List<Employee> employeeList = this.service.fetchEmployees();
        return employeeList.stream()
                .sorted((e1, e2) -> Double.compare(e2.getSalary(), e1.getSalary()))
                .limit(10)
                .map(Employee::getName)
                .collect(Collectors.toList());
    }
}
