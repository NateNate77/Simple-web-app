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
    ="\tSELECT Companies.\"ID\", Companies.\"Name\", Companies.\"HeadCompanyID\", HeadCompany.\"Name\" as \"HeadCompanyName\", count (Users.\"ID\") as \"UsersCount\"\n" +
            "            FROM public.\"Companies\" as Companies\n" +
            "\t\t\tleft join public.\"Staff\" as Users\n" +
            "\t\t\ton Users.\"CompanyID\"=Companies.\"ID\"\n" +
            "            left join public.\"Companies\" as HeadCompany\n" +
            "            on HeadCompany.\"ID\" = Companies.\"HeadCompanyID\"\n %s" +
            "\t\t\tGroup by Companies.\"ID\", Companies.\"Name\", Companies.\"HeadCompanyID\", HeadCompany.\"Name\"\n" +
            "\t\t\t order by Companies.\"ID\"";

    public static final String insrtSQL =  "INSERT INTO public.\"Companies\" (\"Name\", \"HeadCompanyID\") VALUES (\'%s\', %s)";

    public static final String updateSQL = "UPDATE public.\"Companies\"\n" +
            "\tSET \"Name\"=\'%s\', \"HeadCompanyID\"=%s\n" +
            "\tWHERE \"ID\" =%s";

    public static final String deleteCompanySQL = "DELETE FROM public.\"Companies\"\n" +
            "\tWHERE \"ID\"=%s";

    @Override
    public Company mapRow(ResultSet rs, int rowNum) throws SQLException{
        int id = rs.getInt("ID");
        String name = rs.getString("Name");
        int headCompanyId = rs.getInt("HeadCompanyID");
        Integer headCompanyIdNull = rs.wasNull() ? null : headCompanyId;
        String headCompanyName = rs.getString("HeadCompanyName");
        int usersCount = rs.getInt("UsersCount");
        return new Company(id, name, headCompanyIdNull, headCompanyName, usersCount);

    }
}
