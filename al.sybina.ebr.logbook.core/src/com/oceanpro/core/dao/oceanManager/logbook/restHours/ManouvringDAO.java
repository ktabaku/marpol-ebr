package com.oceanpro.core.dao.oceanManager.logbook.restHours;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.restHours.Manouvring;
import com.widee.base.dao.base.GenericDAO;

/**
 * Class name: ManouvringDAO
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
public interface ManouvringDAO extends GenericDAO<Manouvring, Long>{

	void setEntityManager(EntityManager em);
}
