package com.capgemini.EmployeePayroll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//This class performs employee payroll IO service and operations 
public class EmployeePayrollFileIOOperations {

	public static String PAYROLL_TEXT_FILE = "src/PayrollExamples.txt";

	public void writeEmployeePayrollData(List<EmployeePayrollData> employeeList) {
		StringBuffer buffer = new StringBuffer();
		employeeList.forEach(employee -> {
			String employeeDataString = employee.toString().concat("\n");
			buffer.append(employeeDataString);
		});
		try {
			Files.write(Paths.get(PAYROLL_TEXT_FILE), buffer.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long countNoOfEntries() {
		long entries = 0;
		try {
			entries = Files.lines(new File(PAYROLL_TEXT_FILE).toPath()).count();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entries;
	}

	public void printEmployeePayrollData() {
		try {
			Files.lines(new File(PAYROLL_TEXT_FILE).toPath()).forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<EmployeePayrollData> readEmployeePayrollData() {
		List<EmployeePayrollData> employeeList = new ArrayList<EmployeePayrollData>();
		try {
			Files.lines(new File(PAYROLL_TEXT_FILE).toPath()).map(line -> line.trim()).forEach(line -> {
				String data = line.toString();
				String[] dataArr = data.split(",");
				for (int i = 0; i < dataArr.length; i++) {
					int id = Integer.parseInt(dataArr[i].replaceAll("ID = ", ""));
					i++;
					String name = dataArr[i].replaceAll("Name = ", "");
					i++;
					double salary = Double.parseDouble(dataArr[i].replaceAll("Salary = ", ""));
					EmployeePayrollData employeePayrollData = new EmployeePayrollData(id, name, salary);
					employeeList.add(employeePayrollData);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return employeeList;
	}

}
