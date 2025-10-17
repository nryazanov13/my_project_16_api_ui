package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class TestBase {

    @BeforeAll
    static void setAll(){
        RestAssured.baseURI = "https://demoqa.com/";
    }

   @BeforeEach
    void setUp(){

   }

}
