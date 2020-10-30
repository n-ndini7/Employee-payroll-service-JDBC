package com.capgemini;

import org.junit.Test;

import com.capgemini.EmployeePayrollDBService.EmpPayrollJDBCOperations;
import com.capgemini.EmployeePayrollDBService.EmployeePayrollDataJDBC;
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
			List<EmployeePayrollDataJDBC> list1 = e1.showTable();
			Assert.assertEquals(7, list1.size());
			System.out.println(list1);
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}

	}

}
