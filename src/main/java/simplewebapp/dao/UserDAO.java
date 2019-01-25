package simplewebapp.dao;

import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import simplewebapp.domain.User;
import simplewebapp.domain.UserTree;
import simplewebapp.jooq.tables.Companies;
import simplewebapp.jooq.tables.Staff;


import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 19.11.2018.
 */
@Repository
@Transactional
public class UserDAO extends JdbcDaoSupport {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public UserDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    @Autowired
    private DSLContext dsl;

    private Staff staffMain = Staff.STAFF.as("S1");
    private Staff staffBoss = Staff.STAFF.as("S2");

    private SelectOnConditionStep<Record6<Integer, String, Integer, Integer, String, String>> resultQuery() {
        return dsl
                .select(staffMain.ID, staffMain.NAME, staffMain.BOSSID, staffMain.COMPANYID, Companies.COMPANIES.NAME, staffBoss.NAME)
                .from(staffMain)
                .innerJoin(Companies.COMPANIES)
                .on(staffMain.COMPANYID.equal(Companies.COMPANIES.ID))
                .leftJoin(staffBoss)
                .on(staffBoss.ID.equal(staffMain.BOSSID));
    }

    public List<User> getUserList(Result<Record6<Integer, String, Integer, Integer, String, String>> result){

        List<User> list = new ArrayList<>();
        for (Record6 r : result) {

            Integer id = r.getValue(staffMain.ID, Integer.class);
            String name = r.getValue(staffMain.NAME, String.class);
            Integer bossId = r.getValue(staffMain.BOSSID, Integer.class);
            Integer companyId = r.getValue(staffMain.COMPANYID, Integer.class);
            String companyName = r.getValue(Companies.COMPANIES.NAME, String.class);
            String bossName = r.getValue(staffBoss.NAME, String.class);

            User user = new User (id, name, companyId,  bossId, bossName, companyName);

            list.add(user);
        }

        return list;
    }

    public List<User> getUsers() {

        Result<Record6<Integer, String, Integer, Integer, String, String>> result = resultQuery().fetch();

        List<User> list = getUserList(result);

        return list;
    }


    public void addUser(String name, String bossId, String companyId) throws Exception {
        String nameUser = name.trim();
        if(nameUser.isEmpty()){

            throw new Exception("Введите имя");
        }

        Integer bossIdInt = bossId.equals("") ? null : Integer.parseInt(bossId);
        Integer companyIdInt = Integer.parseInt(companyId);

        dsl.insertInto(Staff.STAFF)
                .set(Staff.STAFF.NAME, nameUser)
                .set(Staff.STAFF.BOSSID, bossIdInt)
                .set(Staff.STAFF.COMPANYID, companyIdInt)
                .execute();
    }

    public List<User> getUsersByCompany(String companyId, String id){

        Integer companyIdInt = Integer.parseInt(companyId);
        Result<Record6<Integer, String, Integer, Integer, String, String>> result = resultQuery().where(staffMain.COMPANYID.eq(companyIdInt)).fetch();

       List<User> listUsersByCompany = getUserList(result);
       for (int i = 0; i<listUsersByCompany.size(); i++){
           if(String.valueOf(listUsersByCompany.get(i).getId()).equals(id)){
               listUsersByCompany.remove(i);
           }
       }
       return listUsersByCompany;

    }

    public List<User> getUsersByCompanyForAddNewUser(String companyId){

        Integer companyIdInt = Integer.parseInt(companyId);
        Result<Record6<Integer, String, Integer, Integer, String, String>> result = resultQuery().where(staffMain.COMPANYID.eq(companyIdInt)).fetch();

        List<User> listUsersByCompany = getUserList(result);

        return listUsersByCompany;

    }

    public User getUserForUpdate(String id){

        Integer idInt = Integer.parseInt(id);
        Result<Record6<Integer, String, Integer, Integer, String, String>> result = resultQuery().where(staffMain.ID.eq(idInt)).fetch();
            List<User> user = getUserList(result);

            return user.get(0);


    }

    public void updateUser(String name, String bossId, String companyId, String id) throws Exception {

        Integer idInt = Integer.parseInt(id);
        Result<Record6<Integer, String, Integer, Integer, String, String>> result = resultQuery().where(staffMain.BOSSID.eq(idInt)).fetch();

        List<User> user = getUserList(result);
        String nameUser = name.trim();
        if(nameUser.isEmpty()){

            throw new Exception("Введите имя");
        }

        if(!String.valueOf(getUserForUpdate(id).getCompanyId()).equals(companyId) && user.size()>0){

            throw new Exception("Сотрудник не может быть перемещен в другую организацию, так как у него есть подчиненные");
        }

        if(bossId.equals(id)){
            throw new Exception("Нельзя устанавливать руководителем самого себя");
        }

        Integer bossIdInt = bossId.equals("") ? null : Integer.parseInt(bossId);
        Integer companyIdInt = Integer.parseInt(companyId);


        dsl.update(Staff.STAFF)
                .set(Staff.STAFF.NAME, nameUser)
                .set(Staff.STAFF.COMPANYID, companyIdInt)
                .set(Staff.STAFF.BOSSID, bossIdInt)
                .where(Staff.STAFF.ID.equal(idInt))
                .execute();
    }

    public void deleteUser(String id) throws Exception {

        Integer idInt = Integer.parseInt(id);
        Result<Record6<Integer, String, Integer, Integer, String, String>> result = resultQuery().where(staffMain.BOSSID.eq(idInt)).fetch();

        List<User> user = getUserList(result);
        if(user.size()>0){
            throw new Exception("Сотрудник не может быть удален, так как у него есть подчиненные");
        }
        else {

            dsl.delete(Staff.STAFF)
                    .where(Staff.STAFF.ID.eq(idInt))
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



