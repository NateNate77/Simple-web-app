package simplewebapp.dao;

public class UserUpdate {
    private String name;
    private Integer companyId;
    private Integer bossId;
    private Integer id;


    public String getName(){
        return name;
    }

    public Integer getCompanyId(){
        return companyId;
    }

    public Integer getBossId(){
        return bossId;
    }

    public Integer getId(){
        return id;
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

    public void setId(Integer id){
        this.id = id;
    }


}

