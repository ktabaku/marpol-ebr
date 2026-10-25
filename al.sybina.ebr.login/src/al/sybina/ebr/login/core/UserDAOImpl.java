/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package al.sybina.ebr.login.core;


import java.util.List;

import javax.inject.Inject;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;
import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.security.entities.User;
import com.widee.base.dao.base.impl.GenericDAOImpl;


@Component (name="UserDAOImpl")
public class UserDAOImpl extends GenericDAOImpl<User, Long> implements UserDAO {

	public UserDAOImpl() {
		super(User.class);
	}

	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public User findByUsername(String username) {
		return (User) getFinder().by("username").eq(username).result();
	}

	@Override
	public User create(User user) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		User createdUser = super.create(user);
		entityTransaction.commit();
		return createdUser;
	}

	@Override
	public void deleteUser(Long userId) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		super.delete(userId);
		entityTransaction.commit();
	}

	@Override
	public User update(User user) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		User updatedUser = super.update(user);
		entityTransaction.commit();
		return updatedUser;
	}
	
	@Override
	public List<User> getAllCacheStoreRefresh() {
		Query query = getEntityManager().createQuery("from " + this.persistentClass.getName() + " pc").setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
		return query.getResultList();
	}
	
}

