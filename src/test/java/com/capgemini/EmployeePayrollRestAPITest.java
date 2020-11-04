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

	/*@Test
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

	private Response addEmployeeToJsonServer(EmployeePayrollData employeePayrollData) {
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		return request.post("/employees");
	}*/
}
