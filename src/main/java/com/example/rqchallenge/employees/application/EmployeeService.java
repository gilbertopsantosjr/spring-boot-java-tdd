package com.example.rqchallenge.employees.application;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.util.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Value("${api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<Employee> fetchEmployeeById(String id) {
        String url = apiUrl + "/employees/" + id;
        try {
            logger.info("Fetching employee by id", url);
            ResponseEntity<GenericResponse<Employee>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<GenericResponse<Employee>>() {
                    }
            );

            GenericResponse<Employee> employeeResponse = responseEntity.getBody();
            return Optional.ofNullable( employeeResponse.getData() );
        } catch (Exception e){
            logger.error("Server did not response well to fetching by id", e.getMessage());
        }
        return Optional.empty();
    }

    public List<Employee> fetchEmployees() {
        String url = apiUrl + "/employees";
        try {
            logger.info("Fetching all employees", url);
            ResponseEntity<GenericResponse<List<Employee>>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<GenericResponse<List<Employee>>>() {
                    }
            );

            GenericResponse<List<Employee>> employeeResponse = responseEntity.getBody();
            return employeeResponse != null ? employeeResponse.getData() : Collections.emptyList();
        } catch (Exception e){
            logger.error("Server did not response well to fetching all employees", e.getMessage());
        }
        return Collections.emptyList();
    }


    public Employee createEmployee(Employee employee) {
        String url = apiUrl + "/create";
        logger.info("POST a new employee", url);
        if(employee == null){
            throw new RuntimeException("Employee is required");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Employee> request = new HttpEntity<>(employee, headers);

        try{
            return restTemplate.postForObject(url, request, Employee.class);
        } catch (Exception e) {
            logger.error("Server did not response well to POST", e.getMessage());
            throw e;
        }
    }

    public boolean deleteEmployeeById(String id) {
        String url = apiUrl + "/delete/" + id;
        try {
            restTemplate.delete(url);
            return true;
        } catch (HttpClientErrorException e) {
            logger.error("Server did not response well to DELETE", e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
            throw e;
        }
    }
}