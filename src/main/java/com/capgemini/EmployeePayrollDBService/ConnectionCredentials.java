package com.capgemini.EmployeePayrollDBService;

//This class contains all login credentials of the SQL server(Confidentiality :High)
public class ConnectionCredentials {

	String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
	String userName = "root";
	String password = "Rn@11041997#";

	public String[] getCredentials() {
		String[] confidential = { jdbcURL, userName, password };
		return confidential;
	}
}


