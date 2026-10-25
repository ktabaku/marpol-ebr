package al.sybina.ebr.login.core;

import java.util.List;

import com.oceanpro.core.security.entities.Role;
import com.oceanpro.core.security.entities.User;

import javafx.util.Callback;

public interface LoginService {


	User findUserByUsername(String username);

	User getUserById(Long userId);
	
	User saveUser(User user);

	User updateUser(User user);

	List<User> findAllUsers();

	void deleteAllUsers(List<User> users);

	Role getRoleByRoleName(String roleName);

	List<Role> findAllRoles();

	Role getRoleByRoleDescription(String roleDescription);
	
	

}
