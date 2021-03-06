package com.capgemini.EmployeePayroll;

import java.sql.Date;

public class EmployeePayrollData {
	public int id;
	public String name;
	public String gender;
	public double basic_pay;
	public String phone;
	public String dept;
	public String add;
	public double deductions;
	public double taxable_pay;
	public double tax;
	public double net_pay;
	public Date startDate;
	public int deptid;
	public String company;
	public int compid;

	public EmployeePayrollData(int id, String name, String gender, double basic_pay, String phone, String dept,
			String add, double deductions, double taxable_pay, double tax, double net_pay, Date start) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.basic_pay = basic_pay;
		this.phone = phone;
		this.dept = dept;
		this.add = add;
		this.deductions = deductions;
		this.taxable_pay = taxable_pay;
		this.tax = tax;
		this.net_pay = net_pay;
		this.startDate = start;
	}

	public EmployeePayrollData(String name, String gender, double basic_pay, Date start) {
		this.name = name;
		this.gender = gender;
		this.basic_pay = basic_pay;
		this.startDate = start;
	}

	public EmployeePayrollData(int id, String name, String gender, double basic_pay, Date start) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.basic_pay = basic_pay;
		this.startDate = start;
	}

	public EmployeePayrollData(int id, String name, String gender, double basic_pay, Date start, int deptid,
			String dept, int compid, String company) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.basic_pay = basic_pay;
		this.startDate = start;
		this.deptid = deptid;
		this.dept = dept;
		this.compid = compid;
		this.company = company;
	}

	public EmployeePayrollData() {
		// TODO Auto-generated constructor stub
	}

	public EmployeePayrollData(int id, String name, double basic_pay) {
		this.id = id;
		this.name = name;
		this.basic_pay = basic_pay;
	}

	@Override
	public String toString() {
		return "ID = " + id + ", Name = " + name + ", Gender = " + gender + ", Basic Pay = " + basic_pay + ", Phone = "
				+ phone + ", Department=" + dept + ". Address=" + add + ", Deductions=" + deductions + ", Taxable Pay="
				+ taxable_pay + ", Tax=" + tax + ", Net Pay= " + net_pay + ", Start Date=" + startDate + ", Company: "
				+ company + ", Company Id: " + compid;
	}

}
