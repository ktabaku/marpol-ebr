package al.sybina.ebr.logbook.domain.reladomo;
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
import com.gs.fw.common.mithra.MithraObject;
import com.gs.fw.common.mithra.MithraDataObject;
import com.gs.fw.common.mithra.cache.Cache;
import com.gs.fw.common.mithra.extractor.*;
import com.gs.fw.common.mithra.finder.*;
import com.gs.fw.common.mithra.list.*;
import com.gs.fw.common.mithra.transaction.MithraObjectPersister;
import java.util.Arrays;
import java.util.HashSet;
import com.gs.fw.common.mithra.behavior.*;
import com.gs.fw.common.mithra.behavior.txparticipation.MithraOptimisticLockException;
import com.gs.fw.common.mithra.behavior.state.DatedPersistenceState;
import com.gs.fw.common.mithra.behavior.state.DatedPersistedState;
import com.gs.fw.common.mithra.attribute.update.*;
import com.gs.fw.common.mithra.util.StatisticCounter;
/**
* This file was automatically generated using Mithra 17.1.4. Please do not modify it.
* Add custom logic to its subclass instead.
*/
// Generated from templates/datedtransactional/Abstract.jsp
public abstract class OilLogAbstract extends com.gs.fw.common.mithra.superclassimpl.MithraDatedTransactionalObjectImpl implements Serializable
{
	private static TemporalDirector temporalDirector = new GenericBiTemporalDirector(
		OilLogFinder.businessDate()
	,
		OilLogFinder.processingDate()
	, OilLogFinder.zGetDoubleAttributes(),OilLogFinder.zGetBigDecimalAttributes());
	protected transient Timestamp businessDate;
	protected transient Timestamp processingDate;
	private static final Logger logger = LoggerFactory.getLogger(OilLog.class.getName());
	private static byte MEMORY_STATE = DatedPersistenceState.IN_MEMORY;
	private static byte PERSISTED_STATE = DatedPersistenceState.PERSISTED;
	public OilLogAbstract(Timestamp businessDate
	, Timestamp processingDate
	)
	{
		this.businessDate = TimestampPool.getInstance().getOrAddToCache(businessDate, OilLogFinder.isFullCache());
		this.processingDate = TimestampPool.getInstance().getOrAddToCache(processingDate, OilLogFinder.isFullCache());
		this.persistenceState = MEMORY_STATE;
	}

	public OilLogAbstract(Timestamp businessDate)
	{
		this(businessDate, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate());
	}

	public OilLogData zSynchronizedGetData()
	{
		return (OilLogData) super.zSynchronizedGetData();
	}

	protected boolean checkAsOfAttributesForRefresh(MithraDataObject current)
	{
		boolean refresh = !OilLogFinder.businessDate().dataMatches(current, this.businessDate);
		if (!refresh)
		{
			refresh = !OilLogFinder.processingDate().dataMatches(current, this.processingDate);
		}

		return refresh;
	}

	public TemporalDirector zGetTemporalDirector()
	{
		return temporalDirector;
	}

	public void insert() throws MithraBusinessException
	{
		DatedTransactionalBehavior behavior = this.zGetTransactionalBehaviorForWrite();
		this.checkAndGeneratePrimaryKeys();
		behavior.insert(this);
	}

	public void insertUntil(Timestamp exclusiveUntil) throws MithraBusinessException
	{
		DatedTransactionalBehavior behavior = this.zGetTransactionalBehaviorForWrite();
		checkAndGeneratePrimaryKeys();
		behavior.insertUntil(this, exclusiveUntil);
	}

	public void insertForRecovery() throws MithraBusinessException
	{
		DatedTransactionalBehavior behavior = this.zGetTransactionalBehaviorForWrite();
		checkAndGeneratePrimaryKeys();
		behavior.insertForRecovery(this);
	}

	public void insertWithIncrement() throws MithraBusinessException
	{
		DatedTransactionalBehavior behavior = this.zGetTransactionalBehaviorForWrite();
		this.checkAndGeneratePrimaryKeys();
		behavior.insertWithIncrement(this);
	}

	public void insertWithIncrementUntil(Timestamp exclusiveUntil) throws MithraBusinessException
	{
		DatedTransactionalBehavior behavior = this.zGetTransactionalBehaviorForWrite();
		this.checkAndGeneratePrimaryKeys();
		behavior.insertWithIncrementUntil(this, exclusiveUntil);
	}

	protected void cascadeInsertImpl() throws MithraBusinessException
	{
		DatedTransactionalBehavior _behavior = zGetTransactionalBehaviorForWrite();
		this.checkAndGeneratePrimaryKeys();
		_behavior.insert(this);
	}

