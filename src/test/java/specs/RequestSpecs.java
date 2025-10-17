package specs;

import io.restassured.specification.RequestSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;

public class RequestSpecs {
    public static RequestSpecification baseRequestSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .contentType(JSON);
}
