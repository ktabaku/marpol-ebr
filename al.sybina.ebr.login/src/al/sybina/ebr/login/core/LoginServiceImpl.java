package al.sybina.ebr.login.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.oceanpro.core.security.entities.Role;
import com.oceanpro.core.security.entities.User;

import al.sybina.ebr.commons.utils.AuditLogger;
import al.sybina.ebr.login.Activator;


/**
 * @author Gio
 *
 */
@Creatable("LoginService")
@Transactional
public class LoginServiceImpl implements LoginService {

	private static final Logger auditLogger = LoggerCreator.createLogger(AuditLogger.class);
	
//	@Autowired
	UserDAO userDAO;
	RoleDAO roleDAO;


	private EntityManagerFactory emf;
    private EntityManager em;

	protected void activate() {
//		while(Activator.getEntityManager()==null) {
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//			}
//		}
		em = Activator.getEntityManager();
		
		userDAO = new UserDAOImpl();
		userDAO.setEntityManager(em);
		
		roleDAO = new RoleDAOImpl();
		roleDAO.setEntityManager(em);
	}

    protected void deactivate()
    {
    	userDAO=null;
    	roleDAO=null;
    }

	@Override
	public User findUserByUsername(String username) {
		try {
			User user = userDAO.findByUsername(username);
			return user;
		} catch(NoResultException e) {
			return null;
		}
	}

	@Override
	public User getUserById(Long userId) {
		return userDAO.get(userId);
	}

	@Override
	public User saveUser(User user) {
		User createdUser = userDAO.create(user);
		auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " created user " + createdUser.getUsername());
		return createdUser;
	}

	@Override
	public User updateUser(User user) {
		User updatedUser = userDAO.update(user);
		auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " updated user " + updatedUser.getUsername());
		return updatedUser;
	}

	@Override
	public List<User> findAllUsers() {
		return userDAO.getAllCacheStoreRefresh();
	}

	@Override
	public void deleteAllUsers(List<User> users) {
		for(User user : users) {
			userDAO.deleteUser(user.getId());
			auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " deleted user " + user.getUsername());
		}
	}

	@Override
	public Role getRoleByRoleName(String roleName) {
		return roleDAO.findByName(roleName);
	}

	@Override
	public List<Role> findAllRoles() {
		return roleDAO.getAll();
	}

	@Override
	public Role getRoleByRoleDescription(String roleDescription) {
		return roleDAO.findByDescription(roleDescription);
	}
	



}
