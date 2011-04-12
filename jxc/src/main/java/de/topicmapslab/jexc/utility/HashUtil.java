/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.jexc.utility;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Utility class to support other hash implementations, like gnu.trove
 * 
 * @author Sven Krosse
 * 
 */
public class HashUtil {

	/**
	 * the found class used as {@link Set} implementation
	 */
	private static Class<?> setClass = null;
	/**
	 * the found class used as {@link Map} implementation
	 */
	private static Class<?> mapClass = null;

	/**
	 * Method try to initialize a gnu.trove.THashSet if the library is located
	 * in the class path
	 * 
	 * @param <T>
	 *            the type of elements
	 * @return the created set
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> getHashSet() {
		try {
			return (Set<T>) getSetClass().newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		return new HashSet<T>();
	}

	/**
	 * Method try to initialize a gnu.trove.THashSet if the library is located
	 * in the class path
	 * 
	 * @param initial
	 *            the initial set
	 * @param <T>
	 *            the type of elements
	 * @return the created set
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> getHashSet(Set<T> initial) {
		if (initial == null) {
			return getHashSet();
		}
		try {
			return (Set<T>) getSetClass().getConstructor(Collection.class)
					.newInstance(initial);
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (SecurityException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		}
		return new HashSet<T>(initial);
	}

	/**
	 * Returns the set class to use. If the method is called at the first time,
	 * the set class will be located in the class path.
	 * 
	 * @return the setClass the set class
	 */
	private static Class<?> getSetClass() {
		if (setClass == null) {
			try {
				setClass = Class.forName("gnu.trove.THashSet");
			} catch (ClassNotFoundException e) {
				setClass = HashSet.class;
			} catch (IllegalArgumentException e) {
				setClass = HashSet.class;
			} catch (SecurityException e) {
				setClass = HashSet.class;
			}
		}
		return setClass;
	}

	/**
	 * Method try to initialize a gnu.trove.THashMap if the library is located
	 * in the class path
	 * 
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 * @return the created map
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> getHashMap() {
		try {
			return (Map<K, V>) getMapClass().newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		return new HashMap<K, V>();
	}

	/**
	 * Method try to initialize a gnu.trove.THashMap if the library is located
	 * in the class path
	 * 
	 * @param initial
	 *            the initial map
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 * @return the created map
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> getHashMap(Map<K, V> initial) {
		if (initial == null) {
			return getHashMap();
		}
		try {
			return (Map<K, V>) getMapClass().getConstructor(Map.class)
					.newInstance(initial);
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (SecurityException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		}
		return new HashMap<K, V>(initial);
	}

	/**
	 * Returns the map class to use. If the method is called at the first time,
	 * the set class will be located in the class path.
	 * 
	 * @return the setClass the set class
	 */
	private static Class<?> getMapClass() {
		if (mapClass == null) {
			try {
				mapClass = Class.forName("gnu.trove.THashMap");
			} catch (ClassNotFoundException e) {
				mapClass = HashMap.class;
			} catch (IllegalArgumentException e) {
				mapClass = HashMap.class;
			} catch (SecurityException e) {
				mapClass = HashMap.class;
			}
		}
		return mapClass;
	}

}
