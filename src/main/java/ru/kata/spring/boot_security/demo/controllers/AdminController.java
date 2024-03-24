package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.AdminService;
import ru.kata.spring.boot_security.demo.services.UserDataValidationService;
import ru.kata.spring.boot_security.demo.util.DataValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final DataValidator dataValidator;
    private final UserDataValidationService userDataValidationService;


    @Autowired
    public AdminController(AdminService adminService, DataValidator dataValidator, UserDataValidationService userDataValidationService) {
        this.adminService = adminService;
        this.dataValidator = dataValidator;
        this.userDataValidationService = userDataValidationService;
    }


    @GetMapping()
    public String showAllUsers(Model model, Principal principal) {
        User user = adminService.findUserByName(principal.getName());
        model.addAttribute("currentUser", user);
        List<User> listOfUsers = adminService.getAllUsers();
        model.addAttribute("listOfUsers", listOfUsers);
        model.addAttribute("user", new User());
        model.addAttribute("allErrors", dataValidator.getAllErrorsAsString());
        return "admin/users";
    }


    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        return "/admin/new";
    }


    @PostMapping("")
    public String createUser(@ModelAttribute @Valid User user,
                             @RequestParam(value = "roles", required = false)
                             List<String> roles,
                             RedirectAttributes redirectAttributes) {

        String allErrors = userDataValidationService.validateUserData(user, roles);

        if (!allErrors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorValidation", allErrors);
            return "redirect:/admin";
        }
        adminService.create(user, roles);
        return "redirect:/admin";
    }


    @PostMapping("/user/edit")
    public String update(@ModelAttribute("person") @Valid User user,
                         @RequestParam(value = "role", required = false) @Valid List<String> role,
                         RedirectAttributes redirectAttributes) {

        String allErrors = userDataValidationService.validateUserData(user, role);

        if (!allErrors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorValidation", allErrors);
            return "redirect:/admin";
        }

        adminService.updateUser(user, role);
        return "redirect:/admin";
    }

    @PostMapping("/user/delete")
    public String delete(@RequestParam Long id) {
        adminService.removeUser(id);
        return "redirect:/admin";
    }
}
