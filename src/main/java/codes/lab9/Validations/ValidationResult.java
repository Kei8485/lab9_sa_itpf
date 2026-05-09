package codes.lab9.Validations;

import javax.swing.JComponent;

public class ValidationResult {
    private boolean valid;
    private String message;
    private JComponent focusTarget;

    // constructor
    public ValidationResult(boolean valid, String message, JComponent focusTarget){
        this.valid = valid;
        this.message = message;
        this.focusTarget = focusTarget;
    }

    //getter
    public boolean isValid(){
        return valid;
    }

    public String getMessage(){
        return message;
    }

    public JComponent getFocusTarget(){
        return focusTarget;
    }
}
