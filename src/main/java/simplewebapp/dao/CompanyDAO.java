package simplewebapp.dao;

import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import simplewebapp.domain.Company;
import simplewebapp.domain.CompanyTree;
import simplewebapp.jooq.tables.Companies;
import simplewebapp.jooq.tables.Staff;


import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.count;

/**
 * Created by Admin on 21.11.2018.
 */
@Repository
@Transactional
public class CompanyDAO extends JdbcDaoSupport {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public CompanyDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    @Autowired
    private DSLContext dsl;

    private Companies companyMain = Companies.COMPANIES.as("S1");
    private Companies companyBoss = Companies.COMPANIES.as("S2");

    private SelectOnConditionStep<Record5<Integer, String, Integer, String, Integer>> resultQuery() {
        return dsl
                .select(companyMain.ID, companyMain.NAME, companyMain.HEADCOMPANYID, companyBoss.NAME, count(Staff.STAFF.ID).as("UsersCount"))
                .from(companyMain)
                .leftJoin(Staff.STAFF)
                .on(Staff.STAFF.COMPANYID.equal(companyMain.ID))
                .leftJoin(companyBoss)
                .on(companyBoss.ID.equal(companyMain.HEADCOMPANYID));

    }

    public List<Company> getCompanyList(Result<Record5<Integer, String, Integer, String, Integer>> result){

        List<Company> list = new ArrayList<>();
        for (Record5 r : result) {

            Integer id = r.getValue(companyMain.ID, Integer.class);
            String name = r.getValue(companyMain.NAME, String.class);
            Integer headCompanyId = r.getValue(companyMain.HEADCOMPANYID, Integer.class);
            String headCompanyName = r.getValue(companyBoss.NAME, String.class);
            Integer usersCount = r.getValue("UsersCount", Integer.class);

            Company company = new Company(id, name, headCompanyId, headCompanyName, usersCount);

            list.add(company);
        }

        return list;
    }


    public List<Company> getCompanies() {

        Result<Record5<Integer, String, Integer, String, Integer>> result = resultQuery().groupBy(companyMain.ID, companyMain.NAME, companyMain.HEADCOMPANYID, companyBoss.NAME).fetch();

        List<Company> list = getCompanyList(result);

        return list;

    }


    public void addCompany(String name, String headCompanyId) throws Exception {
        String nameCompany = name.trim();
        if(nameCompany.isEmpty()){

            throw new Exception("Введите название организации");
        }

        Integer headCompanyIdInt = headCompanyId.equals("") ? null : Integer.parseInt(headCompanyId);

        dsl.insertInto(Companies.COMPANIES)
                .set(Companies.COMPANIES.NAME, nameCompany)
                .set(Companies.COMPANIES.HEADCOMPANYID, headCompanyIdInt)
                .execute();

    }

    public Company getCompanyForUpdate(String id){

        Integer idInt = Integer.parseInt(id);
        Result<Record5<Integer, String, Integer, String, Integer>> result = resultQuery().where(companyMain.ID.eq(idInt)).groupBy(companyMain.ID, companyMain.NAME, companyMain.HEADCOMPANYID, companyBoss.NAME).fetch();

        List<Company> company = getCompanyList(result);

        return company.get(0);

    }

    public void updateCompany(String name, String headCompanyId, String id) throws Exception {
        String nameCompany = name.trim();
        if(nameCompany.isEmpty()){

            throw new Exception("Введите название организации");
        }


        if(headCompanyId.equals(id)){
            throw new Exception("Нельзя устанавливать головной организацией саму себя");
        }

        Integer headCompanyIdInt = headCompanyId.equals("") ? null : Integer.parseInt(headCompanyId);
        Integer idInt = Integer.parseInt(id);


        dsl.update(Companies.COMPANIES)
                .set(Companies.COMPANIES.NAME, nameCompany)
                .set(Companies.COMPANIES.HEADCOMPANYID, headCompanyIdInt)
                .where(Companies.COMPANIES.ID.equal(idInt))
                .execute();
    }

    public void deleteCompany(String id) throws Exception {

        Integer idInt = Integer.parseInt(id);
        Result<Record5<Integer, String, Integer, String, Integer>> result = resultQuery().where(companyMain.HEADCOMPANYID.eq(idInt)).groupBy(companyMain.ID, companyMain.NAME, companyMain.HEADCOMPANYID, companyBoss.NAME).fetch();
        List<Company> companies = getCompanyList(result);
        if(companies.size()>0){
            throw new Exception("Организация не может быть удалена, так как у нее есть дочерние организации");
        }

        Result<Record5<Integer, String, Integer, String, Integer>> resultForDelete = resultQuery().where(companyMain.ID.eq(idInt)).groupBy(companyMain.ID, companyMain.NAME, companyMain.HEADCOMPANYID, companyBoss.NAME).fetch();


        List<Company> companiesForDelete = getCompanyList(resultForDelete);
            // проверяем только первый элемент списка, так как id уникальный для каждой организации
            if(companiesForDelete.get(0).getUsersCount()>0){
                throw new Exception("Организация не может быть удалена, так как у нее есть сотрудники");
            }

        dsl.delete(Companies.COMPANIES)
                .where(Companies.COMPANIES.ID.eq(idInt))
                .execute();


    }


    public List<CompanyTree> companyTreeView(){
        List <Company> companyList = getCompanies();
        List<Company> headCompanyList = new ArrayList<>();
        for (int i = 0; i < companyList.size(); i++){
            if(companyList.get(i).getHeadCompanyId() == null){
                headCompanyList.add(companyList.get(i));
            }
        }

        List<CompanyTree> companyTreeList = companyTree(companyList, headCompanyList);
        return companyTreeList;
    }


    public List<CompanyTree> companyTree(List<Company> companyList, List<Company> headCompanyList){
        List<CompanyTree> companyTreeList = new ArrayList<>();

        for (int j = 0; j<headCompanyList.size(); j++){
            CompanyTree companyTree = new CompanyTree();
            companyTree.company = headCompanyList.get(j);
            companyTreeList.add(companyTree);

            recursionCompanyTree(companyTree, companyList);

        }
        return companyTreeList;
    }

    public CompanyTree recursionCompanyTree(CompanyTree companyTree, List<Company> companyList){

        for(int i = 0; i<companyList.size(); i++){

            if(companyList.get(i).getHeadCompanyId() != null && companyList.get(i).getHeadCompanyId() == companyTree.company.getId()){

                CompanyTree companyTreeIn = new CompanyTree();
                companyTreeIn.company = companyList.get(i);
                companyTree.companyTrees.add(companyTreeIn);
                recursionCompanyTree(companyTreeIn, companyList);
            }
        }

        return companyTree;
    }

}
