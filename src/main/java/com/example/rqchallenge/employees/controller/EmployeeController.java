package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.usecases.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController implements IEmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final GetAllEmployeesUseCase getAllEmployeesUseCase;
    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final GetHighestSalaryEmployeeUseCase getHighestSalaryEmployeeUseCase;
    private final GetEmployeeByIdUseCase getEmployeeByIdUseCase;
    private final GetEmployeeByNameUseCase getEmployeeByNameUseCase;
    private final GetTop10HighestEarningEmployeeNames getTop10HighestEarningEmployeeNames;
    private final DeleteEmployeeUseCase deleteEmployeeUseCase;


    public EmployeeController(GetAllEmployeesUseCase getAllEmployeesUseCase, CreateEmployeeUseCase createEmployeeUseCase, GetHighestSalaryEmployeeUseCase getHighestSalaryEmployeeUseCase, GetEmployeeByIdUseCase getEmployeeByIdUseCase, GetEmployeeByNameUseCase getEmployeeByNameUseCase, GetTop10HighestEarningEmployeeNames getTop10HighestEarningEmployeeNames, DeleteEmployeeUseCase deleteEmployeeUseCase) {
        this.getAllEmployeesUseCase = getAllEmployeesUseCase;
        this.createEmployeeUseCase = createEmployeeUseCase;
        this.getHighestSalaryEmployeeUseCase = getHighestSalaryEmployeeUseCase;
        this.getEmployeeByIdUseCase = getEmployeeByIdUseCase;
        this.getEmployeeByNameUseCase = getEmployeeByNameUseCase;
        this.getTop10HighestEarningEmployeeNames = getTop10HighestEarningEmployeeNames;
        this.deleteEmployeeUseCase = deleteEmployeeUseCase;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("receive GET /api/v1/employees");
        List<Employee> employees = this.getAllEmployeesUseCase.execute();
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        logger.info("receive GET /api/v1/employees/" + searchString );
        List<Employee> employees = getEmployeeByNameUseCase.execute(searchString);
        if (employees.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        logger.info("receive GET /api/v1/employees/" + id );
        Optional<Employee> resultOpt = this.getEmployeeByIdUseCase.execute(id);
        if(resultOpt.isPresent()){
            Employee employee = resultOpt.get();
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Double> getHighestSalaryOfEmployees() {
        logger.info("receive GET /api/v1/employees/highestSalary");
        Optional<Employee> resultOpt = this.getHighestSalaryEmployeeUseCase.execute();
        if(resultOpt.isPresent()){
            Employee employee = resultOpt.get();
            return ResponseEntity.ok(employee.getSalary());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> employeeNames = this.getTop10HighestEarningEmployeeNames.execute();
        return ResponseEntity.ok(employeeNames);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        logger.info("receive POST /api/v1/employees");
        try{
            if(!employeeInput.containsKey("employee_age")){
                throw new RuntimeException("age is required");
            }
            int age = Integer.parseInt(employeeInput.get("employee_age").toString());

            if(!employeeInput.containsKey("employee_name")){
                throw new RuntimeException("name is required");
            }
            String name = employeeInput.get("employee_name").toString();

            if(!employeeInput.containsKey("employee_salary")){
                throw new RuntimeException("salary is required");
            }
            double salary = Double.parseDouble(employeeInput.get("employee_salary").toString());

            Employee newEmployee = new Employee();
            newEmployee.setAge(age);
            newEmployee.setName(name);
            newEmployee.setSalary(salary);

            Employee result = this.createEmployeeUseCase.execute(newEmployee);
            return ResponseEntity.ok(result);
        } catch (Exception e){
            logger.error("badRequest POST /api/v1/employees", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        boolean isDeleted = deleteEmployeeUseCase.execute(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
