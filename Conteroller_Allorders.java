package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Conteroller_Allorders implements Initializable {

    @FXML private TableView<Allorders> table_orders;
    @FXML private TableColumn<Allorders, String> nameOrder;
    @FXML private TableColumn<Allorders, String> priceOrder;
    @FXML private TableColumn<Allorders, String> typeOrder;
    @FXML private TableColumn<Allorders, String> statutOrder;
    @FXML private TableColumn<Allorders, String> clientOrder;

    @FXML private TableColumn<Allorders, String> dateOrder1;

    @FXML private TableColumn<Allorders, String> freelancerOrder;
    @FXML private TextField searchField;
    @FXML private Label total_orders;
    @FXML private Label total_orders_acc;
    @FXML private Label total_types_orders;

    ObservableList<Allorders> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameOrder.setCellValueFactory(new PropertyValueFactory<>("title_order"));
        priceOrder.setCellValueFactory(new PropertyValueFactory<>("price_order"));
        typeOrder.setCellValueFactory(new PropertyValueFactory<>("type_order"));
        statutOrder.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateOrder1.setCellValueFactory(new PropertyValueFactory<>("orderdate"));
        freelancerOrder.setCellValueFactory(new PropertyValueFactory<>("freelancer_name"));
        clientOrder.setCellValueFactory(new PropertyValueFactory<>("client_name"));

        loadTotalorders();
        loadTotalordersAcc();
        loadTotalUniqueTypes();
        loadData();
    }

    private void loadTotalorders() {
        int total = Connectiondb.getCount("SELECT COUNT(*) FROM orders ");
        total_orders.setText(String.valueOf(total));
    }

    private void loadTotalordersAcc() {
        int total = Connectiondb.getCount("SELECT COUNT(*) FROM orders WHERE status = 'Accepte'");
        total_orders_acc.setText(String.valueOf(total));
    }

    private void loadTotalUniqueTypes() {
        String query = "SELECT COUNT(DISTINCT type_offer) FROM orders";
        int totalTypes = Connectiondb.getCount(query);
        total_types_orders.setText(String.valueOf(totalTypes));
    }

    private void loadData() {
        ResultSet rs = Connectiondb.display(
                "SELECT o.title_offer, o.price_offer, o.type_offer, o.status, o.order_date, " +
                        "f.freelancer_name, CONCAT(c.firstname, ' ', c.lastname) AS client_name " +
                        "FROM orders o " +
                        "JOIN project_freelancers f ON o.offer_id = f.id " +
                        "JOIN web_signup_clients c ON o.clinet_id = c.id"
        );

        try {
            while (rs != null && rs.next()) {
                list.add(new Allorders(
                        rs.getString("title_offer"),
                        rs.getString("price_offer"),
                        rs.getString("type_offer"),
                        rs.getString("status"),
                        rs.getString("order_date"),
                        rs.getString("freelancer_name"),
                        rs.getString("client_name")
                ));
            }

            FilteredList<Allorders> filteredList = new FilteredList<>(list, b -> true);

            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(order -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    return order.getTitle_order().toLowerCase().contains(lowerCaseFilter)
                            || order.getPrice_order().toLowerCase().contains(lowerCaseFilter)
                            || order.getType_order().toLowerCase().contains(lowerCaseFilter)
                            || order.getStatus().toLowerCase().contains(lowerCaseFilter)
                            || order.getOrderdate().toLowerCase().contains(lowerCaseFilter)
                            || order.getFreelancer_name().toLowerCase().contains(lowerCaseFilter)
                            || order.getClient_name().toLowerCase().contains(lowerCaseFilter);
                });
                updateTotals(filteredList);
            });

            table_orders.setItems(filteredList);

        } catch (SQLException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    private void updateTotals(FilteredList<Allorders> filteredList) {
        int total = filteredList.size();
        total_orders.setText(String.valueOf(total));

        long acceptedCount = filteredList.stream()
                .filter(order -> "Accepte".equalsIgnoreCase(order.getStatus()))
                .count();
        total_orders_acc.setText(String.valueOf(acceptedCount));

        long uniqueTypesCount = filteredList.stream()
                .map(Allorders::getType_order)
                .distinct()
                .count();
        total_types_orders.setText(String.valueOf(uniqueTypesCount));
    }

}
