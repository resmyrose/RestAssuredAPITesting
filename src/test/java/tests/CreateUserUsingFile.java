package tests;

import static io.restassured.RestAssured.baseURI;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import utils.BaseTest;
import utils.FileNameConstants;

public class CreateUserUsingFile extends BaseTest {
	String userFirstName = "Anna";
	//@Test
	public void createUser() {
		try {
			String createUserRequestBody =  FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),"UTF-8");
			baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";
			Response response = 
					RestAssured
					.given()
					.auth().preemptive().basic("Numpy@gmail.com", "userapi123")
					.contentType(ContentType.JSON)
					.body(createUserRequestBody.toString())
				.when()
				.post("/createusers")
			.then()
				.assertThat()
				.statusCode(201)
				.extract()
				.response();
			System.out.println(createUserRequestBody);		
			System.out.println(response.asPrettyString());
			
			// now validating the response object
			JSONArray jsonArry =  JsonPath.read(response.body().asString(), "$..user_first_name");
			System.out.println(jsonArry.get(0));
			userFirstName = (String)jsonArry.get(0);
			Assert.assertEquals(userFirstName, "Anna");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getUserByFirstName() {
		baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";
		Response response =  RestAssured
		.given()
			.auth().basic("Numpy@gmail.com", "userapi123")
			.contentType(ContentType.JSON)
			//.pathParam("userFirstName", userFirstName)
		.when()
			.get("/users/username/{userFirstName}",userFirstName)
		.then()
			.extract()
			.response();
		System.out.println(response.asPrettyString());
		
	}

}
