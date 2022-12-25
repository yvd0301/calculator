package com.example.calculator.controller;

import com.example.calculator.model.AnswerDto;
import com.example.calculator.model.CalculatorDto;
import com.example.calculator.model.SignDto;
import com.example.calculator.utils.Calculator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.EmptyStackException;

@RequiredArgsConstructor
@RestController
public class CalculatorController {

    // 피연산자, 연산자 입력 POST 메소드
    @PostMapping("/api/calculator")
    public ResponseEntity postCalculator(@RequestBody CalculatorDto data) {

        Calculator calculator = Calculator.getInstance();
        calculator.dataPush(data.getData());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // +, - 부호 반전 POST 메소
    @PostMapping("/api/sign")
    public ResponseEntity postSign(@RequestBody SignDto data) {
        Calculator calculator = Calculator.getInstance();
        calculator.reverseSign(data.getSign());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // = 버튼, 계산 결과 GET 메소드
    @GetMapping("/api/result")
    @ResponseBody
    public AnswerDto getResult() {
        double computedResult = 0.0;
        Calculator calculator = Calculator.getInstance();
        try {
            computedResult = calculator.calculate(calculator.dataPull());
        } catch (EmptyStackException e) {
            AnswerDto answerDto = new AnswerDto(computedResult);
            return answerDto;
        }

        calculator.setPreviousResult(computedResult);
        AnswerDto answerDto = new AnswerDto(computedResult, calculator.getEquation());

        return answerDto;
    }

    // C 버튼, 상태 초기화 POST 메소드
    @PostMapping("/api/initialization")
    public ResponseEntity postInitialization() {
        Calculator calculator = Calculator.getInstance();
        calculator.initialize();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
