package com.capgemini;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.capgemini.EmployeePayroll.EmployeePayrollData;
import com.capgemini.EmployeePayroll.EmployeePayrollService;
import com.capgemini.EmployeePayroll.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {
	@Test
	public void given3EmployeeEntries_ShouldMatchTheEmployeeEntries_WhenWrittenToTheFile() {
		EmployeePayrollData[] empArray = { new EmployeePayrollData(200456, "Warren Buffet", 500000.0),
				new EmployeePayrollData(200457, "Amanico Ortega", 200000.0),
				new EmployeePayrollData(200551, "Larry Ellison", 800000.0) };
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(empArray));
		employeePayrollService.writeData(IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(IOService.FILE_IO);
		assertEquals(3, entries);
	}

	// test method to check if the employee data is written to the file

	@Test
	public void given3EmployeeEntries_ShouldMatchTheEmployeeEntries_WhenWrittenToTheFile_AndPrintTheSame() {
		EmployeePayrollData[] empArray = { new EmployeePayrollData(200456, "Warren Buffet", 500000.0),
				new EmployeePayrollData(200457, "Amanico Ortega", 200000.0),
				new EmployeePayrollData(200551, "Larry Ellison", 800000.0) };
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(empArray));
		employeePayrollService.writeData(IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(IOService.FILE_IO);
		employeePayrollService.printData(IOService.FILE_IO); // print method called
		assertEquals(3, entries);
	}

	// test method to check for print function in EmployeePayrollFileIOOperations

	@Test
	public void given3EmployeeEntries_ShouldMatchTheEmployeeEntries_WhenWrittenToTheFile_AndReadTheEmployeePayrollFile() {
		EmployeePayrollData[] empArray = { new EmployeePayrollData(200456, "Warren Buffet", 500000.0),
				new EmployeePayrollData(200457, "Amanico Ortega", 200000.0),
				new EmployeePayrollData(200551, "Larry Ellison", 800000.0) };
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(empArray));
		employeePayrollService.writeData(IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(IOService.FILE_IO);
		employeePayrollService.printData(IOService.FILE_IO);
		List<EmployeePayrollData> employeeList = employeePayrollService.readData(IOService.FILE_IO);
		System.out.println(employeeList);
		assertEquals(3, entries);
	}

	// test method to check for read data function

}
