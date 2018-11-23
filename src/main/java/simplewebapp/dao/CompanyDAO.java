package simplewebapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import simplewebapp.domain.Company;
import simplewebapp.mapper.CompanyMapper;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

    public List<Company> getCompanies() {

        String sql = CompanyMapper.BASE_SQL;

        Object[] params = new Object[] {};
        CompanyMapper mapper = new CompanyMapper();
        List<Company> list = this.getJdbcTemplate().query(sql, params, mapper);

        return list;
    }


    public void addCompany(String name, String headCompanyId){
        String sql = String.format(CompanyMapper.insrtSQL, name , headCompanyId) ;
        this.getJdbcTemplate().update(sql);

    }

    public Company getCompanyForUpdate(String id){
        String sql = CompanyMapper.BASE_SQL + " WHERE Companies.\"ID\" =" + id + ";";
        Object[] params = new Object[] {};
        CompanyMapper mapper = new CompanyMapper();
        List<Company> company = this.getJdbcTemplate().query(sql, params, mapper);
        return company.get(0);

    }

    public void updateCompany(String name, String headCompanyId, String id){
        String sql = String.format(CompanyMapper.updateSQL, name, headCompanyId, id);
        this.getJdbcTemplate().update(sql);

    }

    public void deleteCompany(String id) throws Exception {
        String sql = CompanyMapper.BASE_SQL + " WHERE Companies.\"HeadCompanyID\" =" + id + ";";
        Object[] params = new Object[] {};
        CompanyMapper mapper = new CompanyMapper();
        List<Company> user = this.getJdbcTemplate().query(sql, params, mapper);
        if(user.size()>0){
            throw new Exception("Оргнизация не может быть удалена, тк у нее есть дочерние организации");
        }
        else {
            String deleteFromSql = String.format(CompanyMapper.deleteCompanySQL, id);
            this.getJdbcTemplate().update(deleteFromSql);
        }

    }



}
