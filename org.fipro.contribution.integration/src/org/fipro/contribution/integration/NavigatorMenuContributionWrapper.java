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

public class NavigatorMenuContributionWrapper {
	/**
	 * The unique id of the service.
	 */
	private final String id;
	
	/**
	 * The service instance.
	 */
	private final NavigatorMenuContribution instance;

	/**
	 * The name of the service.
	 */
	private final String name;
	
	/**
	 * The description of the service.
	 */
	private final String description;
	
	/**
	 * The model type for which the service is registered and that should be
	 * injected to the {@link PostConstruct} method.
	 */
	private final String type;

	/**
	 * 
	 * @param id                The unique id of the service.
	 * @param instance 			The service instance.
	 * @param name              The name of the service.
	 * @param description       The description of the service.
	 * @param type              The model type for which the service is
	 *                          registered and that should be injected to the
	 *                          {@link PostConstruct} method.
	 */
	public NavigatorMenuContributionWrapper(String id, NavigatorMenuContribution instance, String name, String description, String type) {
		this.id = id;
		this.instance = instance;
		this.name = name;
		this.description = description;
		this.type = type;
	}
	
	/**
	 * 
	 * @return The unique id of the service.
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 
	 * @return The service instance.
	 */
	public NavigatorMenuContribution getServiceInstance() {
		return this.instance;
	}
	
	/**
	 * 
	 * @return The name of the service.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return The description of the service.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 
	 * @return The model type for which the service is registered and that
	 *         should be injected to the {@link PostConstruct} method.
	 */
	public String getType() {
		return type;
	}

}
