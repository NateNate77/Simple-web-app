package simplewebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import simplewebapp.dao.CompanyDAO;
import simplewebapp.domain.Company;
import simplewebapp.domain.CompanyTree;
import simplewebapp.domain.User;
import simplewebapp.repository.CompanyRepository;
import simplewebapp.repository.UserRepository;

import java.util.ArrayList;
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

         return "redirect:/company";

    }

    @RequestMapping(value = "/update-company", method=RequestMethod.GET)
    public String updateCompany (Model model, @RequestParam(value="id") String id) {

        Company companyUpdate = companyDAO.getCompanyForUpdate(id);
        model.addAttribute("companyUpdate", companyUpdate);
        List<Company> companyList = companyDAO.getCompanies();
        model.addAttribute("companyList", companyList);

        return "updateCompany.html";
    }




    @RequestMapping(value="/update-company", method=RequestMethod.POST)
    public String updateCompany(@RequestParam(value="name") String name, @RequestParam(value="headCompanyId") String headCompanyId, @RequestParam(value="id") String id) {

        if(headCompanyId.equals("")){
            headCompanyId = "null";
        }
        companyDAO.updateCompany(name, headCompanyId, id);

        return "redirect:/company";

    }

    @RequestMapping(value="/delete-company", method=RequestMethod.POST)
    @ResponseBody
    public String deleteUser(@RequestParam(value="id") String id) throws Exception {

        companyDAO.deleteCompany(id);
        return "Success";
    }


    @RequestMapping(value = "/companies-tree-view", method=RequestMethod.GET)
    public String companyTreeView (Model model) {
        List<Company> headCompanyList = new ArrayList<>();
        List <Company> companyList = companyDAO.getCompanies();
        for (int i = 0; i < companyList.size(); i++){
            if(companyList.get(i).getHeadCompanyId() == null){
                headCompanyList.add(companyList.get(i));
            }
        }

        List<CompanyTree> companyTreeList = companyTree(companyList, headCompanyList);
        model.addAttribute("companyTreeList", companyTreeList);

        return "companiesTree.html";
    }


    public List<CompanyTree> companyTree(List<Company> companyList, List<Company> headCompanyList){
        List<CompanyTree> companyTreeList = new ArrayList<>();

        for (int j = 0; j<headCompanyList.size(); j++){
            CompanyTree companyTree = new CompanyTree();
            companyTree.company = headCompanyList.get(j);
            companyTreeList.add(companyTree);

            recursionCompanyTree(companyTree, companyList);

        }
        return companyTreeList;
    }

    public CompanyTree recursionCompanyTree(CompanyTree companyTree, List<Company> companyList){

        for(int i = 0; i<companyList.size(); i++){

            if(companyList.get(i).getHeadCompanyId() != null && companyList.get(i).getHeadCompanyId() == companyTree.company.getId()){

                CompanyTree companyTreeIn = new CompanyTree();
                companyTreeIn.company = companyList.get(i);
                companyTree.companyTrees.add(companyTreeIn);
                recursionCompanyTree(companyTreeIn, companyList);
            }
        }

        return companyTree;
    }

    @RequestMapping(value="/find-companies", method=RequestMethod.POST)
    public String findCompanies(Model model, @RequestParam(value="name") String name) {
        List<Company> findCompaniesList = companyDAO.findCompanies(name);
        model.addAttribute("companyList", findCompaniesList);
        return "company.html";
    }

}
