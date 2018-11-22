package simplewebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import simplewebapp.dao.CompanyDAO;
import simplewebapp.domain.Company;
import simplewebapp.domain.User;
import simplewebapp.repository.CompanyRepository;
import simplewebapp.repository.UserRepository;

import java.util.List;

/**
 * Created by Admin on 17.11.2018.
 */
@Controller
public class CompanyController {

    @Autowired
    private CompanyDAO companyDAO;

    @RequestMapping(value="/company", method= RequestMethod.GET)
    public String getCompanyPage(Model model) {
        List<Company> companyList = companyDAO.getCompanies();
        model.addAttribute("companyList", companyList);
        return "company.html";
    }

    @RequestMapping(value = "/add-new-company", method=RequestMethod.GET)
    public String addNewCompanyPage(Model model) {
        List<Company> companyList = companyDAO.getCompanies();
        model.addAttribute("companyList", companyList);
        return "addNewCompany.html";
    }

    @RequestMapping(value="/add-new-company", method=RequestMethod.POST)
    public String addNewCompany(@RequestParam(value="name") String name, @RequestParam(value="headCompanyId") String headCompanyId) {

        if(headCompanyId.equals("")){
            headCompanyId = "null";
        }
          companyDAO.addCompany(name, headCompanyId);

        return "redirect:/";
    }
}
