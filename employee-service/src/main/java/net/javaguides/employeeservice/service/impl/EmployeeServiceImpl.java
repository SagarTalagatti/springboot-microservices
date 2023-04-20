package net.javaguides.employeeservice.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.employeeservice.dto.EmployeeDto;
import net.javaguides.employeeservice.entity.Employee;
import net.javaguides.employeeservice.exception.ResourceNotFoundException;
import net.javaguides.employeeservice.mapper.AutoEmployeeMapper;
import net.javaguides.employeeservice.repository.EmployeeRepository;
import net.javaguides.employeeservice.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private ModelMapper modelMapper;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

//        Employee employee=new Employee(
//                employeeDto.getId(),
//                employeeDto.getFirstName(),
//                employeeDto.getLastName(),
//                employeeDto.getEmail()
//        );

//        Employee employee=AutoEmployeeMapper.MAPPER.mapToEmployee(employeeDto);
        Employee employee=modelMapper.map(employeeDto, Employee.class);

        Employee savedEmployee=employeeRepository.save(employee);

//        EmployeeDto savedEmployeeDto=new EmployeeDto(
//                savedEmployee.getId(),
//                savedEmployee.getFirstName(),
//                savedEmployee.getLastName(),
//                savedEmployee.getEmail()
//        );

//        EmployeeDto savedEmployeeDto=AutoEmployeeMapper.MAPPER.mapToEmployeeDto(savedEmployee);

        EmployeeDto savedEmployeeDto=modelMapper.map(savedEmployee, EmployeeDto.class);

        return savedEmployeeDto;
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {

        Employee employee=employeeRepository.findById(employeeId).orElseThrow(
                ()->new ResourceNotFoundException("Employee","id",employeeId)
        );

//        EmployeeDto employeeDto=new EmployeeDto(
//                employee.getId(),
//                employee.getFirstName(),
//                employee.getLastName(),
//                employee.getEmail()
//        );

//        EmployeeDto employeeDto=AutoEmployeeMapper.MAPPER.mapToEmployeeDto(employee);

        EmployeeDto employeeDto=modelMapper.map(employee, EmployeeDto.class);

        return employeeDto;
    }
}
