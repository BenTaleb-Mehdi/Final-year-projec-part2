package sample;

import java.util.Date;

public class Allorders {
    private String title_order;
    private String price_order;
    private String type_order;
    private String status;
    private String orderdate;
    private String freelancer_name;



    private String client_name;





    public Allorders(String title_order, String price_order, String type_order, String status,
                     String orderdate, String freelancer_name, String client_name) {
        this.title_order = title_order;
        this.price_order = price_order;
        this.type_order = type_order;
        this.status = status;
        this.orderdate = orderdate;
        this.freelancer_name = freelancer_name;
        this.client_name = client_name; // ✅ This was missing
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }


    public String getTitle_order() {
        return title_order;
    }

    public void setTitle_order(String title_order) {
        this.title_order = title_order;
    }

    public String getPrice_order() {
        return price_order;
    }

    public void setPrice_order(String price_order) {
        this.price_order = price_order;
    }

    public String getType_order() {
        return type_order;
    }

    public void setType_order(String type_order) {
        this.type_order = type_order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getFreelancer_name() {
        return freelancer_name;
    }

    public void setFreelancer_name(String freelancer_name) {
        this.freelancer_name = freelancer_name;
    }
}
