package com.capgemini;

import org.junit.Test;

import com.capgemini.EmployeePayrollDBService.EmpPayrollJDBCOperations;
import com.capgemini.EmployeePayrollDBService.EmployeePayrollJDBCException;

import org.junit.Assert;

public class EmployeePayrollServiceJDBCTest {

	EmpPayrollJDBCOperations e1 = new EmpPayrollJDBCOperations();

	@Test
	public void ConnectToTheDatabase() {
		String status;
		try {
			status = e1.getConnection();
			Assert.assertEquals("Connection Successfull", status);
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}

	}

}
