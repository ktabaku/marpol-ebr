package com.oceanpro.core.dao.oceanManager.logbook.operation;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.operationLog.OilLog;
import com.widee.base.dao.base.GenericDAO;

/**
 * Class name: Oil1LogDAO
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
public interface Oil1LogDAO extends GenericDAO<OilLog, Long>{
	void setEntityManager(EntityManager em);
	Map<String, List<OilLog>> getAllOil1LogByGroup();
	Map<String, List<OilLog>> getOil1LogBySignatureGroup(int fromIndex, int toIndex);
	Map<String, List<OilLog>> getOil1LogBySignatureGroup(List<String> signatureGroupUUIDList);
	Map<String, List<OilLog>> getOil1LogByGroup(int fromIndex, int toIndex);
	Map<String, List<OilLog>> getOil1LogByGroup(List<String> groupUUIDList);
	void deleteGroupByUuid(String groupUUID);
	OilLog getLastLog(String oilLogNumber, String codeLetter, String codeNumber);
	int getAllOil1LogByGroupSize();
	OilLog getLastLog();
	OilLog getLog(String oilLogNumber, String groupUUID, String codeLetter, String codeNumber);
	int getAllOil1LogBySignatureGroupSize();
	
	
}
