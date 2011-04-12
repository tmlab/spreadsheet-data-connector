package de.topicmapslab.jexc.connection;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URLConnection;

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

/**
 * @author Sven Krosse
 */
public class ConnectionUtls {

	/**
	 * Method try to disconnect the established connection by using the specific methods of different implementation, because of the fact that their is no standardized access method.
	 * 
	 * @param connection
	 *            the connection to close
	 */
	public static final void disconnect(URLConnection connection) {
		/*
		 * is JAVA HTTP URL connection
		 */
		if (connection instanceof HttpURLConnection) {
			((HttpURLConnection) connection).disconnect();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		/*
		 * is SUN implementation
		 */
		else {
			Class<? extends URLConnection> clazz = connection.getClass();
			try {
				Method method = clazz.getMethod("close");
				method.invoke(connection);
				Thread.sleep(1);
			} catch (Exception e) {
				// CANNOT CLOSE
			}
		}
	}

}
