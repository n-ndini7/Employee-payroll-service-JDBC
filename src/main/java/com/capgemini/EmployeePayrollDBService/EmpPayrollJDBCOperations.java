package com.capgemini.EmployeePayrollDBService;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.Statement;

import com.capgemini.EmployeePayroll.EmployeePayrollData;
import com.capgemini.EmployeePayrollDBService.EmployeePayrollJDBCException.ExceptionType;

//MultiThreading_UC2 - add multiple employees to the database using threads and record start and stop time to determine the time taken for execution 
public class EmpPayrollJDBCOperations {
	private static PreparedStatement employeePayrollDataStatement;
	public static EmpPayrollJDBCOperations emp;

	public enum SalaryType {
		AVG, SUM, MAX, MIN;
	}

	SalaryType type2;

	public enum UpdateType {
		STATEMENT, PREPARED_STATEMENT;
	}

	UpdateType type;
	private static ConnectionCredentials c1 = null;
	public static String status = null;

	public EmpPayrollJDBCOperations() {
		c1 = new ConnectionCredentials();
	}

	public static EmpPayrollJDBCOperations getInstance() {
		if (emp == null) {
			emp = new EmpPayrollJDBCOperations();
		}
		return emp;
	}

	public static Connection getConnection() throws EmployeePayrollJDBCException {
		String[] credentials = c1.getCredentials();
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new EmployeePayrollJDBCException("Cannot connect to the JDBC Driver!! \nDriverException thrown...",
					ExceptionType.CANNOT_LOAD_DRIVER);
		}
		listDrivers();
		try {
			connection = DriverManager.getConnection(credentials[0], credentials[1], credentials[2]);
			status = "Connection Successfull";
		} catch (Exception e) {
			throw new EmployeePayrollJDBCException("Unable to Connect!! \nWrongLoginCredentialsException thrown... ",
					ExceptionType.WRONG_CREDENTIALS);
		}
		return connection;
	}

	public List<EmployeePayrollData> readEmployeePayrollData() throws EmployeePayrollJDBCException {
		List<EmployeePayrollData> empList = new ArrayList<EmployeePayrollData>();
		String sql = "Select * from employee_payroll2;";
		try (Connection conn = getConnection()) {
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			empList = this.getEmployeePayrollData(result);
		} catch (SQLException e) {
			throw new EmployeePayrollJDBCException("Cannot retrieve data!! \nInvalidDatatypeException thrown...!!",
					ExceptionType.INVALID_DATA);
		}
		return empList;

	}

	public boolean UpdateSalary(UpdateType type) throws EmployeePayrollJDBCException {
		double salary = 3000000;
		String name = "Terissa";
		boolean update = true;
		int res = 0;
		try (Connection con2 = getConnection()) {
			String type1 = type.toString();
			switch (type1) {
			case "STATMENT": {
				String sql2 = String.format("UPDATE employee_payroll SET basic_pay = %.2f WHERE name = '%s';", salary,
						name);
				Statement statement = con2.createStatement();
				res = statement.executeUpdate(sql2);
				if (res == 0) {
					update = false;
					throw new EmployeePayrollJDBCException("Cannot update data!! \nUpdateFailException thrown...!!",
							ExceptionType.UPDATE_FAILED);
				}
			}
			case "PREPARED_STATEMENT": {
				String sql2 = String.format("UPDATE employee_payroll2 SET basic_pay = ? WHERE name = ? ;", salary,
						name);
				PreparedStatement pstmnt = con2.prepareStatement(sql2);
				pstmnt.setDouble(1, salary);
				pstmnt.setString(2, name);
				res = pstmnt.executeUpdate();
				pstmnt.close();
				if (res == 0) {
					update = false;
					throw new EmployeePayrollJDBCException("Cannot update data!! \nUpdateFailException thrown...!!",
							ExceptionType.UPDATE_FAILED);
				}

			}
			}
		} catch (SQLException e) {
			update = false;
			throw new EmployeePayrollJDBCException("Cannot update data!! \nUpdateFailException thrown...!!",
					ExceptionType.UPDATE_FAILED);
		}
		return update;
	}

	public List<EmployeePayrollData> getEmployeePayrollData(String name) {
		List<EmployeePayrollData> employeePayrollData = null;
		if (employeePayrollDataStatement == null)
			this.preparedStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollData = getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollData;
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet result) {
		List<EmployeePayrollData> empList = new ArrayList<>();
		try {
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				String gender = result.getString("gender");
				double basic_pay = result.getDouble("basic_pay");
				Date startDate = result.getDate("start");
				EmployeePayrollData emp = new EmployeePayrollData(id, name, gender, basic_pay, startDate);
				empList.add(emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return empList;
	}

	private void preparedStatementForEmployeeData() {
		try {
			Connection con = getConnection();
			String sql = "select * from employee_payroll2 where name = ?";
			employeePayrollDataStatement = con.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}
	}

	public List<EmployeePayrollData> QueryForGivenDateRange(Date startDate, Date endDate) {
		String sql = String.format("select * from employee_payroll2 where start between '%s' and '%s';", startDate,
				endDate);
		List<EmployeePayrollData> empList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			empList = this.getEmployeePayrollData(result);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EmployeePayrollJDBCException e1) {
			e1.printStackTrace();
		}
		return empList;
	}

	public Map<String, Double> QueryForSalaryByGender(SalaryType type) {
		String type2 = type.toString();
		String sql6 = "";
		Map<String, Double> SalaryMap = new HashMap<>();
		try (Connection connection = getConnection()) {
			switch (type2) {
			case "AVG": {
				sql6 = "select gender,avg(basic_pay) as Retrived_Salary from employee_payroll2 group by gender;";
			}
			case "MAX": {
				sql6 = "select gender,max(basic_pay) as Retrived_Salary from employee_payroll2 group by gender;";
			}
			case "MIN": {
				sql6 = "select gender,min(basic_pay) as Retrived_Salary from employee_payroll2 group by gender;";
			}
			case "SUM": {
				sql6 = "select gender,sum(basic_pay) as Retrived_Salary from employee_payroll2 group by gender;";
			}
			}
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql6);
			while (result.next()) {
				String gender = result.getString("gender");
				double salary = result.getDouble("Retrived_Salary");
				SalaryMap.put(gender, salary);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EmployeePayrollJDBCException e1) {
			e1.printStackTrace();
		}
		return SalaryMap;
	}

	public static int addEmployeePayroll(EmployeePayrollData e1) throws EmployeePayrollJDBCException {
		int res = 0;
		String sql7 = String.format(
				"insert into employee_payroll2(name,gender,basic_pay,start)" + " values ('%s','%s','%.2f','%s');",
				e1.name, e1.gender, e1.basic_pay, e1.startDate);
		try (Connection connection = getConnection()) {
			Statement statement = connection.createStatement();
			res = statement.executeUpdate(sql7);
		} catch (EmployeePayrollJDBCException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return res;
	}

	public static int addEmployeeWithPayrollDetails(EmployeePayrollData e1) throws EmployeePayrollJDBCException {
		int res = 0, res2 = 0;
		Connection connection = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(true);
		} catch (SQLException e4) {
			throw new EmployeePayrollJDBCException("Cannot insert data!! \nInsertRecordFailedException thrown...!!",
					ExceptionType.INSERT_FAILED);
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}
		try (Statement statement1 = connection.createStatement();) {
			String sql8 = String.format(
					"insert into employee_payroll2 (id,name,basic_pay,start,gender) values ('%d','%s',%.2f,'%s','%s');",
					e1.id, e1.name, e1.basic_pay, e1.startDate, e1.gender);
			res = statement1.executeUpdate(sql8);
		} catch (SQLException e4) {
			try {
				connection.rollback();
			} catch (SQLException er) {
				throw new EmployeePayrollJDBCException("Cannot insert data!!\nInsertRecordFailedException thrown...!!",
						ExceptionType.INSERT_FAILED);
			}
		}
		try {
			connection = getConnection();
			connection.setAutoCommit(true);
		} catch (SQLException er) {
			throw new EmployeePayrollJDBCException("Cannot insert data!! \nInsertRecordFailedException thrown...!!",
					ExceptionType.INSERT_FAILED);
		}
		try (Statement statement2 = connection.createStatement();) {
			e1.deductions = e1.basic_pay * 0.2;
			e1.taxable_pay = e1.basic_pay - e1.deductions;
			e1.tax = e1.taxable_pay * 0.1;
			e1.net_pay = e1.basic_pay - e1.tax;
			String sql = String.format(
					"insert into payroll_details (empid,basic_pay,deductions,taxable_pay,tax,net_pay) values ('%d','%.2f','%.2f','%.2f','%.2f','%.2f');",
					e1.id, e1.basic_pay, e1.deductions, e1.taxable_pay, e1.tax, e1.net_pay);
			res2 = statement2.executeUpdate(sql);
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException er) {
				throw new EmployeePayrollJDBCException("Cannot insert data!!\nInsertRecordFailedException thrown...!!",
						ExceptionType.INSERT_FAILED);
			}
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return res + res2;
	}

	public static int addEmployeeWithPayrollDetailsER(EmployeePayrollData e1) {
		int res1 = 0, res2 = 0, res3 = 0, res4 = 0, res5 = 0;
		Connection connection = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format("insert into company2(companyid,company_name) values ('%d','%s');", e1.compid,
					e1.company);
			res1 = statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e9) {
				e9.printStackTrace();
			}
		}
		if (res1 > 0) {
			try (Statement statement = connection.createStatement()) {
				String sql = String.format(
						"insert into employee_payroll2 (id,name,basic_pay,start,gender) values ('%d','%s',%.2f,'%s','%s');",
						e1.id, e1.name, e1.basic_pay, e1.startDate, e1.gender);
				res2 = statement.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					connection.rollback();
				} catch (SQLException e9) {
					e9.printStackTrace();
				}
			}
		}
		if (res2 > 0) {
			try (Statement statement = connection.createStatement()) {
				String sql = String.format("insert into department2(deptid,deptname,empid) values ('%d','%s','%d');",
						e1.deptid, e1.dept, e1.id);
				res3 = statement.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					connection.rollback();
				} catch (SQLException e9) {
					e9.printStackTrace();
				}
			}
		}
		if (res3 > 0) {
			try (Statement statement = connection.createStatement()) {
				e1.deductions = e1.basic_pay * 0.2;
				e1.taxable_pay = e1.basic_pay - e1.deductions;
				e1.tax = e1.taxable_pay * 0.1;
				e1.net_pay = e1.basic_pay - e1.tax;
				String sql = String.format(
						"insert into payroll_details (empid,basic_pay,deductions,taxable_pay,tax,net_pay) values ('%d','%.2f',%.2f,%.2f,%.2f,%.2f);",
						e1.id, e1.basic_pay, e1.deductions, e1.taxable_pay, e1.tax, e1.net_pay);
				res4 = statement.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					connection.rollback();
				} catch (SQLException e9) {
					e9.printStackTrace();
				}
			}
		}
		if (res4 > 0) {
			try (Statement statement = connection.createStatement()) {
				String sql = String.format("insert into employee_department2 (empid,deptid) values ('%d','%d');", e1.id,
						e1.deptid);
				res5 = statement.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					connection.rollback();
				} catch (SQLException e9) {
					e9.printStackTrace();
				}
			}
		}

		try {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return res1 + res2 + res3 + res4 + res5;
	}

	public int removeEmployeeFromDB(EmployeePayrollData e1) {
		Connection connection = null;
		int res = 0;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}
		int employeeId = 0;
		try (Statement statement = connection.createStatement()) {
			String sql = String.format("select * from employee_payroll2 where name = '%s';", e1.name);
			ResultSet result = statement.executeQuery(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format("update employee_payroll2 set is_active = false where id = %d;", e1.id);
			res = statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e9) {
				e9.printStackTrace();
			}
		}
		try {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}

	private static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
		}
	}

}