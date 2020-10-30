package com.capgemini;

import org.junit.Test;

import com.capgemini.EmployeePayroll.EmployeePayrollData;
import com.capgemini.EmployeePayrollDBService.EmpPayrollJDBCOperations;
import com.capgemini.EmployeePayrollDBService.EmpPayrollJDBCOperations.UpdateType;
import com.capgemini.EmployeePayrollDBService.EmployeePayrollJDBCException;
import java.util.List;

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
			Assert.assertEquals(3, list1.size());
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

	//@Test
	//public void givebDBShouldUpdateSalaryUsingPreparedStatementOfAnEmployee_ShouldBeInSyncWithDB() {
		//EmpPayrollJDBCOperations employeePayrollService = new EmpPayrollJDBCOperations();
		//List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData();
		//employeePayrollService.UpdateSalary(UpdateType.PREPARED_STATEMENT);
	//	boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa", 2000000.00);
		//Assert.assertTrue(result);
	//}
}
