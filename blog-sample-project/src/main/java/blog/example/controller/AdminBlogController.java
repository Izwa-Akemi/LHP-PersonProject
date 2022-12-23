package blog.example.controller;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



import blog.example.model.entity.BlogEntity;
import blog.example.model.entity.CategoryEntity;
import blog.example.model.entity.UserEntity;
import blog.example.service.BlogService;
import blog.example.service.CategoryService;
import blog.example.service.UserService;


@Controller
@RequestMapping("/admin/blog")
public class AdminBlogController {
	/**
	 * accountテーブルを操作するための
	 * Serviceクラス
	 */
	@Autowired
	private UserService userService;
	/**
	 * blogテーブルを操作するための
	 * Serviceクラス
	 */
	@Autowired
	BlogService blogService;
	/**
	 * categoryテーブルを操作するための
	 * Serviceクラス
	 */
	@Autowired
	CategoryService categoryServie;


	/*
	 * 管理者側の処理
	 */

	/**
	 * 現在ログインしている人が記載した記事の一覧を出すための処理です。
	 * ―ログインしている人のメールアドレスを使用して、ログインしている人のuserIdとuserNameを取得します。
	 * ―取得したuserIdを使用してログインしている人のブログ記事を取得します。
	 * ―取得した情報をセットして画面から参照可能にします。
	 */
	//管理者側のブログ一覧を表示
	@GetMapping("/all")
	public String getLoginPage(Model model) {
		//		現在のリクエストに紐づく Authentication を取得するには SecurityContextHolder.getContext().getAuthentication() とする。
		//		SecurityContextHolder.getContext() は、現在のリクエストに紐づく SecurityContext を返している。
		//		Authentication.getAuthorities() で、現在のログインユーザーに付与されている権限（GrantedAuthority のコレクション）を取得できる。
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		//ログインした人のメールアドレスを取得
		String userEmail = auth.getName();
		//accountテーブルの中から、ユーザーのEmailで検索をかけて該当するユーザーのID情報を引っ張り出す。
		UserEntity user = userService.selectById(userEmail);

		//accountテーブルの中からログインしているユーザーの名前の取得
		String userName = user.getUserName();

		//accountテーブルの中からログインしているユーザーのIDを取得
		Long userId = user.getUserId();

		//ブログテーブルの中からユーザーIDを使って、そのユーザーが書いたブログ記事のみを取得する
		List<BlogEntity>blogList = blogService.selectByUserId(userId);
		//blogList（ブログの記事一覧情報）とuserName（管理者の名前）をmodelにセットし
		//admin_blog_all.htmlから参照可能にする。
		model.addAttribute("blogList",blogList);
		model.addAttribute("userName",userName);
		return "admin_blog_all.html";
	}


	/**
	 * ブログ記事の登録画面を表示させるための処理です。
	 * ―ログインしている人のメールアドレスを使用して、ログインしている人のuserIdとuserNameを取得します。
	 * ―カテゴリ一覧を取得します
	 * ―取得した情報をセットして画面から参照可能にします。
	 */

	//ブログ記事の登録
	@GetMapping("/register")
	public String getBlogCreatePage(Model model) {
		//		現在のリクエストに紐づく Authentication を取得するには SecurityContextHolder.getContext().getAuthentication() とする。
		//		SecurityContextHolder.getContext() は、現在のリクエストに紐づく SecurityContext を返している。
		//		Authentication.getAuthorities() で、現在のログインユーザーに付与されている権限（GrantedAuthority のコレクション）を取得できる。
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//ログインした人のメールアドレスを取得
		String userEmail = auth.getName();
		//accountテーブルの中から、ユーザーのEmailで検索をかけて該当するユーザーの情報を引っ張り出す。
		UserEntity user = userService.selectById(userEmail);
		//accountテーブルの中からログインしているユーザーのIDを取得
		Long userId = user.getUserId();
		//accountテーブルの中からログインしているユーザーの名前の取得
		String userName = user.getUserName();
		//カテゴリー一覧を取得
		List<CategoryEntity>categoryList = categoryServie.findByAll();

		//userId（管理者のId）、categoryList（カテゴリ一覧）
		//userName（管理者の名前）をmodelにセットし
		//admin_blog_register.htmlから参照可能にする。
		model.addAttribute("userId",userId);
		model.addAttribute("categoryList",categoryList);
		model.addAttribute("userName",userName);

		return "admin_blog_register.html";
	}

