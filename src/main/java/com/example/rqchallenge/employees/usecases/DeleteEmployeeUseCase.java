package com.example.rqchallenge.employees.usecases;

import com.example.rqchallenge.employees.application.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteEmployeeUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteEmployeeUseCase.class);

    @Autowired
    private EmployeeService service;

    public DeleteEmployeeUseCase(EmployeeService service){
        this.service = service;
    }

    public boolean execute(String id) {
        logger.info("Executing useCase rules");
        return this.service.deleteEmployeeById(id);
    }
}
