package com.rest.webservice.weatherapp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rest.webservice.weatherapp.util.util;

public class WeatherappInterfaceTest {

	@BeforeClass
	public static void onceExecutedBeforeAll() {
		System.out.println("@BeforeClass: onceExecutedBeforeAll");
		util.initialize();
	}

	// Test Case for OS dependent System properties
	@Test
	public void OSSystemProp() throws AssertionError {
		System.out.println("Test Case 01");
		System.out.println("Test Case for OS dependent System properties");
		assertNotNull(util.property_path);
	}

	// Test Case whether CityInfo File exist or not
	@Test
	public void CityInfoFileExist() throws AssertionError {
		System.out.println("Test Case 02");
		System.out.println("Test Case whether CityInfo File exist or not");
		System.out.println(util.CityFileInfo);
		File file = new File(util.CityFileInfo);
		assertTrue(file.exists());
		System.out.println("City Information File  : " + util.CityFileInfo + " Exist");

	}

	// Testing the Access Authorization Status Code
	@Test
	public void givenURLDoesNotExists__then401IsReceived() throws ClientProtocolException, IOException {
		System.out.println("Test Case 03");
		System.out.println("Test Case for URL Accesstoken authorization Test");
		System.out.println(util.getProperty("openweathermapURL") + "&units=metric&APPID=" + util.getProperty("AppID"));
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(
				util.getProperty("openweathermapURL") + "&units=metric&APPID=" + util.getProperty("AppID"));
		HttpResponse response = httpClient.execute(getRequest);
		System.out
				.println("response.getStatusLine().getStatusCode()  value : " + response.getStatusLine().getStatusCode()
						+ " " + "HttpStatus.SC_UNAUTHORIZED  " + HttpStatus.SC_UNAUTHORIZED);
		assertThat(response.getStatusLine().getStatusCode(), not(equalTo(HttpStatus.SC_UNAUTHORIZED)));

	}

	// Testing the Media Type
	@Test
	public void givenRequestWithAcceptHeader() throws ClientProtocolException, IOException {
		System.out.println("Test Case 04");
		String jsonMimeType = "application/json";
		System.out.println("Test Case for Media Type in Response");
		System.out.println(util.getProperty("openweathermapURL") + "&units=metric&APPID=" + util.getProperty("AppID"));
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(
				util.getProperty("openweathermapURL") + "&units=metric&APPID=" + util.getProperty("AppID"));
		HttpResponse response = httpClient.execute(getRequest);
		System.out.println(
				"response.getEntity().getContentType() value: " + response.getEntity().getContentType().getValue());
		String[] conttype = response.getEntity().getContentType().getValue().split(";");
		String mimeType = conttype[0];
		System.out.println("Media Type value: " + mimeType);
		assertEquals(jsonMimeType, mimeType);
	}

}
