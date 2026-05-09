package codes.lab9.UI;

import codes.lab9.Objects.Employee;
import codes.lab9.Validations.Validatable;
import codes.lab9.Validations.ValidationResult;
import Storage.EmployeeFileStorage;
import DAO.EmployeeDAO;

import javax.print.Doc;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeForm extends  JFrame implements Validatable{
    private JTextField txtEmployeeCode;
    private JTextField txtFullName;
    private JTextField txtDepartment;
    private JTextField txtEmailAddress;
    private JTextField txtHireDate;
    private JButton btnSave;
    private JButton btnClear;
    private JList<Employee> employeeList;
    private DefaultListModel<Employee> listModel;
    private JLabel lblStatus;

    private List<Employee> employees = new ArrayList<>();

    private EmployeeFileStorage storage = new EmployeeFileStorage();

    private EmployeeDAO dao = new EmployeeDAO();




    public EmployeeForm(){ // the Form UI
        setTitle("Employee Directory Manager");
        setSize(600,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6,2,5,5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        formPanel.add(new JLabel("Employee Code: "));
        txtEmployeeCode = new JTextField();
        formPanel.add(txtEmployeeCode);

        formPanel.add(new JLabel("Full Name: "));
        txtFullName = new JTextField();
        formPanel.add(txtFullName);

        formPanel.add(new JLabel("Department: "));
        txtDepartment = new JTextField();
        formPanel.add(txtDepartment);

        formPanel.add(new JLabel("Email Address: "));
        txtEmailAddress = new JTextField();
        formPanel.add(txtEmailAddress);

        formPanel.add(new JLabel("Hire Date (YYYY-MM-DD):"));
        txtHireDate = new JTextField();
        formPanel.add(txtHireDate);

        btnSave = new JButton("Save");
        btnClear = new JButton("Clear");
        formPanel.add(btnSave);
        formPanel.add(btnClear);

        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);

        listModel = new DefaultListModel<>(); // needs to exist first, this is where the datas are inserted
        employeeList = new JList<>(listModel); // then the JList uses the list so kailangan muna magawa ng default list model
        JScrollPane scrollPane = new JScrollPane(employeeList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Employee List"));

        add(formPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);

        employees = storage.loadFromFile();
        for (Employee emp : employees) {
            listModel.addElement(emp);
        }

        attachDocumentListener();
        attachButtonListeners();
        setVisible(true);

    }


    public void attachDocumentListener(){ // does the live update, updates the status label
        txtEmployeeCode.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStatus();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStatus();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateStatus();

            }
        });
    }

    private void updateStatus(){
        String text = txtEmployeeCode.getText().trim(); // gets the txtEmployee text
        if(text.isEmpty()){
            lblStatus.setText("Employee code is required");
        }else{
            lblStatus.setText("Code: " + text);
        }
    }


    private void attachButtonListeners(){ // event listeners sa mga btns
        btnSave.addActionListener(e -> {
            ValidationResult result = validateForm(); // pag may nangyaring event gagawin nya tong method nato
            if(!result.isValid()){ // uses the validation when not valid
                JOptionPane.showMessageDialog(this, result.getMessage()); // shows the error
                result.getFocusTarget().requestFocusInWindow();
                return;
            }

            Employee emp = new Employee( // if no error gets the text from the input from the form
                    txtEmployeeCode.getText().trim(),
                    txtFullName.getText().trim(),
                    txtDepartment.getText().trim(),
                    txtEmailAddress.getText().trim(),
                    java.time.LocalDate.parse(txtHireDate.getText().trim())
            );

            // adds all the information here
            employees.add(emp); // adds it in the <list> of Employees
            listModel.addElement(emp); // adds it in the list inside the panel
            storage.saveToFile(employees); // saved to the json
            dao.save(emp); // saved in the database
            clearFields();
            lblStatus.setText("Employee saved successfully");
        });

        btnClear.addActionListener(e ->{
            clearFields();
            lblStatus.setText(" ");

        });
    }

    private void clearFields(){
        txtEmployeeCode.setText("");
        txtFullName.setText("");
        txtDepartment.setText("");
        txtEmailAddress.setText("");
        txtHireDate.setText("");
    }


    @Override
    public ValidationResult validateForm(){ // the validation syntax for the save btn when being saved
        if(txtEmployeeCode.getText().trim().isEmpty()){
            return new ValidationResult(false, "Employee code is required", txtEmployeeCode);
        }
        if (txtFullName.getText().trim().isEmpty()) {
            return new ValidationResult(false, "Full name is required", txtFullName);
        }
        if (txtDepartment.getText().trim().isEmpty()) {
            return new ValidationResult(false, "Department is required", txtDepartment);
        }
        if (txtEmailAddress.getText().trim().isEmpty()) {
            return new ValidationResult(false, "Email address is required", txtEmailAddress);
        }
        if (txtHireDate.getText().trim().isEmpty()) {
            return new ValidationResult(false, "Hire date is required", txtHireDate);
        }
        try{
            java.time.LocalDate.parse(txtHireDate.getText().trim());
        }catch (java.time.format.DateTimeParseException e){
            return new ValidationResult(false, "Hire date must be in YYYY-MM-DD Format", txtHireDate);
        }

        return new ValidationResult(true, "Valid", null);
    }
}
