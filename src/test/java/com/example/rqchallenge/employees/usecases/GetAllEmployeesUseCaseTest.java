package com.example.rqchallenge.employees.usecases;

import com.example.rqchallenge.employees.application.EmployeeService;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.util.GenericResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class GetAllEmployeesUseCaseTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    public GetAllEmployeesUseCaseTest() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    ParameterizedTypeReference<GenericResponse<List<Employee>>> param = new ParameterizedTypeReference<GenericResponse<List<Employee>>>() {};

    @Test
    public void testGetAllEmployees() {
        Employee[] employees = {new Employee("gilberto"), new Employee("isaac")};

        GenericResponse<List<Employee>> wrapper = new GenericResponse<List<Employee>>();
        wrapper.setData(Arrays.asList(employees));

        ResponseEntity<GenericResponse<List<Employee>>> responseEntity =
                ResponseEntity.ok(wrapper);

        when(restTemplate.exchange(  "null/employees", HttpMethod.GET, null, param)).thenReturn(responseEntity);

        GetAllEmployeesUseCase useCase = new GetAllEmployeesUseCase(employeeService);
        List<Employee> employeeList = useCase.execute();

        assertNotNull(employeeList);
        verify(restTemplate, times(1)).exchange( "null/employees", HttpMethod.GET, null, param);

        // Assert that the response contains 2 employees
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).hasSize(2);
    }

    @Test
    public void testGetAllEmployees_emptyList() {
        Employee[] employees = {};

        GenericResponse<List<Employee>> wrapper = new GenericResponse<List<Employee>>();
        wrapper.setData(Arrays.asList(employees));

        ResponseEntity<GenericResponse<List<Employee>>> responseEntity =
                ResponseEntity.ok(wrapper);

        when(restTemplate.exchange(  "null/employees", HttpMethod.GET, null, param)).thenReturn(responseEntity);

        GetAllEmployeesUseCase useCase = new GetAllEmployeesUseCase(employeeService);
        List<Employee> employeeList = useCase.execute();

        verify(restTemplate, times(1)).exchange( "null/employees", HttpMethod.GET, null, param);

        // Assert that the response is an empty array
        assertNotNull(employeeList);
        assertThat(employeeList).isEmpty();
    }

    @Test
    public void shouldHandleServiceFailure() {
        when(restTemplate.exchange(  "null/employees", HttpMethod.GET, null, param)).thenThrow(new RestClientException("Service unavailable"));

        // Then - Call the service method and handle the exception
        try {
            GetAllEmployeesUseCase useCase = new GetAllEmployeesUseCase(employeeService);
            useCase.execute();
        } catch (RuntimeException e) {
            // Verify that RestTemplate was called
            verify(restTemplate, times(1)).exchange( "null/employees", HttpMethod.GET, null, param);;

            // Assert that an exception was thrown
            assertThat(e).hasMessage("Service unavailable");
        }
    }

    @Test
    public void shouldReturnCollectionEmpty_whenDataIsNull() {
        ResponseEntity responseEntity =
                ResponseEntity.noContent ().build();

        when(restTemplate.exchange(  "null/employees", HttpMethod.GET, null, param)).thenReturn(responseEntity);

        GetAllEmployeesUseCase useCase = new GetAllEmployeesUseCase(employeeService);
        List<Employee> employeeList = useCase.execute();

        // Assert that the response is an empty array
        assertNotNull(employeeList);
        assertThat(employeeList).isEmpty();

        verify(restTemplate, times(1)).exchange( "null/employees", HttpMethod.GET, null, param);

    }



}
