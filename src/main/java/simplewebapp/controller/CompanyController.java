package simplewebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    @RequestMapping(value="/company", method= RequestMethod.GET)
    public String getCompanyPage(Model model) {
        List<Company> companyList = CompanyRepository.companies;
        model.addAttribute("companyList", companyList);
        return "pages/company";
    }

    @RequestMapping(value = "/add-new-company", method=RequestMethod.GET)
    public String addNewUserPage() {
        return "pages/addNewCompany";
    }

    @RequestMapping(value="/add-new-company", method=RequestMethod.POST)
    public String addNewUser(@RequestParam(value="name") String name, @RequestParam(value="headCompanyId") int headCompanyId) {
        Company company = new Company();
        company.setName(name);
        company.setHeadCompanyId(headCompanyId);
        company.setId(CompanyRepository.counter);
        CompanyRepository.companies.add(company);
        CompanyRepository.counter++;
        return "redirect:/";
    }
}
