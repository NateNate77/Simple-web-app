package simplewebapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

        String sql = String.format(CompanyMapper.BASE_SQL, "");

        Object[] params = new Object[] {};
        CompanyMapper mapper = new CompanyMapper();
        List<Company> list = this.getJdbcTemplate().query(sql, params, mapper);

        return list;
    }


    public void addCompany(String name, String headCompanyId) throws Exception {
        String nameCompany = name.trim();
        if(nameCompany.isEmpty()){

            throw new Exception("Введите название организации");
        }
        String sql = String.format(CompanyMapper.insrtSQL, nameCompany , headCompanyId) ;
        this.getJdbcTemplate().update(sql);

    }

    public Company getCompanyForUpdate(String id){
        String sqlWhere = " WHERE Companies.\"ID\" =" + id;
        String sql = String.format(CompanyMapper.BASE_SQL, sqlWhere);
        Object[] params = new Object[] {};
        CompanyMapper mapper = new CompanyMapper();
        List<Company> company = this.getJdbcTemplate().query(sql, params, mapper);
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
        String sql = String.format(CompanyMapper.updateSQL, nameCompany, headCompanyId, id);
        this.getJdbcTemplate().update(sql);
    }

    public void deleteCompany(String id) throws Exception {
        String sqlWhere = " WHERE Companies.\"HeadCompanyID\" =" + id;

        String sql = String.format(CompanyMapper.BASE_SQL, sqlWhere);
        Object[] params = new Object[] {};
        CompanyMapper mapper = new CompanyMapper();
        List<Company> companies = this.getJdbcTemplate().query(sql, params, mapper);

        if(companies.size()>0){
            throw new Exception("Организация не может быть удалена, так как у нее есть дочерние организации");
        }

            String sqlWhereId = " WHERE Companies.\"ID\" =" + id;
            String sqlForDelete = String.format(CompanyMapper.BASE_SQL, sqlWhereId);
            Object[] params2 = new Object[] {};
            CompanyMapper mapper2 = new CompanyMapper();
            List<Company> companiesForDelete = this.getJdbcTemplate().query(sqlForDelete, params2, mapper2);
            // проверяем только первый элемент списка, так как id уникальный для каждой организации
            if(companiesForDelete.get(0).getUsersCount()>0){
                throw new Exception("Организация не может быть удалена, так как у нее есть сотрудники");
            }
            String deleteFromSql = String.format(CompanyMapper.deleteCompanySQL, id);
            this.getJdbcTemplate().update(deleteFromSql);


    }

}
