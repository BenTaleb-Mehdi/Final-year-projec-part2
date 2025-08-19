package sample;

public class freelancers_level {

    private String firstname;
    private String lastname;
    private String domain;
    private Double income;
    private String levelName;

    public freelancers_level(String firstname, String lastname, String domain, Double income, String levelName) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.domain = domain;
        this.income = income;
        this.levelName = levelName;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
