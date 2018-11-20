package simplewebapp.domain;

/**
 * Created by Admin on 17.11.2018.
 */
public class Company {
    // id, name, headCompanyId

    private int id;
    private String name;
    private int headCompanyId;

    public Company(){

    }

    public Company(int id, String name, int headCompanyId){
        this.id = id;
        this.name = name;
        this.headCompanyId = headCompanyId;
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

    public int getHeadCompanyId() {
        return headCompanyId;
    }

    public void setHeadCompanyId(int headCompanyId) {
        this.headCompanyId = headCompanyId;
    }
}
