package utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.restassured.RestAssured;

public class BaseTest {
	
	private static final Logger logger = LogManager.getLogger(BaseTest.class);
	@BeforeMethod
	public void beforeMethod() {
		// enables logging if validation fails
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	@AfterMethod
	public void afterMehtod(ITestResult result) {
		// 'result' has all the test result information
		if(result.getStatus()==result.FAILURE) {  // if test fails 
			Throwable t = result.getThrowable();
			
			// to print the stack trace
			StringWriter error = new StringWriter(); // converting the stack trace information to string
			t.printStackTrace(new PrintWriter(error));
			
			logger.info(error.toString());
		}
		
	}
}
