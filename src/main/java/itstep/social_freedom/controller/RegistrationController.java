package itstep.social_freedom.controller;

import itstep.social_freedom.entity.User;
import itstep.social_freedom.service.FileService;
import itstep.social_freedom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    FileService fileService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "register/register";
    }

    @PostMapping("/registration/addUser")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm,
                          BindingResult bindingResult, Model model,
                          @RequestParam(value = "avatar") MultipartFile file) {
        if (file != null) {
            if (!file.isEmpty())
                userForm.setAvatarUrl(fileService.uploadFile(file));
        }

        if (bindingResult.hasErrors()) {
            return "register/register";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Passwords do not match");
            return "register/register";
        }
        if (!userService.saveUser(userForm)) {
            model.addAttribute("usernameError", "A user with the same name already exists");
            return "register/register";
        }

        return "redirect:/";
    }

    @GetMapping("registration/edit/{id}")
    public String editUser(@PathVariable(name = "id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("User", user);
        return "/register/edit";
    }
}
