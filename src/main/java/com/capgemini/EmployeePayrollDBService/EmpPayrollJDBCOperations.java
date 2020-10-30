package com.capgemini.EmployeePayrollDBService;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import com.capgemini.EmployeePayrollDBService.EmployeePayrollJDBCException.ExceptionType;

//Uc1 - create payroll service database and connect the java program to database
public class EmpPayrollJDBCOperations {

	private ConnectionCredentials c1 = null;
	// String[] credentials;

	public EmpPayrollJDBCOperations() {
		c1 = new ConnectionCredentials();
	}

	public String getConnection() throws EmployeePayrollJDBCException {
		String[] credentials = c1.getCredentials();
		String status = null;
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");
		} catch (ClassNotFoundException e) {
			throw new EmployeePayrollJDBCException("Cannot connect to the JDBC Driver!! \nDriverException thrown...",
					ExceptionType.CANNOT_LOAD_DRIVER);
		}
		listDrivers();
		try {
			System.out.println("Connecting to database:" + credentials[0]);
			connection = DriverManager.getConnection(credentials[0], credentials[1], credentials[2]);
			status = "Connection Successfull";
			System.out.println(status);
		} catch (Exception e) {
			throw new EmployeePayrollJDBCException("Unable to Connect!! \nWrongLoginCredentialsException thrown... ",
					ExceptionType.WRONG_CREDENTIALS);
		}
		return status;
	}

	private static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			System.out.println("   " + driverClass.getClass().getName());
		}
	}
}


