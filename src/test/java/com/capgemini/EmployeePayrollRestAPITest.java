package com.capgemini;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.capgemini.EmployeePayroll.EmployeePayrollData;
import com.capgemini.EmployeePayroll.EmployeePayrollService;
import com.capgemini.EmployeePayroll.EmployeePayrollService.IOService;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmployeePayrollRestAPITest {

	@Before
	public void Setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	@Test
	public void givenEmployeeInJSONServer_whenRetrieved_ShouldMatchTheCount() {
		EmployeePayrollData[] arrayOfEmps = getEmployeeList();
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		long entries = employeePayrollService.countEntries(IOService.REST_IO);
		Assert.assertEquals(3, entries);
	}

	private EmployeePayrollData[] getEmployeeList() {
		Response response = RestAssured.get("/employees");
		System.out.println("EMPLOYEE PAYROLL ENTRIES IN JSONServer:\n" + response.asString());
		EmployeePayrollData[] arrayOfEmps = new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
		return arrayOfEmps;
	}

	@Test
	public void givenNewEmployee_WhenAdded_ShouldMatchResponseAndCount() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] ArrayOfEmps = getEmployeeList();
		Date d1 = Date.valueOf(LocalDate.now());
		employeePayrollService = new EmployeePayrollService(Arrays.asList(ArrayOfEmps));
		EmployeePayrollData employeePayrollData = new EmployeePayrollData(89, "Marcus", "M", 300000, d1);
		Response response = addEmployeeToJsonServer(employeePayrollData);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);
		employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
		employeePayrollService.addEmployeePayrollToJsonServer(employeePayrollData, IOService.REST_IO);
		long entries = employeePayrollService.countEntries(IOService.REST_IO);
		Assert.assertEquals(3, entries);
	}

	@Test
	public void MultipleEmployeeswhenAdded_ShouldMatchResponseAndCount() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] ArrayOfEmps = getEmployeeList();
		Date d1 = Date.valueOf("2019-08-08");
		Date d2 = Date.valueOf("2018-11-08");
		Date d3 = Date.valueOf("2019-02-12");
		employeePayrollService = new EmployeePayrollService(Arrays.asList(ArrayOfEmps));
		EmployeePayrollData[] arrayOfNewEmps = { new EmployeePayrollData(77, "Sunder", "M", 600000.0, d1),
				new EmployeePayrollData(66, "Mukesh", "M", 100000.0, d2),
				new EmployeePayrollData(88, "Anil", "M", 200000.0, d3) };
		for (EmployeePayrollData employeePayrollData : Arrays.asList(arrayOfNewEmps)) {
			Response response = addEmployeeToJsonServer(employeePayrollData);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);
			employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
			employeePayrollService.addEmployeePayrollToJsonServer(employeePayrollData, IOService.REST_IO);
		}
		long entries = employeePayrollService.countEntries(IOService.REST_IO);
		Assert.assertEquals(6, entries);
	}

	private Response addEmployeeToJsonServer(EmployeePayrollData employeePayrollData) {
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		return request.post("/employees");
	}

	@Test
	public void salaryForEmployeeWhenUpdated_ShouldMatchResponseAndCount() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] ArrayOfEmps = getEmployeeList();
		EmployeePayrollData e1 = new EmployeePayrollData(2, "David", 1234500.0);
		employeePayrollService = new EmployeePayrollService(Arrays.asList(ArrayOfEmps));
		employeePayrollService.updateSalaryinJson(e1.name, e1.basic_pay, IOService.REST_IO);
		EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayrollData("David");
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		Response response = request.put("/employees/" + employeePayrollData.id);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
	}

	@Test
	public void givenEmployeeName_WhenDeleted_ShouldMatch200ResponseAndCount() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] ArrayOfEmps = getEmployeeList();
		employeePayrollService = new EmployeePayrollService(Arrays.asList(ArrayOfEmps));
		EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayrollData("Mukesh");
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		Response response = request.delete("/employees/" + employeePayrollData.id);
		employeePayrollService.deleteEmployeePayroll(employeePayrollData.name, IOService.REST_IO);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
	}

}
