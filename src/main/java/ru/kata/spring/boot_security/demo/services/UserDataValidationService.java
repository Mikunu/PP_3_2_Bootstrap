package ru.kata.spring.boot_security.demo.services;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.util.DataValidator;
import ru.kata.spring.boot_security.demo.util.UserValidator;
import ru.kata.spring.boot_security.demo.util.RoleValidator;

import java.util.List;

@Component
public class UserDataValidationService {

    private final UserValidator userValidator;
    private final RoleValidator roleValidator;
    private final DataValidator dataValidator;

    public UserDataValidationService(
            UserValidator userValidator,
            RoleValidator roleValidator,
            DataValidator dataValidator) {
        this.userValidator = userValidator;
        this.roleValidator = roleValidator;
        this.dataValidator = dataValidator;
    }

    public String validateUserData(User user, List<String> role) {
        BindingResult userBindingResult = new BeanPropertyBindingResult(user, "user");
        BindingResult roleBindingResult = new BeanPropertyBindingResult(role, "role");

        userValidator.validate(user, userBindingResult);
        roleValidator.validate(role, roleBindingResult);

        dataValidator.dataClean();

        dataValidator.validate(userBindingResult);
        dataValidator.validate(roleBindingResult);

        return dataValidator.getAllErrorsAsString();
    }
}
