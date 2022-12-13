package blog.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.example.model.entity.UserEntity;
import blog.example.model.service.UserService;

@Controller
@RequestMapping("/admin/")
public class LoginController {
	@Autowired
	UserService accountService;
	
	//ログイン画面を表示
	@GetMapping("/login")
	public String getLoginPage() {
		return "admin_login.html";
	}
	
	
}
