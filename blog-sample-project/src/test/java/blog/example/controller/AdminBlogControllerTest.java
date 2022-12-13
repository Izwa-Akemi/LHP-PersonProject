package blog.example.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import blog.example.model.dao.BlogDao;
import blog.example.model.entity.BlogEntity;
import blog.example.model.service.BlogService;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminBlogControllerTest {
	@MockBean
	private BlogService blogService;
	@MockBean
	private BlogDao blogDao;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void prepareData() {
		List<BlogEntity> blogs = Collections.singletonList(new BlogEntity());
		when(blogDao.findAll()).thenReturn(blogs);
		List<BlogEntity> usersResult = blogService.selectByAll();
		assertThat(usersResult).isEqualTo(blogs);
	}

	@Test
	public void testGetBlogCreatePage_Succeed() throws Exception {
		UserDetails alice = User.withDefaultPasswordEncoder()
				.username("alice@test.com")
				.password("Alice123456")
				.roles("USER")
				.build();
		
		RequestBuilder request = MockMvcRequestBuilders
				.post("/blog/register")
				.param("blogTitle","Hello")
				.param("fileName","Hello")
				.param("categoryName","Hello")
				.param("message","Hello")
				.param("userId","1L")
				.with(csrf())
				.with(user(alice));
		
		 mockMvc.perform(request)
			.andExpect(redirectedUrl("/blog/all"));
	}
	

	
}
