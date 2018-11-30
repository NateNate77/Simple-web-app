package simplewebapp.controller;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Created by Admin on 17.11.2018.
 */
@Controller
public class UserController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CompanyDAO companyDAO;
    private static final int[] pageSizes = {5, 10};

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String getUserPage(Model model, Optional<Integer> pageSize, Optional<Integer> page, Optional<String> name, Optional<String> nameByCompany) {
       List<User> userList;
       if(name.isPresent() && nameByCompany.isPresent()){
           userList = userDAO.findUserByCompany(name.get(), nameByCompany.get());
           model.addAttribute("name", name.get());
           model.addAttribute("nameByCompany", nameByCompany.get());
       }
       else if(name.isPresent()){
          userList = userDAO.findUser(name.get());
          model.addAttribute("name", name.get());
       }
       else if(nameByCompany.isPresent()){
           userList = userDAO.findCompany(nameByCompany.get());
           model.addAttribute("nameByCompany", nameByCompany.get());
       }
       else {
           userList = userDAO.getUsers();
       }

        List<User> pageUserList = new ArrayList<>();

        int pageSizeInt = !pageSize.isPresent() || pageSize.get() == null ? 5 : pageSize.get();
        int pageInt = !page.isPresent() || page.get() == null ? 1 : page.get();
        for(int i = (pageInt-1)*pageSizeInt; i<userList.size() && i< pageInt*pageSizeInt; i++){
            pageUserList.add(userList.get(i));
        }
        double totalPagesDouble = Math.ceil((double) userList.size()/pageSizeInt);

        int totalPages = (int) totalPagesDouble;

        model.addAttribute("userList", pageUserList);
        model.addAttribute("selectedPageSize", pageSizeInt);
        model.addAttribute("currentPage", pageInt);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSizes", pageSizes);
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
    public List<User> getUsersByCompany(@RequestParam(value="companyId") String companyId,  @RequestParam(value="id") String id) throws IOException {
        List<User> usersByCompany = userDAO.getUsersByCompany(companyId, id);
        return usersByCompany;
    }


    @RequestMapping(value = "/update-user", method=RequestMethod.GET)
    public String updateCompany (Model model, @RequestParam(value="id") String id) throws Exception {

        User userUpdate = userDAO.getUserForUpdate(id);
        model.addAttribute("userUpdate", userUpdate);
        List<Company> companyList = companyDAO.getCompanies();
        model.addAttribute("companyList", companyList);

        return "updateUser.html";
    }

    @RequestMapping(value="/update-user", method=RequestMethod.POST)
    public String updateUser(@RequestParam(value="name") String name, @RequestParam(value="companyId") String companyId, @RequestParam(value="bossId") String bossId, @RequestParam(value="id") String id, Model model) {
        if(bossId.equals("")){
            bossId = "null";
        }
        try{
            userDAO.updateUser(name, bossId, companyId, id);
            return "redirect:/";
        }
        catch (Exception e){
            model.addAttribute("logError", e.getMessage());
            User userUpdate = userDAO.getUserForUpdate(id);
            model.addAttribute("userUpdate", userUpdate);
            List<Company> companyList = companyDAO.getCompanies();
            model.addAttribute("companyList", companyList);

        }
         return "updateUser";
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

}
