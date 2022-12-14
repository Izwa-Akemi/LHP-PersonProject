package blog.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.example.model.entity.CategoryEntity;
import blog.example.service.CategoryService;





@Controller
@RequestMapping("/admin/category/")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	//カテゴリー一覧を表示
	@GetMapping("/all")
	public String getCategoryAll(Model model) {
		List<CategoryEntity>categorylist = categoryService.findByAll();
		model.addAttribute("categorylist",categorylist);

		return "admin_category_all.html";
	}

	//カテゴリー登録画面を表示
	@GetMapping("/register")
	public String geCategoryListRegister() {
		return "admin_category_register.html";
	}
	//カテゴリー登録内容を保存
	@PostMapping("/register")
	public String register(@RequestParam String categoryName) {
		categoryService.insert(categoryName);
		return "redirect:/admin/category/all";
	}
	//カテゴリー編集画面の表示
	@GetMapping("/edit/{categoryId}")
	public String getCategroyEditPage(@PathVariable Long categoryId,Model model) {
		CategoryEntity categoryEntity = categoryService.selectCategoryId(categoryId);
		model.addAttribute("category",categoryEntity);
		return "admin_category_edit.html";
	}
	@PostMapping("/update")
	public String update(@RequestParam String categoryName,@RequestParam Long categoryId) {
		categoryService.update(categoryId, categoryName);
		return "redirect:/admin/category/all";
	}
	//カテゴリー登録内容を削除
		@PostMapping("/delete")
		public String deleteCategory(@RequestParam Long categoryId) {
			categoryService.delete(categoryId);
			return "redirect:/admin/category/all";
		}


}
