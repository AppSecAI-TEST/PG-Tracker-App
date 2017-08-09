package com.ggez.pgtrackerapp.models;

/**
 * Created by Omar Matthew Reyes on 8/9/17.
 */

public class EmployeeType {
    private String employeeTypeName;
    private int employeeTypeId;
    public EmployeeType() {
    }

    public EmployeeType(String employeeTypeName, int employeeTypeId) {
        this.employeeTypeName = employeeTypeName;
        this.employeeTypeId = employeeTypeId;
    }

    public String getEmployeeTypeName() {
        return employeeTypeName;
    }

    public void setEmployeeTypeName(String employeeTypeName) {
        this.employeeTypeName = employeeTypeName;
    }

    public int getEmployeeTypeId() {
        return employeeTypeId;
    }

    public void setEmployeeTypeId(int employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    @Override
    public String toString() {
        return getEmployeeTypeName();
    }
}
