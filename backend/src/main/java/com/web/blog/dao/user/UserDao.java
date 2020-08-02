
package com.web.blog.dao.user;

import java.util.Optional;

import com.web.blog.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, String> {
    User getUserByEmail(String email);

    User getUserByNickname(String nickname);

    User findUserByEmailAndPassword(String email, String password);

    User findUserByEmailAndNickname(String email, String nickname);

    User findUserByNickname(String nickname);

    User findUserByUid(int uid);

    User getUserByPassword(String passowrd);

    Optional<User> findUserByNicknameAndEmail(String nickname, String email);

	User findByEmailAndUid(String email, int uid);
}
