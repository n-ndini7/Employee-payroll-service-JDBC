package com.capgemini.EmployeePayrollDBService;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.sql.Statement;
import com.capgemini.EmployeePayrollDBService.EmployeePayrollJDBCException.ExceptionType;

//UC2-retrieve data from the table
public class EmpPayrollJDBCOperations {

	private static ConnectionCredentials c1 = null;
	public static String status = null;

	public EmpPayrollJDBCOperations() {
		c1 = new ConnectionCredentials();
	}

	public static Connection getConnection() throws EmployeePayrollJDBCException {
		String[] credentials = c1.getCredentials();
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
		return connection;
	}

	public static List<EmployeePayrollDataJDBC> showTable() throws EmployeePayrollJDBCException {
		List<EmployeePayrollDataJDBC> empList = new ArrayList<EmployeePayrollDataJDBC>();
		String sql = "Select * from employee_payroll;";
		try (Connection conn = getConnection()) {
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				String gender = result.getString("gender");
				double basic_pay = result.getDouble("basic_pay");
				String phone = result.getString("phone_no");
				String dept = result.getString("department");
				String add = result.getString("address");
				double deductions = result.getDouble("deductions");
				double taxable_pay = result.getDouble("taxable_pay");
				double tax = result.getDouble("tax");
				double net_pay = result.getDouble("net_pay");
				LocalDate startDate = result.getDate("start").toLocalDate();
				EmployeePayrollDataJDBC emp = new EmployeePayrollDataJDBC(id, name, gender, basic_pay, phone, dept, add,
						deductions, taxable_pay, tax, net_pay, startDate);
				empList.add(emp);
			}
		} catch (SQLException e) {
			throw new EmployeePayrollJDBCException("Cannot retrieve data!! \nInvalidDatatypeException thrown...!!",
					ExceptionType.INVALID_DATA);
		}

		if (empList == null) {
			throw new EmployeePayrollJDBCException("Empty table!! \nEmptySetException thrown...",
					ExceptionType.EMPTY_SET);
		} else {
			return empList;
		}

	}

	private static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			System.out.println("   " + driverClass.getClass().getName());
		}
	}
}
