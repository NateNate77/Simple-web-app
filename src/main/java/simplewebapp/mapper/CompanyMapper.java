package simplewebapp.mapper;

import org.springframework.jdbc.core.RowMapper;
import simplewebapp.domain.Company;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Admin on 21.11.2018.
 */
public class CompanyMapper implements RowMapper<Company> {
    public static final String BASE_SQL //
    ="SELECT Companies.\"ID\", Companies.\"Name\", Companies.\"HeadCompanyID\", HeadCompany.\"Name\" as \"HeadCompanyName\"\n" +
            "\tFROM public.\"Companies\" as Companies\n" +
            "\tleft join public.\"Companies\" as HeadCompany\n" +
            "\ton HeadCompany.\"ID\" = Companies.\"HeadCompanyID\";";
    @Override
    public Company mapRow(ResultSet rs, int rowNum) throws SQLException{
        int id = rs.getInt("ID");
        String name = rs.getString("Name");
        int headCompanyId = rs.getInt("HeadCompanyID");
        Integer headCompanyIdNull = rs.wasNull() ? null : headCompanyId;
        String headCompanyName = rs.getString("HeadCompanyName");
        return new Company(id, name, headCompanyIdNull, headCompanyName);

    }
}
