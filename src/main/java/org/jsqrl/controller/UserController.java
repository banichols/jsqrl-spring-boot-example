package org.jsqrl.controller;

import org.jsqrl.model.MyUser;
import org.jsqrl.model.UserForm;
import org.jsqrl.service.InMemoryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Brent on 7/13/2016.
 */
@Controller
public class UserController {

    @Autowired
    private InMemoryUserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String updateUser(@ModelAttribute UserForm form){
        MyUser user = getAuthenticatedUser();
        userService.updateUserById(user.getId(), form.getFirstName(), form.getEmail());
        return "account";
    }

    private MyUser getAuthenticatedUser(){
        return (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
