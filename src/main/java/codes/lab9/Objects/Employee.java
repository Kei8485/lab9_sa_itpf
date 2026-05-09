package codes.lab9.Objects;

import java.time.LocalDate;

public class Employee {
    private String employeeCode;
    private String fullName;
    private String department;
    private String emailAddress;
    private LocalDate hireDate;

    public Employee() {}



    // Constructor ko
    public Employee(String employeeCode, String fullName, String department, String emailAddress, LocalDate hireDate){
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.department = department;
        this.emailAddress = emailAddress;
        this.hireDate = hireDate;
    }

    // getters ko
    public String getEmployeeCode(){
        return employeeCode;
    }

    public String getFullName(){
        return fullName;
    }

    public String getDepartment(){
        return department;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public LocalDate getHireDate(){
        return hireDate;
    }

    // setter ko

    public void setEmployeeCode(String employeeCode){
        this.employeeCode = employeeCode;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public void setDepartment(String department){
        this.department = department;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }

    public void setHireDate(LocalDate hireDate){
        this.hireDate = hireDate;
    }

    @Override
    public String toString(){
        return employeeCode + " | " + fullName + " | " + department;
    }

}

