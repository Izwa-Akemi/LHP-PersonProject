package blog.example.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import blog.example.config.WebSecurityConfig;
import blog.example.model.dao.UserDao;
import blog.example.model.entity.UserEntity;


@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	  //ユーザの情報を保存する
	public boolean createAccount(String userName,String userEmail, String password) {
		//RegisterControllerから渡されるユーザ情報（ユーザ名、パスワード）を条件にDB検索で検索する
		List<UserEntity> userList = userDao.findByUserNameAndPassword(userName, password);
		//RegisterControllerから渡されるユーザ情報（ユーザ名、パスワード）を条件にDB検索で検索した結果
		//なかった場合には、保存
		if (userList.isEmpty()) {
			userDao.save(new UserEntity(userName,userEmail, password));
			WebSecurityConfig.addUser(userEmail, password);
			return true;
		} else {
			return false;
		}
	}
    //ユーザの一覧を取得する
	public List<UserEntity> getAllAccounts() {
		return userDao.findAll();
	}

	//idを見つける
	//コントローラーでわたってきたuserEmailを基にしてDBを検索
	public UserEntity selectById(String userEmail) {
		return userDao.findByUserEmail(userEmail);
	}

}
