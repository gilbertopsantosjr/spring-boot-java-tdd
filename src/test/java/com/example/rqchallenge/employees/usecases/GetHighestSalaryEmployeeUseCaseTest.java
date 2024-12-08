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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class GetHighestSalaryEmployeeUseCaseTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    ParameterizedTypeReference<GenericResponse<List<Employee>>> param = new ParameterizedTypeReference<GenericResponse<List<Employee>>>() {};


    @Test
    public void shouldReturnTheHighestSalary_whenThereAreEmployees(){
        Employee[] employees = {new Employee(1000.0,"gilberto"), new Employee(2000.0,"isaac")};

        GenericResponse<List<Employee>> wrapper = new GenericResponse<List<Employee>>();
        wrapper.setData(Arrays.asList(employees));

        ResponseEntity<GenericResponse<List<Employee>>> responseEntity =
                ResponseEntity.ok(wrapper);

        when(restTemplate.exchange(  "null/employees", HttpMethod.GET, null, param)).thenReturn(responseEntity);

        GetHighestSalaryEmployeeUseCase useCase = new GetHighestSalaryEmployeeUseCase(employeeService);
        Optional<Employee> employeeOpt = useCase.execute();

        verify(restTemplate, times(1)).exchange( "null/employees", HttpMethod.GET, null, param);

        assertThat(employeeOpt.isPresent()).isTrue();

        assertThat(employeeOpt.get().getSalary()).isEqualTo(2000.0);
    }

    @Test
    public void shouldReturnTheHighestSalary_whenThereAreNotEmployees(){
        Employee[] employees = {};

        GenericResponse<List<Employee>> wrapper = new GenericResponse<List<Employee>>();
        wrapper.setData(Arrays.asList(employees));

        ResponseEntity<GenericResponse<List<Employee>>> responseEntity =
                ResponseEntity.ok(wrapper);

        when(restTemplate.exchange(  "null/employees", HttpMethod.GET, null, param)).thenReturn(responseEntity);

        GetHighestSalaryEmployeeUseCase useCase = new GetHighestSalaryEmployeeUseCase(employeeService);
        Optional<Employee> employeeOpt = useCase.execute();

        verify(restTemplate, times(1)).exchange( "null/employees", HttpMethod.GET, null, param);

        assertThat(employeeOpt.isPresent()).isFalse();

    }

    

}