	@Override
	public Map< RelatedFinder, StatisticCounter > zAddNavigatedRelationshipsStats(RelatedFinder finder, Map< RelatedFinder, StatisticCounter > navigationStats)
	{
		DatedTransactionalBehavior _behavior = zGetTransactionalBehaviorForWrite();
		_behavior.addNavigatedRelationshipsStats(this, finder, navigationStats);
		return navigationStats;
	}

	@Override
	public Map< RelatedFinder, StatisticCounter > zAddNavigatedRelationshipsStatsForUpdate(RelatedFinder parentFinderGeneric, Map< RelatedFinder, StatisticCounter > navigationStats)
	{
		return navigationStats;
	}

	@Override
	public Map< RelatedFinder, StatisticCounter > zAddNavigatedRelationshipsStatsForDelete(RelatedFinder parentFinder, Map< RelatedFinder, StatisticCounter > navigationStats)
	{
		return navigationStats;
	}

	public OilLog zCascadeCopyThenInsert() throws MithraBusinessException
	{
		DatedTransactionalBehavior _behavior = zGetTransactionalBehaviorForWrite();
		this.checkAndGeneratePrimaryKeys();
		OilLog original = (OilLog) _behavior.copyThenInsert(this);
		return original;
	}

	public void cascadeInsertUntil(Timestamp exclusiveUntil) throws MithraBusinessException
	{
		this.insertUntil(exclusiveUntil);
	}

	public OilLog copyDetachedValuesToOriginalOrInsertIfNewUntil(Timestamp exclusiveUntil)
	{
		return (OilLog) this.zCopyDetachedValuesToOriginalOrInsertIfNewUntil(exclusiveUntil);
	}

	public OilLog copyDetachedValuesToOriginalOrInsertIfNewUntilImpl(Timestamp exclusiveUntil, MithraTransaction tx)
	{
		DatedTransactionalBehavior behavior = zGetTransactionalBehaviorForWrite();
		return (OilLog) behavior.updateOriginalOrInsertUntil(this, exclusiveUntil);
	}

	protected DatedTransactionalState zCreateDatedTransactionalState(TemporalContainer container, MithraDataObject data, MithraTransaction threadTx)
	{
		return new DatedTransactionalState(threadTx,
			this.persistenceState, container, data, businessDate,
			OilLogFinder.processingDate().getInfinityDate().equals(this.processingDate) || threadTx.isInFuture(this.processingDate.getTime()));
	}

	public OilLog getNonPersistentCopy() throws MithraBusinessException
	{
		DatedTransactionalBehavior behavior = this.zGetTransactionalBehavior();
		MithraDataObject data = behavior.getCurrentDataForRead(this);
		MithraDataObject newData = data.copy(!behavior.isPersisted());
		Timestamp[] asOfAttributes = new Timestamp[2];
		asOfAttributes[0] = this.businessDate;
		asOfAttributes[1] = this.processingDate;
		OilLogAbstract result = (OilLogAbstract)
		((MithraDatedObjectFactory) this.zGetPortal().getMithraDatedObjectFactory()).createObject(newData, asOfAttributes);
		result.zSetNonTxPersistenceState(MEMORY_STATE);
		if (result.transactionalState != null)
		{
			result.zSetTxPersistenceState(DatedPersistenceState.IN_MEMORY);
		}

		return (OilLog ) result;
	}

	public OilLog zFindOriginal()
	{
		OilLogData data = (OilLogData) this.currentData;
		Operation op;
		op = OilLogFinder.id().eq(data.getId());
		op = op.and(OilLogFinder.businessDate().eq(this.getBusinessDate()));
		op = op.and(OilLogFinder.processingDate().eq(this.getProcessingDate()));
		return OilLogFinder.findOne(op);
	}

	public OilLog getDetachedCopy() throws MithraBusinessException
	{
		DatedTransactionalBehavior behavior = this.zGetTransactionalBehavior();
		Timestamp[] asOfAttributes = new Timestamp[2];
		asOfAttributes[0] = this.businessDate;
		asOfAttributes[1] = this.processingDate;
		OilLogAbstract result = (OilLogAbstract) behavior.getDetachedCopy(this, asOfAttributes);
		if (result.transactionalState != null)
		{
			result.zSetTxPersistenceState(DatedPersistenceState.DETACHED);
		}

		return (OilLog ) result;
	}

	public boolean isModifiedSinceDetachmentByDependentRelationships()
	{
		if(this.isModifiedSinceDetachment()) return true;
		return false;
	}

	public OilLog copyDetachedValuesToOriginalOrInsertIfNewImpl(MithraTransaction tx)
	{
		DatedTransactionalBehavior behavior = this.zGetTransactionalBehaviorForWrite();
		return (OilLog) behavior.updateOriginalOrInsert(this);
	}

