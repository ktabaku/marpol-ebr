package com.oceanpro.core.dao.oceanManager.logbook.restHours.impl;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.restHours.ManouvringDAO;
import com.oceanpro.core.dao.oceanManager.logbook.restHours.RestDataDAO;
import com.oceanpro.core.dao.oceanManager.logbook.restHours.RestDayDAO;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.Manouvring;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.RestData;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.RestDay;
import com.widee.base.dao.base.impl.GenericDAOImpl;

/**
 * Class name: ManouvringDAOImpl
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
@Component (name="manouvringDAO") // Il nome serve solo per i test con la classe TestDao
public class ManouvringDAOImpl extends GenericDAOImpl<Manouvring, Long> implements ManouvringDAO {

	public ManouvringDAOImpl() {
		super(Manouvring.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}


