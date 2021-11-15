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

import javax.inject.Named;

import org.eclipse.core.resources.IResource;
import org.eclipse.e4.core.di.annotations.Evaluate;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Service;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ResourceExpression {
	
	@Evaluate
	public boolean evaluate(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection,
			@Service NavigatorMenuContributionRegistry registry) {
		
		return (selection != null && selection.size() == 1
				&& (selection.getFirstElement() instanceof IResource)
				&& !registry.getServices(selection.getFirstElement().getClass()).isEmpty());
	}
}