	public OilLog copyDetachedValuesToOriginalOrInsertIfNew()
	{
		return (OilLog) this.zCopyDetachedValuesToOriginalOrInsertIfNew();
	}

	public Object readResolve() throws ObjectStreamException
	{
		if (this.persistenceState == DatedPersistenceState.PERSISTED)
		{
			OilLogData data = (OilLogData) this.currentData;
			Operation op = OilLogFinder.businessDate().eq(this.businessDate);
			op = op.and(OilLogFinder.processingDate().eq(this.processingDate));
			op = op.and(OilLogFinder.id().eq(data.getId()));
			return OilLogFinder.findOne(op);
		}

		return this;
	}

	public boolean zHasSameNullPrimaryKeyAttributes(MithraTransactionalObject other)
	{
		return true;
	}

	protected boolean zMustCheckCurrent(MithraDataObject d)
	{
		OilLogData data = (OilLogData) d;
		Timestamp businessTo = data.getBusinessDateTo();
		return businessTo != null && OilLogFinder.businessDate().getInfinityDate().getTime() == businessTo.getTime();
	}

	protected boolean zMustCheckCurrent(MithraDataObject d, Timestamp exclusiveUntil)
	{
		OilLogData data = (OilLogData) d;
		Timestamp businessTo = data.getBusinessDateTo();
		return businessTo != null && businessTo.getTime() >= exclusiveUntil.getTime();
	}

	public boolean isAttachmentDataNull()
	{
		return this.zSynchronizedGetData().isAttachmentDataNull();
	}

	public boolean isAttachmentNameNull()
	{
		return this.zSynchronizedGetData().isAttachmentNameNull();
	}

	public boolean isBusinessDateFromNull()
	{
		return this.zSynchronizedGetData().isBusinessDateFromNull();
	}

	public boolean isBusinessDateToNull()
	{
		return this.zSynchronizedGetData().isBusinessDateToNull();
	}

	public boolean isCodeLetterNull()
	{
		return this.zSynchronizedGetData().isCodeLetterNull();
	}

	public boolean isCodeNumberNull()
	{
		return this.zSynchronizedGetData().isCodeNumberNull();
	}

	public boolean isContentNull()
	{
		return this.zSynchronizedGetData().isContentNull();
	}

	public boolean isCorrectionNoteNull()
	{
		return this.zSynchronizedGetData().isCorrectionNoteNull();
	}

	public boolean isGroupUuidNull()
	{
		return this.zSynchronizedGetData().isGroupUuidNull();
	}

	public boolean isIdNull()
	{
		return this.zSynchronizedGetData().isIdNull();
	}

	public boolean isMasterSigDateNull()
	{
		return this.zSynchronizedGetData().isMasterSigDateNull();
	}

	public boolean isMasterSignatureNull()
	{
		return this.zSynchronizedGetData().isMasterSignatureNull();
	}

	public boolean isMasterSignerNameNull()
	{
		return this.zSynchronizedGetData().isMasterSignerNameNull();
	}

	public boolean isMasterSignerSurnameNull()
	{
		return this.zSynchronizedGetData().isMasterSignerSurnameNull();
	}

	public boolean isNoteNull()
	{
		return this.zSynchronizedGetData().isNoteNull();
	}

	public boolean isOrderInGroupNull()
	{
		return this.zSynchronizedGetData().isOrderInGroupNull();
	}

	public boolean isProcessingDateFromNull()
	{
		return this.zSynchronizedGetData().isProcessingDateFromNull();
	}

	public boolean isProcessingDateToNull()
	{
		return this.zSynchronizedGetData().isProcessingDateToNull();
	}

	public boolean isShipNull()
	{
		return this.zSynchronizedGetData().isShipNull();
	}

	public boolean isSignatureNull()
	{
		return this.zSynchronizedGetData().isSignatureNull();
	}

	public boolean isSignatureGroupUuidNull()
	{
		return this.zSynchronizedGetData().isSignatureGroupUuidNull();
	}

	public boolean isSignerNameNull()
	{
		return this.zSynchronizedGetData().isSignerNameNull();
	}

	public boolean isSignerRankNull()
	{
		return this.zSynchronizedGetData().isSignerRankNull();
	}

	public boolean isSignerSurnameNull()
	{
		return this.zSynchronizedGetData().isSignerSurnameNull();
	}

	public boolean isTypeNull()
	{
		return this.zSynchronizedGetData().isTypeNull();
	}

	public boolean isUuidNull()
	{
		return this.zSynchronizedGetData().isUuidNull();
	}

	public byte[] getAttachmentData()
	{
		return this.zSynchronizedGetData().getAttachmentData();
	}

