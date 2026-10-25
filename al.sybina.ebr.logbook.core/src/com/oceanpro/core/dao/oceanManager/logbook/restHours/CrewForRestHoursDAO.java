package com.oceanpro.core.dao.oceanManager.logbook.restHours;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.restHours.CrewForRestHours;
import com.widee.base.dao.base.GenericDAO;

/**
 * Class name: CrewForRestHoursDAO
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
public interface CrewForRestHoursDAO extends GenericDAO<CrewForRestHours, Long>{

	void setEntityManager(EntityManager em);
}
