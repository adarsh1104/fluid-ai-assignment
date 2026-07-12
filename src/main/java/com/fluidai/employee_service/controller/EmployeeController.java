package com.fluidai.employee_service.controller;

import com.fluidai.employee_service.entity.Employee;
import com.fluidai.employee_service.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

     @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }


    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return repository.save(employee);
    }

     @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PutMapping("/{id}")
public Employee updateEmployee(@PathVariable Long id,
                               @RequestBody Employee updatedEmployee) {

    Employee employee = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

    employee.setName(updatedEmployee.getName());
    employee.setEmail(updatedEmployee.getEmail());

    return repository.save(employee);
}

}