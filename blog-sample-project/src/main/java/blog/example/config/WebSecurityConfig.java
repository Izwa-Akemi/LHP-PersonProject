package blog.example.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import blog.example.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {	
	  @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.formLogin(login -> login
	                .loginPage("/admin/login")
	                .defaultSuccessUrl("/admin/blog/all",true)  
	                .usernameParameter("userEmail") //リクエストパラメータのname属性を明示
	        		.passwordParameter("password")
	                .failureUrl("/admin/login?error")
	                .permitAll()
	        ).logout(logout -> logout
	        		.logoutSuccessUrl("/admin/login")
	        ).authorizeHttpRequests(authz -> authz
	                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
	                .requestMatchers("/blog/**","/admin/register","/css/**", "/js/**","/blog-image/**","/images/**").permitAll()
	                .anyRequest().authenticated()
	        );
	        return http.build();
	    }

	public static UserDetailsManager manager = null;
	@Autowired
	private UserService userService;
	@Bean
	public UserDetailsService userDetailsService() {
		List<UserDetails> users = userService.getAllAccounts().stream().map(
				account -> User.withDefaultPasswordEncoder()
				.username(account.getUserEmail())
				.password(account.getPassword())
				.roles("USER")
				.build()
				).toList();
			
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
