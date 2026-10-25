package al.sybina.ebr.logbook.domain.reladomo;
import com.gs.fw.finder.Operation;
import java.util.*;
public class OilLogList extends OilLogListAbstract
{
	public OilLogList()
	{
		super();
	}

	public OilLogList(int initialSize)
	{
		super(initialSize);
	}

	public OilLogList(Collection c)
	{
		super(c);
	}

	public OilLogList(Operation operation)
	{
		super(operation);
	}
}
