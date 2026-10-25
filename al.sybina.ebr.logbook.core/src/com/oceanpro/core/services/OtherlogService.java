package com.oceanpro.core.services;

import java.util.List;

import com.oceanpro.core.entity.oceanManager.logbook.Refrigerant;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.AlcoholTest;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.DrugTest;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.OzoneA;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.OzoneB;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.Visitor;
import com.oceanpro.core.exception.DocumentException;
import com.widee.base.hibernate.criteria.Finder;


public interface OtherlogService {


	
	//VISITOR
	public List<Visitor> getAllVisitor();	
	public Visitor getVisitor(Long idVisitor) throws DocumentException;
	public Visitor updateVisitor(Visitor visitor);
	public Visitor addVisitor(Visitor visitor);
	public Finder<Visitor> getVisitorFinder();
	
	public Visitor getVisitorByUuid(String uuid);
	
	//ALCOHOL TEST
	public List<AlcoholTest> getAllAlcoholTest();	
	public AlcoholTest getAlcoholTest(Long idAlcoholTest) throws DocumentException;
	public AlcoholTest updateAlcoholTest(AlcoholTest alcoholTest);
	public AlcoholTest addAlcoholTest(AlcoholTest alcoholTest);
	public Finder<AlcoholTest> getAlcoholTestFinder();
	
	public AlcoholTest getAlcoholTestByUuid(String uuid);
	
	//OZONE A
	public List<OzoneA> getAllOzoneA();	
	public OzoneA getOzoneA(Long idOzoneA) throws DocumentException;
	public OzoneA updateOzoneA(OzoneA ozoneA);
	public OzoneA addOzoneA(OzoneA ozoneA);
	public Finder<OzoneA> getOzoneAFinder();
	
	public OzoneA getOzoneAByUuid(String uuid);
	
	//OZONE B
	public List<OzoneB> getAllOzoneB();	
	public OzoneB getOzoneB(Long idOzoneB) throws DocumentException;
	public OzoneB updateOzoneB(OzoneB ozoneB);
	public OzoneB addOzoneB(OzoneB ozoneB);
	public Finder<OzoneB> getOzoneBFinder();

	public OzoneB getOzoneBByUuid(String uuid);
	
	//DRUG TEST
	public List<DrugTest> getAllDrugTest();	
	public DrugTest getDrugTest(Long idDrugTest) throws DocumentException;
	public DrugTest updateDrugTest(DrugTest drugTest);
	public DrugTest addDrugTest(DrugTest drugTest);
	public Finder<DrugTest> getDrugTestFinder();
	
	public DrugTest getDrugTestByUuid(String uuid);
	
}
