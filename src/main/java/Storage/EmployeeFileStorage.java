package Storage;

import codes.lab9.Objects.Employee;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class EmployeeFileStorage {
    private static final String FOLDER = "employee_data";
    private static final String FILE = "employees.json";

    public void saveToFile(List<Employee> employees) { // pang convert ng obj to json files and pang create ng directories
        try {
            Path folder = Paths.get(FOLDER);
            Files.createDirectories(folder);
            Path output = folder.resolve(FILE);
            String text = toJson(employees);
            Files.write(output, text.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private String toJson(List<Employee> employees) { // method para iconvert ung mga obj to json
        StringBuilder sb = new StringBuilder(); // ung code dapat match ung syntax ng json
        sb.append("[\n");

        for (int x = 0; x < employees.size(); x++) {
            Employee emp = employees.get(x);
            sb.append("  {\n");
            sb.append("    \"employeeCode\": \"").append(emp.getEmployeeCode()).append("\",\n");
            sb.append("    \"fullName\": \"").append(emp.getFullName()).append("\",\n");
            sb.append("    \"department\": \"").append(emp.getDepartment()).append("\",\n");
            sb.append("    \"emailAddress\": \"").append(emp.getEmailAddress()).append("\",\n");
            sb.append("    \"hireDate\": \"").append(emp.getHireDate()).append("\"\n");
            sb.append("  }");

            if (x < employees.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("]");
        return sb.toString();
    }

    public List<Employee> loadFromFile() { // kabaliktaran ng saveToFile method, kinukuha nya ung Json files tas ginagawang obj ulit
        List<Employee> employees = new ArrayList<>(); // so ala pang laman to pag niload ung system
        Path file = Paths.get(FOLDER).resolve(FILE); // kukunin mga data

        if (!Files.exists(file)) { // checheck muna kung ung folder ay existing then pag hindi ala pang data nakukuha
            return employees;
        }

        try {
            String content = Files.readString(file);
            employees = fromJson(content); // do the fromJson Method (eto ung nag coconvert back sa Json to Obj)
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return employees; // exists the method if done
    }

    private List<Employee> fromJson(String json) { // converts the json back to obj
        List<Employee> employees = new ArrayList<>(); // empty

        json = json.trim();
        if (json.equals("[]") || json.isEmpty()) { // if empty tatapusin na ung method
            return employees;
        }

        json = json.substring(1, json.length() - 1); // removes the '[' (eto ung asa unang line) and ']' (sa last line)
        String[] objects = json.split("\\},\\s*\\{");
        // explanation ng split
        // finds the first } and then split it if it ends with {
        // and ung \\s* is for including whatever its between it (meaning white space to, iniinclude lng ng \\s* ung white spce)


        for (String obj : objects) { // for each loop sa bawat objects
            // for each obj in json
            obj = obj.replace("{", "").replace("}", "").trim(); // removes the '{' and '}'
            String[] lines = obj.split(",\n"); // finds the character ',' para iisplit un, then stored sa lines na arrayList

            Employee emp = new Employee(); // dito lalagay ung mga converted jsons bago ilagay sa tutuong LIST
            for (String line : lines) {
                // for each line of json naman dito
                line = line.trim(); // removes extra spaces
                String[] parts = line.split("\":\\s*\""); // splits it again ( ung ':' sa json) then stored sa parts arrayList
                if (parts.length < 2) continue; // skips the missing parts and continue the loop

                String key = parts[0].replace("\"", "").trim(); // get the key of the line(the current obj)
                String value = parts[1].replace("\"", "").trim(); // gets the value of the line(the current obj still)

                switch (key) {// then puts the key here to categorize the key
                    // uses the setter inside the employee class
                    // if its found in the use case, it uses the setter method from the employee class
                    case "employeeCode" -> emp.setEmployeeCode(value);
                    case "fullName" -> emp.setFullName(value);
                    case "department" -> emp.setDepartment(value);
                    case "emailAddress" -> emp.setEmailAddress(value);
                    case "hireDate" -> emp.setHireDate(java.time.LocalDate.parse(value));
                }
            }
            // after doing all the loop it adds to the employees list so eto ung mismong obj
            employees.add(emp);
        }

        return employees;
    }
}
