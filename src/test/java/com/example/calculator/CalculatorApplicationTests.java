package com.example.calculator;

import com.example.calculator.model.AnswerDto;
import com.example.calculator.model.CalculatorDto;
import com.example.calculator.model.SignDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalculatorApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // 데이터 POST helper
    private void saveData(String data) {
        CalculatorDto dto = new CalculatorDto(data);
        String url = format("http://localhost:%d/api/calculator", port);
        ResponseEntity<Long> response = restTemplate.postForEntity(url, dto, Long.class);

        assertEquals(response.getStatusCode().toString(), "200 OK");
    }

    // C버튼 초기화 helper
    private void clearData() {
        CalculatorDto dto = new CalculatorDto();
        String url = format("http://localhost:%d/api/initialization", port);
        ResponseEntity<Long> response = restTemplate.postForEntity(url, dto, Long.class);

        assertEquals(response.getStatusCode().toString(), "200 OK");
    }

    // 부호 반전 POST helper
    private void reverseSign(String sign) {
        SignDto signDto = new SignDto(sign);
        String url = format("http://localhost:%d/api/sign", port);
        ResponseEntity<Long> response = restTemplate.postForEntity(url, signDto, Long.class);

        assertEquals(response.getStatusCode().toString(), "200 OK");
    }

    @DisplayName("덧셈 테스트 1")
    @Test
    void testAdd() {

        // Given
        clearData();
        saveData("11");
        saveData("+");
        saveData("22");

        // When
        String url = format("http://localhost:%d/api/result", port);
        ResponseEntity<AnswerDto> response = restTemplate.getForEntity(url, AnswerDto.class);

        // Then
        assertEquals(response.getBody().toString(), "33.0");
    }

    @DisplayName("뺄셈 테스트 1")
    @Test
    void testSub() {

        // Given
        clearData();
        saveData("11");
        saveData("s");
        saveData("22");

        // When
        String url = format("http://localhost:%d/api/result", port);
        ResponseEntity<AnswerDto> response = restTemplate.getForEntity(url, AnswerDto.class);

        // Then
        assertEquals(response.getBody().toString(), "-11.0");
    }

    @DisplayName("곱셈 테스트 1")
    @Test
    void testMul() {

        // Given
        clearData();
        saveData("11");
        saveData("*");
        saveData("22");

        // When
        String url = format("http://localhost:%d/api/result", port);
        ResponseEntity<AnswerDto> response = restTemplate.getForEntity(url, AnswerDto.class);

        // Then
        assertEquals(response.getBody().toString(), "242.0");
    }

    @DisplayName("나누기 테스트 1")
    @Test
    void testDiv() {

        // Given
        clearData();
        saveData("11");
        saveData("/");
        saveData("22");

        // When
        String url = format("http://localhost:%d/api/result", port);
        ResponseEntity<AnswerDto> response = restTemplate.getForEntity(url, AnswerDto.class);

        // Then
        assertEquals(response.getBody().toString(), "0.5");
    }

    @DisplayName("% 연산 테스트 1")
    @Test
    void testPercentOperation1() {

        // Given
        clearData();
        saveData("11");
        saveData("%");

        // When
        String url = format("http://localhost:%d/api/result", port);
        ResponseEntity<AnswerDto> response = restTemplate.getForEntity(url, AnswerDto.class);

        // Then
        assertEquals(response.getBody().toString(), "0.11");
    }

    @DisplayName("% 연산 테스트 2")
    @Test
    void testPercentOperation2() {

        // Given
        clearData();
        saveData("100");
        saveData("%");

        // When
        String url = format("http://localhost:%d/api/result", port);
        ResponseEntity<AnswerDto> response = restTemplate.getForEntity(url, AnswerDto.class);

        // Then
        assertEquals(response.getBody().toString(), "1.0");
    }

    @DisplayName("- 부호 반전 연산 테스트")
    @Test
    void testReverseSign() {

        // Given
        clearData();
        saveData("1");
        saveData("+");
        saveData("2");

        // When
        reverseSign("-");

        // And
        String url = format("http://localhost:%d/api/result", port);
        ResponseEntity<AnswerDto> response = restTemplate.getForEntity(url, AnswerDto.class);

        // Then
        assertEquals(response.getBody().toString(), "-1.0");
    }

    @DisplayName("C 버튼 테스트 1")
    @Test
    void testCButton() {
        // Given
        saveData("11");
        saveData("/");
        saveData("22");

        // When
        clearData();

        // And
        String url = format("http://localhost:%d/api/result", port);
        ResponseEntity<AnswerDto> response = restTemplate.getForEntity(url, AnswerDto.class);

        // Then
        assertEquals(response.getBody().toString(), "0.0");
    }

    @DisplayName("복합 연산 테스트 1")
    @Test
    void testCompositeComputation() {
        // Given
        clearData();
        saveData("1");
        saveData("+");
        saveData("2");
        saveData("*");
        saveData("3");

        // When
        String url = format("http://localhost:%d/api/result", port);
        ResponseEntity<AnswerDto> response = restTemplate.getForEntity(url, AnswerDto.class);

        // Then
        assertEquals(response.getBody().toString(), "7.0");
    }

    @DisplayName("누진 연산 테스트 1")
    @Test
    void testAccumulateComputation() {
        String url = format("http://localhost:%d/api/result", port);
        // Given
        clearData();
        saveData("1");
        saveData("+");
        saveData("2");
        saveData("*");
        saveData("3");
        ResponseEntity<AnswerDto> response = restTemplate.getForEntity(url, AnswerDto.class);

        // When
        saveData("*");
        saveData("2");
        ResponseEntity<AnswerDto> accumulationResponse = restTemplate.getForEntity(url, AnswerDto.class);

        // Then
        assertEquals(accumulationResponse.getBody().toString(), "14.0");
    }
}
