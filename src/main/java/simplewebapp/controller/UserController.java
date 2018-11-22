package simplewebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import simplewebapp.dao.CompanyDAO;
import simplewebapp.dao.UserDAO;
import simplewebapp.domain.Company;
import simplewebapp.domain.User;
import simplewebapp.repository.UserRepository;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;


/**
 * Created by Admin on 17.11.2018.
 */
@Controller
public class UserController {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CompanyDAO companyDAO;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String getUserPage(Model model) {
        List<User> userList = userDAO.getUsers();
        model.addAttribute("userList", userList);
        return "user.html";
    }

    @RequestMapping(value = "/add-new-user", method=RequestMethod.GET)
    public String addNewUserPage(Model model) {
        List<Company> companyList = companyDAO.getCompanies();
        model.addAttribute("companyList", companyList);
        return "addNewUser.html";
    }



    @RequestMapping(value="/add-new-user", method=RequestMethod.POST)
    public String addNewUser(@RequestParam(value="name") String name, @RequestParam(value="companyId") String companyId, @RequestParam(value="bossId") String bossId) {
        if(bossId.equals("")){
            bossId = "null";
        }
        userDAO.addUser(name, bossId, companyId);
        return "redirect:/";
    }


    @RequestMapping(value="/get-users-by-company", method=RequestMethod.POST)
    @ResponseBody
    public List<User> getUsersByCompany(@RequestParam(value="companyId") String companyId) throws IOException {
        List<User> usersByCompany = userDAO.getUsersByCompany(companyId);
        return usersByCompany;
    }

}
