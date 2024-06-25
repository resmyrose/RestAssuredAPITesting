package tests;

import static io.restassured.RestAssured.baseURI;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import pojos.User;
import pojos.UserAddress;
import utils.BaseTest;
import utils.FileNameConstants;

public class CreateUserUsingPojos extends BaseTest {

	@Test
	public void createUser() {

		try {
			//for validating json schema
			String jsonSchema = FileUtils.readFileToString(new File(FileNameConstants.JSON_SCHEMA), "UTF-8");
			
			// pojos
			UserAddress userAddress = new UserAddress("plot-345", "street", "WA", "USA", "685858");
			User user = new User("Anna", "Austin", "1237654335", "AnnaAustin@gmail.com", userAddress);

			// serializing the object
			ObjectMapper objectMapper = new ObjectMapper();

			String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
			System.out.println("request body:" + requestBody);

			// de-serialization
			User userDetails = objectMapper.readValue(requestBody, User.class);
			System.out.println("user first name = " + userDetails.getUser_first_name());
			System.out.println("plot number = " + userDetails.getUserAddress().getPlotNumber());
			System.out.println("schema: = " + jsonSchema);

			baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";

			Response response = RestAssured.given().auth().preemptive().basic("Numpy@gmail.com", "userapi123")
					.contentType(ContentType.JSON).body(requestBody).when().post("/createusers").then().assertThat()
					.statusCode(201).extract().response();

			int userId = response.path("user_id");
			System.out.println("user id = " + userId);
			Response response2 = RestAssured.given().auth().basic("Numpy@gmail.com", "userapi123")
					.contentType(ContentType.JSON).when().get("/user/{userId}", userId)

					.then().body(JsonSchemaValidator.matchesJsonSchema(jsonSchema)).extract().response();
			// System.out.println(response2.asPrettyString());

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
