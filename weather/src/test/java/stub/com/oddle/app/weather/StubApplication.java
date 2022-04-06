package stub.com.oddle.app.weather;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@SpringBootApplication
@Configuration
public class StubApplication {

    private static WireMockServer datapowerMickServer;

    private static void startStubServer() {
        WireMockConfiguration wireMockConfiguration = WireMockConfiguration.wireMockConfig()
                .port(9002)
                .bindAddress("localhost")
                .usingFilesUnderClasspath("stubdata");
        datapowerMickServer = new WireMockServer(wireMockConfiguration);

        datapowerMickServer.start();
        stubForPost("/weather", "data/response_LonDon.json");
    }

    public static void stubForPost(String url, String fileNameResponse) {
        datapowerMickServer.stubFor(
            WireMock.get(WireMock.urlEqualTo(url))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBodyFile(fileNameResponse)
                )
        );
    }

    public static void main(String[] args) {
        SpringApplication.run(StubApplication.class, args);
    }

}
