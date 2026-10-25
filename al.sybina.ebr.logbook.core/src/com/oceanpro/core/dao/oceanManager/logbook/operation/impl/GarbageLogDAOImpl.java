package com.oceanpro.core.dao.oceanManager.logbook.operation.impl;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.operation.GarbageLogDAO;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.GarbageLog;
import com.widee.base.dao.base.impl.GenericDAOImpl;

/**
 * Class name: GarbageLogDAOImpl
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
@Component (name="GarbageLogDAO") // Il nome serve solo per i test con la classe TestDao
public class GarbageLogDAOImpl extends GenericDAOImpl<GarbageLog, Long> implements GarbageLogDAO {

	public GarbageLogDAOImpl() {
		super(GarbageLog.class);
	}
	
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}


