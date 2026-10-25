package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.PermanentNightOrderDAO;
import com.oceanpro.core.entity.oceanManager.logbook.PermanentNightOrder;
import com.widee.base.dao.base.impl.GenericDAOImpl;



@Component (name="permanentOrderDao") // Il nome serve solo per i test con la classe TestDao
public class PermanentNightOrderDAOImpl extends GenericDAOImpl<PermanentNightOrder, Long> implements PermanentNightOrderDAO {

	public PermanentNightOrderDAOImpl() {
		super(PermanentNightOrder.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
