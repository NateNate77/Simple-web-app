package simplewebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplewebapp.dao.*;
import simplewebapp.domain.Company;
import simplewebapp.domain.CompanyTree;

import java.util.List;

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
    public Status addNewCompany(@RequestBody AddNewCompany addNewCompany) throws Exception {

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

    public UpdateCompany updateCompany (@RequestParam(value="id") Integer id) {

        Company companyUpdate = companyDAO.getCompanyForUpdate(id);
        List<Company> companyList = companyDAO.getCompanies();
        UpdateCompany updateCompany = new UpdateCompany(companyUpdate, companyList);

        return updateCompany;
    }

    @RequestMapping(value="/update-company", method=RequestMethod.POST)
    public Status updateCompany(@RequestBody CompanyUpdate companyUpdate) {

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
    public Status deleteCompany(@RequestParam(value="id") Integer id) throws Exception {

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

        List<CompanyTree> companyTreeList = companyDAO.companyTreeView();

        return companyTreeList;
    }

}
