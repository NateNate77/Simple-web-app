package simplewebapp.dao;

import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import simplewebapp.domain.User;
import simplewebapp.domain.UserTree;
import simplewebapp.jooq.tables.Companies;
import simplewebapp.jooq.tables.Staff;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.count;

/**
 * Created by Admin on 19.11.2018.
 */
@Repository
public class UserDAO  {


    @Autowired(required = true)
    private DSLContext dsl;

    private Staff staffMain = Staff.STAFF.as("S1");
    private Staff staffBoss = Staff.STAFF.as("S2");

    private SelectOnConditionStep<Record1<Integer>> resultQueryCountUser(){
        return (SelectOnConditionStep<Record1<Integer>>) dsl
                .select(count(Staff.STAFF.ID).as("UsersCount"))
                .from(Staff.STAFF);
    }


    private SelectOnConditionStep<Record6<Integer, String, Integer, Integer, String, String>> resultQuery() {
        return dsl
                .select(staffMain.ID, staffMain.NAME, staffMain.BOSSID, staffMain.COMPANYID, Companies.COMPANIES.NAME, staffBoss.NAME)
                .from(staffMain)
                .innerJoin(Companies.COMPANIES)
                .on(staffMain.COMPANYID.equal(Companies.COMPANIES.ID))
                .leftJoin(staffBoss)
                .on(staffBoss.ID.equal(staffMain.BOSSID));
    }

    private User getUser(Record6<Integer, String, Integer, Integer, String, String> record){

            Integer id = record.getValue(staffMain.ID, Integer.class);
            String name = record.getValue(staffMain.NAME, String.class);
            Integer bossId = record.getValue(staffMain.BOSSID, Integer.class);
            Integer companyId = record.getValue(staffMain.COMPANYID, Integer.class);
            String companyName = record.getValue(Companies.COMPANIES.NAME, String.class);
            String bossName = record.getValue(staffBoss.NAME, String.class);

             return new User (id, name, companyId,  bossId, bossName, companyName);

    }

    private List<User> getUserList(Result<Record6<Integer, String, Integer, Integer, String, String>> result){

        List<User> list = new ArrayList<>();
        for (Record6 r : result) {

            User user = getUser(r);

            list.add(user);
        }

        return list;
    }

    public List<User> getUsers() {

        Result<Record6<Integer, String, Integer, Integer, String, String>> result = resultQuery().fetch();
        List<User> list = getUserList(result);

        return list;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void addUser(String name, Integer bossId, Integer companyId) throws Exception {
        String nameUser = name.trim();
        if(nameUser.isEmpty()){

            throw new Exception("Введите имя");
        }

        dsl.insertInto(Staff.STAFF)
                .set(Staff.STAFF.NAME, nameUser)
                .set(Staff.STAFF.BOSSID, bossId)
                .set(Staff.STAFF.COMPANYID, companyId)
                .execute();
    }

    public List<User> getUsersByCompany(Integer companyId, Integer id){

        Result<Record6<Integer, String, Integer, Integer, String, String>> result = resultQuery().where(staffMain.COMPANYID.eq(companyId)).fetch();

       List<User> listUsersByCompany = getUserList(result);

        for (int i = 0; i<listUsersByCompany.size(); i++){
            if(listUsersByCompany.get(i).getId()==id){
                listUsersByCompany.remove(i);
            }
        }

       return listUsersByCompany;

    }

    public List<User> getUsersByCompanyForAddNewUser(Integer companyId){

        Result<Record6<Integer, String, Integer, Integer, String, String>> result = resultQuery().where(staffMain.COMPANYID.eq(companyId)).fetch();

        List<User> listUsersByCompany = getUserList(result);

        return listUsersByCompany;

    }

    public User getUserForUpdate(Integer id){

        Record6<Integer, String, Integer, Integer, String, String> result = resultQuery().where(staffMain.ID.eq(id)).fetchOne();
        User user = getUser(result);
        return user;

    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void updateUser(String name, Integer bossId, Integer companyId, Integer id) throws Exception {

        int r = resultQueryCountUser().where(Staff.STAFF.BOSSID.eq(id)).fetchOne(0, int.class);

        String nameUser = name.trim();
        if(nameUser.isEmpty()){

            throw new Exception("Введите имя");
        }
        if(getUserForUpdate(id).getCompanyId()!=companyId && r>0){

            throw new Exception("Сотрудник не может быть перемещен в другую организацию, так как у него есть подчиненные");
        }

        if(bossId==id){
            throw new Exception("Нельзя устанавливать руководителем самого себя");
        }

        dsl.update(Staff.STAFF)
                .set(Staff.STAFF.NAME, nameUser)
                .set(Staff.STAFF.COMPANYID, companyId)
                .set(Staff.STAFF.BOSSID, bossId)
                .where(Staff.STAFF.ID.equal(id))
                .execute();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void deleteUser(Integer id) throws Exception {

        int r = resultQueryCountUser().where(Staff.STAFF.BOSSID.eq(id)).fetchOne(0, int.class);

        if(r>0){
            throw new Exception("Сотрудник не может быть удален, так как у него есть подчиненные");
        }
        else {

            dsl.delete(Staff.STAFF)
                    .where(Staff.STAFF.ID.eq(id))
                    .execute();
        }

    }


    public List<UserTree> staffTreeView(){
        List<User> bossList = new ArrayList<>();
        List <User> userList = getUsers();

        for (int i = 0; i < userList.size(); i++){
            if(userList.get(i).getBossId()==null){
                bossList.add(userList.get(i));
            }
        }
        List<UserTree> userTreeList = userTree(userList, bossList);

        return userTreeList;
    }



    public List<UserTree> userTree(List<User> userList, List<User> bossList){
        List<UserTree> userTreeList = new ArrayList<>();

        for (int j = 0; j<bossList.size(); j++){
            UserTree userTree = new UserTree();
            userTree.user = bossList.get(j);
            userTreeList.add(userTree);

            recursionUserTree(userTree, userList);

        }
        return userTreeList;
    }

    public UserTree recursionUserTree(UserTree userTree, List<User> userList){

        for(int i = 0; i<userList.size(); i++){

            if(userList.get(i).getBossId() != null && userList.get(i).getBossId() == userTree.user.getId()){

                UserTree userTreeIn = new UserTree();
                userTreeIn.user = userList.get(i);
                userTree.userTrees.add(userTreeIn);
                recursionUserTree(userTreeIn, userList);
            }
        }

        return userTree;
    }


}



