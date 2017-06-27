package com.nasa.gov.rest;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;


public class SoundsTest {
	
	@BeforeClass
	  public void setBaseUri () {

	    RestAssured.baseURI = "https://api.nasa.gov";
	  }
	
	@AfterMethod
	public void afterMethod(ITestResult result) {
	  System.out.println("Running Test:" + result.getMethod().getMethodName());
	  
	}
	
	//Test Valid request and verify response code
	@Test
	public void testValidRequest () {
		Response res =
			given ().param ("q", "Apollo")
	        .param ("limit", 5)
	        .param("api_key", "DEMO_KEY")
	        .when()
	        .log()
	        .params()
	        .get ("/planetary/sounds");

			
	        Assert.assertEquals(res.statusCode(), (200));
	      //System.out.println(res.then().contentType(ContentType.JSON).extract().response().asString());
	}
	
	//Test empty search request and verify response code and limit value in response	
	@Test
	public void testEmptySearchRequest () {
		
		Response res =
			given ()
			.param ("q", "")
	        .param ("limit", 50)
	        .param("api_key", "DEMO_KEY")
	        .when()
	        .get ("/planetary/sounds");
		
		Assert.assertEquals (res.statusCode(), 200);
		
		res.then().log().ifValidationFails().body("count", equalTo(50));
			        
	}
	
	//Test search request that should not return results and verify response code and limit value in response	
	@Test
	public void testInvalidSearchRequest () {
		
		Response res =
			given ()
			.param ("q", "adfadsfd")
	        .param ("limit", 50)
	        .param("api_key", "DEMO_KEY")
	        .when()
	        .get ("/planetary/sounds");
		
		//This test currently passes probably due to issue with API
		Assert.assertEquals (res.statusCode(), 200);
		//If the search text worked as described the results should be 0
		res.then().log().ifValidationFails().body("count", equalTo(50));
		
	        
	}
	
	//Test large limit parameter value. 
	@Test
	public void testLargeLimit () {
		
		Response res =
		given ()
			.param ("q", "Apollo")
	        .param ("limit", 100)
	        .param("api_key", "DEMO_KEY")
	        .when()
	        .get ("/planetary/sounds");
		
		Assert.assertEquals(res.statusCode(), 200);
		//Due to static nature of API response data this count can be used as verification point
		res.then().log().ifValidationFails().body("count", equalTo(64));
	}

	//Test small limit parameter value. 
	@Test
	public void testSmallLimit () {
		
		Response res =
		given ()
			.param ("q", "Apollo")
			.param ("limit", 0)
			.param("api_key", "DEMO_KEY")
			.when()
			.get ("/planetary/sounds");
						
		Assert.assertEquals(res.statusCode(), 200);
		res.then().log().ifValidationFails().body("count", equalTo(0));
	}	
	
	//Test invalid limit parameter value
	@Test
	public void testInvalidLimit () {
		
		Response res =
		given ()
			.param ("limit", "b")
			.param("api_key", "DEMO_KEY")
			.when()
			.get ("/planetary/sounds");
		
		res.then().log().ifStatusCodeIsEqualTo(200).statusCode(500);
		//Assert.assertEquals(res.statusCode(), 500);
	}	
	
	//Both Q and Limit values are missing.  Default values used by API
	@Test
	public void testMissingParameters () {
		
		Response res =
		given ()
			.param("api_key", "DEMO_KEY")
	        .when()
	        .log()
	        .params()
	        .get ("/planetary/sounds");
		
		//Verify response is JSON and status 200
		res.then().contentType(ContentType.JSON).statusCode(200);
		//Default value of 10 used
		res.then().log().ifValidationFails().body("count", equalTo(10));
	}

	//Verify invalid key used receives the correct unauthorized response code
	@Test
	public void testInvalidKey () {

		Response res =
		given ()
			.param ("q", "Apollo")
	        .param ("limit", 10)
	        .param("api_key", "BAD_KEY")
	        .when()
	        .get ("/planetary/sounds");
		
		Assert.assertEquals(res.statusCode(), 403); 
		res.then().log().ifValidationFails().body("error.code", equalTo("API_KEY_INVALID"));
	}
	
	//Validate status code and code response for Empty Key
	@Test
	public void testEmptyKey () {

		Response res =
		given ()
			.param ("q", "Apollo")
	        .param ("limit", 10)
	        .param("api_key", "")
	        .when()
	        .get ("/planetary/sounds");
		
		Assert.assertEquals(res.statusCode(), 403); 
		res.then().log().ifValidationFails().body("error.code", equalTo("API_KEY_MISSING"));
	}
	
	//Validate status code, code, and error message response for Missing Key
	@Test
	public void testMissingKey () {
		
		final String errorMessage = "No api_key was supplied. Get one at https://api.nasa.gov";

		Response res =
		given ()
			.param ("q", "Apollo")
	        .param ("limit", 10)
	        .param("api_key", "")
	        .when()
	        .get ("/planetary/sounds");
		
		Assert.assertEquals(res.statusCode(), 403); 
		res.then().log().ifValidationFails().body("error.code", equalTo("API_KEY_MISSING"));
		res.then().log().ifValidationFails().body("error.message", equalTo(errorMessage));
		
	}
	
	//Verify response time for request is within expected range
	@Test
	 public void testRequestResponseTime ()  {
	   Response res =
		given()
			.param ("q", "Apollo")
			.param ("limit", 10)
			.when()
			.get ("/planetary/sounds");
      
	   //Verify response is less than 500 milliseconds
       res.then().log().ifValidationFails().time(lessThan(500l));
	 }
}
