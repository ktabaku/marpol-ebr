package com.oceanpro.core.dao.oceanManager.logbook.restHours;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.restHours.RestData;
import com.widee.base.dao.base.GenericDAO;

/**
 * Class name: RestDataDAO
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
public interface RestDataDAO extends GenericDAO<RestData, Long>{

	void setEntityManager(EntityManager em);
}
