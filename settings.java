package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class settings {

    @FXML
    private TableColumn<Freelancers, String> fname;
    @FXML
    private TableColumn<Freelancers, String> lname;
    @FXML
    private TableColumn<Freelancers, String> username;
    @FXML
    private TableColumn<Freelancers, String> password;
    @FXML
    private TableColumn<Freelancers, String> statut;
    @FXML
    private TableColumn<Freelancers, String> skill;
    @FXML
    private TableColumn<Freelancers, String> date;
    @FXML
    private TableColumn<Freelancers, Void> actionCol;

    @FXML
    private TableView<Freelancers> table;
    @FXML
    private TextField searchField;

    private final ObservableList<Freelancers> list = FXCollections.observableArrayList();
    public void refreshTable() {
        loadData(); // Clear and reload table data
    }

    @FXML
    public void initialize() {
        fname.setCellValueFactory(new PropertyValueFactory<>("fname"));
        lname.setCellValueFactory(new PropertyValueFactory<>("lname"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        statut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        skill.setCellValueFactory(new PropertyValueFactory<>("skill"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));



        // Style statut labels
        statut.setCellFactory(column -> new TableCell<Freelancers, String>() {
            private final Label label = new Label();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(item);
                    label.getStyleClass().removeAll("label-bg-active", "label-bg-inactive");

                    switch (item.toLowerCase()) {
                        case "active":
                            label.getStyleClass().add("label-bg-active");
                            break;
                        case "inactive":
                            label.getStyleClass().add("label-bg-inactive");
                            break;
                        default:
                            label.setStyle("");
                    }

                    setText(null);
                    setGraphic(label);
                }
            }
        });

        // Add Edit button to each row
        addEditButtonToTable();

        // Load data from database
        loadData();

        usernameApp.setCellValueFactory(new PropertyValueFactory<>("usernameApp"));
        PasswordApp.setCellValueFactory(new PropertyValueFactory<>("passwordApp"));
        dateApp.setCellValueFactory(new PropertyValueFactory<>("dateApp"));
        nameApp.setCellValueFactory(new PropertyValueFactory<>("applicationName"));


        loadAdminData(); // Load admin data
        addEditButtonToAdminTable();

    }

    private void addEditButtonToTable() {
        Callback<TableColumn<Freelancers, Void>, TableCell<Freelancers, Void>> cellFactory =
                new Callback<TableColumn<Freelancers, Void>, TableCell<Freelancers, Void>>() {
                    @Override
                    public TableCell<Freelancers, Void> call(final TableColumn<Freelancers, Void> param) {
                        return new TableCell<Freelancers, Void>() {
                            private final Button btn = new Button("Edit");

                            {
                                Image icon = new Image(getClass().getResourceAsStream("/imges/edit.png"));
                                ImageView imageView = new ImageView(icon);
                                imageView.setFitHeight(16);
                                imageView.setFitWidth(16);
                                btn.setGraphic(imageView);
                                btn.setContentDisplay(ContentDisplay.LEFT);
                                btn.getStyleClass().add("btn_Edit");

                                btn.setOnAction(event -> {
                                    Freelancers freelancer = getTableView().getItems().get(getIndex());
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Form_Edit_freelancer.fxml"));
                                        AnchorPane pane = loader.load();
                                        FormEditFreelancer controller = loader.getController();
                                        controller.setFreelancer(freelancer);
                                        controller.setOnUpdateCallback(() -> refreshTable());
                                        Stage stage = new Stage();
                                        stage.setScene(new Scene(pane));
                                        stage.setTitle("Edit Freelancer");
                                        stage.show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                    }
                };
        actionCol.setCellFactory(cellFactory);
    }

    private void loadData() {
        list.clear(); // Clear list before loading to avoid duplicates

        ResultSet rs = Connectiondb.display(
                "SELECT `id`, `firstname`, `lastname`, `username`, `password`, `statut`, `skill_name`, `date_created` FROM `web_signup_freelancers`");

        try {
            while (rs != null && rs.next()) {
                Freelancers freelancer = new Freelancers();
                freelancer.setId(rs.getInt("id"));
                freelancer.setFname(rs.getString("firstname"));
                freelancer.setLname(rs.getString("lastname"));
                freelancer.setUsername(rs.getString("username"));
                freelancer.setPassword(rs.getString("password"));
                freelancer.setStatut(rs.getString("statut"));
                freelancer.setSkill(rs.getString("skill_name"));
                freelancer.setDate(rs.getString("date_created"));

                list.add(freelancer);
            }

            FilteredList<Freelancers> filteredList = new FilteredList<>(list, b -> true);

            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(freelancer -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return freelancer.getFname().toLowerCase().contains(lowerCaseFilter)
                            || freelancer.getLname().toLowerCase().contains(lowerCaseFilter)
                            || freelancer.getUsername().toLowerCase().contains(lowerCaseFilter)
                            || freelancer.getStatut().toLowerCase().contains(lowerCaseFilter)
                            || freelancer.getSkill().toLowerCase().contains(lowerCaseFilter);
                });
            });

            table.setItems(filteredList);

        } catch (SQLException e) {
            System.out.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }




    // Admin application Edit ...
    @FXML
    private TableColumn<TableAdmin, String> usernameApp;

    @FXML
    private TableColumn<TableAdmin, String> PasswordApp;

    @FXML
    private TableColumn<TableAdmin, String> dateApp;

    @FXML
    private TableColumn<TableAdmin, String> nameApp;

    @FXML
    private TableColumn<TableAdmin, Void> optionApp;

    @FXML
    private TableView<TableAdmin> tableApp;


    private final ObservableList<TableAdmin> listAdmin = FXCollections.observableArrayList();


    private void loadAdminData() {
        listAdmin.clear();

        ResultSet rs = Connectiondb.display(
                "SELECT `id`,`nameApp`, `username`, `password`, `date_login` FROM `login`"
        );

        try {
            while (rs != null && rs.next()) {
                TableAdmin admin = new TableAdmin();
                admin.setId(rs.getInt("id"));
                admin.setUsernameApp(rs.getString("username"));
                admin.setPasswordApp(rs.getString("password"));
                admin.setDateApp(rs.getString("date_login"));
                admin.setApplicationName(rs.getString("nameApp"));
                listAdmin.add(admin);
            }

            tableApp.setItems(listAdmin);

        } catch (SQLException e) {
            System.out.println("Error loading admin data: " + e.getMessage());
        }
    }

    private void addEditButtonToAdminTable() {
        Callback<TableColumn<TableAdmin, Void>, TableCell<TableAdmin, Void>> cellFactory =
                new Callback<TableColumn<TableAdmin, Void>, TableCell<TableAdmin, Void>>() {
                    @Override
                    public TableCell<TableAdmin, Void> call(final TableColumn<TableAdmin, Void> param) {
                        return new TableCell<TableAdmin, Void>() {
                            private final Button btn = new Button("Edit");

                            {
                                Image icon = new Image(getClass().getResourceAsStream("/imges/edit.png"));
                                ImageView imageView = new ImageView(icon);
                                imageView.setFitHeight(16);
                                imageView.setFitWidth(16);
                                btn.setGraphic(imageView);
                                btn.setContentDisplay(ContentDisplay.LEFT);
                                btn.getStyleClass().add("btn_Edit_admin");

                                btn.setOnAction(event -> {
                                    TableAdmin admin = getTableView().getItems().get(getIndex());
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Form_Edit_admin.fxml"));
                                        AnchorPane pane = loader.load();
                                        FormEditAdmin controller = loader.getController();
                                        controller.setAdmin(admin);
                                        controller.setOnUpdateCallback(() -> loadAdminData()); // Refresh admin table
                                        Stage stage = new Stage();
                                        stage.setScene(new Scene(pane));
                                        stage.setTitle("Edit Admin");
                                        stage.show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                    }
                };
        optionApp.setCellFactory(cellFactory);
    }






}
