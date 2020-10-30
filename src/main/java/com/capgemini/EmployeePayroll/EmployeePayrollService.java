package com.capgemini.EmployeePayroll;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

//UC6 - added read data function to read employee payroll data so that some analysis can be performed  

public class EmployeePayrollService {

	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO;
	}

	private List<EmployeePayrollData> employeeList;

	public EmployeePayrollService() {

	}

	public EmployeePayrollService(List<EmployeePayrollData> employeeList) {

		this.employeeList = employeeList;
	}

	public static void main(String[] args) {
		ArrayList<EmployeePayrollData> employeeList = new ArrayList<>();
		EmployeePayrollService empService = new EmployeePayrollService(employeeList);
		Scanner sc = new Scanner(System.in);
		empService.readData(sc);
		empService.writeData(IOService.CONSOLE_IO);
	}

	private void readData(Scanner sc) {
		System.out.println("Enter Employee ID: ");
		int id = Integer.parseInt(sc.nextLine());
		System.out.println("Enter Employee name: ");
		String name = sc.nextLine();
		System.out.println("Enter Employee Salary: ");
		Double salary = Double.parseDouble(sc.nextLine());
		employeeList.add(new EmployeePayrollData(id, name, salary));
	}

	// Reads input from the user

	public void writeData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO))
			System.out.println("Writing Employee Payroll Data to Console\n" + employeeList);
		else if (ioService.equals(IOService.FILE_IO))
			new EmployeePayrollFileIOOperations().writeEmployeePayrollData(employeeList);
	}

	// Writes Employee payroll data to console

	public long countEntries(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO))
			return new EmployeePayrollFileIOOperations().countNoOfEntries();
		return 0;
	}

	// Counts number of employee entries

	public void printData(IOService ioService) {
		new EmployeePayrollFileIOOperations().printEmployeePayrollData();
	}

	// prints employee payroll data

	public List<EmployeePayrollData> readData(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO))
			return new EmployeePayrollFileIOOperations().readEmployeePayrollData();
		else
			return null;
	}

	// reads the employee payroll file
}
