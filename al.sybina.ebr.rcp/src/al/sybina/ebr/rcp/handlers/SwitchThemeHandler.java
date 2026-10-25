package al.sybina.ebr.rcp.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.fx.ui.services.theme.ThemeManager;

public class SwitchThemeHandler {
	@Execute
	public void switchTheme(ThemeManager mgr, @Named("themeId") String themeId) {
		mgr.setCurrentThemeId(themeId);
	}
}
