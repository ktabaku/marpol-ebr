/**
 * 
 */
package al.sybina.ebr.S57Library.fiedsRecords.descriptive;

import al.sybina.ebr.S57Library.files.S57ByteBuffer;
import al.sybina.ebr.S57Library.records.S57LogicalRecord;

/**
 * @author cyrille
 *
 */
public class S57DDRField0000 extends S57DDRField{
	
	private String listOfPairedTags;
	public S57DDRField0000(int index, S57ByteBuffer buffer, S57LogicalRecord record) throws Exception{
		super(index, buffer, record);
	    //read the external fileName (unused in S-57)
	    @SuppressWarnings("unused")
		String externalFileName = buffer.getFieldAsString(TERMINATORS);
	    //get the arrayDescriptor
	    listOfPairedTags = buffer.getFieldAsString(TERMINATORS);
	}

	public String toString(){
		return super.toString() + String.format(" paired tags:%s", listOfPairedTags);
	}
}
