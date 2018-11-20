package simplewebapp.domain;

/**
 * Created by Admin on 17.11.2018.
 */
public class User {
    // id, name, companyId, bossId
    private int id;
    private String name;
    private int companyId;
    private Integer bossId;
    private String bossName;
    private String companyName;

    public User(){

    }

    public User(int id, String name, int companyId, Integer bossId, String bossName, String companyName){
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.bossId = bossId;
        this.bossName = bossName;
        this.companyName = companyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public Integer getBossId() {
        return bossId;
    }

    public void setBossId(Integer bossId) {
        this.bossId = bossId;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
