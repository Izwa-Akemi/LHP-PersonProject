package blog.example.it;

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

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterAndLoginTest {
	@Autowired
	private MockMvc mockMvc;

	//登録画面で正常に登録を行い正常にログインした場合

	@Test
	public void testRegisterAndLogin_NewUser_Succeed() throws Exception {
		// 登録ページに接続
		RequestBuilder request = MockMvcRequestBuilders
				.get("/register");
		mockMvc.perform(request)
		//register.htmlを表示
		.andExpect(view().name("register.html"));

		//登録処理を行う
		request = MockMvcRequestBuilders
				.post("/register")
				//ユーザー名としてBobを登録
				.param("userName", "IzawaAkemi")
				//ユーザーのメールアドレスとしてtest@test.comを登録
				.param("userEmail","akemitest@test.com")
				//パスワードとしてBob54321を登録
				.param("password", "Akemi1234")
				.with(csrf());
		mockMvc.perform(request)
		//login.htmlを表示
		.andExpect(view().name("login.html"));

		// ログインする
		request = MockMvcRequestBuilders
				.post("/login")
				//パラメータとしてユーザーの名前とユーザーのパスワードを受け取る。
				.param("userEmail", "akemitest@test.com")
				.param("password", "Akemi1234")
				.with(csrf());


		mockMvc.perform(request)//
		.andExpect(redirectedUrl("/blog/all"));

	}

	@Test
	//登録処理を行ったが、すでにユーザーが存在していて、登録処理に失敗した場合
	public void testRegisterAndLogin_ExistingUsername_Fail() throws Exception {
		// 登録ページに接続
		RequestBuilder request = MockMvcRequestBuilders
				.get("/register");

		mockMvc.perform(request)
		.andExpect(view().name("register.html"));

		//登録処理を行う
		request = MockMvcRequestBuilders
				.post("/register")
				//ユーザー名としてBobを登録
				.param("userName", "IzawaAkemi")
				//ユーザーのメールアドレスとしてtest@test.comを登録
				.param("userEmail","akemitest@test.com")
				//パスワードとしてBob54321を登録
				.param("password", "Akemi1234")
				.with(csrf());


		mockMvc.perform(request)
		//register.htmlを表示
		.andExpect(view().name("register.html"));

	}
	@Test
	//ユーザー登録をしてない状態でログインをした場合
	public void testRegisterAndLogin_UnregisteredUser_Fail() throws Exception {		
		// ログインページに接続
		RequestBuilder request = MockMvcRequestBuilders
				.get("/login");
		
		mockMvc.perform(request)
				.andExpect(view().name("login.html"));
		
		// ログインする
		request = MockMvcRequestBuilders
				.post("/login")
				.param("userEmail", "1234@test.com")
				.param("password", "1234test123")
		        .with(csrf());
		
		mockMvc.perform(request)
		.andExpect(redirectedUrl("/login?error"));
	}


}
