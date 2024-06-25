package tests;

import static io.restassured.RestAssured.baseURI;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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

public class UpdateUserUsingFiles extends BaseTest {
	int userId = 14802;
	@Test
	public void updateUser() {
		try {
			String updateUserRequestBody =  FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BLDY),"UTF-8");
			baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";
			//System.out.println("update user body:"+updateUserRequestBody.toString());
			Response response = 
					RestAssured
					.given()
					.auth().preemptive().basic("Numpy@gmail.com", "userapi123")
					.contentType(ContentType.JSON)
					.body(updateUserRequestBody.toString())
				.when()
				.put("/updateuser/{userId}",userId)
			.then()
				.assertThat()
				//.statusCode(200)
				.extract()
				.response();
			//System.out.println(updateUserRequestBody);		
			System.out.println(response.asPrettyString());
			
			// now validating the response object
			JSONArray jsonArry =  JsonPath.read(response.body().asString(), "$..user_id");
			System.out.println("respose json array:"+jsonArry.get(0));
			int id = (int)jsonArry.get(0);
			System.out.println("user id = "+ id);
			Assert.assertEquals(userId, id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getUserById() {
		baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap";
		Response response =  RestAssured
		.given()
			.auth().basic("Numpy@gmail.com", "userapi123")
			.contentType(ContentType.JSON)
			//.pathParam("userFirstName", userFirstName)
		.when()
			.get("/user/{userId}",userId)
		.then()
			.assertThat()
			.body("user_first_name", Matchers.equalTo("Anna"))
			.extract()
			.response();
		System.out.println("response as pretty string:"+response.asPrettyString());
		
	}

	
}
