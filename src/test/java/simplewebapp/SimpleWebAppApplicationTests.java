package simplewebapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import simplewebapp.dao.CompanyDAO;
import simplewebapp.dao.UserDAO;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SimpleWebAppApplicationTests {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private CompanyDAO companyDAO;

	@Test
	public void getUsersTest() {
		assertEquals(22, userDAO.getUsers().size());
	}

//	@Test
//	public void findUserTest(){
//    assertEquals(1, userDAO.findUser("якуб").size());
//	}

	@Test
	public void getUsersByCompanyTest(){
		assertEquals(5, userDAO.getUsersByCompany("38", "33").size());
	}

	@Test
	public void getUserForUpdate(){
		assertEquals("Долженко Демьян", userDAO.getUserForUpdate("36").getName());
	}

//	@Test
//	public void findCompanyTest(){
//		assertEquals(6, userDAO.findCompany("АВИТЕК").size());
//	}

//	@Test
//	public void findUserByCompanyTest(){
//		assertEquals(1, userDAO.findUserByCompany("сонин", "авитек").size());
//	}

	@Test
	public void getCompaniesTest(){
		assertEquals(7, companyDAO.getCompanies().size());
	}

	@Test
	public void getCompanyForUpdateTest(){
		assertEquals("ВесГрупп", companyDAO.getCompanyForUpdate("41").getName());
	}

//	@Test
//	public void findCompaniesTest(){
//		assertEquals(1, companyDAO.findCompanies("сит").size());
//	}

}
