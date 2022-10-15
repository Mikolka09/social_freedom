package itstep.social_freedom.controller;

import itstep.social_freedom.entity.User;
import itstep.social_freedom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Model model){
        User user = userService.getCurrentUsername();
        model.addAttribute("User", user);
        return "pages/index";
    }
}
