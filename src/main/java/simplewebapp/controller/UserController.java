package simplewebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplewebapp.dao.*;
import simplewebapp.domain.Company;
import simplewebapp.domain.User;
import simplewebapp.domain.UserTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 17.11.2018.
 */

@RestController
public class UserController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CompanyDAO companyDAO;

    @RequestMapping(
            value = "/getUsers",
            method = RequestMethod.GET
    )
    public List<User> getUsers() {
        List<User> userList;

            userList = userDAO.getUsers();

        return userList;
    }

    @RequestMapping(value = "/companies-for-add-new-user", method=RequestMethod.GET)
    public List<Company> addNewUserPage() {
        List<Company> companyList = companyDAO.getCompanies();
        return  companyList;
    }

    @RequestMapping(value="/add-new-user", method=RequestMethod.POST)
    @ResponseBody
    public Status addNewUser(@RequestBody AddNewUser addNewUser) throws Exception {

        try{
            userDAO.addUser(addNewUser.getName(), addNewUser.getBossId(), addNewUser.getCompanyId());

            Status status = new Status(true);
            return status;
        }
        catch (Exception e){

            Status status = new Status(false, e.getMessage());
            return status;
        }

    }

    @RequestMapping(value="/get-users-by-company", method=RequestMethod.POST)
    @ResponseBody
    public List<User> getUsersByCompany(@RequestParam(value="companyId") String companyId,  @RequestParam(value="id") String id) throws IOException {
        List<User> usersByCompany = userDAO.getUsersByCompany(companyId, id);
        return usersByCompany;
    }

    @RequestMapping(value="/get-users-by-company-for-add-new-user", method=RequestMethod.POST)
    @ResponseBody
    public List<User> getUsersByCompanyForAddNewUser(@RequestParam(value="companyId") String companyId) throws IOException {
        List<User> usersByCompany = userDAO.getUsersByCompanyForAddNewUser(companyId);
        return usersByCompany;
    }

    @RequestMapping(
            value = "/getUser-for-update",
            params = { "id" },
            method = RequestMethod.GET
    )
    public UpdateUser getUserForUpdate (@RequestParam(value="id") String id) throws Exception {

        User userUpdate = userDAO.getUserForUpdate(id);
        List<Company> companyList = companyDAO.getCompanies();
        int companyId = userUpdate.getCompanyId();
        String companyIdString = String.valueOf(companyId);
        List<User> usersByCompany = userDAO.getUsersByCompany(companyIdString, id);
        UpdateUser updateUser = new UpdateUser(userUpdate, companyList, usersByCompany);

        return updateUser;
    }

    @RequestMapping(value="/update-user", method=RequestMethod.POST)
    @ResponseBody
    public Status updateUser(@RequestBody UserUpdate userUpdate) {

        try{
            userDAO.updateUser(userUpdate.getName(), userUpdate.getBossId(), userUpdate.getCompanyId(), userUpdate.getId());
            Status status = new Status(true);
            return status;

        }
        catch (Exception e){

            Status status = new Status(false, e.getMessage());
            return status;

        }
    }

    @RequestMapping(value="/delete-user", method=RequestMethod.POST)
    @ResponseBody
    public Status deleteUser(@RequestParam(value="id") String id) throws Exception {

        try{
            userDAO.deleteUser(id);
            Status status = new Status(true);
            return status;
        }
        catch (Exception e){
            Status status = new Status(false, e.getMessage());
            return status;
        }

    }

    @RequestMapping(value = "/get-staff-tree-view", method=RequestMethod.GET)
    public List<UserTree> staffTreeView () {

        List<UserTree> userTreeList = userDAO.staffTreeView();

        return userTreeList;
    }

}
