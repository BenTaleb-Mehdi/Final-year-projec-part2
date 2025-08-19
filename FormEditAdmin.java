package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class FormEditAdmin {

    @FXML
    private TextField input_application_name;

    @FXML
    private TextField input_password_admin;

    @FXML
    private TextField input_username_admin;

    private TableAdmin currentAdmin;

    private Runnable onUpdateCallback;

    public void setOnUpdateCallback(Runnable onUpdateCallback) {
        this.onUpdateCallback = onUpdateCallback;
    }


    // Method to receive data from settings controller
    public void setAdmin(TableAdmin admin) {
        this.currentAdmin = admin;

        input_application_name.setText(admin.getApplicationName());
        input_username_admin.setText(admin.getUsernameApp());
        input_password_admin.setText(admin.getUsernameApp());
        input_password_admin.setText(admin.getPasswordApp());
    }
    @FXML
    void btn_update_admin(ActionEvent event) {
        String updateapplicationname = input_application_name.getText();
        String updatepasswordapp = input_password_admin.getText();
        String updateusernameapp = input_username_admin.getText();


        String Sql = "UPDATE login SET "
                + "nameApp='" + updateapplicationname + "', "
                + "username='" + updateusernameapp + "', "
                + "password='" + updatepasswordapp + "' "
                + "WHERE id=" + currentAdmin.getId();

        int result = Connectiondb.update(Sql);

        if (result > 0) {
            System.out.println("Update successful!");

            // Update currentFreelancer object fields if needed
            currentAdmin.setApplicationName(updateapplicationname);
            currentAdmin.setUsernameApp(updateusernameapp);
            currentAdmin.setPasswordApp(updatepasswordapp);


            // Close the current form/window
            input_application_name.getScene().getWindow().hide();
        } else {
            System.out.println("Update failed!");
        }

        if (onUpdateCallback != null) {
            onUpdateCallback.run(); // This refreshes the table in settings
        }
    }

}
