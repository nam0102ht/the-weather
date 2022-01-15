package com.oddle.app.weather;

import com.oddle.app.weather.api.RequestData;
import com.oddle.app.weather.api.RequestHistory;
import com.oddle.app.weather.api.RequestSave;
import com.oddle.app.weather.common.JsonCommon;
import groovy.util.logging.Slf4j;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeatherApplicationTests {
	@LocalServerPort
	private int port;

	@Order(1)
	@Test
	void callApi_Current() {
		String baseURI = "http://localhost:" + port + "/weather";
		Collection<String> collection = Arrays.stream("Mexico,London,Tokyo,Moscow,Beijing,Lima".split(",")).collect(Collectors.toList());
		collection.forEach(v -> {
			try {
				Thread.sleep(100);
				Response response = RestAssured.given()
						.basePath("")
						.when()
						.contentType("application/json")
						.log()
						.body()
						.get(baseURI + "?city=" + v)
						.then()
						.assertThat()
						.statusCode(HttpStatus.OK.value())
						.extract()
						.response();

				Assertions.assertNotNull(response);
				Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
				Assertions.assertEquals(v, response.body().path("message.data.name"));
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		});
	}

	@Order(2)
	@Test
	void saveWeather_Test() throws IOException {
		String baseURI = "http://localhost:" + port + "/weather";
		RestAssured.baseURI = baseURI;
		RestAssured.port = port;
		RequestSave requestSave = new RequestSave();
		requestSave.setTimestamp(System.currentTimeMillis());
		String str = "{\"coord\":{\"lon\":120.7198,\"lat\":15.0646},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02n\"}],\"base\":\"stations\",\"main\":{\"temp\":297.67,\"feels_like\":298.21,\"temp_min\":297.67,\"temp_max\":297.67,\"pressure\":1015,\"humidity\":78,\"sea_level\":1015,\"grnd_level\":1014},\"visibility\":10000,\"wind\":{\"speed\":1.57,\"deg\":77,\"gust\":4.06},\"clouds\":{\"all\":11},\"dt\":1642165282,\"sys\":{\"type\":2,\"id\":2008256,\"country\":\"PH\",\"sunrise\":1642112775,\"sunset\":1642153518},\"timezone\":28800,\"id\":1699805,\"name\":\"Mexico\",\"cod\":200}";
		requestSave.setData(JsonCommon.convertStrToJson(str));
		try {
			Thread.sleep(100);
			Response response = RestAssured.given()
					.basePath("")
					.when()
					.contentType("application/json")
					.body(requestSave)
					.post(baseURI)
					.then()
					.log()
					.body()
					.assertThat()
					.statusCode(HttpStatus.OK.value())
					.extract()
					.response();

			Assert.notNull(response.body());
			Assertions.assertEquals("Success", response.body().path("message.status"));
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	@Order(3)
	@Test
	void updateWeather_Test() throws IOException {
		String baseURI = "http://localhost:" + port + "/weather";
		RestAssured.baseURI = baseURI;
		RestAssured.port = port;
		String str = "{\"coord\":{\"lon\":120.7198,\"lat\":15.0646},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02n\"}],\"base\":\"stations\",\"main\":{\"temp\":297.67,\"feels_like\":298.21,\"temp_min\":297.67,\"temp_max\":297.67,\"pressure\":1015,\"humidity\":78,\"sea_level\":1015,\"grnd_level\":1014},\"visibility\":10000,\"wind\":{\"speed\":1.57,\"deg\":77,\"gust\":4.06},\"clouds\":{\"all\":11},\"dt\":1642165282,\"sys\":{\"type\":2,\"id\":2008256,\"country\":\"PH\",\"sunrise\":1642112775,\"sunset\":1642153518},\"timezone\":28800,\"id\":1699805,\"name\":\"Mexico\",\"cod\":200}";
		RequestSave requestSave = new RequestSave();
		requestSave.setTimestamp(System.currentTimeMillis());
		requestSave.setData(JsonCommon.convertStrToJson(str));
		AtomicLong id = new AtomicLong();
		try {
			Response responseSave = RestAssured.given()
					.basePath("")
					.when()
					.contentType("application/json")
					.body(requestSave)
					.post(baseURI)
					.then()
					.log()
					.body()
					.assertThat()
					.statusCode(HttpStatus.OK.value())
					.extract()
					.response();

			id.set(Long.parseLong(responseSave.body().path("message.data.id").toString()));

			Thread.sleep(100);
			RequestData requestUpdate = RequestData.builder()
					.timestamp(1642099628888L)
					.id(id.get())
					.nameCity("Mexico")
					.data(JsonCommon.convertStrToJson(str))
					.build();
			Response response = RestAssured.given()
					.basePath("")
					.when()
					.contentType("application/json")
					.body(requestUpdate)
					.put(baseURI)
					.then()
					.log()
					.body()
					.assertThat()
					.statusCode(HttpStatus.OK.value())
					.extract()
					.response();

			Assert.notNull(response.body());
			Assertions.assertEquals(1642099628888L, Long.parseLong(response.body().path("message.data.timestamp").toString()));
			Assertions.assertEquals(str, response.body().path("message.data.data"));
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	@Order(4)
	@Test
	void deleteWeather_Test() throws IOException {
		String baseURI = "http://localhost:" + port + "/weather";
		RestAssured.baseURI = baseURI;
		RestAssured.port = port;
		AtomicLong id = new AtomicLong();
		String str = "{\"coord\":{\"lon\":120.7198,\"lat\":15.0646},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02n\"}],\"base\":\"stations\",\"main\":{\"temp\":297.67,\"feels_like\":298.21,\"temp_min\":297.67,\"temp_max\":297.67,\"pressure\":1015,\"humidity\":78,\"sea_level\":1015,\"grnd_level\":1014},\"visibility\":10000,\"wind\":{\"speed\":1.57,\"deg\":77,\"gust\":4.06},\"clouds\":{\"all\":11},\"dt\":1642165282,\"sys\":{\"type\":2,\"id\":2008256,\"country\":\"PH\",\"sunrise\":1642112775,\"sunset\":1642153518},\"timezone\":28800,\"id\":1699805,\"name\":\"Mexico\",\"cod\":200}";
		RequestSave requestSave = new RequestSave();
		requestSave.setTimestamp(System.currentTimeMillis());
		requestSave.setData(JsonCommon.convertStrToJson(str));
		try {
			Response responseSave = RestAssured.given()
					.basePath("")
					.when()
					.contentType("application/json")
					.body(requestSave)
					.post(baseURI)
					.then()
					.log()
					.body()
					.assertThat()
					.statusCode(HttpStatus.OK.value())
					.extract()
					.response();

			id.set(Long.parseLong(responseSave.body().path("message.data.id").toString()));

			Thread.sleep(100);
			Response response = RestAssured.given()
					.basePath("")
					.when()
					.contentType("application/json")
					.delete(baseURI + "?id=" + id.get())
					.then()
					.log()
					.body()
					.assertThat()
					.statusCode(HttpStatus.OK.value())
					.extract()
					.response();

			Assert.notNull(response.body());
			Assertions.assertEquals("Success", response.body().path("message.status"));
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}


	@Order(5)
	@Test
	void callApi_Histories() {
		String baseURI = "http://localhost:" + port + "/weather/histories";
		RequestHistory requestHistory = new RequestHistory();
		requestHistory.setFrom(1642099623100L);
		requestHistory.setTo(1642099624100L);
		requestHistory.setFromPage(0);
		requestHistory.setToPage(50);
		try {
			Thread.sleep(100);
			Response response = RestAssured.given()
					.basePath("")
					.when()
					.contentType("application/json")
					.body(requestHistory)
					.get(baseURI)
					.then()
					.assertThat()
					.statusCode(HttpStatus.OK.value())
					.extract()
					.response();

			Assertions.assertNotNull(response);
			Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
			Assertions.assertNotNull(response.body().path("message.data"));
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	@Order(6)
	@Test
	void callApi_Current_NotFound() {
		String baseURI = "http://localhost:" + port + "/weather";
		try {
			Thread.sleep(100);
			Response response = RestAssured.given()
					.basePath("")
					.when()
					.contentType("application/json")
					.log()
					.body()
					.get(baseURI + "?city=HoChiMinh")
					.then()
					.assertThat()
					.statusCode(HttpStatus.OK.value())
					.extract()
					.response();

			Assertions.assertNotNull(response);
			Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
			Assertions.assertEquals("city not found", response.getBody().path("message.data.message"));
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

}
