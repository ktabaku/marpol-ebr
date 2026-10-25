package com.oceanpro.core.dao.oceanManager.logbook;

import com.widee.base.dao.base.GenericDAO;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.GmdssAnnex;


public interface GmdssAnnexDAO extends GenericDAO<GmdssAnnex, Long>{

	void setEntityManager(EntityManager em);

}
