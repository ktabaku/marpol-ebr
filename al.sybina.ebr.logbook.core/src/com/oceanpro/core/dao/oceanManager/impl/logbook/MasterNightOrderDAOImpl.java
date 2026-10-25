package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.MasterNightOrderDAO;
import com.oceanpro.core.entity.oceanManager.logbook.MasterNightOrder;

@Component (name="masterOrderDao") // Il nome serve solo per i test con la classe TestDao
public class MasterNightOrderDAOImpl extends GenericDAOImpl<MasterNightOrder, Long> implements MasterNightOrderDAO {

	
	public MasterNightOrderDAOImpl() {
		super(MasterNightOrder.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
