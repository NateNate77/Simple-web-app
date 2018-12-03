package simplewebapp.domain;

/**
 * Created by Admin on 17.11.2018.
 */
public class Company {


    private int id;
    private String name;
    private Integer headCompanyId;
    private String headCompanyName;
    private int usersCount;

    public Company(){

    }

    public Company(int id, String name, Integer headCompanyId, String headCompanyName, int usersCount){
        this.id = id;
        this.name = name;
        this.headCompanyId = headCompanyId;
        this.headCompanyName = headCompanyName;
        this.usersCount = usersCount;
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

    public Integer getHeadCompanyId() {
        return headCompanyId;
    }

    public void setHeadCompanyId(int headCompanyId) {
        this.headCompanyId = headCompanyId;
    }

    public String getHeadCompanyName() {
        return headCompanyName;
    }

    public void setHeadCompanyName(String headCompanyName) {
        this.headCompanyName = headCompanyName;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }
}
