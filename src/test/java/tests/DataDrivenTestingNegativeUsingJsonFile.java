package tests;

import static io.restassured.RestAssured.baseURI;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import pojos.User;
import pojos.UserAddress;
import utils.FileNameConstants;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class DataDrivenTestingNegativeUsingJsonFile {
  @Test(dataProvider = "getTestData")
  //public void dataDrivenTestingUsingJson(List<User> testData) throws IOException {
  public void dataDrivenTestingUsingJson(LinkedHashMap<String, Object>testData) throws IOException {
	  
	  
	  LinkedHashMap<String, Object> userAddressMap =(LinkedHashMap<String, Object>)testData.get("userAddress");
	 // System.out.println("user address pojo==="+userAddressMap);
	  UserAddress userAddress = new UserAddress(userAddressMap.get("plotNumber").toString(), userAddressMap.get("street").toString(),
			  userAddressMap.get("state").toString(), userAddressMap.get("country").toString(),
			  userAddressMap.get("zipCode").toString());
	 
		User user = new User(testData.get("user_first_name").toString(), testData.get("user_last_name").toString(), 
				testData.get("user_contact_number").toString(), testData.get("user_email_id").toString(), userAddress);

		// serializing the object
		ObjectMapper objectMapper = new ObjectMapper();
		
			String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
			System.out.println("request body:" + requestBody);
			
			//for validating json schema
			String jsonSchema = FileUtils.readFileToString(new File(FileNameConstants.JSON_SCHEMA), "UTF-8");
		
		
		baseURI = FileNameConstants.BASE_URI;

		Response response = RestAssured
				.given()
					.auth().preemptive().basic(FileNameConstants.USER_NAME, FileNameConstants.PASSWORD)
					.contentType(ContentType.JSON)
					.body(requestBody)
				.when()
					.post("/createusers")
				.then()
					//.body(JsonSchemaValidator.matchesJsonSchema(jsonSchema)).extract().response()
					.assertThat()
					.statusCode(anyOf(is(409),is(404),is(400)))
					//.statusCode(409).and().body("status",Matchers.equalTo("409 CONFLICT"))
					.extract()
					.response();
		
		//System.out.println(requestBody);
		System.out.println(response.asPrettyString());
		
		
		
		// deleting the user
		/*Response response2 = RestAssured.given().auth().basic(FileNameConstants.USER_NAME, FileNameConstants.PASSWORD)
				.contentType(ContentType.JSON).when().delete("/deleteuser/username/{firstName}", userFirstName).then()
				.assertThat().statusCode(200).extract().response();
		System.out.println("response as pretty string:" + response2.asPrettyString());*/

  }
  @DataProvider(name= "getTestData")
  public Object[] getTestData() {
	  Object[] obj = null;
	  try {
		String jsonTestData =  FileUtils.readFileToString(new File(FileNameConstants.JSON_TEST_DATA_NEGATIVE),"UTF-8");
		  JSONArray jsonArray =  JsonPath.read(jsonTestData, "$");  // "$" separates json objects from each other
		  obj = new Object[jsonArray.size()];
		  for(int i=0;i<jsonArray.size();i++) {
			  obj[i] = jsonArray.get(i);
		  }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return obj;
  }
}
