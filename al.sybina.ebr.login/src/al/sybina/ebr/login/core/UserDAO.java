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

import javax.persistence.EntityManager;

import com.oceanpro.core.security.entities.User;
import com.widee.base.dao.base.GenericDAO;

/**
 * Data Access Object for User related operations.
 */
public interface UserDAO extends GenericDAO<User, Long>{
	void setEntityManager(EntityManager em);

	User findByUsername(String username);

//    User getUser(Long userId);
//
//    User findUser(String username);
//
    User create(User user);
//
//    List<User> getAllUsers();
//
    void deleteUser(Long userId);
//
    @Override
    User update(User user);

	List<User> getAllCacheStoreRefresh();
}
