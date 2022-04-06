package blackbox.com.ntnn.app.weather.stepDef;

import com.fasterxml.jackson.databind.JsonNode;
import com.ntnn.app.weather.common.JsonCommon;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@CucumberContextConfiguration
public class StepDef {
    private static final String DOWNSTREAM_URL = "http://localhost:8099/weather";
    private static final String KEY = "ZTgxMmYzNjM0NmVhMjkwNzM0NjJmZjFlMDVlMmI0NmI=";

    private Response response;
    private JsonNode dataRsp;

    @Before
    public void init() throws InterruptedException {
        Thread.sleep(8000);
    }

    @Given("Send Request with key {string} message to API")
    public void sendRequest(String nameCity) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8099/weather?city=London")
                .method("GET", null)
                .build();
        response = client.newCall(request).execute();
    }

    @When("Service receive message from API")
    public void receiveMsg() throws IOException {
        dataRsp = JsonCommon.convertStrToJson(response.body().string());
    }

    @Then("Response must be {string}")
    public void compare(String expectation) {
        Assertions.assertEquals(200, dataRsp.get("code").asInt());
        Assertions.assertEquals("Success", dataRsp.get("message").get("status").asText());
        Assertions.assertNotNull(dataRsp.get("message").get("data"));
    }
}
