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
package org.fipro.contribution.extended;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.fipro.contribution.integration.NavigatorMenuContribution;
import org.osgi.service.component.annotations.Component;

@Component(property = { "name = File Name", "description = Show the name of the selected file" })
public class FileNameContribution implements NavigatorMenuContribution {

	@PostConstruct
	public void showFileSize(IFile file, Shell shell) {
		MessageDialog.openInformation(
				shell,
				"File size",
				String.format("The size of the selected file is " + file.getName()));
	}
}
