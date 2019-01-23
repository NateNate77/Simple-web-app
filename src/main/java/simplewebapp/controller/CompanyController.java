package simplewebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import simplewebapp.dao.CompanyDAO;
import simplewebapp.domain.Company;
import simplewebapp.domain.CompanyTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Admin on 17.11.2018.
 */
@RestController
public class CompanyController {

    @Autowired
    private CompanyDAO companyDAO;


    @RequestMapping(value="/getCompanies", method= RequestMethod.GET)
    public List<Company> getCompanyPage() {

        List<Company> companyList;
        companyList = companyDAO.getCompanies();

        return companyList;
    }

    @RequestMapping(value = "/list-for-add-new-company", method=RequestMethod.GET)
    public List<Company> addNewCompanyPage() {
        List<Company> companyList = companyDAO.getCompanies();

        return companyList;
    }

    @RequestMapping(value="/add-new-company", method=RequestMethod.POST)
    @ResponseBody
    public Status addNewCompany(@RequestBody AddNewCompany addNewCompany) throws Exception {

        if(addNewCompany.getHeadCompanyId().equals("")){

            addNewCompany.setHeadCompanyId("null");
        }

        try {
            companyDAO.addCompany(addNewCompany.getName(), addNewCompany.getHeadCompanyId());
            Status status = new Status(true);
            return status;
        }

        catch (Exception e){

            Status status = new Status(false, e.getMessage());
            return status;
        }

    }

    @RequestMapping(value = "/get-company-for-update",
            params = { "id" },
            method = RequestMethod.GET)

    public UpdateCompany updateCompany (@RequestParam(value="id") String id) {

        Company companyUpdate = companyDAO.getCompanyForUpdate(id);
        List<Company> companyList = companyDAO.getCompanies();
        UpdateCompany updateCompany = new UpdateCompany(companyUpdate, companyList);

        return updateCompany;
    }




    @RequestMapping(value="/update-company", method=RequestMethod.POST)
    @ResponseBody
    public Status updateCompany(@RequestBody CompanyUpdate companyUpdate) {

        if(companyUpdate.getHeadCompanyId().equals("")){
            companyUpdate.setHeadCompanyId("null");
        }
        try {
            companyDAO.updateCompany(companyUpdate.getName(), companyUpdate.getHeadCompanyId(), companyUpdate.getId());
            Status status = new Status(true);
            return status;
        }
        catch (Exception e){


            Status status = new Status(false, e.getMessage());
            return status;
        }

    }

    @RequestMapping(value="/delete-company", method=RequestMethod.POST)
    @ResponseBody
    public Status deleteCompany(@RequestParam(value="id") String id) throws Exception {

        try {
            companyDAO.deleteCompany(id);
            Status status = new Status(true);
            return status;
        }
        catch (Exception e){
            Status status = new Status(false, e.getMessage());
            return status;
        }


    }


    @RequestMapping(value = "/get-companies-tree-view", method=RequestMethod.GET)
    public List<CompanyTree> companyTreeView () {
        List<Company> headCompanyList = new ArrayList<>();
        List <Company> companyList = companyDAO.getCompanies();
        for (int i = 0; i < companyList.size(); i++){
            if(companyList.get(i).getHeadCompanyId() == null){
                headCompanyList.add(companyList.get(i));
            }
        }

        List<CompanyTree> companyTreeList = companyTree(companyList, headCompanyList);

        return companyTreeList;
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
