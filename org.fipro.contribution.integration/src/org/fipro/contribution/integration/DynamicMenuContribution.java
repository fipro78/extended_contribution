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

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.di.extensions.Service;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.viewers.IStructuredSelection;

public class DynamicMenuContribution {
	
	@AboutToShow
	public void aboutToShow(
			List<MMenuElement> items, 
			EModelService modelService,
			MApplication app,
			@Service NavigatorMenuContributionRegistry registry,
			@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection) {
		
		List<NavigatorMenuContributionWrapper> services = registry.getServices(selection.getFirstElement().getClass());

		services.forEach(s -> {
			MHandledMenuItem menuItem = MMenuFactory.INSTANCE.createHandledMenuItem();
			menuItem.setLabel(s.getName());
			menuItem.setTooltip(s.getDescription());
			menuItem.setElementId(s.getId());
			menuItem.setContributorURI("platform:/plugin/org.fipro.contribution.integration");

			List<MCommand> command = modelService.findElements(
					app, 
					"org.fipro.contribution.integration.command.filenavigatoraction", 
					MCommand.class);
			menuItem.setCommand(command.get(0));

			MParameter parameter = MCommandsFactory.INSTANCE.createParameter();
			parameter.setName("contribution.id");
			parameter.setValue(s.getId());
			menuItem.getParameters().add(parameter);

			parameter = MCommandsFactory.INSTANCE.createParameter();
			parameter.setName("contribution.type");
			parameter.setValue(s.getType());
			menuItem.getParameters().add(parameter);

			items.add(menuItem);
		});

	}
}