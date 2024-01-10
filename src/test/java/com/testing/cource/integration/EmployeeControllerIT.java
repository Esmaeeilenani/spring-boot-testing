package com.testing.cource.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.cource.AbstractTestContainer;
import com.testing.cource.model.Employee;
import com.testing.cource.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIT extends AbstractTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    private static final String API_URL = "/api/v1/employees";


    @BeforeEach
    void setUp() {

        this.employee = new Employee()
                .setId(1L)
                .setFirstName("esmaeeil")
                .setLastName("enani")
                .setEmail("esmaeeil@gmail.com");


        employeeRepository.deleteAll();
    }

    @Test
    void givenEmployee_whenSaveEmployee_thenReturnSavedEmployee() throws Exception {
        // given precondition or setup


        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then verify the output
        response
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));


    }

    @Test
    void givenEmployeeList_whenDoRestRequest_thenReturnListOfEmployee() throws Exception {
        // given precondition or setup
        Employee employee1 = new Employee()
                .setFirstName("esmaeeil2")
                .setLastName("enani2")
                .setEmail("esmaeeil2@email.com");

        List<Employee> employees = List.of(employee, employee1);

        employeeRepository.saveAll(employees);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get(API_URL));


        // then verify the output
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employees.size())));


    }

    @Test
    void givenEmployeeId_whenFindById_thenReturnEmp() throws Exception {
        // given precondition or setup
        Employee e = employeeRepository.save(employee);


        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get(API_URL + "/{id}", e.getId()));


        // then verify the output
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }


    @Test
    void givenEmployeeId_whenFindById_thenReturnError() throws Exception {
        // given precondition or setup


        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get(API_URL + "/{id}", employee.getId()));


        // then verify the output
        response
                .andDo(print())
                .andExpect(status().isNotFound());


    }


    @Test
    void givenEmployeeIdAndUpdatedInfo_whenUpdateEmp_thenUpdatedEmp() throws Exception {
        // given precondition or setup

        Employee saveEmp = employeeRepository.save(employee);

        Employee updateEmployee = new Employee()
                .setFirstName("esmaeeil2")
                .setLastName("enani2")
                .setEmail("esmaeeil2@gmail.com");


        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put(API_URL + "/{id}", saveEmp.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));


        // then verify the output
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saveEmp.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(updateEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updateEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updateEmployee.getEmail())));


    }


    @Test
    void givenEmployeeIdAndUpdatedInfo_whenUpdateEmp_thenThrowError() throws Exception {
        // given precondition or setup

        Employee saveEmp = employeeRepository.save(employee);

        Employee updateEmployee = new Employee()
                .setFirstName("esmaeeil2")
                .setLastName("enani2")
                .setEmail("esmaeeil@gmail.com");


        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put(API_URL + "/{id}", saveEmp.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));


        // then verify the output
        response
                .andDo(print())
                .andExpect(status().isInternalServerError());


    }


}
