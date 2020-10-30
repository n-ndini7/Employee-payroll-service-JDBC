package com.capgemini;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.capgemini.EmployeePayroll.EmployeePayrollData;
import com.capgemini.EmployeePayroll.EmployeePayrollService;
import com.capgemini.EmployeePayroll.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {

	Date d1 = Date.valueOf("2018-01-03");
	Date d2 = Date.valueOf("2020-05-21");
	Date d3 = Date.valueOf("2019-11-13");

	@Test
	public void given3EmployeeEntries_ShouldMatchTheEmployeeEntries_WhenWrittenToTheFile() {
		EmployeePayrollData[] empArray = {
				new EmployeePayrollData(1, "Bill", "M", 3000000, null, "", null, 0, 0, 0, 0, d1),
				new EmployeePayrollData(3, "Charlie", "M", 3000000, null, "", null, 0, 0, 0, 0, d2),
				new EmployeePayrollData(6, "Terissa", "F", 3000000, null, "", null, 0, 0, 0, 0, d3) };
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(empArray));
		employeePayrollService.writeData(IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(IOService.FILE_IO);
		assertEquals(3, entries);
	}

	// test method to check if the employee data is written to the file

	@Test
	public void given3EmployeeEntries_ShouldMatchTheEmployeeEntries_WhenWrittenToTheFile_AndPrintTheSame() {
		EmployeePayrollData[] empArray = {
				new EmployeePayrollData(1, "Bill", "M", 3000000, null, "", null, 0, 0, 0, 0, d1),
				new EmployeePayrollData(3, "Charlie", "M", 3000000, null, "", null, 0, 0, 0, 0, d2),
				new EmployeePayrollData(6, "Terissa", "F", 3000000, null, "", null, 0, 0, 0, 0, d3) };
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(empArray));
		employeePayrollService.writeData(IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(IOService.FILE_IO);
		// employeePayrollService.printData(IOService.FILE_IO); // print method called
		assertEquals(3, entries);
	}

	// test method to check for print function in EmployeePayrollFileIOOperations

	@Test
	public void given3EmployeeEntries_ShouldMatchTheEmployeeEntries_WhenWrittenToTheFile_AndReadTheEmployeePayrollFile() {
		EmployeePayrollData[] empArray = {
				new EmployeePayrollData(1, "Bill", "M", 3000000, null, "", null, 0, 0, 0, 0, d1),
				new EmployeePayrollData(3, "Charlie", "M", 3000000, null, "", null, 0,0, 0, 0, d2),
				new EmployeePayrollData(6, "Terissa", "F", 3000000, null, "", null, 0, 0, 0, 0, d3) };
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(empArray));
		employeePayrollService.writeData(IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(IOService.FILE_IO);
		employeePayrollService.printData(IOService.FILE_IO);//
		List<EmployeePayrollData> employeeList = employeePayrollService.readData(IOService.DB_IO);
		assertEquals(3, entries);
	}

	// test method to check for read data function
}
