package com.oceanpro.core.dao.oceanManager.logbook.restHours.impl;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.restHours.RestDataDAO;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.RestData;
import com.widee.base.dao.base.impl.GenericDAOImpl;

/**
 * Class name: RestDataDAOImpl
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
@Component (name="restDataDAO") // Il nome serve solo per i test con la classe TestDao
public class RestDataDAOImpl extends GenericDAOImpl<RestData, Long> implements RestDataDAO {

	public RestDataDAOImpl() {
		super(RestData.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}


