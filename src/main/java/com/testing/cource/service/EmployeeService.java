package com.testing.cource.service;

import com.testing.cource.exception.DuplicateResourceException;
import com.testing.cource.exception.ResourceNotFoundException;
import com.testing.cource.model.Employee;
import com.testing.cource.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    public Employee saveEmp(Employee employee) {

        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new DuplicateResourceException("this email is already exists " + employee.getEmail());
        }

        return employeeRepository.save(employee);
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long id){
        return employeeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("employee with id: "+id+" is not found"));
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee updateEmployee(Long id, Employee employee) {
        Employee dbEmployee = findById(id);
        employee.setId(dbEmployee.getId());
        return saveEmp(employee);
    }
}
