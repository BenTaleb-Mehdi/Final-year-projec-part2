package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Smart_Analysis {
    @FXML
    private PieChart Piechart_levels;
    @FXML
    private BarChart<String, Number> chart_balance;
    @FXML
    private TableView<freelancers_level> table_classes;

    @FXML
    private TableColumn<freelancers_level, String> firstname;

    @FXML
    private TableColumn<freelancers_level, String> lastname;

    @FXML
    private TableColumn<freelancers_level, String> domain;

    @FXML
    private TableColumn<freelancers_level, Double> income;

    @FXML
    private TableColumn<freelancers_level, String> levelName;
    @FXML
    private Label total_offers_porcentage;
    @FXML
    private Label total_new_freelancers_porcentage;
    @FXML
    private Label total_offers_por_pourcentage;
    @FXML
    private Label total_transactions;
    @FXML
    private Label total_new_freelancers;
    @FXML
    private Label total_offers;

    @FXML
    private TextField searchField;

    @FXML
    private Label total_clients_por_pourcentage;
    @FXML
    private Label total_clients;


    private ObservableList<freelancers_level> freelancerLevels = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        firstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        domain.setCellValueFactory(new PropertyValueFactory<>("domain"));
        income.setCellValueFactory(new PropertyValueFactory<>("income"));
        levelName.setCellValueFactory(new PropertyValueFactory<>("levelName"));
        getTotalProjectsLastMonth();
        getTotalNewFreelancersLastMonth();
        getTotalTransactionsLastMonth();
        gettotalclients();

        loadBalancePerMonth();
        loadPieChartPEI();

        income.setCellFactory(column -> new TableCell<freelancers_level, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("$ %.2f", item));
                    if (item == 0) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: green;");
                    }
                }
            }
        });


        levelName.setCellFactory(column -> new TableCell<freelancers_level, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item.toLowerCase()) {
                        case "junior":
                            setStyle("-fx-text-fill: #4A90E2; -fx-font-weight: bold;");
                            break;
                        case "mid":
                            setStyle("-fx-text-fill: #F5A623; -fx-font-weight: bold;");
                            break;
                        case "senior":
                            setStyle("-fx-text-fill: #27AE60; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("-fx-text-fill: black;");
                            break;
                    }
                }
            }
        });

        loadFreelancerLevels("");


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadFreelancerLevels(newValue.trim());
        });

        table_classes.setItems(freelancerLevels);


    }

    public void gettotalclients() {
        String query = "SELECT COUNT(*) " +
                "FROM web_signup_clients " +
                "WHERE date_created >= DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m-01') " +
                "AND date_created < DATE_FORMAT(CURDATE(), '%Y-%m-01')";

        String query_totalNewFreelancers = "SELECT COUNT(*) FROM web_signup_clients";

        int totalclients = Connectiondb.getCount(query_totalNewFreelancers);
        int totalNewclients = Connectiondb.getCount(query);



        total_clients_por_pourcentage.setText(String.format("+ %d",totalNewclients));
        total_clients.setText(String.valueOf(totalclients));
    }

    public void getTotalProjectsLastMonth() {
        String query = "SELECT COUNT(*) " +
                "FROM project_freelancers " +
                "WHERE date_created >= DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m-01') " +
                "AND date_created < DATE_FORMAT(CURDATE(), '%Y-%m-01')";

        String query_offers = "SELECT COUNT(*) FROM project_freelancers";

        int total = Connectiondb.getCount(query_offers);
        int total_in_month = Connectiondb.getCount(query);


        System.out.println("Last month's total projects: " + total_in_month);
        total_offers.setText(String.valueOf(total));
        total_offers_porcentage.setText(String.format("+ %d", total_in_month));
    }


    public void getTotalNewFreelancersLastMonth() {
        String query = "SELECT COUNT(*) " +
                "FROM web_signup_freelancers " +
                "WHERE date_created >= DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m-01') " +
                "AND date_created < DATE_FORMAT(CURDATE(), '%Y-%m-01')";

        String query_totalNewFreelancers = "SELECT COUNT(*) FROM web_signup_freelancers";

        int totalFreelancers = Connectiondb.getCount(query_totalNewFreelancers);
        int totalNewFreelancers = Connectiondb.getCount(query);



        total_new_freelancers_porcentage.setText(String.format("+ %d",totalNewFreelancers));
        total_new_freelancers.setText(String.valueOf(totalFreelancers));
    }


    public void getTotalTransactionsLastMonth() {
        String query = "SELECT COUNT(*) FROM ecom_orders WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)";

        String query_total = "SELECT SUM(balance) FROM freelancer_accounts";

        double totalLastMonths = Connectiondb.getDoubleValue(query);
        double totalAllTime = Connectiondb.getDoubleValue(query_total);

        total_offers_por_pourcentage.setText("+" + totalLastMonths);
        total_transactions.setText(String.format("%.2f", totalAllTime));
    }
    private void loadPieChartPEI() {
        String query = "SELECT " +
                "CASE " +
                " WHEN income < 1000 THEN 'Junior' " +
                " WHEN income >= 1000 AND income < 3000 THEN 'Mid' " +
                " WHEN income >= 3000 AND income < 5000 THEN 'Senior' " +
                " ELSE 'Expert' " +
                "END AS levelName, " +
                "SUM(income) AS totalIncome " +
                "FROM ( " +
                " SELECT f.id, COALESCE(SUM(fa.balance), 0) AS income " +
                " FROM web_signup_freelancers f " +
                " LEFT JOIN freelancer_accounts fa ON f.id = fa.freelancer_id " +
                " GROUP BY f.id " +
                ") AS sub " +
                "GROUP BY levelName " +
                "HAVING levelName IN ('Junior', 'Mid', 'Senior')";

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        ResultSet rs = Connectiondb.display(query);
        try {
            while (rs != null && rs.next()) {
                String level = rs.getString("levelName");
                double totalIncome = rs.getDouble("totalIncome");
                pieChartData.add(new PieChart.Data(level, totalIncome));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.getStatement().getConnection().close();
            } catch (Exception e) {
                System.out.println("Error closing DB connection: " + e.getMessage());
            }
        }

        Piechart_levels.setData(pieChartData);

    }

    private void loadBalancePerMonth() {
        String query = "SELECT MONTH(created_at) AS month, SUM(balance) AS total " +
                "FROM freelancer_accounts " +
                "GROUP BY MONTH(created_at) " +
                "ORDER BY MONTH(created_at)";

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        double maxTotal = 0;

        ResultSet rs = Connectiondb.display(query);
        try {
            while (rs != null && rs.next()) {
                int month = rs.getInt("month");
                double total = rs.getDouble("total");  // Use double for money sums
                if (total > maxTotal) maxTotal = total;
                String monthLabel = getMonthName(month);

                series.getData().add(new XYChart.Data<>(monthLabel, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Configure Y-axis with maxTotal (scale with some padding)
        NumberAxis yAxis = (NumberAxis) chart_balance.getYAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(maxTotal * 1.1);  // 10% padding on top
        yAxis.setTickUnit(calculateTickUnit(maxTotal)); // Dynamic tick unit
        yAxis.setMinorTickCount(0);

        // Optional: format Y axis labels as currency
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "$", null));

        chart_balance.getData().clear();
        chart_balance.getData().add(series);
    }

    private double calculateTickUnit(double maxValue) {
        // Simple function to decide tick unit for Y-axis depending on max value
        if (maxValue <= 10) return 1;
        else if (maxValue <= 100) return 10;
        else if (maxValue <= 1000) return 100;
        else if (maxValue <= 10000) return 1000;
        else return 10000;
    }

    private String getMonthName(int month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        if (month >= 1 && month <= 12) {
            return months[month - 1];
        } else {
            return "Unknown";
        }
    }



    private void loadFreelancerLevels(String search) {
        // Base query
        String query = "SELECT " +
                "f.firstname, f.lastname, f.skill_name AS domain, " +
                "COALESCE(SUM(fa.balance), 0) AS income, " +
                "CASE " +
                " WHEN COALESCE(SUM(fa.balance), 0) < 1000 THEN 'Junior' " +
                " WHEN COALESCE(SUM(fa.balance), 0) >= 1000 AND COALESCE(SUM(fa.balance), 0) < 3000 THEN 'Mid' " +
                " WHEN COALESCE(SUM(fa.balance), 0) >= 3000 AND COALESCE(SUM(fa.balance), 0) < 5000 THEN 'Senior' " +
                " ELSE 'Expert' " +
                "END AS levelName " +
                "FROM web_signup_freelancers f " +
                "LEFT JOIN freelancer_accounts fa ON f.id = fa.freelancer_id ";

        // Add WHERE clause if search is not empty
        if (search != null && !search.isEmpty()) {
            query += "WHERE f.firstname LIKE '%" + search + "%' " +
                    "OR f.lastname LIKE '%" + search + "%' " +
                    "OR f.skill_name LIKE '%" + search + "%' ";
        }

        query += "GROUP BY f.id, f.firstname, f.lastname, f.skill_name " +
                "ORDER BY income DESC " +
                "LIMIT 5";

        System.out.println("Running query: " + query);

        ResultSet rs = null;
        try {
            rs = Connectiondb.display(query);

            if (rs == null) {
                System.out.println("ResultSet is null");
                return;
            }

            freelancerLevels.clear();

            while (rs.next()) {
                String firstnameVal = rs.getString("firstname");
                String lastnameVal = rs.getString("lastname");
                String domainVal = rs.getString("domain");
                double incomeVal = rs.getDouble("income");
                String levelNameVal = rs.getString("levelName");

                freelancers_level fl = new freelancers_level(firstnameVal, lastnameVal, domainVal, incomeVal, levelNameVal);
                freelancerLevels.add(fl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.getStatement().getConnection().close();
            } catch (Exception e) {
                System.out.println("Error closing DB connection: " + e.getMessage());
            }
        }
    }


}