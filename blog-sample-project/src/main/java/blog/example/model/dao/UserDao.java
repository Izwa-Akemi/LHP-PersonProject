package blog.example.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.example.model.entity.UserEntity;

@Repository
public interface UserDao extends JpaRepository<UserEntity, Long> {
	
	// UserServiceから渡されるユーザ情報（メールアドレス）を条件にDB検索
	UserEntity findByUserEmail(String userEmail);

	// UserServiceから渡されるユーザ情報を基にDBへ保存する
	UserEntity save(UserEntity userEntity);

	// ユーザ情報一覧を取得
	List<UserEntity> findAll();
}