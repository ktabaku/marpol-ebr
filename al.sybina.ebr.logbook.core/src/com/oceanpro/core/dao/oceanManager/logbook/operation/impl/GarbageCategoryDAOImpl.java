package com.oceanpro.core.dao.oceanManager.logbook.operation.impl;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.operation.GarbageCategoryDAO;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.GarbageCategory;
import com.widee.base.dao.base.impl.GenericDAOImpl;

/**
 * Class name: GarbageCategoryDAOImpl
 *
 * Description: 
 * 
 *
 * Company: 
 *
 * @author Gio
 * @date 30/jan/2012
 *
 */
@Component (name="GarbageCategoryDAO") // Il nome serve solo per i test con la classe TestDao
public class GarbageCategoryDAOImpl extends GenericDAOImpl<GarbageCategory, Long> implements GarbageCategoryDAO {

	public GarbageCategoryDAOImpl() {
		super(GarbageCategory.class);
	}
	
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}


