package com.capgemini;

import org.junit.Test;

import com.capgemini.EmployeePayroll.EmployeePayrollData;
import com.capgemini.EmployeePayroll.EmployeePayrollService;
import com.capgemini.EmployeePayroll.EmployeePayrollService.IOService;
import com.capgemini.EmployeePayrollDBService.EmpPayrollJDBCOperations;
import com.capgemini.EmployeePayrollDBService.EmpPayrollJDBCOperations.SalaryType;
import com.capgemini.EmployeePayrollDBService.EmpPayrollJDBCOperations.UpdateType;
import com.capgemini.EmployeePayrollDBService.EmployeePayrollJDBCException;

import java.sql.Date;
import java.time.Instant;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

public class EmployeePayrollServiceJDBCTest {

	private static EmpPayrollJDBCOperations e1 = new EmpPayrollJDBCOperations();

	@Test
	public void ConnectToTheDatabase() {
		try {
			e1.getConnection();
			Assert.assertEquals("Connection Successfull", e1.status);
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void givenDBShoulRetrieveContentsFromTheTable() {
		try {
			List<EmployeePayrollData> list1 = e1.readEmployeePayrollData();
			Assert.assertEquals(9, list1.size());
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void givenDBShouldUpdateSalaryOfAnEmployee() {
		try {
			Assert.assertTrue(e1.UpdateSalary(UpdateType.STATEMENT));
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void givebDBShouldUpdateSalaryUsingPreparedStatementOfAnEmployee() {
		try {
			Assert.assertTrue(e1.UpdateSalary(UpdateType.PREPARED_STATEMENT));
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void givebDBShouldUpdateSalaryUsingPreparedStatementOfAnEmployee_ShouldBeInSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollDataDB(IOService.DB_IO);
		employeePayrollService.updateEmployeeSalaryUsingPrepareStatement("Terissa", 3000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terissa", 3000000.00);
		Assert.assertTrue(result);
	}

	@Test
	public void givenDBShoulRetreiveEmployeesForASpecficDateRange() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollDataDB(IOService.DB_IO);
		Date startDate = Date.valueOf("2018-01-01");
		Date endDate = Date.valueOf(LocalDate.now());
		List<EmployeePayrollData> empList = employeePayrollService.getEmployeeForDateRange(IOService.DB_IO, startDate,
				endDate);
		Assert.assertEquals(9, empList.size());
	}

	@Test
	public void givenDBShouldRetrieveSalaryGroupByGenderAccordingTotypeAsked() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollDataDB(IOService.DB_IO);
		Map<String, Double> salaryMap = employeePayrollService.readAverageSalaryByGender(IOService.DB_IO,
				SalaryType.SUM);
		Assert.assertTrue(salaryMap.get("M").equals(15000000.0) && salaryMap.get("F").equals(18000000.0));
	}

	@Test
	public void givenDBshouldAddNewAmployeePayrollToTheDB() {
		Date d1 = Date.valueOf("2020-02-11");
		EmployeePayrollData e1 = new EmployeePayrollData(11, "Mark", "M", 6000000.0, d1);
		EmployeePayrollService emp = new EmployeePayrollService();
		emp.readEmployeePayrollDataDB(IOService.DB_IO);
		try {
			emp.addEmployeePayrollDatatoDB(e1);
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}
		boolean result = emp.checkEmployeePayrollInSyncWithDB("Mark", 6000000.00);
		Assert.assertTrue(result);
	}

	@Test
	public void givenDBshouldAddNewAmployeePayrollToTheDBAcordingToERDiagram() {
		Date d1 = Date.valueOf("2020-03-09");
		EmployeePayrollData e1 = new EmployeePayrollData(16, "Rigel", "F", 3000000.0, d1, 4, "HR", 707, "Amazon");
		EmployeePayrollService emp = new EmployeePayrollService();
		emp.readEmployeePayrollDataDB(IOService.DB_IO);
		try {
			emp.addEmployeePayrollDatatoDBER(e1);
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}
		boolean result = emp.checkEmployeePayrollInSyncWithDB("Rigel", 3000000.00);
		Assert.assertTrue(result);
	}

	@Test
	public void givenEmployee_WhenRemovedFromPayroll_ShouldSyncWithDB() {
		EmployeePayrollData e1 = new EmployeePayrollData();
		e1.name = "Bill";
		e1.id = 1;
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollDataDB(IOService.DB_IO);
		employeePayrollService.removeEmployeeFromDB(e1);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Bill", 3000000);
		Assert.assertTrue(result);
	}

	@Test
	public void AddedMultipleContactstoAddressBook() {
		Date d1 = Date.valueOf(LocalDate.now());
		EmployeePayrollData[] employees = {
				new EmployeePayrollData(90, "Eva", "F", 3000000.0, d1, 5, "Sales", 101, "Microsoft"),
				new EmployeePayrollData(70, "Dorak", "M", 3000000.0, d1, 9, "HR", 706, "Amazon"),
				new EmployeePayrollData(21, "Ritu", "F", 3000000.0, d1, 6, "Marketing", 100, "Microsoft") };
		EmployeePayrollService empService = new EmployeePayrollService();
		empService.readEmployeePayrollDataDB(IOService.DB_IO);
		Instant start = Instant.now();
		empService.addMultipleEmployeesToDB(Arrays.asList(employees));
		Instant end = Instant.now();
		System.out.println("Duration without Thread : " + Duration.between(start, end));
		boolean result = (empService.checkEmployeePayrollInSyncWithDB("Eva", 3000000)
				&& empService.checkEmployeePayrollInSyncWithDB("Dorak", 3000000))
				&& empService.checkEmployeePayrollInSyncWithDB("Ritu", 3000000) ? true : false;
		Assert.assertTrue(result);

	}

	@Test
	public void AddedMultipleContacts_threadsImplementation() {
		Date d1 = Date.valueOf(LocalDate.now());
		EmployeePayrollData[] employees = {
				new EmployeePayrollData(90, "Eva", "F", 3000000.0, d1, 5, "Sales", 101, "Microsoft"),
				new EmployeePayrollData(70, "Dorak", "M", 3000000.0, d1, 9, "HR", 706, "Amazon"),
				new EmployeePayrollData(21, "Ritu", "F", 3000000.0, d1, 6, "Marketing", 100, "Microsoft") };
		EmployeePayrollService empService = new EmployeePayrollService();
		empService.readEmployeePayrollDataDB(IOService.DB_IO);
		Instant threadStart = Instant.now();
		empService.addEmployeePayrollDataToDBER_threadsImplementation(Arrays.asList(employees));
		Instant threadEnd = Instant.now();
		System.out.println("Duration with Thread : " + Duration.between(threadStart, threadEnd));
		boolean result = (empService.checkEmployeePayrollInSyncWithDB("Eva", 3000000)
				&& empService.checkEmployeePayrollInSyncWithDB("Dorak", 3000000))
				&& empService.checkEmployeePayrollInSyncWithDB("Ritu", 3000000) ? true : false;
		Assert.assertTrue(result);

	}
}