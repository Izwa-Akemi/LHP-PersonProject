package blog.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import blog.example.model.entity.BlogEntity;
import blog.example.model.service.BlogService;
import blog.example.model.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
	@Autowired
	private MockMvc mockMvc;


	@Test
	//ログインページ正常に表示させる
	public void AccessLoginPage() throws Exception{

		RequestBuilder request = MockMvcRequestBuilders//
				.get("/login");

		mockMvc.perform(request)//
		//レスポンスとしてlogin.htmlが返されるはず
		.andExpect(view().name("login.html"));

	}
	@Test
	//ログインが成功した場合、ブログ一覧ページに遷移する
	public void testLogin_CorrectInfo_Succeed() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders
				.post("/login")
				//パラメータとしてユーザーの名前とユーザーのパスワードを受け取る。
				.param("userEmail", "alice@test.com")
				.param("password", "Alice123456")
		        .with(csrf());

		mockMvc.perform(request)//
		.andExpect(redirectedUrl("/blog/all"));
	}


}
