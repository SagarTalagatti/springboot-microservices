package net.javaguides.employeeservice.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.employeeservice.dto.APIResponseDto;
import net.javaguides.employeeservice.dto.DepartmentDto;
import net.javaguides.employeeservice.dto.EmployeeDto;
import net.javaguides.employeeservice.entity.Employee;
import net.javaguides.employeeservice.exception.ResourceNotFoundException;
import net.javaguides.employeeservice.mapper.AutoEmployeeMapper;
import net.javaguides.employeeservice.repository.EmployeeRepository;
import net.javaguides.employeeservice.service.APIClient;
import net.javaguides.employeeservice.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private ModelMapper modelMapper;

//    private RestTemplate restTemplate;

//    private WebClient webClient;

    private APIClient apiClient;

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
    public APIResponseDto getEmployeeById(Long employeeId) {

        Employee employee=employeeRepository.findById(employeeId).orElseThrow(
                ()->new ResourceNotFoundException("Employee","id",employeeId)
        );

//        ResponseEntity<DepartmentDto> responseEntity=restTemplate.getForEntity("http://localhost:8088/api/departments/"+employee.getDepartmentCode(), DepartmentDto.class);
//        DepartmentDto departmentDto=responseEntity.getBody();

//        DepartmentDto departmentDto=webClient.get()
//                                            .uri("http://localhost:8088/api/departments/"+employee.getDepartmentCode())
//                                            .retrieve()
//                                            .bodyToMono(DepartmentDto.class)
//                                            .block();
        DepartmentDto departmentDto=apiClient.getDepartment(employee.getDepartmentCode());
//        EmployeeDto employeeDto=new EmployeeDto(
//                employee.getId(),
//                employee.getFirstName(),
//                employee.getLastName(),
//                employee.getEmail()
//        );

//        EmployeeDto employeeDto=AutoEmployeeMapper.MAPPER.mapToEmployeeDto(employee);

        EmployeeDto employeeDto=modelMapper.map(employee, EmployeeDto.class);

        APIResponseDto apiResponseDto=new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);

        return apiResponseDto;
    }
}
