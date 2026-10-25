package com.oceanpro.core.dao.oceanManager.logbook;

import com.widee.base.dao.base.GenericDAO;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.otherLog.OzoneA;


public interface OzoneADAO extends GenericDAO<OzoneA, Long>{

	void setEntityManager(EntityManager em);

}
