package com.oceanpro.core.dao.oceanManager.logbook.restHours.impl;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.restHours.CrewForRestHoursDAO;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.CrewForRestHours;
import com.widee.base.dao.base.impl.GenericDAOImpl;

/**
 * Class name: CrewForRestHoursDAOImpl
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
@Component (name="crewForRestHoursDAO") // Il nome serve solo per i test con la classe TestDao
public class CrewForRestHoursDAOImpl extends GenericDAOImpl<CrewForRestHours, Long> implements CrewForRestHoursDAO {

	public CrewForRestHoursDAOImpl() {
		super(CrewForRestHours.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}


