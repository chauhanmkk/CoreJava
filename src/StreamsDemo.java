import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class StreamsDemo {

    static class Employee {
        String name;
        String department;
        int salary;
        boolean active;

        Employee(String name, String department, int salary, boolean active) {
            this.name = name;
            this.department = department;
            this.salary = salary;
            this.active = active;
        }
    }

    public static void main(String[] args) {

        List<Employee> employees = List.of(
                new Employee("Mohit",   "IT",      90000,  true),
                new Employee("Rahul",   "IT",      120000, true),
                new Employee("Priya",   "HR",      60000,  true),
                new Employee("Amit",    "HR",      40000,  true),
                new Employee("Sneha",   "IT",      30000,  false),
                new Employee("Vikram",  "Finance", 80000,  true)
        );

        // ---- Standalone functional interfaces ----

        Predicate<Employee> isActive          = e -> e.active;
        Predicate<Employee> salaryAbove50k    = e -> e.salary > 50000;

        Function<Employee, Employee> applyHike = e -> {
            e.salary = e.salary + (int)(0.10 * e.salary); // 10% hike
            return e;  // ← must return Employee, not void
        };

        Consumer<Employee> printEmployee = e ->
                System.out.println(e.name + " | " + e.department + " | " + e.salary);

        Supplier<Employee> defaultEmployee = () ->
                new Employee("Unknown", "General", 30000, false);

        // ---- Stream pipeline ----

        // 1,2,3: filter active + salary > 50k + apply hike
        List<Employee> hikedEmployees = employees.stream()
                .filter(isActive)
                .filter(salaryAbove50k)
                .map(applyHike)
                .collect(Collectors.toList());

        // 4: group by department
        Map<String, List<Employee>> byDept = hikedEmployees.stream()
                .collect(Collectors.groupingBy(e -> e.department));

        System.out.println("=== By Department ===");
        byDept.forEach((dept, emps) -> {
            System.out.println(dept + ":");
            emps.forEach(printEmployee);  // 5: Consumer used here
        });

        // 6: highest paid after hike
        Optional<Employee> highestPaid = hikedEmployees.stream()
                .max(Comparator.comparingInt(e -> e.salary));

        highestPaid.ifPresent(e ->
                System.out.println("\nHighest paid: " + e.name + " | " + e.salary));

        // 7: any IT employee earning > 100000
        boolean anyITAbove100k = hikedEmployees.stream()
                .filter(e -> e.department.equals("IT"))
                .anyMatch(e -> e.salary > 100000);

        System.out.println("\nAny IT > 100k: " + anyITAbove100k);

        // Supplier demo
        Employee fallback = defaultEmployee.get();
        System.out.println("\nDefault: " + fallback.name);
    }
}