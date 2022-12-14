package blog.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import blog.example.model.entity.BlogEntity;
import blog.example.model.entity.CategoryEntity;
import blog.example.service.BlogService;
import blog.example.service.CategoryService;


@Controller
@RequestMapping("/blog/")
public class UserBlogController {
	
	@Autowired
	BlogService blogService;
	@Autowired
	CategoryService categoryServie;

		//ブログ画面の表示（ログイン操作無し）
		@GetMapping("/")
		public String getBlogUserPage(Model model) {
			//ブログの情報を全て取得する
			List<BlogEntity>blogList = blogService.selectByAll();
			model.addAttribute("blogList",blogList);
			//カテゴリの情報を全て取得する
			List<CategoryEntity>categoryList = categoryServie.findByAll();
			model.addAttribute("categoryList",categoryList);
			return "blog.html";
		}

		@GetMapping("/detail/{blogId}")
		public String getBlogUserDetailPage(@PathVariable Long blogId, Model model) {
			//blogのテーブルの中から、blogIdで検索をかけて該当する該当するブログの情報を引っ張り出す。
			BlogEntity blogs = blogService.selectByBlogId(blogId);
			//カテゴリの情報を全て取得する
			List<CategoryEntity>categoryList = categoryServie.findByAll();
			model.addAttribute("categoryList",categoryList);
			model.addAttribute("blogs",blogs);	
			return "blog_detail.html";
		}

		@GetMapping("/category/list/{categoryName}")
		public String getBlogCategoryAllPage(@PathVariable String categoryName, Model model) {
			//blogのテーブルの中から、categoryNameで検索をかけて該当する該当するブログの情報を引っ張り出す。
			List<BlogEntity> blogList = blogService.selectByCategoryName(categoryName);
			//カテゴリの情報を全て取得する
			List<CategoryEntity>categoryList = categoryServie.findByAll();
			model.addAttribute("categoryList",categoryList);
			model.addAttribute("categoryName",categoryName);
			model.addAttribute("blogList",blogList);	
			return "blog_category_list.html";
		}
}
