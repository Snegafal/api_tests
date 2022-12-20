package ApiReqres;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static ApiReqres.Specifications.*;
import static io.restassured.RestAssured.*;

public class ReqresTest {

    private final static String URL = "https://reqres.in/";

    @Test
    public void testCheckAvatarContainsId() {
        installSpecification(requestSpecification(URL), responseSpecification200OK());
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        users.forEach(x -> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        users.forEach(x -> Assert.assertTrue(x.getEmail().endsWith("@reqres.in")));
    }

    @Test
    public void testSuccessfulRegistration() {
        installSpecification(requestSpecification(URL), responseSpecification200OK());

        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in", "pistol");

        SuccessRegister successRegister = given()
                .body(user)
                .when()
                .post("/api/register")
                .then().log().all()
                .extract().as(SuccessRegister.class);

        Assert.assertNotNull(successRegister.getId());
        Assert.assertNotNull(successRegister.getToken());

        Assert.assertEquals(id, successRegister.getId());
        Assert.assertEquals(token, successRegister.getToken());
    }

    @Test
    public void testUnsuccessRegister() {
        installSpecification(requestSpecification(URL), responseSpecification400Error());
        Register user = new Register("sydney@fife", "");

        UnsuccessRegister unsuccessRegister = given()
                .body(user)
                .when()
                .post("/api/register")
                .then().log().all()
                .extract().as(UnsuccessRegister.class);

        Assert.assertEquals("Missing password", unsuccessRegister.getError());
    }

    @Test
    public void testYearsSorting() {
        installSpecification(requestSpecification(URL), responseSpecification200OK());
        List<ResourcesData> resources = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ResourcesData.class);

        List<Integer> years = resources.stream().map(ResourcesData::getYear).collect(Collectors.toList());
        List<Integer> yearsSorted = years.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(years, yearsSorted);
    }
}
