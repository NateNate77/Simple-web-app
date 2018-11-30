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
import java.util.Optional;

/**
 * Created by Admin on 17.11.2018.
 */
@Controller
public class CompanyController {

    @Autowired
    private CompanyDAO companyDAO;
    private static final int[] pageSizes = {5, 10};

    @RequestMapping(value="/company", method= RequestMethod.GET)
    public String getCompanyPage(Model model, Optional<Integer> pageSize, Optional<Integer> page, Optional<String> nameCompany) {

        List<Company> companyList;

        if(nameCompany.isPresent()){
            companyList = companyDAO.findCompanies(nameCompany.get());
            model.addAttribute("nameCompany",nameCompany.get());
        }
        else {
            companyList = companyDAO.getCompanies();
        }

        List<Company> pageCompanyList = new ArrayList<>();

        int pageSizeInt = !pageSize.isPresent() || pageSize.get() == null ? 5 : pageSize.get();
        int pageInt = !page.isPresent() || page.get() == null ? 1 : page.get();
        for(int i = (pageInt-1)*pageSizeInt; i<companyList.size() && i< pageInt*pageSizeInt; i++){
            pageCompanyList.add(companyList.get(i));
        }
        double totalPagesDouble = Math.ceil((double) companyList.size()/pageSizeInt);

        int totalPages = (int) totalPagesDouble;

        model.addAttribute("companyList", pageCompanyList);
        model.addAttribute("selectedPageSize", pageSizeInt);
        model.addAttribute("currentPage", pageInt);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSizes", pageSizes);
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
        for (int i = 0; i<companyList.size(); i++){
            if(String.valueOf(companyList.get(i).getId()).equals(id)){
                companyList.remove(i);
            }
        }
        model.addAttribute("companyList", companyList);

        return "updateCompany.html";
    }




    @RequestMapping(value="/update-company", method=RequestMethod.POST)
    public String updateCompany(@RequestParam(value="name") String name, @RequestParam(value="headCompanyId") String headCompanyId, @RequestParam(value="id") String id, Model model) {

        if(headCompanyId.equals("")){
            headCompanyId = "null";
        }
        try {
            companyDAO.updateCompany(name, headCompanyId, id);
            return "redirect:/company";
        }
        catch (Exception e){
            model.addAttribute("logError", e.getMessage());
            Company companyUpdate = companyDAO.getCompanyForUpdate(id);
            model.addAttribute("companyUpdate", companyUpdate);
            List<Company> companyList = companyDAO.getCompanies();
            for (int i = 0; i<companyList.size(); i++){
                if(String.valueOf(companyList.get(i).getId()).equals(id)){
                    companyList.remove(i);
                }
            }
            model.addAttribute("companyList", companyList);
        }
        return "updateCompany";
    }

    @RequestMapping(value="/delete-company", method=RequestMethod.POST)
    @ResponseBody
    public String deleteCompany(@RequestParam(value="id") String id) throws Exception {

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

}
