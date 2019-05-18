package com.rest.webservice.weatherapp.interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.rest.webservice.weatherapp.util.util;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class WeatherappInterface {

	
	//Variable declaration
	public static BufferedReader br = null;
	public static String line = "";
	public static Map<String, String> hm = null;
	public static ArrayList<String> arrli = null;
	public static Set<String> keys = null;
	public StringBuilder Stbd;
	public static List<List<String>> batch;
	public static String City_id;
	public static File dataFileNew;
	public static int Country_count = 0;
	public static String weatherDetails;
	public static String City_Name;
	public static int City_Id;
	public static int Weather_id;
	public static String Weather_main;
	public static String Weather_desc;
	public static String Weather_icon;
	public static String WeatherFileName;
	public static String WeatherHeader;

	public static void main(String[] args) {
		WeatherappInterface WInterface = new WeatherappInterface();
		
		//property initialization  
		util.initialize();
		//Archiving Output folder
		util.archiveFile(util.property_path + File.separator + "Output");
		//City information file reading process
		city_info();
		//Weather Service Request and Response processing
		WInterface.process();
		//Archiving file contain city information
		util.archiveFile(util.property_path + File.separator + "City_File");
	}

	public void process() {
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			arrli = new ArrayList<String>();
			keys = hm.keySet();
			for (String key : keys) {
				arrli.add(key);
				util.writeIntoLog("CITY_ID values as key : " + key);
			}
			util.writeIntoLog("ArrayList length = No of City_ID = : " + arrli.size());
			//Processing the request in batch for 20 IDS 
			batch = Lists.partition(arrli, 20);
			for (List<String> list : batch) {
				Stbd = new StringBuilder();
				;
				City_id = null;
				for (String str : list) {
					Stbd.append(str + ",");
				}
				util.writeIntoLog("Batch city_id values: " + Stbd.toString());
				City_id = Stbd.toString().substring(0, Stbd.toString().length() - 1);
				util.writeIntoLog("Batch city_id values: " + City_id);
				util.writeIntoLog(util.getProperty("openweathermapURL") + City_id + "&units=metric&APPID="
						+ util.getProperty("AppID"));
				HttpGet getRequest = new HttpGet(util.getProperty("openweathermapURL") + City_id
						+ "&units=metric&APPID=" + util.getProperty("AppID"));
				getRequest.addHeader("accept", "application/json");
				HttpResponse response = httpClient.execute(getRequest);

				if (response.getStatusLine().getStatusCode() != 200) {
					throw new RuntimeException(
							"Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
				}
				HttpEntity entity = response.getEntity();
				String output = EntityUtils.toString(entity);
				util.writeIntoLog("Response --" + output);
				JSONObject obj = new JSONObject(output);
				//Processing Response from Weather service
				processResponse(obj);

			}
			httpClient.getConnectionManager().shutdown();

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static void city_info() {

		hm = new HashMap<String, String>();
		util.writeIntoLog(
				"File Name which has City information : " + util.CityFileInfo + "  Csv split by " + util.delimiter);
		try {
			br = new BufferedReader(new FileReader(util.CityFileInfo));
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] city = line.split(util.delimiter);
				hm.put(city[0], city[1]);
				util.writeIntoLog("CITY_ID = " + city[0] + " , CITY_NAME=" + city[1]);
			}
			util.writeIntoLog("Map values " + hm.toString());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			util.writeIntoLog("FileNotFoundException error msg " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			util.writeIntoLog("IOException error msg " + e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					util.writeIntoLog("IOException error msg " + e.getMessage());
				}
			}
		}

	}

	public void processResponse(JSONObject obj) throws ParseException, JSONException {

		WeatherHeader = "City_Name" + "," + "City_Id" + "," + "Weather_id" + "," + "Weather_main" + "," + "Weather_desc"
				+ "," + "Weather_icon" + "\n";
		Country_count = (Integer) obj.get("cnt");
		util.writeIntoLog(" Total Country Response from Weather Service : " + Country_count);
		if (Country_count > 0) {
			JSONArray arrcnwth = (JSONArray) obj.get("list");
			util.writeIntoLog(" No Of country Response in List of array : " + arrcnwth.length());
			for (int i = 0; i < arrcnwth.length(); i++) {
				JSONObject jobjcntr = (JSONObject) arrcnwth.get(i);
				City_Name = (String) jobjcntr.get("name");
				City_Id = (Integer) jobjcntr.get("id");
				JSONArray weather = (JSONArray) jobjcntr.get("weather");
				WeatherFileName = util.property_path + File.separator + "Output" + File.separator + City_Name + "_"
						+ City_Id + ".csv";
				util.writeIntoLog("Weather File Name : " + WeatherFileName);
				dataFileNew = util.createDataFile(WeatherFileName);

				StringBuffer stbf = new StringBuffer();
				for (int j = 0; j < weather.length(); j++) {
					util.writeIntoLog(" List of Weather array : " + weather.length());
					JSONObject jobweather = (JSONObject) weather.get(j);
					Weather_id = (Integer) jobweather.get("id");
					Weather_main = (String) jobweather.get("main");
					Weather_desc = (String) jobweather.get("description");
					Weather_icon = (String) jobweather.get("icon");
					util.writeIntoLog(" Weather Report : " + Weather_id + " " + Weather_main + " " + Weather_desc + " "
							+ Weather_icon);
					weatherDetails = City_Name + "," + City_Id + "," + Weather_id + "," + Weather_main + ","
							+ Weather_desc + "," + Weather_icon + "\n";
					stbf.append(weatherDetails);
					util.writeIntoLog(" String Buffer value : " + stbf.toString());
				}
				writeDataFile(WeatherFileName, WeatherHeader + stbf.toString());
			}
		}

	}

	public void writeDataFile(String filePath, String data) {

		Writer writeFile;

		util.writeIntoLog("Weather File Path --" + filePath);
		util.writeIntoLog("Weather File Data --" + data);

		if (dataFileNew.exists()) {
			try {
				writeFile = new FileWriter(dataFileNew);
				writeFile.write(data);
				writeFile.close();
			} catch (FileNotFoundException e) {
				util.writeIntoLog(e.getMessage());
				System.out.println(e.getMessage());
				return;
			} catch (IOException e) {
				util.writeIntoLog(e.getMessage());
				System.out.println(e.getMessage());
				return;
			}
		}

	}

}
