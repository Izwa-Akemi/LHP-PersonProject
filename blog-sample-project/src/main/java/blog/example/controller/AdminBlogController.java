package blog.example.controller;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import blog.example.config.WebSecurityConfig;
import blog.example.model.entity.BlogEntity;
import blog.example.model.entity.CategoryEntity;
import blog.example.model.entity.UserEntity;
import blog.example.model.service.BlogService;
import blog.example.model.service.CategoryService;
import blog.example.model.service.UserService;
import lombok.NonNull;


@Controller
@RequestMapping("/admin/blog/")
public class AdminBlogController {

	@Autowired
	private UserService userService;
	@Autowired
	BlogService blogService;
	@Autowired
	CategoryService categoryServie;


	/*
	 * 管理者側の処理
	 */

	//通常のURLデフォルトの場合には、blogallにリダイレクトさせる
	@GetMapping("/")
	public String index(){
		return "redirect:/blog/";
	}
	//管理者側のブログ一覧を表示
	@GetMapping("/all")
	public String getLoginPage(Model model) {
		//		現在のリクエストに紐づく Authentication を取得するには SecurityContextHolder.getContext().getAuthentication() とする。
		//		SecurityContextHolder.getContext() は、現在のリクエストに紐づく SecurityContext を返している。
		//		Authentication.getAuthorities() で、現在のログインユーザーに付与されている権限（GrantedAuthority のコレクション）を取得できる。
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		//ログインした人のメールアドレスを取得
		String userEmail = auth.getName();
		//ユーザーのテーブルの中から、ユーザーのEmailで検索をかけて該当するユーザーのID情報を引っ張り出す。
		UserEntity user = userService.selectById(userEmail);
		//ユーザーのテーブルの中からログインしているユーザーの名前の取得
		String userName = user.getUserName();

		//ユーザーのテーブルの中からログインしているユーザーのIDを取得
		Long userId = user.getUserId();

		//ブログテーブルの中からユーザーIDを使って、そのユーザーが書いたブログ記事のみを取得する
		List<BlogEntity>blogList = blogService.selectByUserId(userId);
		//html側にListに格納した情報を渡す
		model.addAttribute("blogList",blogList);	 
		return "admin_blog_all.html";
	}

	//ブログ記事の登録
	@GetMapping("/register")
	public String getBlogCreatePage(Model model) {
		//		現在のリクエストに紐づく Authentication を取得するには SecurityContextHolder.getContext().getAuthentication() とする。
		//		SecurityContextHolder.getContext() は、現在のリクエストに紐づく SecurityContext を返している。
		//		Authentication.getAuthorities() で、現在のログインユーザーに付与されている権限（GrantedAuthority のコレクション）を取得できる。
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//ログインした人のメールアドレスを取得
		String userEmail = auth.getName();
		//ユーザーのテーブルの中から、ユーザーのEmailで検索をかけて該当するユーザーの情報を引っ張り出す。
		UserEntity user = userService.selectById(userEmail);
		//ユーザーのテーブルの中からログインしているユーザーのIDを取得
		Long userId = user.getUserId();
		//カテゴリー一覧を取得
		List<CategoryEntity>categoryList = categoryServie.findByAll();
		//html側にユーザーidを渡す
		model.addAttribute("userId",userId);
		//html側にListに格納した情報を渡す
		model.addAttribute("categoryList",categoryList);

		return "admin_blog_register.html";
	}

	//登録内容を保存
	@PostMapping("/register")
	public String register(@RequestParam String blogTitle,@RequestParam("blogImage") MultipartFile blogImage,@RequestParam String categoryName,@RequestParam String message,@RequestParam Long userId) {
		//ファイルの名前を取得する
		String fileName = blogImage.getOriginalFilename();

		//ファイルのアップロード処理
		try {
			File blogFile = new File("./src/main/resources/static/blog-image/"+fileName);
			byte[] bytes = blogImage.getBytes();
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(blogFile));
			out.write(bytes);
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		//ファイルのアップロード処理後に、サービスクラスのメソッドに値を渡して保存する
		blogService.insert(blogTitle, fileName, categoryName, message, userId);

		return "redirect:/admin/blog/all";
	}

	//ブログ記事の詳細を表示させる
	//リンクタグで記載したblogIdを取得する
	@GetMapping("/edit/{blogId}")
	public String getBlogDetailPage(@PathVariable Long blogId, Model model) {
		//		現在のリクエストに紐づく Authentication を取得するには SecurityContextHolder.getContext().getAuthentication() とする。
		//		SecurityContextHolder.getContext() は、現在のリクエストに紐づく SecurityContext を返している。
		//		Authentication.getAuthorities() で、現在のログインユーザーに付与されている権限（GrantedAuthority のコレクション）を取得できる。
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//ログインした人のメールアドレスを取得
		String userEmail = auth.getName();
		//ユーザーのテーブルの中から、ユーザーのEmailで検索をかけて該当するユーザーの情報を引っ張り出す。
		UserEntity user = userService.selectById(userEmail);
		//ユーザーのテーブルの中から、ユーザー名を取得
		String userName = user.getUserName();
		//ユーザーのテーブルの中から、ユーザーIDを取得
		Long userId = user.getUserId();
		//カテゴリ―の一覧を取得し
		List<CategoryEntity>categoryList = categoryServie.findByAll();
		//blogのテーブルの中から、blogIdで検索をかけて該当する該当するブログの情報を引っ張り出す。
		BlogEntity blogs = blogService.selectByBlogId(blogId);
		model.addAttribute("userId",userId);
		model.addAttribute("blogs",blogs);	
		model.addAttribute("categoryList",categoryList);
		return "admin_blog_edit.html";
	}

	//登録内容を修正（更新）
	@PostMapping("/update")
	public String updateData(@RequestParam Long blogId,@RequestParam String blogTitle,@RequestParam("blogImage") MultipartFile blogImage,@RequestParam String categoryName,@RequestParam String message,@RequestParam Long userId) {
		//ファイルの名前を取得する
		String fileName = blogImage.getOriginalFilename();
		//ファイルのアップロード処理
		try {
			File blogFile = new File("./src/main/resources/static/blog-image/"+fileName);
			byte[] bytes = blogImage.getBytes();
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(blogFile));
			out.write(bytes);
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		//ファイルのアップロード処理後に、サービスクラスのメソッドに値を渡して保存する
		blogService.update(blogId, blogTitle, fileName, categoryName, message, userId);

		return "redirect:/admin/blog/all";
	}

	//ブログの内容を削除
	@PostMapping("/delete")
	public String blogDelete(@RequestParam Long blogId) {
		blogService.deleteBlog(blogId);
		return "redirect:/admin/blog/all";
	}


}
