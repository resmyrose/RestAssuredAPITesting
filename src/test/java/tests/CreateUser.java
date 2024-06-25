package tests;

import static io.restassured.RestAssured.baseURI;
import static org.testng.Assert.ARRAY_MISMATCH_TEMPLATE;
import static org.testng.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import utils.BaseTest;
import utils.FileNameConstants;
public class CreateUser extends BaseTest {
	int usrId = 14356;
	@Test
	public void createUser() throws IOException {
		
		
		JSONObject user = new JSONObject();
		JSONObject userAddress = new JSONObject();
		
		user.put("user_first_name", "Resmy");
		user.put("user_last_name", "Austin");
		user.put("user_contact_number", "6759933540");
		user.put("user_email_id", "resmyr@gmail.com");
		user.put("userAddress", userAddress);
		
		userAddress.put("plotNumber", "Plot-5A");
		userAddress.put("street", "StreetOne");
		userAddress.put("state", "WA");
		userAddress.put("country", "USA");
		userAddress.put("zipCode", 386968);
		//for validating json schema
		String jsonSchema = FileUtils.readFileToString(new File(FileNameConstants.JSON_SCHEMA), "UTF-8");
		
		baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap"; 
		Response response =  RestAssured
			.given()
				.auth().preemptive().basic("Numpy@gmail.com", "userapi123")
				.contentType(ContentType.JSON)
				.body(user.toString())
				//.log().body()
			.when()
				.post("/createusers")
			.then()
				.body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
				.assertThat()
				//.log().ifValidationFails()
				.contentType(ContentType.JSON)
				.statusCode(201)
				.body("user_first_name",Matchers.equalTo("Resmy"))
				.extract()
				.response();
		//System.out.println(response.body().asPrettyString());
		if(response.statusCode()==409) {
			System.out.println("Already existing value");
		}
		
		//success; user created with "user_id": 14356
		
		// returns the userId of the user created 
		usrId = response.path("user_id");
	}
	
	//@Test
	public void getUserById() {
		
		baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";
		Response response =  RestAssured
		.given()
		.auth().preemptive().basic("Numpy@gmail.com", "userapi123")
			.contentType(ContentType.JSON)
			.pathParam("userId", usrId)
		.when()
			.get("/user/{userId}")
		.then()
			.extract()
			.response();
		System.out.println();
		System.out.println(response.body().asPrettyString());
	}
	

}
