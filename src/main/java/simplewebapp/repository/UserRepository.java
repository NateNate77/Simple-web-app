package simplewebapp.repository;

import simplewebapp.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 17.11.2018.
 */
public class UserRepository {

    public static List<User> users = new ArrayList<User>();

    public static int counter = 0;
}
