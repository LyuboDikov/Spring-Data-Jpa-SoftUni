package common;

import java.util.List;

public class TaskListMessages {
public static final String TASK_2 = "2 - 'Change casing'(Change to UPPER case the names of towns with up to 5 letters);";
public static final String TASK_3 = "3 - 'Contains employee'(Check if the employee is within the database);";
public static final String TASK_4 = "4 - 'Employees with Salary Over 50k' (Returns the first name of all employees with salary over 50k);";
public static final String TASK_5 = "5 - 'Employees from Department' (Returns a list of all employees from the selected department, along with their salary);";
public static final String TASK_6 = "6 - 'Adding new Address and Updating Employee' (Creating new address and set it to an Employee, whose Last Name is entered as input);";
public static final String TASK_7 = "7 - 'Addresses with Employee count' (Returns the first 10 addresses, ordered by the number of employees who live there (descending));";
public static final String TASK_8 = "8 - 'Get Employee with Project' (By entering an Employee Id, returns the employee Full name, job title and projects, ordered by the project name (lexicographically));";
public static final String TASK_9 = "9 - 'Find last 10 projects' (Returns the last 10 started projects name, description, start and end date, ordered lexicographically);";
public static final String TASK_10 = "10 - 'Increase salaries' (Increasing the salaries of all employees from the Engineering, Tool Design, Marketing or Information Services departments by 12%, then returns their Full name and salary);";
public static final String TASK_11 = "11 - 'Find Employees by first name' (Returns the Full name, job title and salary of all employees whose first name starts with a pattern, entered as an input);";
public static final String TASK_12 = "12 - 'Employees Maximum Salary' (Returns all departments names along with the maximum salary for each department);";
public static final String TASK_13 = "13 - 'Remove Towns' (Removes a town, which name is entered as an input. Removes all addresses in this town and returns how many addresses were deleted);";

public List<String> taskMessages;

    public TaskListMessages() {
        this.taskMessages = List.of(TASK_2, TASK_3, TASK_4, TASK_5, TASK_6, TASK_7, TASK_8, TASK_9, TASK_10, TASK_11, TASK_12, TASK_13);
    }

    public List<String> getTaskMessages() {
        return taskMessages;
    }
}

