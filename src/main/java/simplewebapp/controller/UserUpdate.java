package simplewebapp.controller;

public class UserUpdate {
    private String name;
    private String companyId;
    private String bossId;
    private String id;


    public String getName(){
        return name;
    }

    public String getCompanyId(){
        return companyId;
    }

    public String getBossId(){
        return bossId;
    }

    public String getId(){
        return id;
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

    public void setId(String id){
        this.id = id;
    }


}

