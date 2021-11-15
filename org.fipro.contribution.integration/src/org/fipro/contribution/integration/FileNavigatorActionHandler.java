/*********************************************************************************
 * Copyright (c) 2021 Dirk Fauth.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Dirk Fauth <dirk.fauth@gmail.com> - initial API and implementation
 ********************************************************************************
 */
package org.fipro.contribution.integration;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.extensions.Service;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;

public class FileNavigatorActionHandler {
	
	@Execute
	public void execute(
			@Named("contribution.type") String type,
			@Named("contribution.id") String id,
			@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection,
			@Service NavigatorMenuContributionRegistry registry,
			IEclipseContext context) {
		
		NavigatorMenuContributionWrapper wrapper = registry.getService(type, id);

		if (wrapper != null) {

			IEclipseContext activeContext = context.createChild(type + " NavigatorMenuContribution");
			activeContext.set(wrapper.getType(), selection.getFirstElement());

			try {
				ContextInjectionFactory.invoke(wrapper.getServiceInstance(), PostConstruct.class, activeContext);
			} finally {
				// dispose the context after the execution to avoid memory leaks
				activeContext.dispose();
			}
		}
	}
}