	public void setAttachmentData(byte[] newValue)
	{
		zSetByteArray(OilLogFinder.attachmentData(), newValue, false );
	}

	public void setAttachmentDataUntil(byte[] newValue, Timestamp exclusiveUntil)
	{
		zSetByteArray(OilLogFinder.attachmentData(), newValue, false , exclusiveUntil);
	}

	public String getAttachmentName()
	{
		return this.zSynchronizedGetData().getAttachmentName();
	}

	public void setAttachmentName(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.attachmentName(), newValue, false );
	}

	public void setAttachmentNameUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.attachmentName(), newValue, false , exclusiveUntil);
	}

	public Timestamp getBusinessDateFrom()
	{
		return this.zSynchronizedGetData().getBusinessDateFrom();
	}

	public void setBusinessDateFrom(Timestamp newValue)
	{
		zSetTimestamp(OilLogFinder.businessDateFrom(), newValue, false );
	}

	public Timestamp getBusinessDateTo()
	{
		return this.zSynchronizedGetData().getBusinessDateTo();
	}

	public void setBusinessDateTo(Timestamp newValue)
	{
		zSetTimestamp(OilLogFinder.businessDateTo(), newValue, false );
	}

	public String getCodeLetter()
	{
		return this.zSynchronizedGetData().getCodeLetter();
	}

	public void setCodeLetter(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		if (newValue != null && newValue.length() > 1)
		{
			throw new MithraBusinessException("Attribute 'codeLetter' cannot exceed maximum length of 1: " + newValue);
		}

		zSetString(OilLogFinder.codeLetter(), newValue, false );
	}

	public void setCodeLetterUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		if (newValue != null && newValue.length() > 1)
		{
			throw new MithraBusinessException("Attribute 'codeLetter' cannot exceed maximum length of 1: " + newValue);
		}

		zSetString(OilLogFinder.codeLetter(), newValue, false , exclusiveUntil);
	}

	public String getCodeNumber()
	{
		return this.zSynchronizedGetData().getCodeNumber();
	}

	public void setCodeNumber(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		if (newValue != null && newValue.length() > 8)
		{
			throw new MithraBusinessException("Attribute 'codeNumber' cannot exceed maximum length of 8: " + newValue);
		}

		zSetString(OilLogFinder.codeNumber(), newValue, false );
	}

	public void setCodeNumberUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		if (newValue != null && newValue.length() > 8)
		{
			throw new MithraBusinessException("Attribute 'codeNumber' cannot exceed maximum length of 8: " + newValue);
		}

		zSetString(OilLogFinder.codeNumber(), newValue, false , exclusiveUntil);
	}

	public String getContent()
	{
		return this.zSynchronizedGetData().getContent();
	}

	public void setContent(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.content(), newValue, false );
	}

	public void setContentUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.content(), newValue, false , exclusiveUntil);
	}

	public String getCorrectionNote()
	{
		return this.zSynchronizedGetData().getCorrectionNote();
	}

	public void setCorrectionNote(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.correctionNote(), newValue, false );
	}

	public void setCorrectionNoteUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.correctionNote(), newValue, false , exclusiveUntil);
	}

	public String getGroupUuid()
	{
		return this.zSynchronizedGetData().getGroupUuid();
	}

	public void setGroupUuid(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.groupUuid(), newValue, false );
	}

	public void setGroupUuidUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.groupUuid(), newValue, false , exclusiveUntil);
	}

	public long getId()
	{
		return this.zSynchronizedGetData().getId();
	}

	public void setId(long newValue)
	{
		zSetLong(OilLogFinder.id(), newValue, true ,false);
	}

	public void setIdUntil(long newValue, Timestamp exclusiveUntil)
	{
		zSetLong(OilLogFinder.id(), newValue, true , exclusiveUntil,false);
	}

	public Date getMasterSigDate()
	{
		return this.zSynchronizedGetData().getMasterSigDate();
	}

	public void setMasterSigDate(Date newValue)
	{
		zSetDate(OilLogFinder.masterSigDate(), newValue, false );
	}

	public void setMasterSigDateUntil(Date newValue, Timestamp exclusiveUntil)
	{
		zSetDate(OilLogFinder.masterSigDate(), newValue, false , exclusiveUntil);
	}

	public String getMasterSignature()
	{
		return this.zSynchronizedGetData().getMasterSignature();
	}

	public void setMasterSignature(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.masterSignature(), newValue, false );
	}

	public void setMasterSignatureUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.masterSignature(), newValue, false , exclusiveUntil);
	}

	public String getMasterSignerName()
	{
		return this.zSynchronizedGetData().getMasterSignerName();
	}

	public void setMasterSignerName(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.masterSignerName(), newValue, false );
	}

	public void setMasterSignerNameUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.masterSignerName(), newValue, false , exclusiveUntil);
	}

	public String getMasterSignerSurname()
	{
		return this.zSynchronizedGetData().getMasterSignerSurname();
	}

	public void setMasterSignerSurname(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.masterSignerSurname(), newValue, false );
	}

	public void setMasterSignerSurnameUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.masterSignerSurname(), newValue, false , exclusiveUntil);
	}

	public String getNote()
	{
		return this.zSynchronizedGetData().getNote();
	}

	public void setNote(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.note(), newValue, false );
	}

	public void setNoteUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.note(), newValue, false , exclusiveUntil);
	}

	public String getOrderInGroup()
	{
		return this.zSynchronizedGetData().getOrderInGroup();
	}

	public void setOrderInGroup(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.orderInGroup(), newValue, false );
	}

	public void setOrderInGroupUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.orderInGroup(), newValue, false , exclusiveUntil);
	}

	public Timestamp getProcessingDateFrom()
	{
		return this.zSynchronizedGetData().getProcessingDateFrom();
	}

	public void setProcessingDateFrom(Timestamp newValue)
	{
		zSetTimestamp(OilLogFinder.processingDateFrom(), newValue, false );
	}

	public Timestamp getProcessingDateTo()
	{
		return this.zSynchronizedGetData().getProcessingDateTo();
	}

	public void setProcessingDateTo(Timestamp newValue)
	{
		zSetTimestamp(OilLogFinder.processingDateTo(), newValue, false );
	}

	public long getShip()
	{
		OilLogData data = this.zSynchronizedGetData();
		if (data.isShipNull()) MithraNullPrimitiveException.throwNew("ship", data);
		return data.getShip();
	}

	public void setShip(long newValue)
	{
		zSetLong(OilLogFinder.ship(), newValue, false ,true);
	}

	public void setShipUntil(long newValue, Timestamp exclusiveUntil)
	{
		zSetLong(OilLogFinder.ship(), newValue, false , exclusiveUntil,true);
	}

	public String getSignature()
	{
		return this.zSynchronizedGetData().getSignature();
	}

	public void setSignature(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.signature(), newValue, false );
	}

	public void setSignatureUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.signature(), newValue, false , exclusiveUntil);
	}

	public String getSignatureGroupUuid()
	{
		return this.zSynchronizedGetData().getSignatureGroupUuid();
	}

	public void setSignatureGroupUuid(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.signatureGroupUuid(), newValue, false );
	}

	public void setSignatureGroupUuidUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.signatureGroupUuid(), newValue, false , exclusiveUntil);
	}

	public String getSignerName()
	{
		return this.zSynchronizedGetData().getSignerName();
	}

	public void setSignerName(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.signerName(), newValue, false );
	}

	public void setSignerNameUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.signerName(), newValue, false , exclusiveUntil);
	}

	public String getSignerRank()
	{
		return this.zSynchronizedGetData().getSignerRank();
	}

	public void setSignerRank(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.signerRank(), newValue, false );
	}

	public void setSignerRankUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.signerRank(), newValue, false , exclusiveUntil);
	}

	public String getSignerSurname()
	{
		return this.zSynchronizedGetData().getSignerSurname();
	}

	public void setSignerSurname(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.signerSurname(), newValue, false );
	}

	public void setSignerSurnameUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.signerSurname(), newValue, false , exclusiveUntil);
	}

	public String getType()
	{
		return this.zSynchronizedGetData().getType();
	}

	public void setType(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		if (newValue != null && newValue.length() > 4)
		{
			throw new MithraBusinessException("Attribute 'type' cannot exceed maximum length of 4: " + newValue);
		}

		zSetString(OilLogFinder.type(), newValue, false );
	}

	public void setTypeUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		if (newValue != null && newValue.length() > 4)
		{
			throw new MithraBusinessException("Attribute 'type' cannot exceed maximum length of 4: " + newValue);
		}

		zSetString(OilLogFinder.type(), newValue, false , exclusiveUntil);
	}

	public String getUuid()
	{
		return this.zSynchronizedGetData().getUuid();
	}

	public void setUuid(String newValue)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.uuid(), newValue, false );
	}

	public void setUuidUntil(String newValue, Timestamp exclusiveUntil)
	{
		if (newValue != null) newValue = newValue.trim();
		zSetString(OilLogFinder.uuid(), newValue, false , exclusiveUntil);
	}

	protected void issuePrimitiveNullSetters(DatedTransactionalBehavior behavior, MithraDataObject data, boolean mustCheckCurrent)
	{
		zNullify(behavior, data, OilLogFinder.ship(), false, mustCheckCurrent);
	}

	protected boolean zCheckInfiniteBusinessDate(MithraDataObject data)
	{
		OilLogData data2 = (OilLogData) data;
		Timestamp businessTo = data2.getBusinessDateTo();
		return (businessTo != null && OilLogFinder.businessDate().getInfinityDate().getTime() ==
		businessTo.getTime());
	}

	public void setShipNull()
	{
		zNullify(OilLogFinder.ship(), false);
	}

	public void setShipNullUntil(Timestamp exclusiveUntil)
	{
		zNullifyUntil(OilLogFinder.ship(), false, exclusiveUntil);
	}

	public Timestamp getBusinessDate()
	{
		return this.businessDate;
	}

	public Timestamp getProcessingDate()
	{
		return this.processingDate;
	}

	public void zPersistDetachedRelationships(MithraDataObject _data)
	{
		OilLogData _newData = (OilLogData) _data;
	}

	public void zSetTxDetachedDeleted()
	{
		DatedTransactionalBehavior behavior = this.zGetTransactionalBehavior();
		if (behavior.isDetached() && behavior.isDeleted()) return;
		OilLogData _newData = (OilLogData) behavior.getCurrentDataForRead(this);
		this.zSetTxPersistenceState(DatedPersistenceState.DETACHED_DELETED);
	}

	public void zSetNonTxDetachedDeleted()
	{
		DatedTransactionalBehavior behavior = this.zGetTransactionalBehavior();
		OilLogData _newData = (OilLogData) behavior.getCurrentDataForRead(this);
		this.zSetNonTxPersistenceState(DatedPersistenceState.DETACHED_DELETED);
	}

	public Cache zGetCache()
	{
		return OilLogFinder.getMithraObjectPortal().getCache();
	}

	private Logger getLogger()
	{
		return logger;
	}

	public MithraDataObject zAllocateData()
	{
		return new OilLogData();
	}

	public MithraDataObject zRefreshWithLock(boolean lock)
	{
		OilLogData data = (OilLogData) OilLogFinder.getMithraObjectPortal().refreshDatedObject(this, lock);
		if (data == null)
		{
			throw new MithraDeletedException("OilLog has been deleted.");
		}

		return data;
	}

	public void setFromOilLogData( OilLogData data )
	{
		this.zSetData(data);
	}

	protected void zSetFromOilLogData( OilLogData data )
	{
		this.zSetData(data);
		this.zSetNonTxPersistenceState(PERSISTED_STATE);
	}

	public MithraTransactionalDatabaseObject zGetDatabaseObject()
	{
		return (MithraTransactionalDatabaseObject) OilLogFinder.getMithraObjectPortal().getDatabaseObject();
	}

	public MithraObjectPortal zGetPortal()
	{
		return OilLogFinder.getMithraObjectPortal();
	}

	public OilLog getOriginalPersistentObject()
	{
		return this.zFindOriginal();
	}

	public void copyNonPrimaryKeyAttributesFrom(OilLogAbstract from) throws MithraBusinessException
	{
		this.copyNonPrimaryKeyAttributesFrom((MithraTransactionalObject)from);
	}

	protected void copyNonPrimaryKeyAttributesFromImpl(MithraTransactionalObject f, MithraTransaction tx) throws MithraBusinessException
	{
		OilLogAbstract from = (OilLogAbstract) f;
		OilLogData newData = from.zSynchronizedGetData();
		this.setAttachmentData(newData.getAttachmentData());
		this.setAttachmentName(newData.getAttachmentName());
		this.setCodeLetter(newData.getCodeLetter());
		this.setCodeNumber(newData.getCodeNumber());
		this.setContent(newData.getContent());
		this.setCorrectionNote(newData.getCorrectionNote());
		this.setGroupUuid(newData.getGroupUuid());
		this.setMasterSigDate(newData.getMasterSigDate());
		this.setMasterSignature(newData.getMasterSignature());
		this.setMasterSignerName(newData.getMasterSignerName());
		this.setMasterSignerSurname(newData.getMasterSignerSurname());
		this.setNote(newData.getNote());
		this.setOrderInGroup(newData.getOrderInGroup());
		if (from.isShipNull())
		{
			this.setShipNull();
		}
		else
		{
			this.setShip(newData.getShip());
		}

		this.setSignature(newData.getSignature());
		this.setSignatureGroupUuid(newData.getSignatureGroupUuid());
		this.setSignerName(newData.getSignerName());
		this.setSignerRank(newData.getSignerRank());
		this.setSignerSurname(newData.getSignerSurname());
		this.setType(newData.getType());
		this.setUuid(newData.getUuid());
	}

	public void zPersistDetachedRelationshipsUntil(MithraDataObject _data, Timestamp exclusiveUntil)
	{
		OilLogData _newData = (OilLogData) _data;
	}

	public void zInsertRelationshipsUntil(MithraDataObject _data, Timestamp exclusiveUntil)
	{
		OilLogData _newData = (OilLogData) _data;
	}

	public void copyNonPrimaryKeyAttributesUntilFrom(OilLogAbstract from, Timestamp exclusiveUntil) throws MithraBusinessException
	{
		this.copyNonPrimaryKeyAttributesUntilFrom((MithraDatedTransactionalObject)from, exclusiveUntil);
	}

	protected void copyNonPrimaryKeyAttributesUntilFromImpl(MithraDatedTransactionalObject f, Timestamp exclusiveUntil, MithraTransaction tx) throws MithraBusinessException
	{
		OilLogAbstract from = (OilLogAbstract)f;
		OilLogData newData = from.zSynchronizedGetData();
		this.setAttachmentDataUntil(newData.getAttachmentData(), exclusiveUntil);
		this.setAttachmentNameUntil(newData.getAttachmentName(), exclusiveUntil);
		this.setCodeLetterUntil(newData.getCodeLetter(), exclusiveUntil);
		this.setCodeNumberUntil(newData.getCodeNumber(), exclusiveUntil);
		this.setContentUntil(newData.getContent(), exclusiveUntil);
		this.setCorrectionNoteUntil(newData.getCorrectionNote(), exclusiveUntil);
		this.setGroupUuidUntil(newData.getGroupUuid(), exclusiveUntil);
		this.setMasterSigDateUntil(newData.getMasterSigDate(), exclusiveUntil);
		this.setMasterSignatureUntil(newData.getMasterSignature(), exclusiveUntil);
		this.setMasterSignerNameUntil(newData.getMasterSignerName(), exclusiveUntil);
		this.setMasterSignerSurnameUntil(newData.getMasterSignerSurname(), exclusiveUntil);
		this.setNoteUntil(newData.getNote(), exclusiveUntil);
		this.setOrderInGroupUntil(newData.getOrderInGroup(), exclusiveUntil);
		if (from.isShipNull())
		{
			this.setShipNullUntil(exclusiveUntil);
		}
		else
		{
			this.setShipUntil(newData.getShip(), exclusiveUntil);
		}

		this.setSignatureUntil(newData.getSignature(), exclusiveUntil);
		this.setSignatureGroupUuidUntil(newData.getSignatureGroupUuid(), exclusiveUntil);
		this.setSignerNameUntil(newData.getSignerName(), exclusiveUntil);
		this.setSignerRankUntil(newData.getSignerRank(), exclusiveUntil);
		this.setSignerSurnameUntil(newData.getSignerSurname(), exclusiveUntil);
		this.setTypeUntil(newData.getType(), exclusiveUntil);
		this.setUuidUntil(newData.getUuid(), exclusiveUntil);
	}

	protected void zCheckOptimisticLocking(MithraTransaction tx, MithraDataObject d, MithraDataObject nd)
	{
		OilLogData newData = (OilLogData) d;
		OilLogData data = (OilLogData) nd;
		if (OilLogFinder.getMithraObjectPortal().getTxParticipationMode(tx).isOptimisticLocking()
		&& !tx.retryOnOptimisticLockFailure() && !newData.getProcessingDateFrom().equals(data.getProcessingDateFrom()))
		{
			throw new MithraOptimisticLockException("Optimistic lock failure. "+data.zGetPrintablePrimaryKey());
		}
	}

	protected boolean issueUpdates(DatedTransactionalBehavior behavior, MithraDataObject data, MithraDataObject newData)
	{
		boolean changed = false;
		changed |= zUpdateByteArray(behavior, data, newData, OilLogFinder.attachmentData(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.attachmentName(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.codeLetter(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.codeNumber(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.content(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.correctionNote(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.groupUuid(), false);
		changed |= zUpdateLong(behavior, data, newData, OilLogFinder.id(), false);
		changed |= zUpdateDate(behavior, data, newData, OilLogFinder.masterSigDate(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.masterSignature(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.masterSignerName(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.masterSignerSurname(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.note(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.orderInGroup(), false);
		changed |= zUpdateLong(behavior, data, newData, OilLogFinder.ship(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.signature(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.signatureGroupUuid(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.signerName(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.signerRank(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.signerSurname(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.type(), false);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.uuid(), false);
		return changed;
	}

	protected boolean issueUpdatesUntil(DatedTransactionalBehavior behavior, MithraDataObject data, MithraDataObject newData, Timestamp until)
	{
		boolean changed = false;
		changed |= zUpdateByteArray(behavior, data, newData, OilLogFinder.attachmentData(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.attachmentName(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.codeLetter(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.codeNumber(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.content(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.correctionNote(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.groupUuid(), false, until);
		changed |= zUpdateLong(behavior, data, newData, OilLogFinder.id(), false, until);
		changed |= zUpdateDate(behavior, data, newData, OilLogFinder.masterSigDate(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.masterSignature(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.masterSignerName(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.masterSignerSurname(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.note(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.orderInGroup(), false, until);
		changed |= zUpdateLong(behavior, data, newData, OilLogFinder.ship(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.signature(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.signatureGroupUuid(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.signerName(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.signerRank(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.signerSurname(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.type(), false, until);
		changed |= zUpdateString(behavior, data, newData, OilLogFinder.uuid(), false, until);
		return changed;
	}

	public void cascadeTerminate()
	{
		this.terminate();
	}

	public void cascadeTerminateUntil(Timestamp exclusiveUntil) throws MithraBusinessException
	{
		this.terminateUntil(exclusiveUntil);
	}

	public void zSerializePrimaryKey(ObjectOutput out) throws IOException
	{
		OilLogData data = ((OilLogData)this.zGetCurrentOrTransactionalData());
		data.zSerializePrimaryKey(out);
		MithraTimestamp.writeTimezoneInsensitiveTimestampWithInfinity(out, businessDate, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate());
		MithraTimestamp.writeTimezoneInsensitiveTimestampWithInfinity(out, processingDate, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate());
	}

	public void zWriteDataClassName(ObjectOutput out) throws IOException
	{
	}

	public void zSerializeFullData(ObjectOutput out) throws IOException
	{
		OilLogData data = ((OilLogData)this.zGetCurrentDataWithCheck());
		data.zSerializeFullData(out);
		MithraTimestamp.writeTimezoneInsensitiveTimestampWithInfinity(out, businessDate, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate());
		MithraTimestamp.writeTimezoneInsensitiveTimestampWithInfinity(out, processingDate, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate());
	}

	public void zSerializeFullTxData(ObjectOutput out) throws IOException
	{
		OilLogData data = ((OilLogData)this.zGetTxDataForRead());
		data.zSerializeFullData(out);
		MithraTimestamp.writeTimezoneInsensitiveTimestampWithInfinity(out, businessDate, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate());
		MithraTimestamp.writeTimezoneInsensitiveTimestampWithInfinity(out, processingDate, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate());
	}

	public long generateAndSetId()
	{
		long nextValue =(long) this.generateId();
		this.setId(nextValue);
		return nextValue;
	}

	public boolean zGetIsIdSet()
	{
		OilLogData data = this.zSynchronizedGetData();
		return data.zGetIsIdSet();
	}

	protected long generateId()
	throws MithraBusinessException
	{
		MaxFromTablePrimaryKeyGenerator primaryKeyGenerator =
		MithraPrimaryKeyGenerator.getInstance().getMaxFromTablePrimaryKeyGenerator(OilLogFinder.id(),null);
		return (long)primaryKeyGenerator.getNextId();
	}

	private void checkAndGeneratePrimaryKeys()
	{
		DatedTransactionalBehavior behavior = this.zGetTransactionalBehaviorForWrite();
		OilLogData data = (OilLogData) behavior.getCurrentDataForWrite(this);
		if (!data.zGetIsIdSet())
		{
			long newValue = this.generateId();
			data.setId(newValue);
		}
	}

	protected void zSerializeAsOfAttributes(java.io.ObjectOutputStream out) throws IOException
	{
		MithraTimestamp.writeTimezoneInsensitiveTimestampWithInfinity(out, businessDate, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate());
		MithraTimestamp.writeTimezoneInsensitiveTimestampWithInfinity(out, processingDate, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate());
	}

	protected void zDeserializeAsOfAttributes(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		this.businessDate = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestampWithInfinity(in, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate()), OilLogFinder.isFullCache());
		this.processingDate = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestampWithInfinity(in, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate()), OilLogFinder.isFullCache());
	}

	public boolean zDataMatches(Object data, Timestamp[] asOfDates)
	{
		OilLogData localData = (OilLogData) data;
		MithraDataObject thisData = this.zGetCurrentOrTransactionalData();
		return thisData != null && localData.hasSamePrimaryKeyIgnoringAsOfAttributes(thisData)
		&& this.businessDate.equals(asOfDates[0])
		&& this.processingDate.equals(asOfDates[1])
		;
	}

	protected static void zConfigNonTx()
	{
		MEMORY_STATE = DatedPersistenceState.IN_MEMORY_NON_TRANSACTIONAL;
		PERSISTED_STATE = DatedPersistenceState.PERSISTED_NON_TRANSACTIONAL;
	}

	protected static void zConfigFullTx()
	{
		MEMORY_STATE = DatedPersistenceState.IN_MEMORY;
		PERSISTED_STATE = DatedPersistenceState.PERSISTED;
	}
}
