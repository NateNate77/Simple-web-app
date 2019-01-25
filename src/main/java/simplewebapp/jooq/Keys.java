/*
 * This file is generated by jOOQ.
 */
package simplewebapp.jooq;


import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;

import simplewebapp.jooq.tables.Companies;
import simplewebapp.jooq.tables.Staff;
import simplewebapp.jooq.tables.records.CompaniesRecord;
import simplewebapp.jooq.tables.records.StaffRecord;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<CompaniesRecord, Integer> IDENTITY_COMPANIES = Identities0.IDENTITY_COMPANIES;
    public static final Identity<StaffRecord, Integer> IDENTITY_STAFF = Identities0.IDENTITY_STAFF;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<CompaniesRecord> COMPANIES_PKEY = UniqueKeys0.COMPANIES_PKEY;
    public static final UniqueKey<StaffRecord> STAFF_PKEY = UniqueKeys0.STAFF_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<CompaniesRecord, CompaniesRecord> COMPANIES__HEADCOMPANYID = ForeignKeys0.COMPANIES__HEADCOMPANYID;
    public static final ForeignKey<StaffRecord, StaffRecord> STAFF__BOSSID = ForeignKeys0.STAFF__BOSSID;
    public static final ForeignKey<StaffRecord, CompaniesRecord> STAFF__COMPANYID = ForeignKeys0.STAFF__COMPANYID;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<CompaniesRecord, Integer> IDENTITY_COMPANIES = Internal.createIdentity(Companies.COMPANIES, Companies.COMPANIES.ID);
        public static Identity<StaffRecord, Integer> IDENTITY_STAFF = Internal.createIdentity(Staff.STAFF, Staff.STAFF.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<CompaniesRecord> COMPANIES_PKEY = Internal.createUniqueKey(Companies.COMPANIES, "Companies_pkey", Companies.COMPANIES.ID);
        public static final UniqueKey<StaffRecord> STAFF_PKEY = Internal.createUniqueKey(Staff.STAFF, "Staff_pkey", Staff.STAFF.ID);
    }

    private static class ForeignKeys0 {
        public static final ForeignKey<CompaniesRecord, CompaniesRecord> COMPANIES__HEADCOMPANYID = Internal.createForeignKey(simplewebapp.jooq.Keys.COMPANIES_PKEY, Companies.COMPANIES, "Companies__HeadCompanyID", Companies.COMPANIES.HEADCOMPANYID);
        public static final ForeignKey<StaffRecord, StaffRecord> STAFF__BOSSID = Internal.createForeignKey(simplewebapp.jooq.Keys.STAFF_PKEY, Staff.STAFF, "Staff__BossID", Staff.STAFF.BOSSID);
        public static final ForeignKey<StaffRecord, CompaniesRecord> STAFF__COMPANYID = Internal.createForeignKey(simplewebapp.jooq.Keys.COMPANIES_PKEY, Staff.STAFF, "Staff__CompanyID", Staff.STAFF.COMPANYID);
    }
}