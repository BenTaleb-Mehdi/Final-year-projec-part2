package sample;

public class TableAdmin {
    private int id;
    private String usernameApp;
    private  String passwordApp;
    private String dateApp;
    private String applicationName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsernameApp() {
        return usernameApp;
    }

    public void setUsernameApp(String usernameApp) {
        this.usernameApp = usernameApp;
    }

    public String getPasswordApp() {
        return passwordApp;
    }

    public void setPasswordApp(String passwordApp) {
        this.passwordApp = passwordApp;
    }

    public String getDateApp() {
        return dateApp;
    }

    public void setDateApp(String dateApp) {
        this.dateApp = dateApp;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
