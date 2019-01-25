package simplewebapp.dao;

public class AddNewCompany {

    private String name;
    private String headCompanyId;


    public String getName(){
        return name;
    }

    public String getHeadCompanyId(){
        return headCompanyId;
    }


    public void setName(String name){
        this.name = name;
    }

    public void setHeadCompanyId(String headCompanyId){
        this.headCompanyId = headCompanyId;
    }

}
