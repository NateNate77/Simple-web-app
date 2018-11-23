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

//    public User findUser(int id) {
//
//        String sql = UserMapper.BASE_SQL + " where Staff.ID = ? ";
//
//        Object[] params = new Object[] { id };
//        UserMapper mapper = new UserMapper();
//        try {
//            User user = this.getJdbcTemplate().queryForObject(sql, params, mapper);
//            return user;
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
//    }
//
    public void addUser(String name, String bossId, String companyId){
        String sql = String.format(UserMapper.insrtSQL, name , bossId, companyId) ;
        this.getJdbcTemplate().update(sql);
    }

    public List<User> getUsersByCompany(String companyId){
        String sql = UserMapper.BASE_SQL + " WHERE Users.\"CompanyID\" =" + companyId + ";";
        Object[] params = new Object[] {};
        UserMapper mapper = new UserMapper();
       List<User> listUsersByCompany = this.getJdbcTemplate().query(sql, params, mapper);
       return listUsersByCompany;

    }

    public User getUserForUpdate(String id){
        String sql = UserMapper.BASE_SQL + " WHERE Users.\"ID\" =" + id + ";";
        Object[] params = new Object[] {};
        UserMapper mapper = new UserMapper();
        List<User> user = this.getJdbcTemplate().query(sql, params, mapper);
        return user.get(0);
    }

    public void updateUser(String name, String bossId, String companyId, String id){
        String sql = String.format(UserMapper.updateUserSQL, name, bossId, companyId, id);
        this.getJdbcTemplate().update(sql);
    }

    public void deleteUser(String id) throws Exception {
        String sql = UserMapper.BASE_SQL + " WHERE Users.\"BossID\" =" + id + ";";
        Object[] params = new Object[] {};
        UserMapper mapper = new UserMapper();
        List<User> user = this.getJdbcTemplate().query(sql, params, mapper);
        if(user.size()>0){
            throw new Exception("Пользователь не может быть удален, тк у него есть подчиненные");
        }
        else {
            String deleteFromSql = String.format(UserMapper.deleteUserSQL, id);
            this.getJdbcTemplate().update(deleteFromSql);
        }

    }



}
