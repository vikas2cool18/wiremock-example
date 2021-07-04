package com.udacity.timezones.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

class TimeZoneServiceTest {

    static WireMockServer wireMock = new WireMockServer(wireMockConfig().port(8089));
    private static String serverPath = "http://localhost:8089";
    private static String fizzBuzzPath = "/api/timezone/Europe/";

    TimeZoneService timeZoneService;

    @BeforeAll
    static void setup() {
        wireMock.start();
    }
    @AfterAll
    static void cleanup() {
        wireMock.stop();
    }

    @BeforeEach
    void init() {
        wireMock.resetAll();
        timeZoneService = new TimeZoneService(serverPath);
    }

    @Test
    void getAvailableTimezoneText() {
        String expected="amsterdam, brussels";
        wireMock.stubFor(
                WireMock.get(urlEqualTo("/api/timezone/Europe"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("[\"Amsterdam\", \"Andorra\", \"Astrakhan\", \"Athens\"]")
                        )
        );
        String response = timeZoneService.getAvailableTimezoneText("Europe");
        assertTrue(response.contains("Available timezones in Europe are Amsterdam, Andorra"));
    }
}