/*******************************************************************************
 * Copyright 2010, Topic Map Lab ( http://www.topicmapslab.de )
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.topicmapslab.jexc.utility;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Special tokenizer class with secure string detection
 * 
 * @author Sven Krosse
 * 
 */
public class Tokenizer {

	private static final char SINGLE_QUOTE = '\'';
	private static final char DOUBLE_QUOTE = '"';

	private final String str;
	private final String delim;

	private final List<String> tokens = new LinkedList<String>();
	private Iterator<String> iterator;

	/**
	 * constructor
	 * 
	 * @param str
	 *            the string to tokenize
	 * @param delim
	 *            the delimer
	 */
	public Tokenizer(String str, String delim) {
		this.str = str;
		this.delim = delim;
		tokenize();
	}

	/**
	 * Internal method to tokenize the given string
	 */
	private final void tokenize() {
		StringBuilder builder = new StringBuilder();
		boolean isSingleQuoted = false;
		boolean isDoubleQuoted = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			/*
			 * is single quote and not part of a double quoted string
			 */
			if (SINGLE_QUOTE == c && !isDoubleQuoted) {
				isSingleQuoted = !isSingleQuoted;
				builder.append(c);
				continue;
			}
			/*
			 * is double quote and not part of a single quoted string
			 */
			else if (DOUBLE_QUOTE == c && !isSingleQuoted) {
				isDoubleQuoted = !isDoubleQuoted;
				builder.append(c);
				continue;
			}
			/*
			 * is delimer token but not part of a string
			 */
			else if (delim.equalsIgnoreCase(String.valueOf(c)) && !isDoubleQuoted && !isSingleQuoted) {
				tokens.add(builder.toString());
				builder = new StringBuilder();
				continue;
			}
			builder.append(c);
		}
		tokens.add(builder.toString());
	}

	/**
	 * Checks if there is a next token
	 * 
	 * @return <code>true</code> if there is a next token, <code>false</code>
	 *         otherwise
	 */
	public boolean hasNext() {
		if (iterator == null) {
			iterator = tokens.iterator();
		}
		return iterator.hasNext();
	}

	/**
	 * Returns the next token of this tokenizer
	 * 
	 * @return the next token
	 * @throws NoSuchElementException
	 *             thrown if there is not next element
	 */
	public String nextToken() throws NoSuchElementException {
		if (iterator == null) {
			iterator = tokens.iterator();
		}
		return iterator.next();
	}

	/**
	 * Returns the tokens as a string array
	 * 
	 * @return the tokens as array
	 */
	public String[] asArray() {
		return tokens.toArray(new String[0]);
	}

	/**
	 * Returns the tokens as a string array
	 * 
	 * @param maxItems
	 *            the maximum number of returned items
	 * @return the tokens as array
	 */
	public String[] asArray(int maxItems) {
		String[] array = new String[maxItems < tokens.size() ? maxItems : tokens.size()];
		for (int i = 0; i < tokens.size(); i++) {
			if (i < maxItems) {
				array[i] = tokens.get(i);
			} else {
				array[maxItems - 1] += delim;
				array[maxItems - 1] += tokens.get(i);
			}
		}
		return array;
	}

}
