package com.example.rqchallenge.employees.usecases;

import com.example.rqchallenge.employees.application.EmployeeService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.example.rqchallenge.employees.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CreateEmployeeUseCaseTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    public CreateEmployeeUseCaseTest(){
        employee = new Employee(1, "John Doe", 50000, 30, "profile");
    }

    @Test
    public void testCreateEmployeeThenSuccess() {
        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(employee);

        CreateEmployeeUseCase useCase = new CreateEmployeeUseCase(employeeService);
        Employee createdEmployee = useCase.execute(employee);

        assertThat( createdEmployee.getName()).isEqualTo("John Doe");
        assertThat( createdEmployee.getSalary()).isEqualTo(50000d);
        assertThat( createdEmployee.getAge()).isEqualTo(30);
    }

    @Test
    public void testCreateEmployee_BadRequest() {
        // Simulating a 400 Bad Request error
        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Call the method and expect an exception
        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            employeeService.createEmployee(employee);
        });

        // Verifying the exception is due to bad request
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(((HttpClientErrorException) exception).getStatusCode());
    }

    @Test
    public void testCreateEmployee_InternalServerError() {
        // Simulating a 500 Internal Server Error
        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Call the method and expect an exception
        Exception exception = assertThrows(HttpServerErrorException.class, () -> {
            employeeService.createEmployee(employee);
        });

        // Verifying the exception is due to server error
        assertThat(HttpStatus.INTERNAL_SERVER_ERROR).isEqualTo(((HttpServerErrorException) exception).getStatusCode());
    }

    @Test
    public void testCreateEmployee_TimeoutOrOtherException() {
        // Simulating a general exception (e.g., timeout or network error)
        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new RuntimeException("Connection Timeout"));

        // Call the method and expect an exception
        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeService.createEmployee(employee);
        });

        // Verifying the exception message
        assertThat("Connection Timeout").isEqualTo(exception.getMessage());
    }

    @Test
    public void testCreateEmployee_NullResponse() {
        // Simulating a case where the API returns a null response
        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(null);

        // Call the method and expect the result to be null
        Employee createdEmployee = employeeService.createEmployee(employee);

        // Verifying that the employee returned is null
        assertThat(createdEmployee).isNull();
    }

}
