package blog.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import blog.example.model.service.UserService;



@Controller
@RequestMapping("/admin/")
public class ReigisterController {
	@Autowired
	private UserService accountService;

	//登録画面を表示
	@GetMapping("/register")
	public String getRegisterPage() {
		return "admin_register.html";
	}
	//ユーザー情報の登録
	@PostMapping("/register")
	public String register(@RequestParam String userName,@RequestParam String userEmail,
			@RequestParam String password, Model model) {
		//もし保存をした場合には、login.htmlへ遷移する
		if (accountService.createAccount(userName, userEmail,password)) {
			return "login.html";
		} else {
			//そうでない場合には、register.htmlに遷移する
			return "admin_register.html";

		}

	}
}
