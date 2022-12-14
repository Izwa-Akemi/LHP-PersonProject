package blog.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.example.model.dao.CategoryDao;
import blog.example.model.entity.CategoryEntity;


@Service
public class CategoryService {
	@Autowired
	CategoryDao categoryDao;

	//一覧表示
	public List<CategoryEntity> findByAll() {
		return categoryDao.findAll();
	}

	//内容を保存
	public void insert(String categoryName) {
		categoryDao.save(new CategoryEntity(categoryName));
	}

	//編集
	public void update(Long categoryId,String categoryName) {
		categoryDao.save(new CategoryEntity(categoryId,categoryName));
	}
	
	//CategoryIDを基にDBで検索をかける
	public CategoryEntity selectCategoryId(Long categoryId) {
		return categoryDao.findByCategoryId(categoryId);
	}

	//削除
	public void delete(Long categoryId) {
		categoryDao.deleteById(categoryId);
	}
}
