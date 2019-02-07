package simplewebapp.dao;

public class CompanyUpdate {
    private String name;
    private Integer headCompanyId;
    private Integer id;


    public String getName(){
        return name;
    }

    public Integer getHeadCompanyId(){
        return headCompanyId;
    }

    public Integer getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHeadCompanyId(Integer headCompanyId){
        this.headCompanyId = headCompanyId;
    }

    public void setId(Integer id){
        this.id = id;
    }

}
