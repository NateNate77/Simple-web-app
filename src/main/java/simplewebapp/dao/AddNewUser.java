package simplewebapp.dao;

public class AddNewUser {
    private String name;
    private Integer companyId;
    private Integer bossId;

    public String getName(){
        return name;
    }

    public Integer getCompanyId(){
        return companyId;
    }

    public Integer getBossId(){
        return bossId;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCompanyId(Integer companyId){
        this.companyId = companyId;
    }
    public void setBossId(Integer bossId){
        this.bossId = bossId;
    }


}
