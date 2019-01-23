package simplewebapp.controller;

import simplewebapp.domain.Company;

import java.util.ArrayList;
import java.util.List;

public class UpdateCompany {
    public Company company;
    public List<Company> companyList;


    public UpdateCompany(Company company, List<Company> companyList) {
        this.company = company;
        this.companyList = companyList;

    }
}
