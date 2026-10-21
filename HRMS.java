import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HRMS {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("========================================");
            System.out.println("       HRMS MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Create New Organization");
            System.out.println("2. Login to Organization");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    registerOrganization(sc);
                    break;

                case "2":
                    loginOrganization(sc);
                    break;


                case "3":
                    running = false;
                    System.out.println("Thank you for using HRMS!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
    }

    private static void registerOrganization(Scanner sc) {

        System.out.println("--- ORGANIZATION REGISTRATION ---");

        System.out.print("Organization name: ");
        String name = sc.nextLine().trim();

        System.out.print("Address: ");
        String address = sc.nextLine().trim();

        System.out.print("PAN: ");
        String pan = sc.nextLine().trim();

        System.out.print("GST: ");
        String gst = sc.nextLine().trim();

        System.out.print("Department: ");
        String department = sc.nextLine().trim();

        System.out.print("Login email: ");
        String email = sc.nextLine().trim();

        System.out.print("Password: ");
        String password = sc.nextLine();

        System.out.print("Employee count: ");
        int employeeCount;

        try {
            employeeCount = Integer.parseInt(sc.nextLine().trim());

            if (employeeCount < 0) {
                System.out.println(
                        "Employee count cannot be negative."
                );
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid employee count.");
            return;
        }

        if (name.isBlank() || email.isBlank()
                || password.isBlank()) {
            System.out.println(
                    "Name, email and password are required."
            );
            return;
        }


        String sql = "INSERT INTO organizations " +
                "(Organization_name, Address, Pan, gst, " +
                "Department, Email, LoginPassword, Employee_count) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, pan);
            ps.setString(4, gst);
            ps.setString(5, department);
            ps.setString(6, email);


            ps.setString(7, password);

            ps.setInt(8, employeeCount);

            ps.executeUpdate();

            System.out.println(
                    "\nOrganization registered successfully!"
            );
            System.out.println(
                    "Organization details saved in MySQL."
            );

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println(
                        "This email is already registered."
                );
            } else {
                System.out.println(
                        "Database error: " + e.getMessage()
                );
            }
        }
    }



    public static void loginOrganization(Scanner sc) {
        System.out.println("\n===== Organization Login =====");

        System.out.print("Enter Organization Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        String sql = "SELECT Organization_id, Organization_name " +
                "FROM organizations " +
                "WHERE Email = ? AND LoginPassword = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int organizationId = rs.getInt("Organization_id");
                    String organizationName =
                            rs.getString("Organization_name");

                    System.out.println("Login successful!");
                    System.out.println("Welcome, " + organizationName + "!");
                    System.out.println("Organization ID: " + organizationId);


                    {


                        while (true) {
                            System.out.println();
                            System.out.println("+==========================================+");
                            System.out.println("|              HRMS PORTAL                 |");
                            System.out.println("+==========================================+");
                            System.out.println("| Organization: " + organizationName);
                            System.out.println("| Organization ID: " + organizationId);
                            System.out.println("+==========================================+");
                            System.out.println("|              MAIN DASHBOARD              |");
                            System.out.println("+==========================================+");
                            System.out.println("|  1. Employee Management                  |");
                            System.out.println("|  2. Attendance Management                |");
                            System.out.println("|  3. Salary & Payroll                     |");
                            System.out.println("|  4. Notice Management                    |");
                            System.out.println("|  5. Holiday Management                   |");
                            System.out.println("|  6. Logout                               |");
                            System.out.println("+==========================================+");
                            System.out.print("  Enter your choice: ");

                            String choice = sc.nextLine().trim();

                            switch (choice) {
                                case "1":
                                    employeeManagement(sc, organizationId);
                                    break;

                                case "2":
                                    attendanceManagement(sc, organizationId);
                                    break;

                                case "3":
                                    salaryPayrollManagement(sc, organizationId);
                                    break;

                                case "4":
                                    noticeManagement(sc, organizationId);
                                    break;


                                case "5":
                                    holidayManagement(sc, organizationId);
                                    break;


                                case "6":
                                    System.out.println("\nYou have logged out successfully.");
                                    return;

                                default:
                                    System.out.println(
                                            "Invalid choice. Please enter a number from 1 to 6.");
                            }

                            System.out.println("\nPress Enter to return to the dashboard...");
                            sc.nextLine();
                        }
                    }

                } else {
                    System.out.println(
                            "Invalid email or password. Please try again."
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error during login.");
            e.printStackTrace();
        }
    }





    public static void employeeManagement(
            Scanner sc, int organizationId) {

        while (true) {
            System.out.println("\n+==================================+");
            System.out.println("|       EMPLOYEE MANAGEMENT       |");
            System.out.println("+==================================+");
            System.out.println("| 1. Add Employee                 |");
            System.out.println("| 2. View Employees               |");
            System.out.println("| 3. Update Employee              |");
            System.out.println("| 4. Delete Employee              |");
            System.out.println("| 5. Back to Dashboard            |");
            System.out.println("+==================================+");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    addEmployee(sc, organizationId);
                    break;

                case "2":
                    viewEmployees(organizationId);
                    break;

                case "3":
                    updateEmployee(sc, organizationId);
                    break;

                case "4":
                    deleteEmployee(sc, organizationId);
                    break;

                case "5":
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

            if (!choice.equals("5")) {
                System.out.println("\nPress Enter to continue...");
                sc.nextLine();
            }
        }
    }


    public static void addEmployee(
            Scanner sc, int organizationId) {

        System.out.println("\n--- ADD EMPLOYEE ---");

        System.out.print("Employee name: ");
        String name = sc.nextLine().trim();

        System.out.print("Employee email: ");
        String email = sc.nextLine().trim();

        System.out.print("Phone number: ");
        String phone = sc.nextLine().trim();

        System.out.print("Department: ");
        String department = sc.nextLine().trim();

        System.out.print("Designation: ");
        String designation = sc.nextLine().trim();

        System.out.print("Salary: ");
        String salaryInput = sc.nextLine().trim();

        if (name.isEmpty() || email.isEmpty()) {
            System.out.println("Name and email are required.");
            return;
        }

        try {
            java.math.BigDecimal salary =
                    new java.math.BigDecimal(salaryInput);

            if (salary.signum() < 0) {
                System.out.println("Salary cannot be negative.");
                return;
            }

            String sql = "INSERT INTO employees " +
                    "(organization_id, employee_name, email, Phone, " +
                    "department, designation, salary) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (java.sql.Connection con =
                         DatabaseConnection.getConnection();
                 java.sql.PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setInt(1, organizationId);
                ps.setString(2, name);
                ps.setString(3, email);
                ps.setString(4, phone);
                ps.setString(5, department);
                ps.setString(6, designation);
                ps.setBigDecimal(7, salary);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println(
                            "Employee added successfully!");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println(
                    "Invalid salary. Enter a number, such as 25000.00.");
        } catch (java.sql.SQLException e) {
            System.out.println("Could not add employee.");
            e.printStackTrace();
        }
    }

    public static void viewEmployees(int organizationId) {

        String sql = "SELECT employee_id, employee_name, email, " +
                "phone, department, designation, salary " +
                "FROM employees WHERE organization_id = ? " +
                "ORDER BY employee_id";

        try (java.sql.Connection con =
                     DatabaseConnection.getConnection();
             java.sql.PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, organizationId);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                System.out.println("\n--- EMPLOYEE LIST ---");

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    System.out.println("------------------------------");
                    System.out.println("ID: " +
                            rs.getInt("employee_id"));
                    System.out.println("Name: " +
                            rs.getString("employee_name"));
                    System.out.println("Email: " +
                            rs.getString("email"));
                    System.out.println("Phone: " +
                            rs.getString("phone"));
                    System.out.println("Department: " +
                            rs.getString("department"));
                    System.out.println("Designation: " +
                            rs.getString("designation"));
                    System.out.println("Salary: " +
                            rs.getBigDecimal("salary"));
                }

                if (!found) {
                    System.out.println("No employees found yet.");
                }
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Could not retrieve employees.");
            e.printStackTrace();
        }
    }

    public static void updateEmployee(
            Scanner sc, int organizationId) {

        System.out.println("\n--- UPDATE EMPLOYEE ---");

        try {
            System.out.print("Enter Employee ID to update: ");
            int employeeId = Integer.parseInt(sc.nextLine().trim());

            System.out.print("New employee name: ");
            String name = sc.nextLine().trim();

            System.out.print("New employee email: ");
            String email = sc.nextLine().trim();

            System.out.print("New phone number: ");
            String phone = sc.nextLine().trim();

            System.out.print("New department: ");
            String department = sc.nextLine().trim();

            System.out.print("New designation: ");
            String designation = sc.nextLine().trim();

            System.out.print("New salary: ");
            String salaryInput = sc.nextLine().trim();

            if (name.isEmpty() || email.isEmpty()) {
                System.out.println("Name and email are required.");
                return;
            }

            java.math.BigDecimal salary =
                    new java.math.BigDecimal(salaryInput);

            if (salary.signum() < 0) {
                System.out.println("Salary cannot be negative.");
                return;
            }

            String sql = "UPDATE employees SET " +
                    "employee_name = ?, email = ?, phone = ?, " +
                    "department = ?, designation = ?, salary = ? " +
                    "WHERE employee_id = ? AND organization_id = ?";

            try (java.sql.Connection con =
                         DatabaseConnection.getConnection();
                 java.sql.PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, phone);
                ps.setString(4, department);
                ps.setString(5, designation);
                ps.setBigDecimal(6, salary);
                ps.setInt(7, employeeId);
                ps.setInt(8, organizationId);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println(
                            "Employee updated successfully!");
                } else {
                    System.out.println(
                            "Employee ID not found in your organization.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println(
                    "Invalid input. Employee ID and salary must be numbers.");
        } catch (java.sql.SQLException e) {
            System.out.println("Could not update employee.");
            e.printStackTrace();
        }
    }

    public static void deleteEmployee(
            Scanner sc, int organizationId) {

        System.out.println("\n--- DELETE EMPLOYEE ---");

        try {
            System.out.print("Enter Employee ID to delete: ");
            int employeeId = Integer.parseInt(sc.nextLine().trim());

            System.out.print(
                    "Are you sure you want to delete this employee? (yes/no): ");
            String confirmation = sc.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("Delete operation cancelled.");
                return;
            }

            String sql = "DELETE FROM employees " +
                    "WHERE employee_id = ? AND organization_id = ?";

            try (java.sql.Connection con =
                         DatabaseConnection.getConnection();
                 java.sql.PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setInt(1, employeeId);
                ps.setInt(2, organizationId);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println(
                            "Employee deleted successfully!");
                } else {
                    System.out.println(
                            "Employee ID not found in your organization.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid numeric Employee ID.");
        } catch (java.sql.SQLException e) {
            System.out.println("Could not delete employee.");
            System.out.println(
                    "The employee may have attendance or payroll records linked to them.");
            e.printStackTrace();
        }
    }



    public static void attendanceManagement(
            Scanner sc, int organizationId) {

        while (true) {
            System.out.println("\n+==================================+");
            System.out.println("|      ATTENDANCE MANAGEMENT      |");
            System.out.println("+==================================+");
            System.out.println("| 1. Mark Attendance              |");
            System.out.println("| 2. View Attendance              |");
            System.out.println("| 3. Back to Dashboard            |");
            System.out.println("+==================================+");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    markAttendance(sc, organizationId);
                    break;

                case "2":
                    viewAttendance(organizationId);
                    break;

                case "3":
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

            if (!choice.equals("3")) {
                System.out.println("\nPress Enter to continue...");
                sc.nextLine();
            }
        }
    }



    public static void markAttendance(
            Scanner sc, int organizationId) {

        System.out.println("\n--- MARK ATTENDANCE ---");

        try {
            System.out.print("Enter Employee ID: ");
            int employeeId = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter date (YYYY-MM-DD): ");
            java.time.LocalDate date =
                    java.time.LocalDate.parse(sc.nextLine().trim());

            System.out.println("1. Present");
            System.out.println("2. Absent");
            System.out.println("3. On Leave");
            System.out.print("Select status: ");

            String choice = sc.nextLine().trim();
            String status;

            switch (choice) {
                case "1":
                    status = "Present";
                    break;
                case "2":
                    status = "Absent";
                    break;
                case "3":
                    status = "On Leave";
                    break;
                default:
                    System.out.println("Invalid attendance status.");
                    return;
            }

            System.out.print("Remarks (optional): ");
            String remarks = sc.nextLine().trim();

            String sql = "INSERT INTO attendance " +
                    "(organization_id, employee_id, attendance_date, status, remarks) " +
                    "SELECT ?, employee_id, ?, ?, ? FROM employees " +
                    "WHERE employee_id = ? AND organization_id = ?";

            try (java.sql.Connection con =
                         DatabaseConnection.getConnection();
                 java.sql.PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setInt(1, organizationId);
                ps.setDate(2, java.sql.Date.valueOf(date));
                ps.setString(3, status);
                ps.setString(4, remarks.isEmpty() ? null : remarks);
                ps.setInt(5, employeeId);
                ps.setInt(6, organizationId);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println(
                            "Attendance marked successfully!");
                } else {
                    System.out.println(
                            "Employee not found in your organization.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Employee ID must be a valid number.");
        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD.");
        } catch (java.sql.SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println(
                        "Attendance already exists for this employee on that date.");
            } else {
                System.out.println("Could not save attendance.");
                e.printStackTrace();
            }
        }
    }




    public static void viewAttendance(int organizationId) {

        String sql = "SELECT a.attendance_id, a.employee_id, " +
                "e.employee_name, a.attendance_date, a.status, a.remarks " +
                "FROM attendance a " +
                "JOIN employees e ON a.employee_id = e.employee_id " +
                "WHERE a.organization_id = ? " +
                "ORDER BY a.attendance_date DESC, a.employee_id";

        try (java.sql.Connection con =
                     DatabaseConnection.getConnection();
             java.sql.PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, organizationId);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                System.out.println("\n--- ATTENDANCE RECORDS ---");

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    System.out.println("----------------------------------");
                    System.out.println("Attendance ID: " +
                            rs.getInt("attendance_id"));
                    System.out.println("Employee ID: " +
                            rs.getInt("employee_id"));
                    System.out.println("Employee Name: " +
                            rs.getString("employee_name"));
                    System.out.println("Date: " +
                            rs.getDate("attendance_date"));
                    System.out.println("Status: " +
                            rs.getString("status"));
                    System.out.println("Remarks: " +
                            rs.getString("remarks"));
                }

                if (!found) {
                    System.out.println("No attendance records found.");
                }
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Could not retrieve attendance.");
            e.printStackTrace();
        }
    }

    public static void salaryPayrollManagement(
            Scanner sc, int organizationId) {

        while (true) {
            System.out.println("\n+==================================+");
            System.out.println("|       SALARY & PAYROLL           |");
            System.out.println("+==================================+");
            System.out.println("| 1. Generate Payroll              |");
            System.out.println("| 2. View Payroll Records          |");
            System.out.println("| 3. Back to Dashboard             |");
            System.out.println("+==================================+");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    generatePayroll(sc, organizationId);
                    break;
                case "2":
                    viewPayroll(organizationId);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

            if (!choice.equals("3")) {
                System.out.println("\nPress Enter to continue...");
                sc.nextLine();
            }
        }
    }


    public static void generatePayroll(
            Scanner sc, int organizationId) {

        System.out.println("\n--- GENERATE PAYROLL ---");

        try {
            System.out.print("Enter Employee ID: ");
            int employeeId = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter payroll month (YYYY-MM): ");
            String monthInput = sc.nextLine().trim();

            java.time.YearMonth yearMonth =
                    java.time.YearMonth.parse(monthInput);

            java.sql.Date payrollMonth = java.sql.Date.valueOf(
                    yearMonth.atDay(1));

            System.out.print("Enter basic salary: ");
            java.math.BigDecimal basic =
                    new java.math.BigDecimal(sc.nextLine().trim());

            System.out.print("Enter allowances: ");
            java.math.BigDecimal allowances =
                    new java.math.BigDecimal(sc.nextLine().trim());

            System.out.print("Enter deductions: ");
            java.math.BigDecimal deductions =
                    new java.math.BigDecimal(sc.nextLine().trim());

            if (basic.signum() < 0 ||
                    allowances.signum() < 0 ||
                    deductions.signum() < 0) {
                System.out.println(
                        "Salary values cannot be negative.");
                return;
            }

            java.math.BigDecimal netSalary =
                    basic.add(allowances).subtract(deductions);

            if (netSalary.signum() < 0) {
                System.out.println(
                        "Deductions cannot exceed basic salary plus allowances.");
                return;
            }

            String sql = "INSERT INTO payroll " +
                    "(organization_id, employee_id, payroll_month, " +
                    "basic_salary, allowances, deductions, net_salary) " +
                    "SELECT ?, employee_id, ?, ?, ?, ?, ? FROM employees " +
                    "WHERE employee_id = ? AND organization_id = ?";

            try (java.sql.Connection con =
                         DatabaseConnection.getConnection();
                 java.sql.PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setInt(1, organizationId);
                ps.setDate(2, payrollMonth);
                ps.setBigDecimal(3, basic);
                ps.setBigDecimal(4, allowances);
                ps.setBigDecimal(5, deductions);
                ps.setBigDecimal(6, netSalary);
                ps.setInt(7, employeeId);
                ps.setInt(8, organizationId);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("+==================================+");
                    System.out.println("|       PAYROLL GENERATED          |");
                    System.out.println("+==================================+");
                    System.out.println("Employee ID : " + employeeId);
                    System.out.println("Month       : " + monthInput);
                    System.out.println("Basic Salary: " + basic);
                    System.out.println("Allowances  : " + allowances);
                    System.out.println("Deductions  : " + deductions);
                    System.out.println("------------------------------------");
                    System.out.println("Net Salary  : " + netSalary);
                    System.out.println("Payment     : Pending");
                    System.out.println("+==================================+");
                } else {
                    System.out.println(
                            "Employee not found in your organization.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println(
                    "Employee ID or salary amount is invalid.");
        } catch (java.time.format.DateTimeParseException e) {
            System.out.println(
                    "Invalid month. Enter it as YYYY-MM, for example 2026-10.");
        } catch (java.sql.SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println(
                        "Payroll already exists for this employee and month.");
            } else {
                System.out.println("Could not generate payroll.");
                e.printStackTrace();
            }
        }
    }


    public static void viewPayroll(int organizationId) {

        String sql = "SELECT p.payroll_id, p.employee_id, " +
                "e.employee_name, p.payroll_month, " +
                "p.basic_salary, p.allowances, p.deductions, " +
                "p.net_salary, p.payment_status " +
                "FROM payroll p " +
                "JOIN employees e ON p.employee_id = e.employee_id " +
                "WHERE p.organization_id = ? " +
                "ORDER BY p.payroll_month DESC, p.employee_id";

        try (java.sql.Connection con =
                     DatabaseConnection.getConnection();
             java.sql.PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, organizationId);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                System.out.println("\n--- PAYROLL RECORDS ---");

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    System.out.println("\n------------------------------------");
                    System.out.println("Payroll ID   : " +
                            rs.getInt("payroll_id"));
                    System.out.println("Employee ID  : " +
                            rs.getInt("employee_id"));
                    System.out.println("Employee Name: " +
                            rs.getString("employee_name"));
                    System.out.println("Month        : " +
                            rs.getDate("payroll_month"));
                    System.out.println("Basic Salary : " +
                            rs.getBigDecimal("basic_salary"));
                    System.out.println("Allowances   : " +
                            rs.getBigDecimal("allowances"));
                    System.out.println("Deductions   : " +
                            rs.getBigDecimal("deductions"));
                    System.out.println("Net Salary   : " +
                            rs.getBigDecimal("net_salary"));
                    System.out.println("Payment Status: " +
                            rs.getString("payment_status"));
                }

                if (!found) {
                    System.out.println("No payroll records found.");
                }
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Could not retrieve payroll records.");
            e.printStackTrace();
        }
    }


    public static void noticeManagement(
            Scanner sc, int organizationId) {

        while (true) {
            System.out.println("\n+==================================+");
            System.out.println("|          NOTICE MANAGEMENT       |");
            System.out.println("+==================================+");
            System.out.println("| 1. Add Notice                    |");
            System.out.println("| 2. View Notices                  |");
            System.out.println("| 3. Delete Notice                 |");
            System.out.println("| 4. Back to Dashboard             |");
            System.out.println("+==================================+");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    addNotice(sc, organizationId);
                    break;
                case "2":
                    viewNotices(organizationId);
                    break;
                case "3":
                    deleteNotice(sc, organizationId);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

            if (!choice.equals("4")) {
                System.out.println("\nPress Enter to continue...");
                sc.nextLine();
            }
        }
    }

    public static void addNotice(
            Scanner sc, int organizationId) {

        System.out.println("\n--- ADD NOTICE ---");

        try {
            System.out.print("Enter notice title: ");
            String title = sc.nextLine().trim();

            System.out.print("Enter notice description: ");
            String description = sc.nextLine().trim();

            System.out.print("Enter notice date (YYYY-MM-DD): ");
            String date = sc.nextLine().trim();

            if (title.isEmpty() || description.isEmpty()) {
                System.out.println(
                        "Title and description cannot be empty.");
                return;
            }

            java.sql.Date noticeDate =
                    java.sql.Date.valueOf(date);

            String sql = "INSERT INTO notices" +
                    "(organization_id, title, description, notice_date) " +
                    "VALUES (?, ?, ?, ?)";

            try (java.sql.Connection con =
                         DatabaseConnection.getConnection();
                 java.sql.PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setInt(1, organizationId);
                ps.setString(2, title);
                ps.setString(3, description);
                ps.setDate(4, noticeDate);

                ps.executeUpdate();

                System.out.println(
                        "\nNotice added successfully!");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Invalid date. Use YYYY-MM-DD, for example 2026-10-16.");
        } catch (java.sql.SQLException e) {
            System.out.println("Could not add notice.");
            e.printStackTrace();
        }
    }



    public static void viewNotices(int organizationId) {

        System.out.println("\n--- ORGANIZATION NOTICES ---");

        String sql = "SELECT notice_id, title, description, notice_date " +
                "FROM notices WHERE organization_id = ? " +
                "ORDER BY notice_date DESC, notice_id DESC";

        try (java.sql.Connection con =
                     DatabaseConnection.getConnection();
             java.sql.PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, organizationId);

            try (java.sql.ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    System.out.println("\n------------------------------");
                    System.out.println("Notice ID: " +
                            rs.getInt("notice_id"));
                    System.out.println("Title: " +
                            rs.getString("title"));
                    System.out.println("Description: " +
                            rs.getString("description"));
                    System.out.println("Date: " +
                            rs.getDate("notice_date"));
                }

                if (!found) {
                    System.out.println("No notices found.");
                }
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Could not retrieve notices.");
            e.printStackTrace();
        }
    }

    public static void deleteNotice(
            Scanner sc, int organizationId) {

        System.out.println("\n--- DELETE NOTICE ---");

        try {
            System.out.print("Enter Notice ID to delete: ");
            int noticeId = Integer.parseInt(sc.nextLine().trim());

            String sql = "DELETE FROM notices " +
                    "WHERE notice_id = ? AND organization_id = ?";

            try (java.sql.Connection con =
                         DatabaseConnection.getConnection();
                 java.sql.PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setInt(1, noticeId);
                ps.setInt(2, organizationId);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println(
                            "Notice deleted successfully!");
                } else {
                    System.out.println(
                            "Notice ID not found in your organization.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid numeric Notice ID.");
        } catch (java.sql.SQLException e) {
            System.out.println("Could not delete notice.");
            e.printStackTrace();
        }
    }


    public static void holidayManagement(
            Scanner sc, int organizationId) {

        while (true) {
            System.out.println("\n+==================================+");
            System.out.println("|          HOLIDAY MANAGEMENT      |");
            System.out.println("+==================================+");
            System.out.println("| 1. Add Holiday                   |");
            System.out.println("| 2. View Holidays                 |");
            System.out.println("| 3. Delete Holiday                |");
            System.out.println("| 4. Back to Dashboard             |");
            System.out.println("+==================================+");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    addHoliday(sc, organizationId);
                    break;
                case "2":
                    viewHolidays(organizationId);
                    break;
                case "3":
                    deleteHoliday(sc, organizationId);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

            if (!choice.equals("4")) {
                System.out.println("\nPress Enter to continue...");
                sc.nextLine();
            }
        }
    }



    public static void addHoliday(
            Scanner sc, int organizationId) {

        System.out.println("\n--- ADD HOLIDAY ---");

        try {
            System.out.print("Enter holiday name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter holiday date (YYYY-MM-DD): ");
            String dateText = sc.nextLine().trim();

            System.out.print("Enter description: ");
            String description = sc.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Holiday name cannot be empty.");
                return;
            }

            java.sql.Date holidayDate =
                    java.sql.Date.valueOf(dateText);

            String sql = "INSERT INTO holidays " +
                    "(organization_id, holiday_name, holiday_date, description) " +
                    "VALUES (?, ?, ?, ?)";

            try (java.sql.Connection con =
                         DatabaseConnection.getConnection();
                 java.sql.PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setInt(1, organizationId);
                ps.setString(2, name);
                ps.setDate(3, holidayDate);
                ps.setString(4,
                        description.isEmpty() ? null : description);

                ps.executeUpdate();

                System.out.println("\nHoliday added successfully!");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Invalid date. Use YYYY-MM-DD, for example 2026-10-16.");
        } catch (java.sql.SQLException e) {
            System.out.println("Could not add holiday.");
            e.printStackTrace();
        }
    }




    public static void viewHolidays(int organizationId) {

        System.out.println("\n--- ORGANIZATION HOLIDAYS ---");

        String sql = "SELECT holiday_id, holiday_name, " +
                "holiday_date, description FROM holidays " +
                "WHERE organization_id = ? " +
                "ORDER BY holiday_date ASC";

        try (java.sql.Connection con =
                     DatabaseConnection.getConnection();
             java.sql.PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, organizationId);

            try (java.sql.ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    System.out.println("\n------------------------------");
                    System.out.println("Holiday ID: " +
                            rs.getInt("holiday_id"));
                    System.out.println("Holiday Name: " +
                            rs.getString("holiday_name"));
                    System.out.println("Date: " +
                            rs.getDate("holiday_date"));
                    System.out.println("Description: " +
                            rs.getString("description"));
                }

                if (!found) {
                    System.out.println("No holidays found.");
                }
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Could not retrieve holidays.");
            e.printStackTrace();
        }
    }

    public static void deleteHoliday(
            Scanner sc, int organizationId) {

        System.out.println("\n--- DELETE HOLIDAY ---");

        try {
            System.out.print("Enter Holiday ID to delete: ");
            int holidayId = Integer.parseInt(sc.nextLine().trim());

            String sql = "DELETE FROM holidays " +
                    "WHERE holiday_id = ? AND organization_id = ?";

            try (java.sql.Connection con =
                         DatabaseConnection.getConnection();
                 java.sql.PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setInt(1, holidayId);
                ps.setInt(2, organizationId);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("Holiday deleted successfully!");
                } else {
                    System.out.println(
                            "Holiday ID not found in your organization.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid numeric Holiday ID.");
        } catch (java.sql.SQLException e) {
            System.out.println("Could not delete holiday.");
            e.printStackTrace();
        }
    }
}
