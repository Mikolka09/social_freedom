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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
public class AdminController {
    private final UserService userService;

    @Autowired
    private FileService fileService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String index(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "admin/index";
    }

    @GetMapping("/admin/users")
    public String usersList(Model model) {
        List<User> users = userService.allUsers();
        model.addAttribute("allUsers", users);
        return "admin/users";
    }

    @PostMapping("/admin")
    public String deleteUser(@RequestParam(required = true, defaultValue = "") Long userId,
                             @RequestParam(required = true, defaultValue = "") String action,
                             Model model) {
        if (action.equals("delete")) {
            userService.deleteUser(userId);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/gt/{userId}")
    public String gtUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergtList(userId));
        return "admin";
    }

    @GetMapping("admin/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("User", user);
        return "admin/user-edit";
    }

    @PostMapping("/admin/edit-store")
    public String Edit(@ModelAttribute("userForm") @Valid User userForm,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes,
                       @RequestParam(value = "user_id") Long user_id,
                       @RequestParam(value = "avatar") MultipartFile file,
                       @RequestParam(value = "username") String username,
                       @RequestParam(value = "email") String email,
                       @RequestParam(value = "not_removed") Boolean not_removed) {

        String path = "../admin/users/edit/" + user_id;
        User user = userService.findUserById(user_id);
        if (bindingResult.hasErrors()) {
            redirectAttributes.getFlashAttributes().clear();
            redirectAttributes.addFlashAttribute("error", "Not all fields are filled!");
            return "redirect:" + (path);
        }
        user.setEmail(email);
        user.setUsername(username);

        if (not_removed) {
            user.setRemoved(false);
        }

        if (file != null) {
            if (!file.isEmpty())
                user.setAvatarUrl(fileService.uploadFile(file, "avatar/"));
        }

        if (!userService.save(user)) {
            redirectAttributes.getFlashAttributes().clear();
            redirectAttributes.addFlashAttribute("error", "A user with the same name already exists!");
            return "redirect:" + (path);
        }

        return "redirect:/admin/users";
    }
}
