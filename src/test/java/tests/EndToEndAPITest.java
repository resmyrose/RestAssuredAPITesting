package tests;

import static io.restassured.RestAssured.baseURI;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import utils.BaseTest;
import utils.FileNameConstants;

public class EndToEndAPITest extends BaseTest {
	private static final Logger logger = LogManager.getLogger(EndToEndAPITest.class);
	String userFirstName = "Anna";
	int userId;

	@Test(priority = 1)
	public void createUser() {
		
		logger.info("EndToEndAPITest test execution started...");
		try {
			String createUserRequestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),
					"UTF-8");
			baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";
			Response response = RestAssured
					.given()
						.auth().preemptive().basic("Numpy@gmail.com", "userapi123")
						.contentType(ContentType.JSON)
						.body(createUserRequestBody.toString()).when().post("/createusers")
					.then()
						.assertThat()
						.statusCode(201)
					.extract()
						.response();
			System.out.println(createUserRequestBody);
			System.out.println(response.asPrettyString());

			// now validating the response object
			JSONArray jsonArry = JsonPath.read(response.body().asString(), "$..user_first_name"); // user_id
			System.out.println(jsonArry.get(0));
			userFirstName = (String) jsonArry.get(0);
			Assert.assertEquals(userFirstName, "Anna");
			JSONArray jsonArry2 = JsonPath.read(response.body().asString(), "$..user_id");
			userId = (int) jsonArry2.get(0);
			System.out.println("new user id is: " + userId);

		/*	// get user by first name
			Response response1 = RestAssured
					.given()
						.auth().basic("Numpy@gmail.com", "userapi123")
						.contentType(ContentType.JSON)
					// .pathParam("userFirstName", userFirstName)
					.when()
						.get("/users/username/{userFirstName}", userFirstName)
					.then()
						.extract()
						.response();
			System.out.println(response1.asPrettyString());

			 // update user
			String updateUserRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BLDY),
					"UTF-8");
			Response response2 = RestAssured
					.given()
						.auth().preemptive().basic("Numpy@gmail.com", "userapi123")
						.contentType(ContentType.JSON)
						.body(updateUserRequestBody.toString())
					.when()
						.put("/updateuser/{userId}", userId)
					.then()
						.assertThat()
						 .statusCode(200)
						.extract().response();
			// System.out.println(updateUserRequestBody);
			System.out.println(response2.asPrettyString());

			// now validating the response object
			jsonArry = JsonPath.read(response2.body().asString(), "$..user_id");
			System.out.println("respose json array:" + jsonArry.get(0));
			int id = (int) jsonArry.get(0);
			System.out.println("user id = " + id);
			Assert.assertEquals(userId, id);

			// delete user
			Response response3 = RestAssured
					.given()
						.auth().basic("Numpy@gmail.com", "userapi123")
						.contentType(ContentType.JSON)
					.when()
						.delete("/deleteuser/username/{firstName}", userFirstName)
					.then()
						.assertThat()
						.statusCode(200)
						.extract()
						.response();
			System.out.println("response as pretty string:" + response3.asPrettyString()); */

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test(priority = 1)
	public void getUserByFirstName() {
		baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";
		Response response = RestAssured
				.given()
					.auth().basic("Numpy@gmail.com", "userapi123")
					.contentType(ContentType.JSON) // .pathParam("userFirstName", userFirstName)
				.when()
					.get("/users/username/{userFirstName}", userFirstName)
				.then()
					.extract()
						.response();
		System.out.println(response.asPrettyString());

	}
	  
	@Test(priority = 2)
	public void updateUser() {
		try {
			String updateUserRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BLDY),
					"UTF-8");
			baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";
			Response response = RestAssured
					.given()
						.auth().preemptive().basic("Numpy@gmail.com", "userapi123")
						.contentType(ContentType.JSON).body(updateUserRequestBody.toString())//
					.when()
						.put("/updateuser/{userId}", userId)
					.then()
						.assertThat()
						.statusCode(200)
						.extract().response();
			// System.out.println(updateUserRequestBody);
			System.out.println(response.asPrettyString());

			// now validating the response object JSONArray jsonArry =
			JSONArray jsonArry = JsonPath.read(response.body().asString(), "$..user_id");
			System.out.println("respose json array:" + jsonArry.get(0));
			int id = (int) jsonArry.get(0);
			System.out.println("user id = " + id);
			Assert.assertEquals(userId, id);
		} catch (IOException e) {
			
			// TODO Auto-generated catch block e.printStackTrace();
		}
		logger.info("EndToEndAPITest test execution ended...");
	}

	@Test(priority = 2)
	public void getUserById() {
		baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";
		Response response = RestAssured.given().auth().basic("Numpy@gmail.com", "userapi123")
				.contentType(ContentType.JSON) // .pathParam("userFirstName", userFirstName)
				.when().get("/user/{userId}", userId).then().assertThat()
				.body("user_first_name", Matchers.equalTo("Anna")).extract().response();
		System.out.println("response as pretty string:" + response.asPrettyString());

	}
	  
	@Test(priority = 3)
	public void deleteUser() {
		baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";
		Response response = RestAssured.given().auth().basic("Numpy@gmail.com", "userapi123")
				.contentType(ContentType.JSON).when().delete("/deleteuser/username/{firstName}", userFirstName).then()
				.assertThat().statusCode(200).extract().response();
		System.out.println("response as pretty string:" + response.asPrettyString());
	}
	 

}
