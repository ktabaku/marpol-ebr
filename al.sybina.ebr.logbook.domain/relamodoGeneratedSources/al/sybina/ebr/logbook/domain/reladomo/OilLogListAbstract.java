package al.sybina.ebr.logbook.domain.reladomo;
import java.util.*;
import com.gs.fw.common.mithra.MithraList;
import com.gs.fw.common.mithra.MithraManager;
import com.gs.fw.common.mithra.MithraManagerProvider;
import com.gs.fw.common.mithra.finder.*;
import com.gs.fw.common.mithra.list.*;
import com.gs.fw.common.mithra.list.merge.TopLevelMergeOptions;
import com.gs.fw.finder.OrderBy;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gs.fw.common.mithra.*;
import com.gs.fw.common.mithra.attribute.*;
import com.gs.fw.common.mithra.util.*;
import com.gs.fw.common.mithra.notification.*;
import com.gs.fw.common.mithra.notification.listener.*;
import com.gs.fw.common.mithra.list.cursor.Cursor;
import com.gs.fw.common.mithra.bulkloader.*;
/**
* This file was automatically generated using Mithra 17.1.4. Please do not modify it.
* Add custom logic to its subclass instead.
*/
// Generated from templates/transactional/ListAbstract.jsp
public class OilLogListAbstract extends DelegatingList<OilLog> implements MithraDatedTransactionalList<OilLog>
{
	public OilLogListAbstract()
	{
		super();
		this.setDelegated(AbstractTransactionalNonOperationBasedList.DEFAULT);
		AbstractTransactionalNonOperationBasedList.DEFAULT.init(this);
	}

	public OilLogListAbstract(int initialSize)
	{
		super();
		this.setDelegated(AbstractTransactionalNonOperationBasedList.DEFAULT);
		AbstractTransactionalNonOperationBasedList.DEFAULT.init(this, initialSize);
	}

	public OilLogListAbstract(Collection c)
	{
		super();
		this.setDelegated(AbstractTransactionalNonOperationBasedList.DEFAULT);
		AbstractTransactionalNonOperationBasedList.DEFAULT.init(this, c);
	}

	public OilLogListAbstract(com.gs.fw.finder.Operation operation)
	{
		super(operation);
		this.setDelegated(AbstractTransactionalOperationBasedList.DEFAULT);
	}

	public OilLog[] elements()
	{
		OilLog[] result = new OilLog[size()];
		this.zGetDelegated().toArray(this, result);
		return result;
	}

	public OilLogList intersection(OilLogList another)
	{
		return (OilLogList)super.intersection(another);
	}

	public OilLog getOilLogAt(int index)
	{
		return (OilLog)this.get(index);
	}

	public MithraObjectPortal getMithraObjectPortal()
	{
		return OilLogFinder.getMithraObjectPortal();
	}

	public OilLogList getNonPersistentCopy()
	{
		OilLogList result = new OilLogList();
		zCopyNonPersistentInto(result);
		return result;
	}

	public OilLogList asAdhoc()
	{
		return (OilLogList) super.asAdhoc();
	}

	public MithraDatedTransactionalList getNonPersistentGenericCopy()
	{
		return this.getNonPersistentCopy();
	}

	/**
	* Return a view of this list that implements GS Collections MutableList API.
	* Since the returned list will be operation-based, it is effectively read-only,
	* so mutating methods will throw a RuntimeException.
	* (Implemented by a light-weight adapter, not a copy)
	*/
	public com.gs.collections.api.list.MutableList<OilLog> asGscList()
	{
		return com.gs.collections.impl.list.mutable.ListAdapter.adapt(this);
	}

	public OilLogList merge(MithraTransactionalList<OilLog> incoming, TopLevelMergeOptions<OilLog> mergeOptions)
	{
		return (OilLogList) super.merge(incoming, mergeOptions);
	}

	public OilLogList getDetachedCopy()
	{
		OilLogList result = new OilLogList();
		zDetachInto(result);
		return result;
	}

	public void zMakeDetached(Operation op, Object previousDetachedList)
	{
		super.zMakeDetached(op, previousDetachedList);
	}

	protected void generateAndSetPrimaryKeys()
	{
	}

	public void setId(long newValue)
	{
		zSetLong(OilLogFinder.id(), newValue);
	}

	public void setUuid(String newValue)
	{
		zSetString(OilLogFinder.uuid(), newValue);
	}

	public void setShip(long newValue)
	{
		zSetLong(OilLogFinder.ship(), newValue);
	}

	public void setType(String newValue)
	{
		zSetString(OilLogFinder.type(), newValue);
	}

	public void setCodeLetter(String newValue)
	{
		zSetString(OilLogFinder.codeLetter(), newValue);
	}

	public void setCodeNumber(String newValue)
	{
		zSetString(OilLogFinder.codeNumber(), newValue);
	}

	public void setContent(String newValue)
	{
		zSetString(OilLogFinder.content(), newValue);
	}

	public void setSignature(String newValue)
	{
		zSetString(OilLogFinder.signature(), newValue);
	}

	public void setGroupUuid(String newValue)
	{
		zSetString(OilLogFinder.groupUuid(), newValue);
	}

	public void setOrderInGroup(String newValue)
	{
		zSetString(OilLogFinder.orderInGroup(), newValue);
	}

	public void setSignerRank(String newValue)
	{
		zSetString(OilLogFinder.signerRank(), newValue);
	}

	public void setSignerName(String newValue)
	{
		zSetString(OilLogFinder.signerName(), newValue);
	}

	public void setSignerSurname(String newValue)
	{
		zSetString(OilLogFinder.signerSurname(), newValue);
	}

	public void setNote(String newValue)
	{
		zSetString(OilLogFinder.note(), newValue);
	}

	public void setCorrectionNote(String newValue)
	{
		zSetString(OilLogFinder.correctionNote(), newValue);
	}

	public void setMasterSignerName(String newValue)
	{
		zSetString(OilLogFinder.masterSignerName(), newValue);
	}

	public void setMasterSignerSurname(String newValue)
	{
		zSetString(OilLogFinder.masterSignerSurname(), newValue);
	}

	public void setMasterSignature(String newValue)
	{
		zSetString(OilLogFinder.masterSignature(), newValue);
	}

	public void setMasterSigDate(Date newValue)
	{
		zSetDate(OilLogFinder.masterSigDate(), newValue);
	}

	public void setSignatureGroupUuid(String newValue)
	{
		zSetString(OilLogFinder.signatureGroupUuid(), newValue);
	}

	public void setAttachmentName(String newValue)
	{
		zSetString(OilLogFinder.attachmentName(), newValue);
	}

	public void setAttachmentData(byte[] newValue)
	{
		zSetByteArray(OilLogFinder.attachmentData(), newValue);
	}

	public void setBusinessDateFrom(Timestamp newValue)
	{
		zSetTimestamp(OilLogFinder.businessDateFrom(), newValue);
	}

	public void setBusinessDateTo(Timestamp newValue)
	{
		zSetTimestamp(OilLogFinder.businessDateTo(), newValue);
	}

	public void setProcessingDateFrom(Timestamp newValue)
	{
		zSetTimestamp(OilLogFinder.processingDateFrom(), newValue);
	}

	public void setProcessingDateTo(Timestamp newValue)
	{
		zSetTimestamp(OilLogFinder.processingDateTo(), newValue);
	}

	public void setShipNull()
	{
		zSetAttributeNull(OilLogFinder.ship());
	}

	public void purgeAll()
	{
		super.purgeAll();
	}

	public void terminateAll()
	{
		super.terminateAll();
	}
}
