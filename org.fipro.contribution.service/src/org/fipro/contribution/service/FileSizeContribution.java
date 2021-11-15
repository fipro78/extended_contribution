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
package org.fipro.contribution.service;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.fipro.contribution.integration.NavigatorMenuContribution;
import org.osgi.service.component.annotations.Component;

@Component(property = { "name = File Size", "description = Show the size of the selected file" })
public class FileSizeContribution implements NavigatorMenuContribution {

	@PostConstruct
	public void showFileSize(IFile file, Shell shell) {
		URI uri = file.getRawLocationURI();
		Path path = Paths.get(uri);
		try {
			long size = Files.size(path);
			MessageDialog.openInformation(
					shell,
					"File size",
					String.format("The size of the selected file is %d bytes", size));
		} catch (IOException e) {
			MessageDialog.openError(
					shell, 
					"Failed to retrieve the file size", 
					"Exception occured on retrieving the file size: " + e.getLocalizedMessage());
		}
	}
}
