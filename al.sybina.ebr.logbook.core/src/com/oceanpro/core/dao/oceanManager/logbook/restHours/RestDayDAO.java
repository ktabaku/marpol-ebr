package com.oceanpro.core.dao.oceanManager.logbook.restHours;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.restHours.RestDay;
import com.widee.base.dao.base.GenericDAO;

/**
 * Class name: RestDayDAO
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
public interface RestDayDAO extends GenericDAO<RestDay, Long>{

	void setEntityManager(EntityManager em);
}
