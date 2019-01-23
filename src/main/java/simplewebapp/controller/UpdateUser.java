package simplewebapp.controller;

import simplewebapp.domain.Company;
import simplewebapp.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Владелец on 16.01.2019.
 */
public class UpdateUser {
   public User user;
   public List<Company> companyList = new ArrayList<>();
   public List<User> usersByCompany = new ArrayList<>();

    public UpdateUser(User userUpdate, List<Company> companyList, List<User> usersByCompany) {
        this.user = userUpdate;
        this.companyList = companyList;
        this.usersByCompany = usersByCompany;

    }
}
