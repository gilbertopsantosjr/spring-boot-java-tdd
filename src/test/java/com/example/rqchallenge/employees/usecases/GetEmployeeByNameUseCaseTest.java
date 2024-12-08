package com.example.rqchallenge.employees.usecases;

import com.example.rqchallenge.employees.application.EmployeeService;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.util.GenericResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class GetEmployeeByNameUseCaseTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    ParameterizedTypeReference<GenericResponse<List<Employee>>> param = new ParameterizedTypeReference<GenericResponse<List<Employee>>>() {};

    @Test
    public void shouldReturnCollectionEmtptyWhenNoMatchesOrContainsWhereFoundByName() {
        Employee[] employees = {};

        GenericResponse<List<Employee>> wrapper = new GenericResponse<List<Employee>>();
        wrapper.setData(Arrays.asList(employees));

        ResponseEntity<GenericResponse<List<Employee>>> responseEntity =
                ResponseEntity.ok(wrapper);

        when(restTemplate.exchange("null/employees", HttpMethod.GET, null, param)).thenReturn(responseEntity);

        GetEmployeeByNameUseCase useCase = new GetEmployeeByNameUseCase(employeeService);
        List<Employee> employeeList = useCase.execute("gilberto");

        assertNotNull(employeeList);
        verify(restTemplate, times(1)).exchange("null/employees", HttpMethod.GET, null, param);

        assertThat(employeeList).isEmpty();
    }

    @Test
    public void shouldReturnAllEmployeesWhoseNameMatchesTheStringInputProvided(){
        Employee[] employees = {new Employee("gilberto"), new Employee("isaac")};

        GenericResponse<List<Employee>> wrapper = new GenericResponse<List<Employee>>();
        wrapper.setData(Arrays.asList(employees));

        ResponseEntity<GenericResponse<List<Employee>>> responseEntity =
                ResponseEntity.ok(wrapper);

        when(restTemplate.exchange(  "null/employees", HttpMethod.GET, null, param)).thenReturn(responseEntity);

        GetEmployeeByNameUseCase useCase = new GetEmployeeByNameUseCase(employeeService);
        List<Employee> employeeList = useCase.execute("gilberto");

        assertNotNull(employeeList);
        verify(restTemplate, times(1)).exchange( "null/employees", HttpMethod.GET, null, param);

        // Assert that the response contains 1 employees
        assertThat(employeeList).hasSize(1);
        assertThat(employeeList.get(0).getName()).isEqualTo("gilberto");
    }

    @Test
    public void shouldReturnAllEmployeesWhoseNameContainsTheStringInputProvided(){
        Employee[] employees = {new Employee("gilberto"), new Employee("isaac")};

        GenericResponse<List<Employee>> wrapper = new GenericResponse<List<Employee>>();
        wrapper.setData(Arrays.asList(employees));

        ResponseEntity<GenericResponse<List<Employee>>> responseEntity =
                ResponseEntity.ok(wrapper);

        when(restTemplate.exchange(  "null/employees", HttpMethod.GET, null, param)).thenReturn(responseEntity);

        GetEmployeeByNameUseCase useCase = new GetEmployeeByNameUseCase(employeeService);
        List<Employee> employeeList = useCase.execute("isaa");

        assertNotNull(employeeList);
        verify(restTemplate, times(1)).exchange( "null/employees", HttpMethod.GET, null, param);

        // Assert that the response contains 1 employees
        assertThat(employeeList).hasSize(1);
        assertThat(employeeList.get(0).getName()).isEqualTo("isaac");

    }

}
