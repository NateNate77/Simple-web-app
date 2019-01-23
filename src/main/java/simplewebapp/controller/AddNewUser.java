package simplewebapp.controller;

public class AddNewUser {
    private String name;
    private String companyId;
    private String bossId;

    public String getName(){
        return name;
    }

    public String getCompanyId(){
        return companyId;
    }

    public String getBossId(){
        return bossId;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCompanyId(String companyId){
        this.companyId = companyId;
    }
    public void setBossId(String bossId){
        this.bossId = bossId;
    }


}
