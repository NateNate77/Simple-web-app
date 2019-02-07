package simplewebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Владелец on 15.01.2019.
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public String welcome() {
        return "user";
    }


    @RequestMapping(value="/update-user")
    public String updateUser() {
        return "updateUser";
    }


    @RequestMapping(value="/add-new-user")
    public String addNewUser() {
        return "addNewUser";
    }

    @RequestMapping("/company")
    public String company() {
        return "company";
    }

    @RequestMapping(value="/update-company")
    public String updateCompany() {
        return "updateCompany";
    }

    @RequestMapping(value="/add-new-company")
    public String addNewCompany() {
        return "addNewCompany";
    }

    @RequestMapping(value="/companies-tree-view")
    public String companiesTreeView() {
        return "companiesTree";
    }

    @RequestMapping(value="/staff-tree-view")
    public String staffTreeView() {
        return "staffTree";
    }

}
