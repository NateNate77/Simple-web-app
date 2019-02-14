package simplewebapp.dao;

import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import simplewebapp.domain.Company;
import simplewebapp.domain.CompanyTree;
import simplewebapp.jooq.tables.Companies;
import simplewebapp.jooq.tables.Staff;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.count;

/**
 * Created by Admin on 21.11.2018.
 */
@Repository
public class CompanyDAO  {


    @Autowired(required = true)
    private DSLContext dsl;

    private Companies companyMain = Companies.COMPANIES.as("S1");
    private Companies companyBoss = Companies.COMPANIES.as("S2");

    private SelectOnConditionStep<Record1<Integer>> resultQueryCompanyCount(){
        return (SelectOnConditionStep<Record1<Integer>>) dsl
                .select(count(Companies.COMPANIES.ID).as("CompaniesCount"))
                .from(Companies.COMPANIES);
    }


    private SelectOnConditionStep<Record5<Integer, String, Integer, String, Integer>> resultQuery() {
        return dsl
                .select(companyMain.ID, companyMain.NAME, companyMain.HEADCOMPANYID, companyBoss.NAME, count(Staff.STAFF.ID).as("UsersCount"))
                .from(companyMain)
                .leftJoin(Staff.STAFF)
                .on(Staff.STAFF.COMPANYID.equal(companyMain.ID))
                .leftJoin(companyBoss)
                .on(companyBoss.ID.equal(companyMain.HEADCOMPANYID));

    }

    private List<Company> getCompanyList(Result<Record5<Integer, String, Integer, String, Integer>> result){

        List<Company> list = new ArrayList<>();
        for (Record5 r : result) {

            Company company = getCompany(r);

            list.add(company);
        }

        return list;
    }

    private Company getCompany(Record5 record) {
        Integer id = record.getValue(companyMain.ID, Integer.class);
        String name = record.getValue(companyMain.NAME, String.class);
        Integer headCompanyId = record.getValue(companyMain.HEADCOMPANYID, Integer.class);
        String headCompanyName = record.getValue(companyBoss.NAME, String.class);
        Integer usersCount = record.getValue("UsersCount", Integer.class);

        return new Company(id, name, headCompanyId, headCompanyName, usersCount);
    }


    public List<Company> getCompanies() {

        Result<Record5<Integer, String, Integer, String, Integer>> result = resultQuery()
                .groupBy(companyMain.ID, companyMain.NAME, companyMain.HEADCOMPANYID, companyBoss.NAME)
                .fetch();

        List<Company> list = getCompanyList(result);

        return list;

    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void addCompany(String name, Integer headCompanyId) throws Exception {
        String nameCompany = name.trim();
        if(nameCompany.isEmpty()){

            throw new Exception("Введите название организации");
        }

        dsl.insertInto(Companies.COMPANIES)
                .set(Companies.COMPANIES.NAME, nameCompany)
                .set(Companies.COMPANIES.HEADCOMPANYID, headCompanyId)
                .execute();

    }

    public Company getCompanyForUpdate(Integer id) {

        Record5<Integer, String, Integer, String, Integer> result = resultQuery()
                .where(companyMain.ID.eq(id))
                .groupBy(companyMain.ID, companyMain.NAME, companyMain.HEADCOMPANYID, companyBoss.NAME)
                .fetchOne();

        Company company = getCompany(result);
        return company;

    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void updateCompany(String name, Integer headCompanyId, Integer id) throws Exception {
        String nameCompany = name.trim();
        if(nameCompany.isEmpty()){

            throw new Exception("Введите название организации");
        }

        if(headCompanyId==id){
            throw new Exception("Нельзя устанавливать головной организацией саму себя");
        }

        dsl.update(Companies.COMPANIES)
                .set(Companies.COMPANIES.NAME, nameCompany)
                .set(Companies.COMPANIES.HEADCOMPANYID, headCompanyId)
                .where(Companies.COMPANIES.ID.equal(id))
                .execute();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void deleteCompany(Integer id) throws Exception {

        int r = resultQueryCompanyCount().where(Companies.COMPANIES.HEADCOMPANYID.eq(id)).fetchOne(0, int.class);
        if(r>0){
            throw new Exception("Организация не может быть удалена, так как у нее есть дочерние организации");
        }

                Record5<Integer, String, Integer, String, Integer> resultForDelete = resultQuery()
                .where(companyMain.ID.eq(id))
                .groupBy(companyMain.ID, companyMain.NAME, companyMain.HEADCOMPANYID, companyBoss.NAME)
                .fetchOne();

            Company companiesForDelete = getCompany(resultForDelete);
            if(companiesForDelete.getUsersCount()>0){
                throw new Exception("Организация не может быть удалена, так как у нее есть сотрудники");
            }

        dsl.delete(Companies.COMPANIES)
                .where(Companies.COMPANIES.ID.eq(id))
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
