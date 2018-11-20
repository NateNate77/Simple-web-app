package simplewebapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import simplewebapp.domain.Company;
import simplewebapp.mapper.CompanyMapper;

import javax.sql.DataSource;
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

}
