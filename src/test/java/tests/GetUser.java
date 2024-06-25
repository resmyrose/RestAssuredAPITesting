package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import io.restassured.response.Response;
import utils.BaseTest;

import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;


public class GetUser extends BaseTest {
	@Test
	public void getAllUsers() {
		
		baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";
		Response response = 
				given().
					auth().preemptive().basic("Numpy@gmail.com", "userapi123").
				when().
					get("/users").
					then().
						assertThat().
						statusCode(200).
						header("Content-Type", "application/json").
						body("user_first_name",hasItem("Numpy")).
						body("user_id[0]",equalTo(1)).
						extract()
						.response();
		Assert.assertTrue(response.getBody().asString().contains("user_first_name"));
		System.out.println(response.getStatusCode());
		System.out.println(response.body().asPrettyString());
			
		
		
	}

}
