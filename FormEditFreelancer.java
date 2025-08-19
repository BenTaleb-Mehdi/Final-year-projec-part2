package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FormEditFreelancer{

    @FXML
    private TextField input_fname;

    @FXML
    private TextField input_lname;

    @FXML
    private TextField input_password;

    @FXML
    private TextField input_username;

    private Freelancers currentFreelancer;



    private Runnable onUpdateCallback;

    public void setOnUpdateCallback(Runnable onUpdateCallback) {
        this.onUpdateCallback = onUpdateCallback;
    }



    // Method to receive data from settings controller
    public void setFreelancer(Freelancers freelancer) {
        this.currentFreelancer = freelancer;

        input_fname.setText(freelancer.getFname());
        input_lname.setText(freelancer.getLname());
        input_username.setText(freelancer.getUsername());
        input_password.setText(freelancer.getPassword());
    }




    @FXML
        void btn_update (ActionEvent event){
            // You can add logic here to update the database with edited values
            // For example:
            String updatedFname = input_fname.getText();
            String updatedLname = input_lname.getText();
            String updatedUsername = input_username.getText();
            String updatedPassword = input_password.getText();

            String Sql = "UPDATE web_signup_freelancers SET "
                    + "firstname='" + updatedFname + "', "
                    + "lastname='" + updatedLname + "', "
                    + "username='" + updatedUsername + "', "
                    + "password='" + updatedPassword + "' "
                    + "WHERE id=" + currentFreelancer.getId();

            int result = Connectiondb.update(Sql);

            if (result > 0) {
                System.out.println("Update successful!");

                // Update currentFreelancer object fields if needed
                currentFreelancer.setFname(updatedFname);
                currentFreelancer.setLname(updatedLname);
                currentFreelancer.setUsername(updatedUsername);
                currentFreelancer.setPassword(updatedPassword);

                // Close the current form/window
                input_fname.getScene().getWindow().hide();
            } else {
                System.out.println("Update failed!");
            }

        if (onUpdateCallback != null) {
            onUpdateCallback.run(); // This refreshes the table in settings
        }


        }




}

