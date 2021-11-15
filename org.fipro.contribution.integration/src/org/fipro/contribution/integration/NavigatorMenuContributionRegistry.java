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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.ClassUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.Logger;
import org.osgi.service.log.LoggerFactory;

@Component(service = NavigatorMenuContributionRegistry.class)
public class NavigatorMenuContributionRegistry {

	LoggerFactory factory;
	Logger logger;

	private ConcurrentHashMap<String, Map<String, NavigatorMenuContributionWrapper>> registry = new ConcurrentHashMap<>();
	
	@Reference(
			cardinality = ReferenceCardinality.MULTIPLE,
			policy = ReferencePolicy.DYNAMIC)
	protected void bindService(NavigatorMenuContribution service, Map<String, Object> properties) {
		
		String className = getClassName(service, properties);
		if (className != null) {
			Map<String, NavigatorMenuContributionWrapper> services = 
					this.registry.computeIfAbsent(className, key -> new ConcurrentHashMap<String, NavigatorMenuContributionWrapper>());
			
			String id = (String) properties.getOrDefault("id", service.getClass().getName());
			if (!services.containsKey(id)) {
				services.put(id,
						new NavigatorMenuContributionWrapper(
								id, 
								service, 
								(String) properties.getOrDefault("name", service.getClass().getSimpleName()), 
								(String) properties.getOrDefault("description", null),
								className));
			} else {
				if (this.logger != null) {
					this.logger.error("A NavigatorMenuContribution with the ID {} already exists!", id);
				} else {
					System.out.println("A NavigatorMenuContribution with the ID " + id + " already exists!");
				}
			}
		} else {
			if (this.logger != null) {
				this.logger.error("Unable to extract contribution class name for NavigatorMenuContribution {}", service.getClass().getName());
			} else {
				System.out.println("Unable to extract contribution class name for NavigatorMenuContribution " + service.getClass().getName());
			}
		}
	}

	protected void unbindService(NavigatorMenuContribution service, Map<String, Object> properties) {
		String className = getClassName(service, properties);
		String id = (String) properties.getOrDefault("id", service.getClass().getName());
		if (className != null) {
			Map<String, NavigatorMenuContributionWrapper> services = this.registry.getOrDefault(className, new HashMap<>());
			services.remove(id);
		}
	}
	
	/**
	 * Get all service wrapper objects that are registered for the given classes.
	 * 
	 * @param clazz The class for which the wrappers are requested.
	 * @return All service wrapper objects that are registered for the
	 *         given classes.
	 */
	@SuppressWarnings("unchecked")
	public List<NavigatorMenuContributionWrapper> getServices(Class<?> clazz) {
		HashSet<String> classNames = new LinkedHashSet<>();
		if (clazz != null) {
			classNames.add(clazz.getName());
			List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(clazz);
			classNames.addAll(allInterfaces.stream().map(Class::getName).collect(Collectors.toList()));
		}
		
		return classNames.stream()
				.filter(Objects::nonNull)
				.flatMap(name -> this.registry.getOrDefault(name, new HashMap<>()).values().stream())
				.collect(Collectors.toList());
	}

	/**
	 * 
	 * @param className The class name for which the service is registered.
	 * @param id        The id of the service.
	 * @return The service wrapper.
	 */
	public NavigatorMenuContributionWrapper getService(String className, String id) {
		return this.registry.getOrDefault(className, new HashMap<>()).get(id);
	}
	
	/**
	 * Extracts the class name for which the service should be
	 * registered. Returns the first parameter of the method annotated with
	 * {@link PostConstruct} .
	 * 
	 * @param service The service for which the contribution class name
	 *                      should be returned.
	 * @param properties    The component properties map of the
	 *                      service object.
	 * @return The contribution class name for which the service should be
	 *         registered.
	 */
	private String getClassName(NavigatorMenuContribution service, Map<String, Object> properties) {
		String className = null;
		
		// find method annotated with @PostConstruct
		Class<?> contributionClass = service.getClass();
		Method[] methods = contributionClass.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(PostConstruct.class)) {
				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes.length > 0) {
					if (Collection.class.isAssignableFrom(parameterTypes[0])) {
						// extract generic information for List support
						Type[] genericParameterTypes = method.getGenericParameterTypes();
						if (genericParameterTypes[0] instanceof ParameterizedType) {
							Type[] typeArguments = ((ParameterizedType)genericParameterTypes[0]).getActualTypeArguments();
							className = typeArguments.length > 0 ? typeArguments[0].getTypeName() : null;
						}
					} else {
						className = parameterTypes[0].getName();
					}
					break;
				}
			}
		}

		return className;
	}

	// get the OSGi LoggerFactory injected as the DS 1.4 logger injection is not yet supported in PDE
	
	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	void setLogger(LoggerFactory factory) {
		this.factory = factory;
		this.logger = factory.getLogger(getClass());
	}

	void unsetLogger(LoggerFactory loggerFactory) {
		if (this.factory == loggerFactory) {
			this.factory = null;
			this.logger = null;
		}
	}
}
