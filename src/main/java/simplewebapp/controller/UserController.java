package simplewebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import simplewebapp.dao.CompanyDAO;
import simplewebapp.dao.UserDAO;
import simplewebapp.domain.Company;
import simplewebapp.domain.User;
import simplewebapp.domain.UserTree;
import simplewebapp.repository.UserRepository;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 17.11.2018.
 */
@Controller
public class UserController {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CompanyDAO companyDAO;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String getUserPage(Model model) {
        List<User> userList = userDAO.getUsers();
        model.addAttribute("userList", userList);
        return "user.html";
    }

    @RequestMapping(value = "/add-new-user", method=RequestMethod.GET)
    public String addNewUserPage(Model model) {
        List<Company> companyList = companyDAO.getCompanies();
        model.addAttribute("companyList", companyList);
        return "addNewUser.html";
    }



    @RequestMapping(value="/add-new-user", method=RequestMethod.POST)
    public String addNewUser(@RequestParam(value="name") String name, @RequestParam(value="companyId") String companyId, @RequestParam(value="bossId") String bossId) {
        if(bossId.equals("")){
            bossId = "null";
        }
        userDAO.addUser(name, bossId, companyId);
        return "redirect:/";
    }


    @RequestMapping(value="/get-users-by-company", method=RequestMethod.POST)
    @ResponseBody
    public List<User> getUsersByCompany(@RequestParam(value="companyId") String companyId) throws IOException {
        List<User> usersByCompany = userDAO.getUsersByCompany(companyId);
        return usersByCompany;
    }


    @RequestMapping(value = "/update-user", method=RequestMethod.GET)
 //   @ResponseBody
    public String updateCompany (Model model, @RequestParam(value="id") String id) {

        User userUpdate = userDAO.getUserForUpdate(id);
        model.addAttribute("userUpdate", userUpdate);
        List<Company> companyList = companyDAO.getCompanies();
        model.addAttribute("companyList", companyList);
//        List<User> usersByCompany = userDAO.getUsersByCompany(companyId);

        return "updateUser.html";
    }

    @RequestMapping(value="/update-user", method=RequestMethod.POST)
    public String updateUser(@RequestParam(value="name") String name, @RequestParam(value="companyId") String companyId, @RequestParam(value="bossId") String bossId, @RequestParam(value="id") String id) {
        if(bossId.equals("")){
            bossId = "null";
        }
        userDAO.updateUser(name, bossId, companyId, id);
        return "redirect:/";
    }

    @RequestMapping(value="/delete-user", method=RequestMethod.POST)
    @ResponseBody
    public String deleteUser(@RequestParam(value="id") String id) throws Exception {

        userDAO.deleteUser(id);
        return "Success";
    }


    @RequestMapping(value = "/staff-tree-view", method=RequestMethod.GET)
    public String staffTreeView (Model model) {
        List<User> bossList = new ArrayList<>();
        List <User> userList = userDAO.getUsers();
        for (int i = 0; i < userList.size(); i++){
            if(userList.get(i).getBossId()==null){
                bossList.add(userList.get(i));
            }
        }



        List<UserTree> userTreeList = userTree(userList, bossList);
        model.addAttribute("userTreeList", userTreeList);

        return "staffTree.html";
    }


    public List<UserTree> userTree(List<User> userList, List<User> bossList){
        List<UserTree> userTreeList = new ArrayList<>();



        for (int j = 0; j<bossList.size(); j++){
            UserTree userTree = new UserTree();
            userTree.user = bossList.get(j);
            userTreeList.add(userTree);

            recursionUserTree(userTree, userList);

        }
        return userTreeList;
    }

    public UserTree recursionUserTree(UserTree userTree, List<User> userList){

        for(int i = 0; i<userList.size(); i++){

                if(userList.get(i).getBossId() != null && userList.get(i).getBossId() == userTree.user.getId()){

                    UserTree userTreeIn = new UserTree();
                    userTreeIn.user = userList.get(i);
                    userTree.userTrees.add(userTreeIn);
                    recursionUserTree(userTreeIn, userList);
                }
        }

        return userTree;
    }


    @RequestMapping(value="/find-user", method=RequestMethod.POST)
    public String findUser(Model model, @RequestParam(value="name") String name) {
        List<User> findUserList = userDAO.findUser(name);
        model.addAttribute("userList", findUserList);
        return "user.html";
    }

    @RequestMapping(value="/find-company", method=RequestMethod.POST)
    public String findCompany(Model model, @RequestParam(value="companyName") String companyName) {
        List<User> findCompanyList = userDAO.findCompany(companyName);
        model.addAttribute("userList", findCompanyList);
        return "user.html";
    }

}
