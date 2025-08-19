package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class dashboard_controller {
    @FXML
    private Label balance_total_porcentage;
    @FXML
    private Label offers_total_porcentage;

    @FXML
    private Label orders_total_porcentage;
    @FXML
    private Label clients_total_porcentage;

    @FXML
    private Label freelancers_total_porcentage;



    @FXML
    private Label name_freelancer;
    @FXML
    private Label type_skill;
    @FXML
    private BarChart<String, Number> LineChart;

    @FXML
    private Label Total_clients;

    @FXML
    private VBox freelancerTemplateBox;
    @FXML
    private Label Total_freelancers;

    @FXML
    private Label total_balance;
    @FXML
    private Label Total_Orderes;

    @FXML
    private Label total_offers;

    @FXML
    public void initialize() {

        loadTotalFreelancers();
        loadTotalClients();
        loadTotalOffers();
        loadTotaloreders();
        loadTotalBalance();
        loadFreelancersPerMonth();
        loadTopFreelancers();
    }

    private void loadTopFreelancers() {
        String sql = "SELECT wsf.firstname, wsf.lastname, wsf.skill_name " +
                "FROM web_signup_freelancers wsf " +
                "JOIN freelancer_accounts fa ON wsf.id = fa.freelancer_id " +
                "ORDER BY fa.balance DESC LIMIT 5";

        ResultSet rs = Connectiondb.display(sql);

        try {
            while (rs != null && rs.next()) {
                String fullName = rs.getString("firstname") + " " + rs.getString("lastname");
                String typeAccount = rs.getString("skill_name");

                HBox freelancerBox = createFreelancerHBox(fullName, typeAccount);
                freelancerTemplateBox.getChildren().add(freelancerBox);
            }
            if (rs != null) rs.getStatement().getConnection().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HBox createFreelancerHBox(String name, String type) {
        HBox hbox = new HBox(10); // spacing between elements
        hbox.getStyleClass().add("best_freelancer");

        // Load the image (replace with your actual path or logic)
        ImageView imageView = new ImageView(new Image(getClass().getResource("/imges/iconusers.png").toExternalForm()));
        imageView.setFitHeight(24);
        imageView.setFitWidth(24);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(name);
        HBox nameBox = new HBox(5, imageView, nameLabel); // small spacing between image and name

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label typeLabel = new Label(type);

        hbox.getChildren().addAll(nameBox, spacer, typeLabel);
        VBox.setMargin(hbox, new Insets(0, 0, 7, 0));

        return hbox;
    }



    private void loadTotalFreelancers() {
        String query = "SELECT COUNT(*) " +
                "FROM web_signup_freelancers " +
                "WHERE date_created >= DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m-01') " +
                "AND date_created < DATE_FORMAT(CURDATE(), '%Y-%m-01')";

        String queryTotal = "SELECT COUNT(*) FROM web_signup_freelancers";

        int total = Connectiondb.getCount(queryTotal);
        int totalLastMonth = Connectiondb.getCount(query);


        System.out.println("Last month's new freelancers: " + total);
        Total_freelancers.setText(String.valueOf(total));
        freelancers_total_porcentage.setText(String.format("+ %d", totalLastMonth));

    }

    private void loadTotalClients() {
        String query = "SELECT COUNT(*) " +
                "FROM web_signup_clients " +
                "WHERE date_created >= DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m-01') " +
                "AND date_created < DATE_FORMAT(CURDATE(), '%Y-%m-01')";
        int total = Connectiondb.getCount("SELECT COUNT(*) FROM web_signup_clients");
        int totalLastMonth = Connectiondb.getCount(query);

        clients_total_porcentage.setText(String.format("+ %d", totalLastMonth));
        Total_clients.setText(String.valueOf(total));

    }

    private void loadTotalOffers() {
        String queryLastMonth = "SELECT COUNT(*) FROM project_freelancers " +
                "WHERE date_created >= DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m-01') " +
                "AND date_created < DATE_FORMAT(CURDATE(), '%Y-%m-01')";
        int total = Connectiondb.getCount("SELECT COUNT(*) FROM project_freelancers");
        int totalLastMonth = Connectiondb.getCount(queryLastMonth);

        offers_total_porcentage.setText(String.format("+ %d", totalLastMonth));
        total_offers.setText(String.valueOf(total));
    }

    private void loadTotaloreders() {
        String queryLastMonth = "SELECT COUNT(*) FROM orders " +
                "WHERE date_created >= DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m-01') " +
                "AND date_created < DATE_FORMAT(CURDATE(), '%Y-%m-01')";
        int total = Connectiondb.getCount("SELECT COUNT(*) FROM orders");
        int totalLastMonth = Connectiondb.getCount(queryLastMonth);

        orders_total_porcentage.setText(String.format("+ %d", totalLastMonth));
        Total_Orderes.setText(String.valueOf(total));
    }
    private void loadTotalBalance() {
        String query = "SELECT SUM(balance) FROM freelancer_accounts " +
                "WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)";

        String query_total = "SELECT SUM(balance) FROM freelancer_accounts";

        double totalLast6Months = Connectiondb.getDoubleValue(query);
        double totalAllTime = Connectiondb.getDoubleValue(query_total);



        balance_total_porcentage.setText(String.format("%.1f $", totalLast6Months));
        total_balance.setText(String.format("%.2f", totalAllTime));

    }






    private void loadFreelancersPerMonth() {
        String query = "SELECT MONTH(date_created) AS month, COUNT(*) AS total FROM web_signup_freelancers GROUP BY MONTH(date_created) ORDER BY MONTH(date_created)";

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        int maxTotal = 0;

        ResultSet rs = Connectiondb.display(query);
        try {
            while (rs != null && rs.next()) {
                int month = rs.getInt("month");
                int total = rs.getInt("total");
                if (total > maxTotal) maxTotal = total;
                String monthLabel = getMonthName(month);

                series.getData().add(new XYChart.Data<>(monthLabel, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Configure Y-axis with calculated maxTotal
        NumberAxis yAxis = (NumberAxis) LineChart.getYAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(maxTotal + 1);  // Add a little padding above max
        yAxis.setTickUnit(1);
        yAxis.setMinorTickCount(0);
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number object) {
                if (object == null) return "";
                return Integer.toString(object.intValue());
            }
        });
        LineChart.getData().clear();
        LineChart.getData().add(series);
    }
    private String getMonthName(int month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        if (month >= 1 && month <= 12) {
            return months[month - 1];
        } else {
            return "Unknown";
        }
    }




}
