package simplewebapp.mapper;

import simplewebapp.domain.User;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Admin on 18.11.2018.
 */
public class UserMapper implements RowMapper<User> {

    public static final String BASE_SQL //
            = "SELECT Users.\"ID\", Users.\"Name\", Users.\"BossID\", Users.\"CompanyID\", Companies.\"Name\" as \"CompanyName\", Boss.\"Name\" as \"BossName\"\n" +
            "\tFROM public.\"Staff\" as Users\n" +
            "\tInner join public.\"Companies\" as Companies\n" +
            "\ton Users.\"CompanyID\" = Companies.\"ID\"\n" +
            "\tleft join public.\"Staff\" as Boss\n" +
            "\ton Boss.\"ID\" = Users.\"BossID\"";

    public static final String insrtSQL = "INSERT INTO public.\"Staff\" (\"Name\", \"BossID\", \"CompanyID\") VALUES (\'%s\', %s, %s)";

    public static final String updateUserSQL = "UPDATE public.\"Staff\"\n" +
            "\tSET \"Name\"=\'%s\', \"BossID\"=%s, \"CompanyID\"=%s\n" +
            "\tWHERE \"ID\"= %s";

    public static final String deleteUserSQL = "DELETE FROM public.\"Staff\"\n" +
            "\tWHERE \"ID\" =%s";

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

        int id = rs.getInt("ID");
        String name = rs.getString("Name");
        int bossId = rs.getInt("BossID");
        Integer bossIdNull = rs.wasNull() ? null : bossId;
        int companyId = rs.getInt("CompanyID");
        String bossName = rs.getString("BossName");
        String companyName = rs.getString("CompanyName");

        return new User (id, name, companyId,  bossIdNull,  bossName, companyName);
    }


}
