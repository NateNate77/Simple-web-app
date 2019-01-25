package simplewebapp.dao;

public class CompanyUpdate {
    private String name;
    private String headCompanyId;
    private String id;


    public String getName(){
        return name;
    }

    public String getHeadCompanyId(){
        return headCompanyId;
    }

    public String getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHeadCompanyId(String headCompanyId){
        this.headCompanyId = headCompanyId;
    }

    public void setId(String id){
        this.id = id;
    }

}
