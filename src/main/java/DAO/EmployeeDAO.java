package DAO;

import codes.lab9.Objects.Employee;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private static final String URL  = "jdbc:mysql://localhost:3306/employee_directory";
    private static final String USER = "root";
    private static final String PASS = "";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public List<Employee> readAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                employees.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error reading all: " + e.getMessage());
        }

        return employees;
    }

    public Employee findById(String employeeCode) {
        String sql = "SELECT * FROM employees WHERE employee_code = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, employeeCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding employee: " + e.getMessage());
        }

        return null;
    }

    public void save(Employee emp) {
        String sql = "INSERT INTO employees (employee_code, full_name, department, email_address, hire_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emp.getEmployeeCode());
            ps.setString(2, emp.getFullName());
            ps.setString(3, emp.getDepartment());
            ps.setString(4, emp.getEmailAddress());
            ps.setDate(5, Date.valueOf(emp.getHireDate()));
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error saving to database: " + e.getMessage());
        }
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getString("employee_code"),
                rs.getString("full_name"),
                rs.getString("department"),
                rs.getString("email_address"),
                rs.getDate("hire_date").toLocalDate()
        );
    }
}