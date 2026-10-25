package al.sybina.ebr.rcp.propertytesters;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.e4.ui.model.application.ui.MUIElement;

public class ActiveViewPropertyTester extends PropertyTester
{
  @Override
  public boolean test(final Object receiver, final String property, final Object [] args, final Object expectedValue)
  {
    if (receiver instanceof MUIElement)
     {
       MUIElement element = (MUIElement)receiver;

       return element.getElementId().equals(expectedValue);
     }

    return false;
  }
}