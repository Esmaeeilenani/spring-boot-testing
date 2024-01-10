package com.testing.cource.integration;



import com.testing.cource.AbstractTestContainer;
import com.testing.cource.model.Employee;

import static org.assertj.core.api.Assertions.*;

import com.testing.cource.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerWebTestClientIT extends AbstractTestContainer {


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee;

    private static final String API_URL = "/api/v1/employees";


    @BeforeEach
    void setUp() {

        this.employee = new Employee()
                .setFirstName("esmaeeil")
                .setLastName("enani")
                .setEmail("esmaeeil@gmail.com");


    }

    @Test
    void givenEmployee_whenSaveEmployee_thenReturnSavedEmployee() throws Exception {
        // given precondition or setup


        // when - action or the behaviour that we are going test

        Employee emp = webTestClient.post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employee), Employee.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(new ParameterizedTypeReference<Employee>() {
                })
                .consumeWith(System.out::println)
                .returnResult()
                .getResponseBody();

        // then verify the output
       assertThat(emp.getId()).isNotNull();


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
        List<Employee> Emps = webTestClient.get()
                .uri(API_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Employee>>() {
                })
                .consumeWith(System.out::println)
                .returnResult()
                .getResponseBody();


        // then verify the output
        assertThat(Emps).isNotEmpty();
        assertThat(Emps.size()).isEqualTo(2);



    }

    @Test
    void givenEmployeeId_whenFindById_thenReturnEmp() throws Exception {
        // given precondition or setup


        // when - action or the behaviour that we are going test


        // then verify the output

    }


    @Test
    void givenEmployeeId_whenFindById_thenReturnError() throws Exception {
        // given precondition or setup


        // when - action or the behaviour that we are going test


        // then verify the output


    }


    @Test
    void givenEmployeeIdAndUpdatedInfo_whenUpdateEmp_thenUpdatedEmp() throws Exception {
        // given precondition or setup


        // when - action or the behaviour that we are going test


        // then verify the output


    }


    @Test
    void givenEmployeeIdAndUpdatedInfo_whenUpdateEmp_thenThrowError() throws Exception {
        // given precondition or setup


        // when - action or the behaviour that we are going test


        // then verify the output


    }


}
