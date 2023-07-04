import common.*;
import entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private BufferedReader bufferedReader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }


    @Override
    public void run() {
        System.out.println("Please select one of the following tasks:");
        TaskListMessages messages = new TaskListMessages();
        messages.getTaskMessages().forEach(System.out::println);
        System.out.println();
        System.out.println("Please enter the task number: ");

        try {
            int exerciseNum = Integer.parseInt(bufferedReader.readLine());

            switch (exerciseNum) {
                case 2 -> changeCasingTask();
                case 3 -> containsEmployeeTask();
                case 4 -> employeesWithSalaryOver50k();
                case 5 -> employeesFromDepartment();
                case 6 -> addingNewAddressAndUpdateEmployee();
                case 7 -> addressesWithEmployeeCount();
                case 8 -> getEmployeeWithProject();
                case 9 -> findLatest10Projects();
                case 10 -> increaseSalaries();
                case 11 -> findEmployeesByFirstName();
                case 12 -> employeesMaximumSalary();
                case 13 -> removeTowns();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    private void removeTowns() throws IOException {
        System.out.println("Enter town name:");

        String townName = bufferedReader.readLine();

        Town town = entityManager.createQuery(
                "SELECT t FROM Town t " +
                "WHERE t.name = :t_name", Town.class)
                .setParameter("t_name", townName)
                .getSingleResult();

        int deletedRows = removeAddressesByTownId(town.getId());

        entityManager.getTransaction().begin();
        entityManager.remove(town);
        entityManager.getTransaction().commit();

        if (deletedRows == 1) {
            System.out.printf("%d address in %s deleted%n", deletedRows, townName);
        } else if (deletedRows > 1) {
            System.out.printf("%d addresses in %s deleted%n", deletedRows, townName);
        }

    }

    private int removeAddressesByTownId(Integer id) {

        List<Address> addresses = entityManager.createQuery(
                "SELECT a FROM Address a " +
                "WHERE a.town.id = :p_id", Address.class)
                .setParameter("p_id", id)
                .getResultList();

        entityManager.getTransaction().begin();
        addresses.forEach(entityManager::remove);
        entityManager.getTransaction().commit();

        return addresses.size();
    }

    @SuppressWarnings("unchecked")
    private void employeesMaximumSalary() {
        List<Object[]> resultList = entityManager.createNativeQuery(
                "SELECT d.name, MAX(e.salary) AS 'max_salary' FROM departments d " +
                "JOIN employees e on d.department_id = e.department_id " +
                "GROUP BY d.name " +
                "HAVING `max_salary` NOT BETWEEN 30000 AND 70000").getResultList();

        resultList.forEach(result -> System.out.printf("%s %s%n", result[0], result[1]));

}

    private void findEmployeesByFirstName() throws IOException {
        System.out.println("Please enter a starting text for the employee first name:");
        String startingString = bufferedReader.readLine();

        List<Employee> employees = entityManager.createQuery(
                "SELECT e FROM Employee e " +
                        "WHERE e.firstName LIKE :p_like", Employee.class)
                .setParameter("p_like", startingString + "%")
                .getResultList();

        employees.forEach(employee ->
                System.out.printf("%s %s - %s - ($%.2f)%n",
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getJobTitle(),
                        employee.getSalary()));
    }

    private void increaseSalaries() {
        entityManager.getTransaction().begin();

         entityManager.createQuery(
                        "UPDATE Employee e " +
                                "SET e.salary = e.salary * :salary_increase " +
                                "WHERE e.department.id IN :ids")
                .setParameter("ids", Set.of(1, 2, 4, 11))
                 .setParameter("salary_increase", BigDecimal.valueOf(1.12))
                .executeUpdate();

        entityManager.getTransaction().commit();

        List<Employee> employees = entityManager.createQuery(
                        "SELECT e FROM Employee e " +
                                "WHERE e.department.id IN :ids", Employee.class)
                .setParameter("ids", List.of(1, 2, 4, 11))
                .getResultList();

        for (Employee employee : employees) {
            System.out.printf("%s %s (%.2f)%n",
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getSalary());
        }
    }

    private void findLatest10Projects() {
        List<Project> projects = entityManager.createQuery(
                        "SELECT p FROM Project p " +
                                "ORDER BY p.startDate DESC " +
                                "LIMIT 10", Project.class)
                .getResultList();

        projects.sort(Comparator.comparing(Project::getName));

        for (Project project : projects) {
            System.out.printf("Project name: %s%n", project.getName());
            System.out.printf("        Project Description: %s%n", project.getDescription());
            System.out.printf("        Project Start Date:%s%n",Timestamp.valueOf(project.getStartDate()));
            System.out.printf("        Project End Date: %s%n",
                    project.getEndDate() == null ? "null" : Timestamp.valueOf(project.getEndDate()));
        }
    }

    private void getEmployeeWithProject() throws IOException {
        System.out.println("Please enter employee ID:");
        Long employeeID = Long.parseLong(bufferedReader.readLine());

        Employee employee = entityManager.find(Employee.class, employeeID);

        List<String> projectsNames = employee.getProjects().stream().map(Project::getName).sorted().toList();

        System.out.printf("%s %s - %s%n",
                employee.getFirstName(),
                employee.getLastName(),
                employee.getJobTitle());

        for (String project : projectsNames) {
            System.out.println("      " + project);
        }
    }

    private void addressesWithEmployeeCount() {
        List<Address> addresses = entityManager.createQuery(
                        "SELECT a FROM Address a " +
                                "ORDER BY SIZE(a.employees) DESC", Address.class)
                .setMaxResults(10)
                .getResultList();

        addresses.forEach(address -> System.out.printf("%s, %s - %d employees%n",
                address.getText(),
                address.getTown() == null ? "Unknown" : address.getTown().getName(),
                address.getEmployees().size()));
    }

    private void addingNewAddressAndUpdateEmployee() throws IOException {
        System.out.println("Please enter employee last name:");
        String lastName = bufferedReader.readLine();

        Employee employee = entityManager.createQuery(
                        "SELECT e FROM Employee e " +
                                "WHERE e.lastName = :l_name", Employee.class)
                .setParameter("l_name", lastName)
                .getSingleResult();

        Address address = createAddress("Vitoshka 15");

        entityManager.getTransaction().begin();
        employee.setAddress(address);
        entityManager.getTransaction().commit();
    }

    private Address createAddress(String addressText) {
        Address address = new Address();
        address.setText(addressText);

        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();

        return address;
    }

    private void employeesFromDepartment() throws IOException {
        System.out.println("Please enter department name:");
        String departmentName = bufferedReader.readLine();

        entityManager.createQuery(
                        "SELECT e FROM Employee e " +
                                "WHERE e.department.name = :dep_name " +
                                "ORDER BY e.salary, e.id", Employee.class)
                .setParameter("dep_name", departmentName)
                .getResultStream()
                .forEach(employee ->
                        System.out.printf("%s %s from %s - $%.2f%n",
                                employee.getFirstName(),
                                employee.getLastName(),
                                employee.getDepartment().getName(),
                                employee.getSalary())
                );
    }

    private void employeesWithSalaryOver50k() {
        entityManager.createQuery(
                        "SELECT e FROM Employee e " +
                                "WHERE e.salary > :min_salary",
                        Employee.class)
                .setParameter("min_salary", BigDecimal.valueOf(50000L))
                .getResultStream()
                .map(Employee::getFirstName)
                .forEach(System.out::println);
    }

    private void containsEmployeeTask() throws IOException {
        System.out.println("Please enter employee full name, so you can check if it's in the database: ");
        String[] fullName = bufferedReader.readLine().split("\\s+");

        String firstName = fullName[0];
        String lastName = fullName[1];

        Long singleResult = entityManager.createQuery(
                        "SELECT COUNT(e) FROM Employee e " +
                                "WHERE e.firstName = :f_name " +
                                "AND e.lastName = :l_name",
                        Long.class)
                .setParameter("f_name", firstName)
                .setParameter("l_name", lastName)
                .getSingleResult();

        System.out.println(singleResult == 0 ? "No" : "Yes");
    }

    private void changeCasingTask() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(
                "UPDATE Town t " +
                        "SET t.name = UPPER(t.name) " +
                        "WHERE length(t.name) <= 5 ");

        int changedTownNamesCount = query.executeUpdate();

        System.out.println(changedTownNamesCount);

        entityManager.getTransaction().commit();
    }
}
