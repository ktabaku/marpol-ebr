package al.sybina.ebr.logbook.domain.reladomo;
import java.util.*;
import java.sql.Timestamp;
import java.sql.Date;
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
import com.gs.fw.common.mithra.finder.PrintablePreparedStatement;
import com.gs.fw.common.mithra.finder.RelatedFinder;
import com.gs.fw.common.mithra.cache.offheap.MithraOffHeapDataObject;
import com.gs.fw.common.mithra.cache.offheap.OffHeapDataStorage;
/**
* This file was automatically generated using Mithra 17.1.4. Please do not modify it.
* Add custom logic to its subclass instead.
*/
public class OilLogData
implements MithraDataObject
{
	private byte _dataVersion;
	private byte isNullBits0 = 0;
	private byte[] attachmentData;
	private String attachmentName;
	private Timestamp businessDateFrom;
	private Timestamp businessDateTo;
	private String codeLetter;
	private String codeNumber;
	private String content;
	private String correctionNote;
	private String groupUuid;
	private long id;
	private Date masterSigDate;
	private String masterSignature;
	private String masterSignerName;
	private String masterSignerSurname;
	private String note;
	private String orderInGroup;
	private Timestamp processingDateFrom;
	private Timestamp processingDateTo;
	private long ship;
	private String signature;
	private String signatureGroupUuid;
	private String signerName;
	private String signerRank;
	private String signerSurname;
	private String type;
	private String uuid;
	public boolean isAttachmentDataNull()
	{
		return this.getAttachmentData() == null;
	}

	public boolean isAttachmentNameNull()
	{
		return this.getAttachmentName() == null;
	}

	public boolean isBusinessDateFromNull()
	{
		return this.getBusinessDateFrom() == null;
	}

	public boolean isBusinessDateToNull()
	{
		return this.getBusinessDateTo() == null;
	}

	public boolean isCodeLetterNull()
	{
		return this.getCodeLetter() == null;
	}

	public boolean isCodeNumberNull()
	{
		return this.getCodeNumber() == null;
	}

	public boolean isContentNull()
	{
		return this.getContent() == null;
	}

	public boolean isCorrectionNoteNull()
	{
		return this.getCorrectionNote() == null;
	}

	public boolean isGroupUuidNull()
	{
		return this.getGroupUuid() == null;
	}

	public boolean isIdNull()
	{
		return false;
	}

	public boolean isMasterSigDateNull()
	{
		return this.getMasterSigDate() == null;
	}

	public boolean isMasterSignatureNull()
	{
		return this.getMasterSignature() == null;
	}

	public boolean isMasterSignerNameNull()
	{
		return this.getMasterSignerName() == null;
	}

	public boolean isMasterSignerSurnameNull()
	{
		return this.getMasterSignerSurname() == null;
	}

	public boolean isNoteNull()
	{
		return this.getNote() == null;
	}

	public boolean isOrderInGroupNull()
	{
		return this.getOrderInGroup() == null;
	}

	public boolean isProcessingDateFromNull()
	{
		return this.getProcessingDateFrom() == null;
	}

	public boolean isProcessingDateToNull()
	{
		return this.getProcessingDateTo() == null;
	}

	public boolean isSignatureNull()
	{
		return this.getSignature() == null;
	}

	public boolean isSignatureGroupUuidNull()
	{
		return this.getSignatureGroupUuid() == null;
	}

	public boolean isSignerNameNull()
	{
		return this.getSignerName() == null;
	}

	public boolean isSignerRankNull()
	{
		return this.getSignerRank() == null;
	}

	public boolean isSignerSurnameNull()
	{
		return this.getSignerSurname() == null;
	}

	public boolean isTypeNull()
	{
		return this.getType() == null;
	}

	public boolean isUuidNull()
	{
		return this.getUuid() == null;
	}

	public boolean isShipNull()
	{
		return (isNullBits0 & 1) != 0 ;
	}

	public void zSerializeFullData(ObjectOutput out) throws IOException
	{
		zWriteNullBits(out);
		if (attachmentData == null) out.writeInt(-1); else 
		{
			out.writeInt(attachmentData.length); out.write(attachmentData); 
		}

		;
		out.writeObject(this.attachmentName);
		MithraTimestamp.writeTimezoneInsensitiveTimestamp(out, this.businessDateFrom);
		MithraTimestamp.writeTimezoneInsensitiveTimestampWithInfinity(out, this.businessDateTo, OilLogFinder.businessDate().getInfinityDate());
		out.writeObject(this.codeLetter);
		out.writeObject(this.codeNumber);
		out.writeObject(this.content);
		out.writeObject(this.correctionNote);
		out.writeObject(this.groupUuid);
		out.writeLong(this.id);
		out.writeBoolean(_isIdSet);
		MithraTimestamp.writeTimezoneInsensitiveDate(out, this.masterSigDate);
		out.writeObject(this.masterSignature);
		out.writeObject(this.masterSignerName);
		out.writeObject(this.masterSignerSurname);
		out.writeObject(this.note);
		out.writeObject(this.orderInGroup);
		MithraTimestamp.writeTimezoneInsensitiveTimestamp(out, this.processingDateFrom);
		MithraTimestamp.writeTimezoneInsensitiveTimestampWithInfinity(out, this.processingDateTo, OilLogFinder.processingDate().getInfinityDate());
		out.writeLong(this.ship);
		out.writeObject(this.signature);
		out.writeObject(this.signatureGroupUuid);
		out.writeObject(this.signerName);
		out.writeObject(this.signerRank);
		out.writeObject(this.signerSurname);
		out.writeObject(this.type);
		out.writeObject(this.uuid);
	}

	private void zWriteNullBits(ObjectOutput out) throws IOException
	{
		out.writeByte(this.isNullBits0);
	}

	public byte[] getAttachmentData()
	{
		return this.attachmentData;
	}

	public void setAttachmentData(byte[] value)
	{
		this.attachmentData = value;
	}

	public void setAttachmentDataNull()
	{
		this.setAttachmentData(null);
	}

	public String getAttachmentName()
	{
		return this.attachmentName;
	}

	public int zGetAttachmentNameAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(attachmentName);
	}

	public void setAttachmentName(String value)
	{
		this.attachmentName = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setAttachmentNameNull()
	{
		this.setAttachmentName(null);
	}

	public Timestamp getBusinessDateFrom()
	{
		return this.businessDateFrom;
	}

	public long zGetBusinessDateFromAsLong()
	{
		if (businessDateFrom == null) return TimestampPool.OFF_HEAP_NULL;
		return businessDateFrom.getTime();
	}

	public void setBusinessDateFrom(Timestamp value)
	{
		this.businessDateFrom = TimestampPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache(), OilLogFinder.isOffHeap());
	}

	public void setBusinessDateFromNull()
	{
		this.setBusinessDateFrom(null);
	}

	public Timestamp getBusinessDateTo()
	{
		return this.businessDateTo;
	}

	public long zGetBusinessDateToAsLong()
	{
		if (businessDateTo == null) return TimestampPool.OFF_HEAP_NULL;
		return businessDateTo.getTime();
	}

	public void setBusinessDateTo(Timestamp value)
	{
		if (!value.equals(this.businessDateTo))
		{
			zIncrementDataVersion();
		}

		this.businessDateTo = TimestampPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache(), OilLogFinder.isOffHeap());
	}

	public void setBusinessDateToNull()
	{
		throw new RuntimeException("should never be called");
	}

	public String getCodeLetter()
	{
		return this.codeLetter;
	}

	public int zGetCodeLetterAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(codeLetter);
	}

	public void setCodeLetter(String value)
	{
		this.codeLetter = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setCodeLetterNull()
	{
		this.setCodeLetter(null);
	}

	public String getCodeNumber()
	{
		return this.codeNumber;
	}

	public int zGetCodeNumberAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(codeNumber);
	}

	public void setCodeNumber(String value)
	{
		this.codeNumber = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setCodeNumberNull()
	{
		this.setCodeNumber(null);
	}

	public String getContent()
	{
		return this.content;
	}

	public int zGetContentAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(content);
	}

	public void setContent(String value)
	{
		this.content = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setContentNull()
	{
		this.setContent(null);
	}

	public String getCorrectionNote()
	{
		return this.correctionNote;
	}

	public int zGetCorrectionNoteAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(correctionNote);
	}

	public void setCorrectionNote(String value)
	{
		this.correctionNote = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setCorrectionNoteNull()
	{
		this.setCorrectionNote(null);
	}

	public String getGroupUuid()
	{
		return this.groupUuid;
	}

	public int zGetGroupUuidAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(groupUuid);
	}

	public void setGroupUuid(String value)
	{
		this.groupUuid = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setGroupUuidNull()
	{
		this.setGroupUuid(null);
	}

	public long getId()
	{
		return this.id;
	}

	public void setId(long value)
	{
		this.id = value;
		_isIdSet = true;
	}

	public void setIdNull()
	{
		throw new RuntimeException("should never be called");
	}

	public Date getMasterSigDate()
	{
		return this.masterSigDate;
	}

	public void setMasterSigDate(java.util.Date value)
	{
		if (value == null || value instanceof java.sql.Date)
		{
			this.masterSigDate = (java.sql.Date) value;
		}
		else
		{
			this.masterSigDate = new java.sql.Date(value.getTime());
		}
	}

	public void setMasterSigDateNull()
	{
		this.setMasterSigDate(null);
	}

	public String getMasterSignature()
	{
		return this.masterSignature;
	}

	public int zGetMasterSignatureAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(masterSignature);
	}

	public void setMasterSignature(String value)
	{
		this.masterSignature = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setMasterSignatureNull()
	{
		this.setMasterSignature(null);
	}

	public String getMasterSignerName()
	{
		return this.masterSignerName;
	}

	public int zGetMasterSignerNameAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(masterSignerName);
	}

	public void setMasterSignerName(String value)
	{
		this.masterSignerName = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setMasterSignerNameNull()
	{
		this.setMasterSignerName(null);
	}

	public String getMasterSignerSurname()
	{
		return this.masterSignerSurname;
	}

	public int zGetMasterSignerSurnameAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(masterSignerSurname);
	}

	public void setMasterSignerSurname(String value)
	{
		this.masterSignerSurname = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setMasterSignerSurnameNull()
	{
		this.setMasterSignerSurname(null);
	}

	public String getNote()
	{
		return this.note;
	}

	public int zGetNoteAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(note);
	}

	public void setNote(String value)
	{
		this.note = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setNoteNull()
	{
		this.setNote(null);
	}

	public String getOrderInGroup()
	{
		return this.orderInGroup;
	}

	public int zGetOrderInGroupAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(orderInGroup);
	}

	public void setOrderInGroup(String value)
	{
		this.orderInGroup = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setOrderInGroupNull()
	{
		this.setOrderInGroup(null);
	}

	public Timestamp getProcessingDateFrom()
	{
		return this.processingDateFrom;
	}

	public long zGetProcessingDateFromAsLong()
	{
		if (processingDateFrom == null) return TimestampPool.OFF_HEAP_NULL;
		return processingDateFrom.getTime();
	}

	public void setProcessingDateFrom(Timestamp value)
	{
		this.processingDateFrom = TimestampPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache(), OilLogFinder.isOffHeap());
	}

	public void setProcessingDateFromNull()
	{
		this.setProcessingDateFrom(null);
	}

	public Timestamp getProcessingDateTo()
	{
		return this.processingDateTo;
	}

	public long zGetProcessingDateToAsLong()
	{
		if (processingDateTo == null) return TimestampPool.OFF_HEAP_NULL;
		return processingDateTo.getTime();
	}

	public void setProcessingDateTo(Timestamp value)
	{
		if (!value.equals(this.processingDateTo))
		{
			zIncrementDataVersion();
		}

		this.processingDateTo = TimestampPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache(), OilLogFinder.isOffHeap());
	}

	public void setProcessingDateToNull()
	{
		throw new RuntimeException("should never be called");
	}

	public long getShip()
	{
		return this.ship;
	}

	public void setShip(long value)
	{
		this.ship = value;
		isNullBits0 = (byte)((int)isNullBits0 & ~( 1));
	}

	public String getSignature()
	{
		return this.signature;
	}

	public int zGetSignatureAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(signature);
	}

	public void setSignature(String value)
	{
		this.signature = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setSignatureNull()
	{
		this.setSignature(null);
	}

	public String getSignatureGroupUuid()
	{
		return this.signatureGroupUuid;
	}

	public int zGetSignatureGroupUuidAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(signatureGroupUuid);
	}

	public void setSignatureGroupUuid(String value)
	{
		this.signatureGroupUuid = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setSignatureGroupUuidNull()
	{
		this.setSignatureGroupUuid(null);
	}

	public String getSignerName()
	{
		return this.signerName;
	}

	public int zGetSignerNameAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(signerName);
	}

	public void setSignerName(String value)
	{
		this.signerName = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setSignerNameNull()
	{
		this.setSignerName(null);
	}

	public String getSignerRank()
	{
		return this.signerRank;
	}

	public int zGetSignerRankAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(signerRank);
	}

	public void setSignerRank(String value)
	{
		this.signerRank = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setSignerRankNull()
	{
		this.setSignerRank(null);
	}

	public String getSignerSurname()
	{
		return this.signerSurname;
	}

	public int zGetSignerSurnameAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(signerSurname);
	}

	public void setSignerSurname(String value)
	{
		this.signerSurname = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setSignerSurnameNull()
	{
		this.setSignerSurname(null);
	}

	public String getType()
	{
		return this.type;
	}

	public int zGetTypeAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(type);
	}

	public void setType(String value)
	{
		this.type = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setTypeNull()
	{
		this.setType(null);
	}

	public String getUuid()
	{
		return this.uuid;
	}

	public int zGetUuidAsInt()
	{
		return StringPool.getInstance().getOffHeapAddressWithoutAdding(uuid);
	}

	public void setUuid(String value)
	{
		this.uuid = StringPool.getInstance().getOrAddToCache(value, OilLogFinder.isFullCache());
	}

	public void setUuidNull()
	{
		this.setUuid(null);
	}

	public void setShipNull()
	{
		isNullBits0 = (byte)((int)isNullBits0 | 1);
	}

	public byte zGetIsNullBits0()
	{
		return this.isNullBits0;
	}

	public void zSetIsNullBits0(byte newValue)
	{
		this.isNullBits0 = newValue;
	}

	public boolean _isIdSet;
	public boolean zGetIsIdSet()
	{
		return _isIdSet;
	}

	public byte zGetDataVersion() 
	{
		return _dataVersion; 
	}

	public void zSetDataVersion(byte version) 
	{
		this._dataVersion = version; 
	}

	public void zIncrementDataVersion()
	{
		_dataVersion++;
		if (_dataVersion > 120) _dataVersion = (byte) 0;
	}

	protected void copyInto(OilLogData copy, boolean withRelationships)
	{
		copy.isNullBits0 = this.isNullBits0;
		copy.attachmentData = this.attachmentData;
		copy.attachmentName = this.attachmentName;
		copy.businessDateFrom = this.businessDateFrom;
		copy.businessDateTo = this.businessDateTo;
		copy.codeLetter = this.codeLetter;
		copy.codeNumber = this.codeNumber;
		copy.content = this.content;
		copy.correctionNote = this.correctionNote;
		copy.groupUuid = this.groupUuid;
		copy.id = this.id;
		copy.masterSigDate = this.masterSigDate;
		copy.masterSignature = this.masterSignature;
		copy.masterSignerName = this.masterSignerName;
		copy.masterSignerSurname = this.masterSignerSurname;
		copy.note = this.note;
		copy.orderInGroup = this.orderInGroup;
		copy.processingDateFrom = this.processingDateFrom;
		copy.processingDateTo = this.processingDateTo;
		copy.ship = this.ship;
		copy.signature = this.signature;
		copy.signatureGroupUuid = this.signatureGroupUuid;
		copy.signerName = this.signerName;
		copy.signerRank = this.signerRank;
		copy.signerSurname = this.signerSurname;
		copy.type = this.type;
		copy.uuid = this.uuid;
		copy._isIdSet = this._isIdSet;
		if (withRelationships)
		{
		}
	}

	public void zDeserializeFullData(ObjectInput in) throws IOException, ClassNotFoundException
	{
		this.isNullBits0 = in.readByte();
		int _attachmentDataLength = in.readInt(); if (_attachmentDataLength == -1) attachmentData = null; else 
		{
			attachmentData = new byte[_attachmentDataLength];in.readFully(attachmentData);
		}

		;
		this.attachmentName = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.businessDateFrom = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestamp(in), OilLogFinder.isFullCache(), OilLogFinder.isOffHeap());
		this.businessDateTo = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestampWithInfinity(in, OilLogFinder.businessDate().getInfinityDate()), OilLogFinder.isFullCache(), OilLogFinder.isOffHeap());
		this.codeLetter = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.codeNumber = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.content = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.correctionNote = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.groupUuid = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.id = in.readLong();
		_isIdSet = in.readBoolean();
		this.masterSigDate = (Date)MithraTimestamp.readTimezoneInsensitiveDate(in);
		this.masterSignature = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.masterSignerName = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.masterSignerSurname = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.note = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.orderInGroup = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.processingDateFrom = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestamp(in), OilLogFinder.isFullCache(), OilLogFinder.isOffHeap());
		this.processingDateTo = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestampWithInfinity(in, OilLogFinder.processingDate().getInfinityDate()), OilLogFinder.isFullCache(), OilLogFinder.isOffHeap());
		this.ship = in.readLong();
		this.signature = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.signatureGroupUuid = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.signerName = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.signerRank = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.signerSurname = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.type = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
		this.uuid = StringPool.getInstance().getOrAddToCache((String)in.readObject(), OilLogFinder.isFullCache());
	}

	public boolean hasSamePrimaryKeyIgnoringAsOfAttributes(MithraDataObject other)
	{
		if (this == other) return true;
		final OilLogData otherData = (OilLogData) other;
		if (getId() != otherData.getId())
		{
			return false;
		}

		return true;
	}

	public void zSerializePrimaryKey(ObjectOutput out) throws IOException
	{
		MithraTimestamp.writeTimezoneInsensitiveTimestamp(out, this.businessDateFrom);
		MithraTimestamp.writeTimezoneInsensitiveTimestampWithInfinity(out, this.businessDateTo, OilLogFinder.businessDate().getInfinityDate());
		out.writeLong(this.id);
		MithraTimestamp.writeTimezoneInsensitiveTimestamp(out, this.processingDateFrom);
		MithraTimestamp.writeTimezoneInsensitiveTimestampWithInfinity(out, this.processingDateTo, OilLogFinder.processingDate().getInfinityDate());
	}

	public void zDeserializePrimaryKey(ObjectInput in) throws IOException, ClassNotFoundException
	{
		this.businessDateFrom = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestamp(in), OilLogFinder.isFullCache(), OilLogFinder.isOffHeap());
		this.businessDateTo = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestampWithInfinity(in, OilLogFinder.businessDate().getInfinityDate()), OilLogFinder.isFullCache(), OilLogFinder.isOffHeap());
		this.id = in.readLong();
		this.processingDateFrom = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestamp(in), OilLogFinder.isFullCache(), OilLogFinder.isOffHeap());
		this.processingDateTo = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestampWithInfinity(in, OilLogFinder.processingDate().getInfinityDate()), OilLogFinder.isFullCache(), OilLogFinder.isOffHeap());
	}

	public void clearRelationships()
	{
		clearAllDirectRefs();
	}

	public void clearAllDirectRefs()
	{
	}

	public void zSerializeRelationships(ObjectOutputStream out) throws IOException
	{
	}

	public void zDeserializeRelationships(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
	}

	public MithraOffHeapDataObject zCopyOffHeap()
	{
		throw new RuntimeException("off heap no supported");
	}

	public void copyNonPkAttributes(MithraDataObject newData)
	{
		final OilLogData oilLogData = (OilLogData) newData;
		this.setAttachmentData(oilLogData.getAttachmentData());
		this.setAttachmentName(oilLogData.getAttachmentName());
		this.setBusinessDateFrom(oilLogData.getBusinessDateFrom());
		this.setBusinessDateTo(oilLogData.getBusinessDateTo());
		this.setCodeLetter(oilLogData.getCodeLetter());
		this.setCodeNumber(oilLogData.getCodeNumber());
		this.setContent(oilLogData.getContent());
		this.setCorrectionNote(oilLogData.getCorrectionNote());
		this.setGroupUuid(oilLogData.getGroupUuid());
		this.setMasterSigDate(oilLogData.getMasterSigDate());
		this.setMasterSignature(oilLogData.getMasterSignature());
		this.setMasterSignerName(oilLogData.getMasterSignerName());
		this.setMasterSignerSurname(oilLogData.getMasterSignerSurname());
		this.setNote(oilLogData.getNote());
		this.setOrderInGroup(oilLogData.getOrderInGroup());
		this.setProcessingDateFrom(oilLogData.getProcessingDateFrom());
		this.setProcessingDateTo(oilLogData.getProcessingDateTo());
		this.setShip(oilLogData.getShip());
		if (oilLogData.isShipNull()) this.setShipNull();
		this.setSignature(oilLogData.getSignature());
		this.setSignatureGroupUuid(oilLogData.getSignatureGroupUuid());
		this.setSignerName(oilLogData.getSignerName());
		this.setSignerRank(oilLogData.getSignerRank());
		this.setSignerSurname(oilLogData.getSignerSurname());
		this.setType(oilLogData.getType());
		this.setUuid(oilLogData.getUuid());
	}

	public boolean zNonPrimaryKeyAttributesChanged(MithraDataObject newData)
	{
		return this.zNonPrimaryKeyAttributesChanged(newData, 0.0);
	}

	public boolean zNonPrimaryKeyAttributesChanged(MithraDataObject newData, double toleranceForFloatingPointFields)
	{
		final OilLogData other = (OilLogData) newData;
		if (!isAttachmentDataNull() ? !Arrays.equals(getAttachmentData(),other.getAttachmentData()) : !other.isAttachmentDataNull())
		{
			return true;
		}

		if (!isAttachmentNameNull() ? !getAttachmentName().equals(other.getAttachmentName()) : !other.isAttachmentNameNull())
		{
			return true;
		}

		if (!isCodeLetterNull() ? !getCodeLetter().equals(other.getCodeLetter()) : !other.isCodeLetterNull())
		{
			return true;
		}

		if (!isCodeNumberNull() ? !getCodeNumber().equals(other.getCodeNumber()) : !other.isCodeNumberNull())
		{
			return true;
		}

		if (!isContentNull() ? !getContent().equals(other.getContent()) : !other.isContentNull())
		{
			return true;
		}

		if (!isCorrectionNoteNull() ? !getCorrectionNote().equals(other.getCorrectionNote()) : !other.isCorrectionNoteNull())
		{
			return true;
		}

		if (!isGroupUuidNull() ? !getGroupUuid().equals(other.getGroupUuid()) : !other.isGroupUuidNull())
		{
			return true;
		}

		if (!isMasterSigDateNull() ? !getMasterSigDate().equals(other.getMasterSigDate()) : !other.isMasterSigDateNull())
		{
			return true;
		}

		if (!isMasterSignatureNull() ? !getMasterSignature().equals(other.getMasterSignature()) : !other.isMasterSignatureNull())
		{
			return true;
		}

		if (!isMasterSignerNameNull() ? !getMasterSignerName().equals(other.getMasterSignerName()) : !other.isMasterSignerNameNull())
		{
			return true;
		}

		if (!isMasterSignerSurnameNull() ? !getMasterSignerSurname().equals(other.getMasterSignerSurname()) : !other.isMasterSignerSurnameNull())
		{
			return true;
		}

		if (!isNoteNull() ? !getNote().equals(other.getNote()) : !other.isNoteNull())
		{
			return true;
		}

		if (!isOrderInGroupNull() ? !getOrderInGroup().equals(other.getOrderInGroup()) : !other.isOrderInGroupNull())
		{
			return true;
		}

		if ( isShipNull() != other.isShipNull() || getShip() != other.getShip())
		{
			return true;
		}

		if (!isSignatureNull() ? !getSignature().equals(other.getSignature()) : !other.isSignatureNull())
		{
			return true;
		}

		if (!isSignatureGroupUuidNull() ? !getSignatureGroupUuid().equals(other.getSignatureGroupUuid()) : !other.isSignatureGroupUuidNull())
		{
			return true;
		}

		if (!isSignerNameNull() ? !getSignerName().equals(other.getSignerName()) : !other.isSignerNameNull())
		{
			return true;
		}

		if (!isSignerRankNull() ? !getSignerRank().equals(other.getSignerRank()) : !other.isSignerRankNull())
		{
			return true;
		}

		if (!isSignerSurnameNull() ? !getSignerSurname().equals(other.getSignerSurname()) : !other.isSignerSurnameNull())
		{
			return true;
		}

		if (!isTypeNull() ? !getType().equals(other.getType()) : !other.isTypeNull())
		{
			return true;
		}

		if (!isUuidNull() ? !getUuid().equals(other.getUuid()) : !other.isUuidNull())
		{
			return true;
		}

		return false;
	}

	public MithraDataObject copy()
	{
		OilLogData copy = new OilLogData();
		this.copyInto(copy, true);
		return copy;
	}

	public MithraDataObject copy(boolean withRelationships)
	{
		OilLogData copy = new OilLogData();
		this.copyInto(copy, withRelationships);
		return copy;
	}

	public String zGetPrintablePrimaryKey()
	{
		String result = "";
		result += "businessDateFrom: "+(isBusinessDateFromNull() ? "null" : PrintablePreparedStatement.timestampFormat.print(zGetBusinessDateFromAsLong()))+ " / ";
		result += "businessDateTo: "+(isBusinessDateToNull() ? "null" : PrintablePreparedStatement.timestampFormat.print(zGetBusinessDateToAsLong()))+ " / ";
		result += "id: "+(""+getId())+ " / ";
		result += "processingDateFrom: "+(isProcessingDateFromNull() ? "null" : PrintablePreparedStatement.timestampFormat.print(zGetProcessingDateFromAsLong()))+ " / ";
		result += "processingDateTo: "+(isProcessingDateToNull() ? "null" : PrintablePreparedStatement.timestampFormat.print(zGetProcessingDateToAsLong()))+ " / ";
		return result;
	}

	public boolean zAsOfAttributesFromEquals(MithraDataObject other)
	{
		boolean result = true;
		OilLogData otherData = (OilLogData) other;
		result &= zGetBusinessDateFromAsLong() == otherData.zGetBusinessDateFromAsLong();
		result &= zGetProcessingDateFromAsLong() == otherData.zGetProcessingDateFromAsLong();
		return result;
	}

	public boolean zAsOfAttributesChanged(MithraDataObject other)
	{
		OilLogData otherData = (OilLogData) other;
		if (zGetBusinessDateToAsLong() != otherData.zGetBusinessDateToAsLong())
		{
			return true;
		}

		if (zGetBusinessDateFromAsLong() != otherData.zGetBusinessDateFromAsLong())
		{
			return true;
		}

		if (zGetProcessingDateToAsLong() != otherData.zGetProcessingDateToAsLong())
		{
			return true;
		}

		if (zGetProcessingDateFromAsLong() != otherData.zGetProcessingDateFromAsLong())
		{
			return true;
		}

		return false;
	}

	public void zWriteDataClassName(ObjectOutput out) throws IOException
	{
	}

	public String zReadDataClassName(ObjectInput in) throws IOException, ClassNotFoundException
	{
		return "al.sybina.ebr.logbook.domain.reladomo.OilLogData";
	}

	public boolean changed(MithraDataObject newData)
	{
		if(zNonPrimaryKeyAttributesChanged(newData))
		{
			return true;
		}
		else
		{
			return zAsOfAttributesChanged(newData);
		}
	}

	public boolean zHasSameNullPrimaryKeyAttributes(MithraDataObject newData)
	{
		return true;
	}

	public MithraObjectPortal zGetMithraObjectPortal(int hierarchyDepth)
	{
		return OilLogFinder.getMithraObjectPortal();
	}

	public MithraObjectPortal zGetMithraObjectPortal()
	{
		return OilLogFinder.getMithraObjectPortal();
	}

	public Number zGetIdentityValue()
	{
		return null;
	}

	public boolean zHasIdentity()
	{
		return false;
	}

	public void zSetIdentity(Number identityValue)
	{
	}

	public String zGetSerializationClassName()
	{
		return "al.sybina.ebr.logbook.domain.reladomo.OilLogData";
	}
}
