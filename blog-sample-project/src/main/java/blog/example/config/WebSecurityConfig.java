package blog.example.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import blog.example.model.service.UserService;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/blog/**","/admin/register","/css/**", "/js/**","/blog-image/**","/images/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/admin/login")
		.defaultSuccessUrl("/admin/blog/all")
		.usernameParameter("userEmail") //リクエストパラメータのname属性を明示
		.passwordParameter("password")
		.permitAll()
		.and()
		.logout()
		.logoutSuccessUrl("/admin/login")
		.permitAll();
	}

	public static UserDetailsManager manager = null;
	@Autowired
	private UserService userService;

	@Override
	@Bean
	public UserDetailsService userDetailsService() {
		List<UserDetails> users = userService.getAllAccounts().stream().map(
				account -> User.withDefaultPasswordEncoder()
				.username(account.getUserEmail())
				.password(account.getPassword())
				.roles("USER")
				.build()
				).toList();
		//		UserDetails user1 =
		//				User.withDefaultPasswordEncoder()
		//				.username("alice@test.com")
		//				.password("Alice123456")
		//				.roles("USER")
		//				.build();
		//        manager.createUser(user1);
		
		manager = new InMemoryUserDetailsManager(users);
		manager.createUser(User.withDefaultPasswordEncoder()
				.username("alice@test.com")
				.password("Alice123456")
				.roles("USER")
				.build());
		return manager;
	}


	public static void addUser(String userEmail, String password) {
		manager.createUser(User.withDefaultPasswordEncoder()
				.username(userEmail)
				.password(password)
				.roles("USER")
				.build());
	}


}