	/**
	 * ブログ記事を登録させるための処理です。
	 * ―画像名を取得し、blog-imageにアップロードする作業を行います。
	 * ―入力された情報によってblogテーブルに保存処理を行います。
	 * ―保存処理後は、ブログ一覧画面にリダイレクトさせます。
	 */
	//登録内容を保存
	@PostMapping("/register")
	public String register(@RequestParam String blogTitle,@RequestParam("blogImage") MultipartFile blogImage,@RequestParam String categoryName,@RequestParam String message,@RequestParam Long userId) {

		//画像ファイル名を取得する
		String fileName = blogImage.getOriginalFilename();

		//ファイルのアップロード処理
		try {
			//画像ファイルの保存先を指定する。
			File blogFile = new File("./src/main/resources/static/blog-image/"+fileName);
			//画像ファイルからバイナリデータを取得する
			byte[] bytes = blogImage.getBytes();
			//画像を保存（書き出し）するためのバッファを用意する
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(blogFile));
			//画像ファイルの書き出しする。
			out.write(bytes);
			//バッファを閉じることにより、書き出しを正常終了させる。
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		//ファイルのアップロード処理後に、サービスクラスのメソッドに値を渡して保存する
		blogService.insert(blogTitle, fileName, categoryName, message, userId);

		return "redirect:/admin/blog/all";
	}

	/**
	 * ブログ記事の編集画面を表示させるための処理です。
	 * ―リンクからblogIdを取得する
	 * ―blogIdに紐づくレコードを探す
	 * ―取得した情報をセットして画面から参照可能にします。
	 */
	//リンクタグで記載したblogIdを取得する
	@GetMapping("/edit/{blogId}")
	public String getBlogDetailPage(@PathVariable Long blogId, Model model) {
		//		現在のリクエストに紐づく Authentication を取得するには SecurityContextHolder.getContext().getAuthentication() とする。
		//		SecurityContextHolder.getContext() は、現在のリクエストに紐づく SecurityContext を返している。
		//		Authentication.getAuthorities() で、現在のログインユーザーに付与されている権限（GrantedAuthority のコレクション）を取得できる。
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//ログインした人のメールアドレスを取得
		String userEmail = auth.getName();
		//accountテーブルの中から、ユーザーのEmailで検索をかけて該当するユーザーの情報を引っ張り出す。
		UserEntity user = userService.selectById(userEmail);
		//accountテーブルの中から、ユーザー名を取得
		String userName = user.getUserName();
		//accountテーブルの中から、ユーザーIDを取得
		Long userId = user.getUserId();
		//カテゴリ―の一覧を取得
		List<CategoryEntity>categoryList = categoryServie.findByAll();
		//blogのテーブルの中から、blogIdで検索をかけて該当する該当するブログの情報を引っ張り出す。
		BlogEntity blogs = blogService.selectByBlogId(blogId);

		//userId（管理者のId）、categoryList（カテゴリ一覧）
		//userName（管理者の名前）、blogs(idに紐づいたブログ記事）をmodelにセットし
		//admin_blog_edit.htmlから参照可能にする。
		model.addAttribute("userId",userId);
		model.addAttribute("blogs",blogs);	
		model.addAttribute("categoryList",categoryList);
		model.addAttribute("userName",userName);
		return "admin_blog_edit.html";
	}
	/**
	 * ブログ記事を更新させるための処理です。
	 * ―画像名を取得し、blog-imageにアップロードする作業を行います。
	 * ―入力された情報によってblogテーブルに更新処理を行います。
	 * ―更新処理後は、ブログ一覧画面にリダイレクトさせます。
	 */
	//登録内容を修正（更新）
	@PostMapping("/update")
	public String updateData(@RequestParam Long blogId,@RequestParam String blogTitle,@RequestParam("blogImage") MultipartFile blogImage,@RequestParam String categoryName,@RequestParam String message,@RequestParam Long userId) {
		//画像ファイル名を取得する
		String fileName = blogImage.getOriginalFilename();
		//ファイルのアップロード処理
		try {
			//画像ファイルの保存先を指定する。
			File blogFile = new File("./src/main/resources/static/blog-image/"+fileName);
			//画像ファイルからバイナリデータを取得する。
			byte[] bytes = blogImage.getBytes();
			//画像を保存（書き出し）するためのバッファを用意する。
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(blogFile));
			//画像ファイルの書き出しする。
			out.write(bytes);
			//バッファを閉じることにより、書き出しを正常終了させる。
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		//ファイルのアップロード処理後に、サービスクラスのメソッドに値を渡して更新を行う。
		blogService.update(blogId, blogTitle, fileName, categoryName, message, userId);

		return "redirect:/admin/blog/all";
	}

	/**
	 * ブログ記事を削除させるための処理です。
	 * ―blogIdに紐づくレコードを探す
	 * ―紐づくレコードを削除する
	 */
	//ブログの内容を削除
	@PostMapping("/delete")
	public String blogDelete(@RequestParam Long blogId) {
		//Serviceクラスに値をわたし、削除処理を行う。
		blogService.deleteBlog(blogId);
		return "redirect:/admin/blog/all";
	}


}
