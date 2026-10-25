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


import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.security.entities.Role;
import com.widee.base.dao.base.impl.GenericDAOImpl;


@Component (name="UserDAOImpl")
public class RoleDAOImpl extends GenericDAOImpl<Role, Long> implements RoleDAO {

	public RoleDAOImpl() {
		super(Role.class);
	}

	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public Role findByName(String name) {
		return (Role) getFinder().by("name").eq(name).result();
	}

	@Override
	public Role findByDescription(String description) {
		return (Role) getFinder().by("description").eq(description).result();
	}
}

