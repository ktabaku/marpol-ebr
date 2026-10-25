package com.oceanpro.core.services;

import java.util.List;

import com.oceanpro.core.entity.oceanManager.logbook.GmdssAnnex;
import com.oceanpro.core.entity.oceanManager.logbook.GmdssSecB;
import com.oceanpro.core.entity.oceanManager.logbook.GmdssSecC;
import com.oceanpro.core.entity.oceanManager.logbook.Vhf;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.OzoneA;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.Visitor;
import com.oceanpro.core.exception.DocumentException;
import com.widee.base.hibernate.criteria.Finder;


public interface CommunicationlogService {


	//VHF
	public List<Vhf> getAllVhf();
	public Vhf getVhf(Long idVhf) throws DocumentException;
	public Vhf updateVhf(Vhf vhf);
	public Vhf addVhf(Vhf deckVhf);
	public Vhf getVhfByUuid(String uuid);
	
	public Finder<Vhf> getVhfFinder();
	
	//GMDSS SEC B 
	public List<GmdssSecB> getAllGmdssB();
	public GmdssSecB getGmdssb(Long idGmdssb) throws DocumentException;
	public GmdssSecB updateGmdssb(GmdssSecB gmdssb);
	public GmdssSecB addGmdssb(GmdssSecB gmdssb);	
	public GmdssSecB getGmdssSecBByUuid(String uuid);
	
	public Finder<GmdssSecB> getGmdssSecBFinder();
	
	//GMDSS SEC C
	public List<GmdssSecC> getAllGmdssC();
	public GmdssSecC getGmdssc(Long idGmdssc) throws DocumentException;
	public GmdssSecC updateGmdssc(GmdssSecC gmdssc);
	public GmdssSecC addGmdssc(GmdssSecC gmdssc);	
	public GmdssSecC getGmdssSecCByUuid(String uuid);
	
	public Finder<GmdssSecC> getGmdssSecCFinder();
	
	//GMDSS ANNEX
	public List<GmdssAnnex> getAllGmdssAnnex();
	public GmdssAnnex getGmdssAnnex(Long idGmdssAnnex) throws DocumentException;
	public GmdssAnnex updateGmdssAnnex(GmdssAnnex gmdssAnnex);
	public GmdssAnnex addGmdssAnnex(GmdssAnnex gmdssAnnex);	
	public GmdssAnnex getGmdssAnnexByUuid(String uuid);
	
	public Finder<GmdssAnnex> getGmdssAnnexFinder();
	
}
