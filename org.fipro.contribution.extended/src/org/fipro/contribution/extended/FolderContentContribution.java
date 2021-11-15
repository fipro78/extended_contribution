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

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.fipro.contribution.integration.NavigatorMenuContribution;
import org.osgi.service.component.annotations.Component;

@Component(property = { "name = Folder Content", "description = Show the number of files in the selected folder" })
public class FolderContentContribution implements NavigatorMenuContribution {

	@PostConstruct
	public void showFolderContent(IFolder folder, Shell shell) {
		URI uri = folder.getRawLocationURI();
		Path path = Paths.get(uri);
		try {
			long count = Files.list(path).count();
			MessageDialog.openInformation(
					shell,
					"Folder Content",
					String.format("The folder contains %d files", count));
		} catch (IOException e) {
			MessageDialog.openError(
					shell, 
					"Failed to retrieve the folder content", 
					"Exception occured on retrieving the folder content: " + e.getLocalizedMessage());
		}
	}

}
