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
public class GetTop10HighestEarningEmployeeNamesTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    ParameterizedTypeReference<GenericResponse<List<Employee>>> param = new ParameterizedTypeReference<GenericResponse<List<Employee>>>() {};

    @Test
    public void shouldReturnTop10EmployeesNameWith10HighestEarning(){
        Employee[] employees = {
                new Employee(1000.0,"gilberto"),
                new Employee(2000.0,"isaac"),
                new Employee(2100.0,"fernanda"),
                new Employee(2150.0,"schimanski"),
                new Employee(2155.0,"santos"),
                new Employee(2155.0,"junior"),
                new Employee(2255.0,"jefte"),
                new Employee(2455.0,"davi"),
                new Employee(2555.0,"rezende"),
                new Employee(2655.0,"levina"),
                new Employee(2755.0,"quezia"),
                new Employee(2855.0,"priscilla"),
                new Employee(2955.0,"ysabel"),
                new Employee(3055.0,"juda"),
                new Employee(3155.0,"pedro"),
                new Employee(3255.0,"marcos"),
        };

        GenericResponse<List<Employee>> wrapper = new GenericResponse<List<Employee>>();
        wrapper.setData(Arrays.asList(employees));

        ResponseEntity<GenericResponse<List<Employee>>> responseEntity =
                ResponseEntity.ok(wrapper);

        when(restTemplate.exchange(  "null/employees", HttpMethod.GET, null, param)).thenReturn(responseEntity);

        GetTop10HighestEarningEmployeeNames useCase = new GetTop10HighestEarningEmployeeNames(employeeService);
        List<String> employeeList = useCase.execute();

        verify(restTemplate, times(1)).exchange( "null/employees", HttpMethod.GET, null, param);

        assertNotNull(employeeList);


        assertThat(employeeList.get(0)).isEqualTo("marcos");
        assertThat(employeeList.get(1)).isEqualTo("pedro");
        assertThat(employeeList.get(2)).isEqualTo("juda");
        assertThat(employeeList.get(3)).isEqualTo("ysabel");
        assertThat(employeeList.get(4)).isEqualTo("priscilla");
        assertThat(employeeList.get(5)).isEqualTo("quezia");
        assertThat(employeeList.get(6)).isEqualTo("levina");
        assertThat(employeeList.get(7)).isEqualTo("rezende");
        assertThat(employeeList.get(8)).isEqualTo("davi");
        assertThat(employeeList.get(9)).isEqualTo("jefte");

        assertThat(employeeList.size() == 10).isTrue();
    }

}
