package com.capgemini.EmployeePayrollDBService;

//This class throws custom exceptions for employee payroll service JDBC program
public class EmployeePayrollJDBCException extends Exception {

	public enum ExceptionType {
		WRONG_CREDENTIALS, CANNOT_LOAD_DRIVER, INVALID_DATA;
	}

	ExceptionType type;

	public EmployeePayrollJDBCException(String message, ExceptionType type) {
		super(message);
		this.type = type;
	}
}