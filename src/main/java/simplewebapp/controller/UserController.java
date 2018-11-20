package simplewebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import simplewebapp.dao.UserDAO;
import simplewebapp.domain.User;
import simplewebapp.repository.UserRepository;

import java.util.List;


/**
 * Created by Admin on 17.11.2018.
 */
@Controller
public class UserController {
    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String getUserPage(Model model) {
        //List<User> userList = UserRepository.users;
        List<User> userList = userDAO.getUsers();
        model.addAttribute("userList", userList);
        return "user.html";
    }

    @RequestMapping(value = "/add-new-user", method=RequestMethod.GET)
    public String addNewUserPage() {
        return "pages/addNewUser";
    }

    @RequestMapping(value="/add-new-user", method=RequestMethod.POST)
    public String addNewUser(@RequestParam(value="name") String name, @RequestParam(value="companyId") int companyId, @RequestParam(value="bossId") int bossId) {
        User user = new User();
        user.setName(name);
        user.setCompanyId(companyId);
        user.setBossId(bossId);
        user.setId(UserRepository.counter);
        UserRepository.users.add(user);
        UserRepository.counter++;
        return "redirect:/";
    }
}
