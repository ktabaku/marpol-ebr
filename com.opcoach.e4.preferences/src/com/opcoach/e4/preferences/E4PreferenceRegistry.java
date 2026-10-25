/*******************************************************************************
 * Copyright (c) 2014 OPCoach.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     OPCoach - initial API and implementation
 *******************************************************************************/
package com.opcoach.e4.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.core.services.log.Logger;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;

@Creatable
@Singleton
public class E4PreferenceRegistry {

	public static final String PREFS_PAGE_XP = "com.opcoach.e4.preferences.e4PreferencePages"; // $NON-NLS-1$
	public static final String PREF_STORE_PROVIDER = "com.opcoach.e4.preferences.e4PreferenceStoreProvider"; // $NON-NLS-1$
	protected static final String ELMT_PAGE = "page"; // $NON-NLS-1$
	protected static final String ATTR_ID = "id"; // $NON-NLS-1$
	protected static final String ATTR_CATEGORY = "category"; // $NON-NLS-1$
	protected static final String ATTR_CLASS = "class"; // $NON-NLS-1$
	protected static final String ATTR_NAME = "name"; // $NON-NLS-1$

	protected static final String ATTR_PLUGIN_ID = "pluginId"; // $NON-NLS-1$
	protected static final String ATTR_ID_IN_WBCONTEXT = "idInWorkbenchContext"; // $NON-NLS-1$

	@Inject
	protected Logger logger;

	@Inject
	protected IEclipseContext context;

	@Inject
	protected IExtensionRegistry registry;

	private PreferencesFx preferencesFx;


	public PreferencesFx getPreferencesFx() {
		if (preferencesFx != null)
			return preferencesFx;

		List<Category> childrenNodes = new ArrayList<Category>();
		IContributionFactory factory = context.get(IContributionFactory.class);

		for (IConfigurationElement elmt : registry.getConfigurationElementsFor(PREFS_PAGE_XP)) {
			String bundleId = elmt.getNamespaceIdentifier();
			if (!elmt.getName().equals(ELMT_PAGE)) {
				logger.warn("unexpected element: {0}", elmt.getName());
				continue;
			} else if (isEmpty(elmt.getAttribute(ATTR_ID)) || isEmpty(elmt.getAttribute(ATTR_NAME))) {
				logger.warn("missing id and/or name: {}", bundleId);
				continue;
			}
			if (elmt.getAttribute(ATTR_CLASS) != null) {
				E4PreferencePage page = null;
				try {
					String prefPageURI = getClassURI(bundleId, elmt.getAttribute(ATTR_CLASS));
					Object object = factory.create(prefPageURI, context);
					if (!(object instanceof E4PreferencePage)) {
						logger.error("Expected instance of E4PreferencePage: {0}", elmt.getAttribute(ATTR_CLASS));
						continue;
					}
					page = (E4PreferencePage) object;
					Category cat = page.getCategory();
					if(cat!=null) childrenNodes.add(cat);

				} catch (ClassNotFoundException e) {
					logger.error(e);
					continue;
				}
			}
		}
		preferencesFx = PreferencesFx.of(E4PreferenceRegistry.class, childrenNodes.toArray(new Category[childrenNodes.size()]))
				.persistApplicationState(true).persistWindowState(true).saveSettings(true).debugHistoryMode(false).buttonsVisibility(true);
		return preferencesFx;
	}

	private String getClassURI(String definingBundleId, String spec) throws ClassNotFoundException {
		if (spec.startsWith("platform:")) {
			return spec;
		} // $NON-NLS-1$
		return "bundleclass://" + definingBundleId + '/' + spec;
	}

	private boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

}
