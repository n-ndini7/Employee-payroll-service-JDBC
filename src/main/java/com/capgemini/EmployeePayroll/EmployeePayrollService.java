package com.capgemini.EmployeePayroll;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.capgemini.EmployeePayrollDBService.EmpPayrollJDBCOperations;
import com.capgemini.EmployeePayrollDBService.EmpPayrollJDBCOperations.SalaryType;
import com.capgemini.EmployeePayrollDBService.EmpPayrollJDBCOperations.UpdateType;
import com.capgemini.EmployeePayrollDBService.EmployeePayrollJDBCException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class EmployeePayrollService {

	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO;
	}

	private List<EmployeePayrollData> employeeList;
	// private List<EmployeePayrollData> employeePayrollList;
	private EmpPayrollJDBCOperations employeePayrollDBService;

	public EmployeePayrollService() {
		employeePayrollDBService = EmpPayrollJDBCOperations.getInstance();
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
		System.out.println("Enter Employee gender: ");
		String gender = sc.nextLine();
		System.out.println("Enter Employee Salary: ");
		Double salary = Double.parseDouble(sc.nextLine());
		System.out.println("Enter Employee phone: ");
		String phone = sc.nextLine();
		System.out.println("Enter Employee department: ");
		String dept = sc.nextLine();
		System.out.println("Enter Employee Address: ");
		String add = sc.nextLine();
		System.out.println("Enter Employee Deductions: ");
		Double ded = Double.parseDouble(sc.nextLine());
		System.out.println("Enter Employee Taxable pay: ");
		Double tp = Double.parseDouble(sc.nextLine());
		System.out.println("Enter Employee tax: ");
		Double tax = Double.parseDouble(sc.nextLine());
		System.out.println("Enter Employee net pay: ");
		Double net = Double.parseDouble(sc.nextLine());
		System.out.println("Enter the Start Date :");
		Date date = Date.valueOf(sc.nextLine());
		employeeList.add(new EmployeePayrollData(id, name, gender, salary, phone, dept, add, ded, tp, tax, net, date));
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

	public List<EmployeePayrollData> readEmployeePayrollDataDB(IOService dbIo) {
		if (dbIo.equals(IOService.DB_IO)) {
			try {
				this.employeeList = employeePayrollDBService.readEmployeePayrollData();
			} catch (EmployeePayrollJDBCException e) {
				e.printStackTrace();
			}
		}
		return this.employeeList;
	}

	public void updateEmployeeSalary(String name, double salary) {
		boolean result;
		try {
			result = employeePayrollDBService.UpdateSalary(UpdateType.PREPARED_STATEMENT);
			if (result == false)
				return;
			EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
			if (employeePayrollData != null)
				employeePayrollData.basic_pay = salary;
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}

	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		for (EmployeePayrollData data : employeeList) {
			if (data.name.equals(name)) {
				return data;
			}
		}
		return null;
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name, double salary) {
		for (EmployeePayrollData data : employeeList) {
			if (data.name.equals(name)) {
				if (Double.compare(data.basic_pay, salary) == 0) {
					// System.out.println(data);
					return true;
				}
			}
		}
		return false;
	}

	public void updateEmployeeSalaryUsingPrepareStatement(String name, double salary) {
		boolean result;
		try {
			result = employeePayrollDBService.UpdateSalary(UpdateType.PREPARED_STATEMENT);
			if (result == false)
				return;
			EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
			if (employeePayrollData != null)
				employeePayrollData.basic_pay = salary;
		} catch (EmployeePayrollJDBCException e) {
			e.printStackTrace();
		}

	}

	public List<EmployeePayrollData> getEmployeeForDateRange(IOService DB_IO, Date startDate, Date endDate) {
		if (DB_IO.equals(IOService.DB_IO)) {
			return employeePayrollDBService.QueryForGivenDateRange(startDate, endDate);
		}
		return null;
	}

	public Map<String, Double> readAverageSalaryByGender(IOService DB_IO, SalaryType type) {
		if (DB_IO.equals(IOService.DB_IO)) {
			return employeePayrollDBService.QueryForSalaryByGender(type);
		}
		return null;
	}

	public int addEmployeePayrollDatatoDB(EmployeePayrollData e) throws EmployeePayrollJDBCException {
		int check2 = 0;
		int count = EmpPayrollJDBCOperations.addEmployeeWithPayrollDetails(e);
		if (count == 2) {
			employeeList.add(e);
			check2++;
		}
		System.out.println(employeeList);
		return check2;
	}

	public int addEmployeePayrollDatatoDBER(EmployeePayrollData e) throws EmployeePayrollJDBCException {
		int check2 = 0;
		int count = EmpPayrollJDBCOperations.addEmployeeWithPayrollDetailsER(e);
		if (count == 5) {
			employeeList.add(e);
			check2++;
		}
		return check2;
	}

	public void addMultipleEmployeesToDB(List<EmployeePayrollData> list) {
		for (EmployeePayrollData e3 : list) {
			int count = EmpPayrollJDBCOperations.addEmployeeWithPayrollDetailsER(e3);
			if (count == 5) {
				employeeList.add(e3);
			}
		}
	}

	public synchronized void addEmployeePayrollDataToDBER_threadsImplementation(List<EmployeePayrollData> list) {
		Map<Integer, Boolean> statusMap = new HashMap<Integer, Boolean>();
		list.forEach(e1 -> {
			statusMap.put(e1.hashCode(), false);
			Runnable task = () -> {
				System.out.println("Employee Being Added : " + Thread.currentThread().getName());
				try {
					this.addEmployeePayrollDatatoDBER(e1);
				} catch (EmployeePayrollJDBCException e) {
					e.printStackTrace();
				}
				statusMap.put(e1.hashCode(), true);
				System.out.println("Employee Added : " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, e1.name);
			thread.start();
		});
		while (statusMap.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {

			}
		}
	}

	public boolean removeEmployeeFromDB(EmployeePayrollData e1) {
		int check = employeePayrollDBService.removeEmployeeFromDB(e1);
		if (check == 1)
			return true;
		else
			return false;
	}
}