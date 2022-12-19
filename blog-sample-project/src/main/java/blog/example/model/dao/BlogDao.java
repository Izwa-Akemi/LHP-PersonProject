package blog.example.model.dao;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import blog.example.model.entity.BlogEntity;
import jakarta.transaction.Transactional;





@Repository
public interface BlogDao extends JpaRepository<BlogEntity, Long> { 
	//ブログの内容を保存
	BlogEntity save(BlogEntity blogEntity);
	//ブログテーブルのuser_idとアカウントテーブルのuserIdを使ってテーブルを結合させてuserIdで検索をかけてデータを取得
	@Query(value="SELECT b.blog_id,b.blog_title,b.blog_image,b.category_name,b.message,b.user_id From blog b INNER JOIN account a ON b.user_id = a.user_id WHERE b.user_id=?1",nativeQuery = true)
	List<BlogEntity>findByUserId(Long userId);

	//blogIdを使用してDBに検索をかける
	BlogEntity findByBlogId(Long blogId);
	
	//ブログテーブルのすべての情報を取得
	List<BlogEntity>findAll();
	
	//カテゴリー名を使用して、DBに検索をかける
	List<BlogEntity>findByCategoryName(String categoryName);

	//blogIdを取得して該当するブログ情報を削除する
	@Transactional
	List<BlogEntity> deleteByBlogId(Long blogId);
}
