package blog.example.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.example.model.entity.CategoryEntity;



public interface CategoryDao extends JpaRepository<CategoryEntity, Long> {
	//カテゴリー内容の保存
	CategoryEntity save(CategoryEntity categoryEntity);
	//categoryIdを使用してDBに検索をかける
	CategoryEntity findByCategoryId(Long categoryId);
	//削除
	List<CategoryEntity> deleteByCategoryId(Long categoryId);
	//カテゴリー一覧を取得
	List<CategoryEntity> findAll();
}
