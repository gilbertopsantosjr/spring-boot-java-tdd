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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class GetEmployeeByIdUseCaseTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    ParameterizedTypeReference<GenericResponse<Employee>> param = new ParameterizedTypeReference<GenericResponse<Employee>>() {};

    @Test
    public void testGetEmployeesById_found() throws Exception {
        Employee employee = new Employee(1, "John Doe", 50000, 30, "");

        GenericResponse<Employee> wrapper = new GenericResponse<Employee>();
        wrapper.setData(employee);

        ResponseEntity<GenericResponse<Employee>> responseEntity =
                ResponseEntity.ok(wrapper);

        when(restTemplate.exchange(  "null/employees/1", HttpMethod.GET, null, param)).thenReturn(responseEntity);

        GetEmployeeByIdUseCase useCase = new GetEmployeeByIdUseCase(employeeService);
        Optional<Employee> employeeOpt = useCase.execute("1");

        verify(restTemplate, times(1)).exchange( "null/employees/1", HttpMethod.GET, null, param);

        assertThat(employeeOpt.get()).isNotNull();
    }

    @Test
    public void testGetEmployeesById_notFound() throws Exception {

        GenericResponse<Employee> wrapper = new GenericResponse<Employee>();
        wrapper.setData(null);

        ResponseEntity<GenericResponse<Employee>> responseEntity =
                ResponseEntity.ok(wrapper);

        when(restTemplate.exchange(  "null/employees/1", HttpMethod.GET, null, param)).thenReturn(responseEntity);

        GetEmployeeByIdUseCase useCase = new GetEmployeeByIdUseCase(employeeService);
        Optional<Employee> employeeOpt = useCase.execute("1");

        verify(restTemplate, times(1)).exchange( "null/employees/1", HttpMethod.GET, null, param);

        assertThat(employeeOpt.isEmpty()).isTrue();
    }



    }
