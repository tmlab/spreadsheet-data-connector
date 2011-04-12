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
package de.topicmapslab.jexc.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.topicmapslab.jexc.exception.JeXcException;

/**
 * @author Sven Krosse
 * 
 */
public class JeXcConnection {

	private Workbook workBook;
	private boolean xlsx;
	private String path;
	private final boolean copyRemoteFile;
	private File temporaryFile;
	private boolean isRemote = false;

	/**
	 * constructor
	 * 
	 * @param path
	 *            the path of the remote or local file
	 * @param copyRemoteFile
	 *            flag indicates if the file located remote should be copied as temporary file
	 * @throws JeXcException
	 *             thrown if stream content is invalid
	 */
	private JeXcConnection(final String path, final boolean copyRemoteFile) throws JeXcException {
		this.path = path;
		this.copyRemoteFile = copyRemoteFile;
		loadWorkbook();
	}

	/**
	 * Internal method to load the workbook instance
	 * 
	 * @param stream
	 *            the stream
	 * @throws JeXcException
	 *             thrown if workbook type is invalid
	 */
	private void loadWorkbook() throws JeXcException {
		InputStream stream;
		/*
		 * check if path is local file
		 */
		File file = new File(path);
		if (file.exists()) {
			try {
				stream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				throw new JeXcException("Unknown source location! Location '" + path + "' cannot be located!", e);
			}
		}
		/*
		 * open connection to remote file
		 */
		else {
			try {
				URL url = new URL(path);
				URLConnection connection = url.openConnection();
				stream = connection.getInputStream();
				/*
				 * check if the option flag is set indicates that a remote file should be copied as temporary file
				 */
				if ( copyRemoteFile ){	
					/*
					 * destroy the old temporary file
					 */
					if ( temporaryFile != null ){
						temporaryFile.deleteOnExit();
					}
					/*
					 * create temporary file
					 */
					temporaryFile = File.createTempFile("jecx", ".xls");
					/*
					 * copy content of remote file
					 */
					OutputStream os = new FileOutputStream(temporaryFile); 
					int c = stream.read();
					while ( c != -1 ){
						os.write(c);
						c = stream.read();
					}
					os.flush();
					os.close();
					stream.close();
					ConnectionUtls.disconnect(connection);
					/*
					 * open stream to temporary file
					 */
					stream = new FileInputStream(temporaryFile);					
				}
				isRemote = true;
			} catch (Exception e) {
				throw new JeXcException("Unknown source location! Location '" + path + "' cannot be located!", e);
			}
		}
		/*
		 * try to load as XLSX
		 */
		try {
			workBook = new XSSFWorkbook(stream);
			xlsx = true;
		} catch (Exception e) {
			Logger.getLogger(getClass()).debug("Content is not valid XLSX.", e);
			/*
			 * try to load as XLS
			 */
			try {
				workBook = new HSSFWorkbook(stream);
				xlsx = false;
			} catch (Exception ex) {
				Logger.getLogger(getClass()).debug("Content is not valid XLS.", ex);
				throw new JeXcException("Content is invalid.!",ex);
			}
		}
		
		try{
			stream.close();
		}catch(IOException ex){
			Logger.getLogger(getClass()).debug("Cannot close stream.", ex);
			throw new JeXcException("Cannot close stream.!");
		}
	}

	/**
	 * Open a new connection to a Xlsx or Xls document
	 * 
	 * @param path
	 *            the file path or URL
	 * @param copyRemoteFile
	 *            flag indicates if the file located remote should be copied as temporary file
	 * @return the connection and never <code>null</code>
	 * @throws JeXcException
	 *             thrown if connection cannot be established or stream content is invalid
	 */
	public static JeXcConnection openConnection(final String path, final boolean copyRemoteFile) throws JeXcException {
		return new JeXcConnection(path, copyRemoteFile);
	}

	/**
	 * Returns the workbook for excel
	 * 
	 * @return the workBook
	 */
	Workbook getWorkBook() {
		return workBook;
	}

	/**
	 * Indicates if the given content is valid xlsx or xls
	 * 
	 * @return <code>true</code> if content is XLSX, <code>false</code> if content is XLS.
	 */
	public boolean isXlsx() {
		return xlsx;
	}

	/**
	 * Method creates a new statement which can be used to query the underlying XLSX document.
	 * 
	 * @return the created statement
	 * @throws JeXcException
	 *             thrown if statement cannot create
	 */
	public JeXcStatement createStatement() throws JeXcException {
		// loadWorkbook();
		return new JeXcStatement(this);
	}

	/**
	 * Closing the connection
	 * 
	 * @throws JeXcException
	 */
	public void close() throws JeXcException {
		workBook = null;
	}

	/**
	 * Returns the time stamp reflecting the last modification of the source.
	 * 
	 * @return the time stamp of last modification
	 */
	public Calendar getLastModificationOfSource() throws JeXcException {
		/*
		 * check if path is local file
		 */
		File file = new File(path);
		long timeInMills;
		if (file.exists()) {
			timeInMills = file.lastModified();
		}
		/*
		 * open connection to remote file
		 */
		else {
			try {
				URL url = new URL(path);
				URLConnection con = url.openConnection();
				timeInMills = con.getLastModified();
				ConnectionUtls.disconnect(con);
			} catch (Exception e) {
				throw new JeXcException("Unknown source location! Location '" + path + "' cannot be located!", e);
			}
		}
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeInMills);
		return c;
	}

	/**
	 * Refreshing the connection to Xlsx file
	 * 
	 * @throws JeXcException
	 *             thrown if reload fails
	 */
	public void refresh() throws JeXcException {
		loadWorkbook();
	}

	/**
	 * Method returns a boolean value indicates if the source of the JeXc connection is a remote file.
	 * @return <code>true</code> if the source is remote, <code>false</code> otherwise
	 */
	public boolean isRemote() {
		return isRemote;
	}
	
}
