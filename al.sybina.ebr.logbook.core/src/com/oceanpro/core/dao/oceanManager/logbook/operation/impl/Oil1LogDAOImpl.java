package com.oceanpro.core.dao.oceanManager.logbook.operation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.config.QueryHints;
import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.operation.Oil1LogDAO;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.OilLog;
import com.widee.base.dao.base.impl.GenericDAOImpl;

/**
 * Class name: Oil1LogDAOImpl
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
@Component (name="Oil1LogDAOImpl") // Il nome serve solo per i test con la classe TestDao
public class Oil1LogDAOImpl extends GenericDAOImpl<OilLog, Long> implements Oil1LogDAO {

	public Oil1LogDAOImpl() {
		super(OilLog.class);
	}
	
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public Map<String, List<OilLog>> getAllOil1LogByGroup() {
		String q="from OilLog c group by c.groupUuid";
		TypedQuery<OilLog> query = getEntityManager().createQuery(q, OilLog.class);
		List<OilLog> result = query.getResultList();
		Map<String, List<OilLog>> resultMap = new HashMap<String, List<OilLog>>();
		for(OilLog oilLog : result) {
			List<OilLog> list = resultMap.get(oilLog.getGroupUuid());
			if(list==null) {
				List<OilLog> list2 = new ArrayList<OilLog>();
				list2.add(oilLog);
				resultMap.put(oilLog.getGroupUuid(), list2);
			} else {
				list.add(oilLog);
			}
		}
		return resultMap;
	}
	
	@Override
	public Map<String, List<OilLog>> getOil1LogBySignatureGroup(int fromIndex, int toIndex) {
		if(fromIndex>toIndex) return new HashMap<String, List<OilLog>>();
		String q="select distinct o.signatureGroupUuid from OilLog o";
		TypedQuery<String> query = getEntityManager().createQuery(q, String.class);
		query.setFirstResult(fromIndex);
		query.setMaxResults(toIndex-fromIndex);
		List<String> result = query.getResultList();
		return getOil1LogBySignatureGroup(result);
	}
	@Override
	public Map<String, List<OilLog>> getOil1LogBySignatureGroup(List<String> signatureGroupUUIDList) {
		if(signatureGroupUUIDList == null || signatureGroupUUIDList.isEmpty()) return new HashMap<String, List<OilLog>>();
		String q="from OilLog c where c.signatureGroupUuid in :signatureGroupUUIDList group by c.signatureGroupUuid";
		TypedQuery<OilLog> query = getEntityManager().createQuery(q, OilLog.class);
		query.setParameter("signatureGroupUUIDList", signatureGroupUUIDList);
		List<OilLog> result = query.getResultList();
		Map<String, List<OilLog>> resultMap = new HashMap<String, List<OilLog>>();
		for(OilLog oilLog : result) {
			List<OilLog> list = resultMap.get(oilLog.getSignatureGroupUuid());
			if(list==null) {
				List<OilLog> list2 = new ArrayList<OilLog>();
				list2.add(oilLog);
				resultMap.put(oilLog.getSignatureGroupUuid(), list2);
			} else {
				list.add(oilLog);
			}
		}
		return resultMap;
	}

	@Override
	public Map<String, List<OilLog>> getOil1LogByGroup(int fromIndex, int toIndex) {
		if(fromIndex>toIndex) return new HashMap<String, List<OilLog>>();
		String q="select distinct o.groupUuid from OilLog o";
		TypedQuery<String> query = getEntityManager().createQuery(q, String.class);
		query.setFirstResult(fromIndex);
		query.setMaxResults(toIndex-fromIndex);
		List<String> result = query.getResultList();
		return getOil1LogByGroup(result);
	}
	@Override
	public Map<String, List<OilLog>> getOil1LogByGroup(List<String> groupUUIDList) {
		if(groupUUIDList == null || groupUUIDList.isEmpty()) return new HashMap<String, List<OilLog>>();
		String q="from OilLog c where c.groupUuid in :groupUUIDList group by c.groupUuid";
		TypedQuery<OilLog> query = getEntityManager().createQuery(q, OilLog.class);
		query.setParameter("groupUUIDList", groupUUIDList);
		List<OilLog> result = query.getResultList();
		Map<String, List<OilLog>> resultMap = new HashMap<String, List<OilLog>>();
		for(OilLog oilLog : result) {
			List<OilLog> list = resultMap.get(oilLog.getGroupUuid());
			if(list==null) {
				List<OilLog> list2 = new ArrayList<OilLog>();
				list2.add(oilLog);
				resultMap.put(oilLog.getGroupUuid(), list2);
			} else {
				list.add(oilLog);
			}
		}
		return resultMap;
	}
	
	@Override
	public void deleteGroupByUuid(String groupUUID) {
		String q="delete from OilLog c where c.groupUuid=:groupUuid";
		TypedQuery<OilLog> query = getEntityManager().createQuery(q, OilLog.class);
		query.setParameter("groupUuid", groupUUID);
		query.executeUpdate();
	}

	@Override
	public OilLog getLastLog(String oilLogNumber, String codeLetter, String codeNumber) {
		String q="from OilLog c where c.type=:oilLogNumber and c.codeLetter=:codeLetter and c.codeNumber=:codeNumber order by c.insertDate DESC";
		TypedQuery<OilLog> query = getEntityManager().createQuery(q, OilLog.class);
		query.setParameter("oilLogNumber", oilLogNumber);
		query.setParameter("codeLetter", codeLetter);
		query.setParameter("codeNumber", codeNumber);
		query.setMaxResults(1);
		List<OilLog> result = query.getResultList();
		if(result.size()>0) return result.get(0);
		else return null;
	}

	@Override
	public int getAllOil1LogByGroupSize() {
		String q = "select count(distinct o.groupUuid) from OilLog o";
		return ((Long)getEntityManager().createQuery(q).getSingleResult()).intValue();
	}

	@Override
	public int getAllOil1LogBySignatureGroupSize() {
		String q = "select count(distinct o.signatureGroupUuid) from OilLog o";
		return ((Long)getEntityManager().createQuery(q).getSingleResult()).intValue();
	}
	
	@Override
	public OilLog getLastLog() {
		String q="from OilLog c order by c.date DESC";
		TypedQuery<OilLog> query = getEntityManager().createQuery(q, OilLog.class);
		query.setMaxResults(1);
		List<OilLog> result = query.getResultList();
		if(result.size()>0) return result.get(0);
		else return null;
	}

	@Override
	public OilLog getLog(String oilLogNumber, String groupUUID, String codeLetter, String codeNumber) {
		String q="from OilLog c where c.type=:oilLogNumber and c.groupUuid=:groupUuid and c.codeLetter=:codeLetter and c.codeNumber=:codeNumber order by c.insertDate DESC";
		TypedQuery<OilLog> query = getEntityManager().createQuery(q, OilLog.class);
		query.setParameter("oilLogNumber", oilLogNumber);
		query.setParameter("groupUuid", groupUUID);
		query.setParameter("codeLetter", codeLetter);
		query.setParameter("codeNumber", codeNumber);
		query.setMaxResults(1);
		List<OilLog> result = query.getResultList();
		if(result.size()>0) return result.get(0);
		else return null;
	}


}


