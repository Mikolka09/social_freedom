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
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "register/register";
    }

    @PostMapping("/registration/addUser")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm,
                          BindingResult bindingResult, Model model) {

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

    @PostMapping("/register/edit")
    public RedirectView Edit(@ModelAttribute("userForm") @Valid User userForm,
                             BindingResult bindingResult, Model model,
                             @RequestParam(value = "id") Long id,
                             @RequestParam(value = "avatar") MultipartFile file,
                             @RequestParam(value = "username") String username,
                             @RequestParam(value = "email") String email,
                             @RequestParam(value = "password") String password,
                             @RequestParam(value = "passwordConfirm") String passwordConfirm) {
        String path = "registration/edit/" + userForm.getId();
        userForm.setId(id);
        userForm.setEmail(email);
        userForm.setUsername(username);
        if (Objects.equals(password, "") && Objects.equals(passwordConfirm, "")) {
            return new RedirectView(path);
        } else {
            userForm.setPassword(password);
            userForm.setPasswordConfirm(passwordConfirm);
        }


        if (file != null) {
            if (!file.isEmpty())
                userForm.setAvatarUrl(fileService.uploadFile(file, "avatar"));
        }

        if (bindingResult.hasErrors()) {
            return new RedirectView(path);
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Passwords do not match");
            return new RedirectView(path);
        }
        if (!userService.save(userForm)) {
            model.addAttribute("usernameError", "A user with the same name already exists");
            return new RedirectView(path);
        }

        return new RedirectView("/");
    }

    @GetMapping("registration/edit/{id}")
    public String editUser(@PathVariable(name = "id") Long id, Model model) {
        User user = userService.findUserById(id);
        String url = user.getAvatarUrl();
        user.setAvatarUrl("http://localhost:8080/" + url);
        model.addAttribute("User", user);
        return "/register/edit";
    }
}
