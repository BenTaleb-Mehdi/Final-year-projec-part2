package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class controller_dashboard implements Initializable {

    @FXML
    private BorderPane border;

    @FXML
    private Pane content;

    @FXML
    private AnchorPane dash;


private void loading(String fxfile){
    try {

        Parent newContent = FXMLLoader.load(getClass().getResource(fxfile));
        border.setCenter(newContent);
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    @FXML
    public void btn_freelancers(ActionEvent actionEvent) {
        try{
            AnchorPane root = FXMLLoader.load(getClass().getResource("Freelaners.fxml"));
            border.setCenter(root);
        }catch (IOException e) {
            System.out.println(e);
        }
    }
    @FXML
    void btn_allOrders(ActionEvent actionEvent) {
        loading("All_orders.fxml");
    }
    @FXML
    public void btn_dashboard(ActionEvent actionEvent) {
        loading("dashboard.fxml");
    }

    @FXML
    void btn_analyses(ActionEvent event) {
        try{
            AnchorPane root = FXMLLoader.load(getClass().getResource("Smart_Analysis.fxml"));
            border.setCenter(root);
        }catch (IOException e) {
            System.out.println(e);
        }
    }


    @FXML
    void btn_settings(ActionEvent actionEvent) {
        try {
            AnchorPane root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
            border.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btn_logout(ActionEvent event) {
        try {
            // Load the login scene
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

            // Close the current window (dashboard)
            Stage currentStage = (Stage) border.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loading("dashboard.fxml");
    }
}

