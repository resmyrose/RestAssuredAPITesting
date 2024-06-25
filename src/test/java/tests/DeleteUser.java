package tests;

import static io.restassured.RestAssured.baseURI;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.BaseTest;

public class DeleteUser extends BaseTest {
	String firstName = "Anna";
	@Test
	public void deleteUser() {
		baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";
		Response response =  RestAssured
		.given()
			.auth().basic("Numpy@gmail.com", "userapi123")
			.contentType(ContentType.JSON)
		.when()
			.delete("/deleteuser/username/{firstName}",firstName)
		.then()
			.assertThat()
			.statusCode(200)
			.extract()
			.response();
		System.out.println("response as pretty string:"+response.asPrettyString());
	}

}