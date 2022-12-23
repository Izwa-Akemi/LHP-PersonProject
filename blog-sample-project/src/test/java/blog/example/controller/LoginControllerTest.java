package blog.example.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
	@Autowired
	private MockMvc mockMvc;


	@Test
	//ログインページ正常に表示させる
	public void AccessLoginPage() throws Exception{

		RequestBuilder request = MockMvcRequestBuilders//
				.get("/admin/login");

		mockMvc.perform(request)//
		//レスポンスとしてlogin.htmlが返されるはず
		.andExpect(view().name("admin_login.html"));

	}
	@Test
	//ログインが成功した場合、ブログ一覧ページに遷移する
	public void testLogin_CorrectInfo_Succeed() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders
				.post("/admin/login")
				//パラメータとしてユーザーの名前とユーザーのパスワードを受け取る。
				.param("userEmail", "alice@test.com")
				.param("password", "Alice123456")
		        .with(csrf());

		mockMvc.perform(request)//
		.andExpect(redirectedUrl("/admin/blog/all"));
	}


}
