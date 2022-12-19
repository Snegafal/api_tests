import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiMethods {

    private final String OPENWEATHERMAP_APIKEY = "4d2055e52f8dedbce377c3206d2931cc";
    private final int ID_STPETERSBURG = 498817;

    @Test
    public void testStatusCodeAndStatusLine() {
        RestAssured.baseURI = "https://reqres.in/";

        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET, "/api/users/4");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 200 OK");
    }

    @Test
    public void testSuccessfulRegistration() {
        RestAssured.baseURI = "https://reqres.in/";
        RequestSpecification httpRequest = RestAssured.given();

        JSONObject requestParameters = new JSONObject();
        requestParameters.put("email", "eve.holt@reqres.in");
        requestParameters.put("password", "pistol");
        httpRequest.header("Content-type", "application/json");
        httpRequest.body(requestParameters.toJSONString());

        Response response = httpRequest.request(Method.POST, "/api/register");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.statusLine(), "HTTP/1.1 200 OK");
    }

    @Test
    public void testUnsuccessfulRegistration() {
        RestAssured.baseURI = "https://reqres.in/";
        RequestSpecification httpRequest = RestAssured.given();

        JSONObject requestedParameters = new JSONObject();
        requestedParameters.put("email", "sydney@fife");

        httpRequest.header("Content-type", "application/json");
        httpRequest.body(requestedParameters.toJSONString());
        Response response = httpRequest.request(Method.POST, "/api/register");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.statusLine(), "HTTP/1.1 400 Bad Request");
        Assert.assertEquals(response.jsonPath().get("error"), "Missing password");
    }

    @Test
    public void testHeaders() {
        RestAssured.baseURI = "https://api.openweathermap.org";
        RequestSpecification httpRequest = RestAssured.given();

        Response response = httpRequest.request(Method.GET, "/data/2.5/weather?id="
                + ID_STPETERSBURG + "&appid=" + OPENWEATHERMAP_APIKEY);

        Assert.assertEquals(response.header("Content-Length"), "482");
        Assert.assertEquals(response.header("X-Cache-Key"), "/data/2.5/weather?id=498817");
        Assert.assertEquals(response.header("Access-Control-Allow-Methods"), "GET, POST");
    }

    @Test
    public void getAllHeaders() {
        RestAssured.baseURI = "https://api.openweathermap.org";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET, "/data/2.5/weather?id="
                + ID_STPETERSBURG + "&appid=" + OPENWEATHERMAP_APIKEY);

        Headers allHeaders = response.headers();
        for (Header header : allHeaders) {
            System.out.println(header.getName() + ": " + header.getValue());
        }
    }

    @Test
    public void testResponseContainsCityName() {
        RestAssured.baseURI = "https://api.openweathermap.org";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET, "/data/2.5/weather?id="
                + ID_STPETERSBURG + "&appid=" + OPENWEATHERMAP_APIKEY);

        String responseBody = response.getBody().asString();

        Assert.assertTrue(responseBody.contains("Saint Petersburg"));
    }
}
