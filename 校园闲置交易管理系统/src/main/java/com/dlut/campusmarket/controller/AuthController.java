package com.dlut.campusmarket.controller;

import com.dlut.campusmarket.model.User;
import com.dlut.campusmarket.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        return session.getAttribute("authUser") == null ? "auth/login" : "redirect:/dashboard";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request,
                        RedirectAttributes redirectAttributes) {
        User user = userService.authenticate(username, password);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "用户名或密码不正确，请重新输入");
            return "redirect:/login";
        }
        request.getSession(true);
        request.changeSessionId();
        request.getSession().setAttribute("authUser", user);
        return "redirect:/dashboard";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        if (!userService.register(user)) {
            bindingResult.rejectValue("username", "duplicate", "该用户名已被注册");
            return "auth/register";
        }
        redirectAttributes.addFlashAttribute("success", "注册成功，请使用新账号登录");
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}
