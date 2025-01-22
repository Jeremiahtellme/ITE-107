# Payroll System
## Features

How It Works

Add Employee
- The user can input details such as `Employee ID`, `Name`, `Hourly Rate`, and `Hours Worked`.
- The system will calculate the gross and net pay (with a 20% tax deduction) and display it in the table.

Calculate Pay
- The user can enter an employee ID, and the system will calculate and display the gross and net pay for that employee.

Save Records
- The user can save all payroll records to a file named `payroll.txt`. This file will be created if it doesn't already exist.

Display Records
- The application can display all the saved payroll records in a table, showing `Employee ID`, `Name`, `Hourly Rate`, `Hours Worked`, `Gross Pay`, and `Net Pay`.

Load Records
- The application automatically loads any existing records from `payroll.txt` into the table when the application starts.

File Structure

- `PayrollSystem.java`: Main Java class containing the logic for the payroll system.
- `payroll.txt`: A text file used to store payroll records.
- `README.md`: This file.

Example Usage

1. Adding an Employee:
   - Enter employee details, such as `Employee ID`, `Name`, `Hourly Rate`, and `Hours Worked`, and click the "Add Employee" button.
   - The system calculates the pay and updates the table with the new employee record.

2. Calculating Pay:
   - Enter an existing `Employee ID` and click "Calculate Pay" to view the gross and net pay.

3. Saving Records:
   - Click "Save Record" to save all payroll records to the `payroll.txt` file.

4. Displaying Records:
   - Click "Display Records" to view all existing payroll records in the table.

