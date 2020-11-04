package com.capgemini.EmployeePayroll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
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
					String gender = dataArr[i].replaceAll("Gender = ", "");
					i++;
					double salary = Double.parseDouble(dataArr[i].replaceAll("Basic Pay = ", ""));
					i++;
					String phone = dataArr[i].replaceAll("Phone = ", "");
					i++;
					String dept = dataArr[i].replaceAll("Department=", "");
					i++;
					String add = dataArr[i].replaceAll("Address=", "");
					i++;
					double ded = Double.parseDouble(dataArr[i].replaceAll("Deductions =", ""));
					i++;
					double tp = Double.parseDouble(dataArr[i].replaceAll("Taxable Pay=", ""));
					i++;
					double tax = Double.parseDouble(dataArr[i].replaceAll("Tax =", ""));
					i++;
					double np = Double.parseDouble(dataArr[i].replaceAll("Net Pay =", ""));
					i++;
					Date start = Date.valueOf(dataArr[i].replaceAll("Start Date =", ""));
					EmployeePayrollData employeePayrollData = new EmployeePayrollData(id, name, gender, salary, phone,
							dept, add, ded, tp, tax, np, start);
					employeeList.add(employeePayrollData);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return employeeList;
	}

}
