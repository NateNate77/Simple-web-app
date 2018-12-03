package simplewebapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import simplewebapp.domain.User;
import simplewebapp.mapper.UserMapper;

import javax.sql.DataSource;
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

    public List<User> getUsers() {

        String sql = UserMapper.BASE_SQL;

        Object[] params = new Object[] {};
        UserMapper mapper = new UserMapper();
        List<User> list = this.getJdbcTemplate().query(sql, params, mapper);

        return list;
    }

    public List<User> findUser(String name) {
        String findByName = name.trim();

        String sql = UserMapper.BASE_SQL + " WHERE Users.\"Name\" ilike \'%" + findByName + "%\'";

        Object[] params = new Object[] {};
        UserMapper mapper = new UserMapper();
        try {
            List users = this.getJdbcTemplate().query(sql, params, mapper);
            return users;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void addUser(String name, String bossId, String companyId){
        String sql = String.format(UserMapper.insrtSQL, name , bossId, companyId) ;
        this.getJdbcTemplate().update(sql);
    }

    public List<User> getUsersByCompany(String companyId, String id){
        String sql = UserMapper.BASE_SQL + " WHERE Users.\"CompanyID\" =" + companyId + ";";
        Object[] params = new Object[] {};
        UserMapper mapper = new UserMapper();
       List<User> listUsersByCompany = this.getJdbcTemplate().query(sql, params, mapper);
       for (int i = 0; i<listUsersByCompany.size(); i++){
           if(String.valueOf(listUsersByCompany.get(i).getId()).equals(id)){
               listUsersByCompany.remove(i);
           }
       }
       return listUsersByCompany;

    }

    public User getUserForUpdate(String id){

            String sql = UserMapper.BASE_SQL + " WHERE Users.\"ID\" =" + id + ";";
            Object[] params = new Object[] {};
            UserMapper mapper = new UserMapper();
            List<User> user = this.getJdbcTemplate().query(sql, params, mapper);

            return user.get(0);


    }

    public void updateUser(String name, String bossId, String companyId, String id) throws Exception {

        String sqlWhereIdIsBoss = UserMapper.BASE_SQL + " WHERE Users.\"BossID\" =" + id + ";";
        Object[] params = new Object[] {};
        UserMapper mapper = new UserMapper();
        List<User> user = this.getJdbcTemplate().query(sqlWhereIdIsBoss, params, mapper);
        if(!String.valueOf(getUserForUpdate(id).getCompanyId()).equals(companyId) && user.size()>0){

            throw new Exception("Сотрудник не может быть перемещен в другую организацию, так как у него есть подчиненные");
        }

        if(bossId.equals(id)){
            throw new Exception("Нельзя устанавливать руководителем самого себя");
        }
        String sql = String.format(UserMapper.updateUserSQL, name, bossId, companyId, id);
        this.getJdbcTemplate().update(sql);
    }

    public void deleteUser(String id) throws Exception {
        String sql = UserMapper.BASE_SQL + " WHERE Users.\"BossID\" =" + id + ";";
        Object[] params = new Object[] {};
        UserMapper mapper = new UserMapper();
        List<User> user = this.getJdbcTemplate().query(sql, params, mapper);
        if(user.size()>0){
            throw new Exception("Пользователь не может быть удален, так как у него есть подчиненные");
        }
        else {
            String deleteFromSql = String.format(UserMapper.deleteUserSQL, id);
            this.getJdbcTemplate().update(deleteFromSql);
        }

    }

    public List<User> findCompany(String companyName) {
        String findByCompanyName = companyName.trim();

        String sql = UserMapper.BASE_SQL + " WHERE Companies.\"Name\" ilike \'%" + findByCompanyName + "%\'";

        Object[] params = new Object[] {};
        UserMapper mapper = new UserMapper();
        try {
            List companies = this.getJdbcTemplate().query(sql, params, mapper);
            return companies;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<User> findUserByCompany(String name, String nameByCompany){
        String findName = name.trim();
        String findNameByCompany = nameByCompany.trim();

        String sql = UserMapper.BASE_SQL + " WHERE Users.\"Name\" ilike \'%" + findName + "%\'" + " AND Companies.\"Name\" ilike \'%" + findNameByCompany + "%\'";

        Object[] params = new Object[] {};
        UserMapper mapper = new UserMapper();

        List findUserByCompany = this.getJdbcTemplate().query(sql, params, mapper);
        return findUserByCompany;
    }

}
