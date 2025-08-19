package sample;

        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.collections.transformation.FilteredList;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
        import javafx.scene.layout.AnchorPane;
        import javafx.util.Callback;

        import java.awt.event.ActionEvent;
        import java.io.IOException;
        import java.sql.ResultSet;
        import java.sql.SQLException;

public class controller_Freelaners_blocked {

    @FXML
    private TableView<Freelancers> table;

    @FXML
    private TableColumn<Freelancers, String> cin;

    @FXML
    private TableColumn<Freelancers, String> date;

    @FXML
    private TableColumn<Freelancers, String> email;

    @FXML
    private TableColumn<Freelancers, String> fname;

    @FXML
    private TableColumn<Freelancers, String> lname;

    @FXML
    private TableColumn<Freelancers, String> phone;

    @FXML
    private TableColumn<Freelancers, String> skill;

    @FXML
    private javafx.scene.layout.AnchorPane AnchorPane;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Freelancers, Void> actionCol;

    public void btn_block(javafx.event.ActionEvent actionEvent) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("Freelaners.fxml"));
            AnchorPane.getChildren().setAll(pane);
        } catch (IOException e) {
            System.out.println("Error loading FXML: " + e.getMessage());
        }
    }

    ObservableList<Freelancers> list = FXCollections.observableArrayList();
    @FXML
    public void initialize() {

        // Set up name column (change this to your actual column and property)
        fname.setCellValueFactory(new PropertyValueFactory<>("fname"));
        lname.setCellValueFactory(new PropertyValueFactory<>("lname"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        cin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        skill.setCellValueFactory(new PropertyValueFactory<>("skill"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));





        // Add button to each row
        Callback<TableColumn<Freelancers, Void>, TableCell<Freelancers, Void>> cellFactory =
                new Callback<TableColumn<Freelancers, Void>, TableCell<Freelancers, Void>>() {
                    @Override
                    public TableCell<Freelancers, Void> call(final TableColumn<Freelancers, Void> param) {
                        return new TableCell<Freelancers, Void>() {

                            private final Button btn = new Button("Un Block");

                            {btn.getStyleClass().add("btn_unblock");
                                btn.setOnAction(event -> {

                                    Freelancers freelancer = getTableView().getItems().get(getIndex());
                                    int id = freelancer.getId();

                                    // Update status in database
                                    String query = "UPDATE web_signup_freelancers SET statut = 'active' WHERE id = " + id;
                                    int updatedRows = Connectiondb.update(query);

                                    if (updatedRows > 0) {
                                        System.out.println("Freelancer with ID " + id + " set to inactive.");
                                        list.clear();  // Clear old data
                                        loadData();    // Reload fresh data from DB
                                    } else {
                                        System.out.println("Failed to update status.");
                                    }
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
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
        loadData();



    }

    private void loadData() {
        ResultSet rs = Connectiondb.display(
                "SELECT `id`, `firstname`, `lastname`, `email`, `phone`, `cin`, `skill_name`, `date_created` FROM `web_signup_freelancers` WHERE statut = 'inactive'");

        try {
            while (rs != null && rs.next()) {
                Freelancers freelancer = new Freelancers();
                freelancer.setId(rs.getInt("id"));
                freelancer.setFname(rs.getString("firstname"));
                freelancer.setLname(rs.getString("lastname"));
                freelancer.setEmail(rs.getString("email"));
                freelancer.setPhone(rs.getString("phone"));
                freelancer.setCin(rs.getString("cin"));
                freelancer.setSkill(rs.getString("skill_name"));
                freelancer.setDate(rs.getString("date_created"));

                list.add(freelancer);
            }
            FilteredList<Freelancers> filteredList = new FilteredList<>(list, b -> true);

            // Search by first or last name
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(freelancer -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    return freelancer.getFname().toLowerCase().contains(lowerCaseFilter)
                            || freelancer.getLname().toLowerCase().contains(lowerCaseFilter)
                            || freelancer.getEmail().toLowerCase().contains(lowerCaseFilter)
                            || freelancer.getSkill().toLowerCase().contains(lowerCaseFilter);
                });
            });

            // Set list to table (just displaying)
            table.setItems(filteredList);

        } catch (SQLException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }


}
