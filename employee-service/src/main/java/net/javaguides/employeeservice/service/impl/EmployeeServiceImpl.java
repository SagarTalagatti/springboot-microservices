package net.javaguides.employeeservice.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private ModelMapper modelMapper;

    private static final Logger LOGGER=LoggerFactory.getLogger(EmployeeServiceImpl.class);

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

//    @CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Override
    public APIResponseDto getEmployeeById(Long employeeId) {

        LOGGER.info("Inside getEmployeeById(Long employeeId) method");

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

    public APIResponseDto getDefaultDepartment(Long employeeId, Exception ex) {

        LOGGER.info("Inside getDefaultDepartment(Long employeeId) method");

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
//        DepartmentDto departmentDto=apiClient.getDepartment(employee.getDepartmentCode());
//        EmployeeDto employeeDto=new EmployeeDto(
//                employee.getId(),
//                employee.getFirstName(),
//                employee.getLastName(),
//                employee.getEmail()
//        );

//        EmployeeDto employeeDto=AutoEmployeeMapper.MAPPER.mapToEmployeeDto(employee);

        DepartmentDto departmentDto=new DepartmentDto();
        departmentDto.setDepartmentName("R&D");
        departmentDto.setDepartmentCode("RD101");
        departmentDto.setDepartmentDescription("Research and Development");

        EmployeeDto employeeDto=modelMapper.map(employee, EmployeeDto.class);

        APIResponseDto apiResponseDto=new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);

        return apiResponseDto;
    }
}
