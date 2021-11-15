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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.fipro.contribution.integration.NavigatorMenuContribution;
import org.osgi.service.component.annotations.Component;

@Component(property = { "name = File Copy", "description = Create a copy of the selected file" })
public class FileCopyContribution implements NavigatorMenuContribution {

	@PostConstruct
	public void copyFile(IFile file, Shell shell) {
		URI uri = file.getRawLocationURI();
		Path path = Paths.get(uri);
		Path toPath = Paths.get(path.getParent().toString(), "CopyOf_" + file.getName());
		try {
			Files.copy(path, toPath);
			
			// refresh the navigator
			file.getParent().refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (IOException | CoreException e) {
			MessageDialog.openError(
					shell, 
					"Failed to copy the file size", 
					"Exception occured on copying the file: " + e.getLocalizedMessage());
		}
	}

}
