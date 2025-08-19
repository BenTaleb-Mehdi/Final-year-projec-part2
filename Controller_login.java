package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Connectiondb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller_login {

    @FXML
    private PasswordField input_password;

    @FXML
    private TextField input_username;

    @FXML
    private Label alear_username;

    @FXML
    private Label alear_password;

    @FXML
    private AnchorPane dash;

    private void loading(String fxfile) {
        try {
            Parent newContent = FXMLLoader.load(getClass().getResource(fxfile));
            dash.getChildren().clear();  // NPE happens here if dash == null
            dash.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btn_login(ActionEvent event) throws IOException {
        boolean valid = true;

        // Validate username
        if (input_username.getText().isEmpty()) {
            alear_username.setVisible(true);
            valid = false;
        } else {
            alear_username.setVisible(false);
        }

        // Validate password
        if (input_password.getText().isEmpty()) {
            alear_password.setVisible(true);
            valid = false;
        } else {
            alear_password.setVisible(false);
        }

        if (!valid) {
            // Stop processing if inputs are invalid
            return;
        }

        Connection conn = Connectiondb.connect();
        if (conn != null) {
            String sql = "SELECT * FROM login WHERE username = ? AND password = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, input_username.getText());
                ps.setString(2, input_password.getText());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Successful login: load dashboard.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("dash.fxml"));
                        Parent root = loader.load();

                        Stage stage = (Stage) dash.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login Failed");
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid username or password.");
                        alert.showAndWait();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // You might want to show an alert for DB error here
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Connection failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to connect to the database.");
            alert.showAndWait();
        }
    }
}
