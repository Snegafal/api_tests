package ApiReqres;

import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static ApiReqres.Specifications.requestSpecification;
import static ApiReqres.Specifications.responseSpecification200OK;
import static io.restassured.RestAssured.*;

public class ReqresTest {

    private final static String URL = "https://reqres.in/";

    @Test
    public void testCheckAvatarContainsId() {
        Specifications.installSpecification(requestSpecification(URL), responseSpecification200OK());
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        users.forEach(x -> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        users.forEach(x -> Assert.assertTrue(x.getEmail().endsWith("@reqres.in")));
    }
}
