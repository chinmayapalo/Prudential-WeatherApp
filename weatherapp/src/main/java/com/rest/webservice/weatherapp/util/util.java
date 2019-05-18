package com.rest.webservice.weatherapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.sun.javafx.PlatformUtil;

public class util {
	public static File logFile;

	public util() {

	}

	public static String property_path;
	public static String logFileName;
	public static String delimiter;
	public static String outputDirectory;
	public static String dataFileFullPath;
	public static StringWriter sw = null;
	public static String CityFileInfo;

	
	//For Initialization process
	public static void initialize() {
		
		//Setting env variable for different OS
		if (PlatformUtil.isWindows()) {
			System.out.println("We are using Windows OS. Windows properties are loading. ");
			property_path = System.getenv("PROPERTY_NAME");
		} else {
			System.out.println("We are not using Windows OS. Properties are loading for the OS. ");
			System.out.println(System.getProperty("os.name"));
			property_path = System.getProperty("user.dir");
		}
		delimiter = getProperty("service.dataColumnSeperator");
		if (delimiter == null || "".equals(delimiter)) {
			delimiter = ",";
		}
		dataFileFullPath = property_path;
		System.out.println("The file.DataFilePath is " + dataFileFullPath);
		logFileName = dataFileFullPath + File.separator + getProperty("OutputFolderName") + File.separator
				+ getProperty("LogFileName");

		createLogFile();

		CityFileInfo = dataFileFullPath + File.separator + getProperty("CityFileFolderName") + File.separator
				+ getProperty("CityFileName");
	}

	public static String getProperty(String propertyName) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(
					property_path + File.separatorChar + "Configuration" + File.separatorChar + "loader.properties"));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return props.getProperty(propertyName);

	}

	public static void createLogFile() {
		logFile = createDataFile(logFileName);
	}

	public static File createDataFile(String dataFileFullName) {
		boolean dirFlag = false;
		System.out.println("Data file full name is " + dataFileFullName);
		File convDir = new File(dataFileFullName);
		try {
			dirFlag = convDir.createNewFile();
			System.out.println("** Data File Created :" + dataFileFullName);
		} catch (SecurityException Se) {
			System.out.println("Error while creating File in Java:" + Se);
		} catch (IOException e) {
			System.out.println("File Error :" + e.getMessage());
			e.printStackTrace();
		}
		return convDir;
	}

	public static void writeIntoLog(String msg) {
		if (!(logFile.exists())) {
			createLogFile();
		} else {
			FileWriter writer;
			try {
				writer = new FileWriter(logFile, true);
				writer.append(msg);
				writer.append("\n");
				writer.close();
				System.out.println(msg);
			} catch (FileNotFoundException e) {
				System.out.println("File Not Found Error :" + e.getMessage());
				return;
			} catch (IOException e) {
				System.out.println("File Error :" + e.getMessage());
				e.printStackTrace();
			}

		}
	}

	public static void archiveFile(String Folderpath) {
		util.writeIntoLog("Archiving Folder Name : " + Folderpath);
		File directory = new File(Folderpath);
		File[] listOfFiles = directory.listFiles();
		util.writeIntoLog("Total no of FIles inside Archiving Folder : " + listOfFiles.length);
		for (File file : listOfFiles) {
			util.writeIntoLog("Archiving File Name : " + file.getName());
			try {
				util.moveFile(Folderpath, file.getName(), util.property_path + File.separator + "Archive");
			} catch (Exception e) {
				util.sw = new StringWriter();
				e.printStackTrace(new PrintWriter(util.sw));
				util.writeIntoLog(util.sw.toString());
			}
		}
	}

	public static void moveFile(String filePath, String fileName, String archiveFolderName) {
		util.writeIntoLog("Source File Path is-- " + filePath);
		util.writeIntoLog("SOurce File Name is-- " + fileName);
		util.writeIntoLog("ArchiveFolderName is-- " + archiveFolderName);
		StringWriter errors = new StringWriter();
		String destFileName = "";
		File sourceFolder = new File(filePath);
		File destinationFolder = new File(archiveFolderName);
		util.writeIntoLog("sourceFolder Name is-- " + sourceFolder);
		util.writeIntoLog("destinationFolder Name is-- " + destinationFolder);
		File[] listOfFiles = sourceFolder.listFiles();
		try {
			for (File file : listOfFiles) {
				if (file.isFile()) {
					if (file.toString().contains(fileName.substring(0, fileName.indexOf(".")))) {
						writeIntoLog("File Name-->" + file.getName());
						try {
							destFileName = fileName.substring(0, fileName.indexOf(".")) + "_" + new Date().getTime()
									+ fileName.substring(fileName.indexOf("."), fileName.length());
							writeIntoLog("Destination File Name-->" + destFileName);
						} catch (Exception e) {
							e.printStackTrace(new PrintWriter(errors));
							writeIntoLog(errors.toString());
							return;
						}
						File destFile = new File(archiveFolderName + File.separatorChar + destFileName);
						writeIntoLog(archiveFolderName + File.separatorChar + destFileName);
						FileUtils.copyFile(file, destFile);

						writeIntoLog(file.getName() + " is backed up as " + destFile.getName());
						FileUtils.forceDelete(file.getCanonicalFile());
						writeIntoLog(file.getName() + " is deleted");

					}

				} else {
					FileUtils.moveToDirectory(file, destinationFolder, true);
				}
			}
		} catch (IOException ex) {

			ex.printStackTrace(new PrintWriter(errors));
			writeIntoLog(errors.toString());
		}

	}

}
