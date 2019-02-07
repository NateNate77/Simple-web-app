package simplewebapp.dao;

public class AddNewCompany {

    private String name;
    private Integer headCompanyId;


    public String getName(){
        return name;
    }

    public Integer getHeadCompanyId(){
        return headCompanyId;
    }


    public void setName(String name){
        this.name = name;
    }

    public void setHeadCompanyId(Integer headCompanyId){
        this.headCompanyId = headCompanyId;
    }

}
