package com.example.calculator.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
@NoArgsConstructor
public class CalculatorDto {
    private String data;

    public CalculatorDto(String data) {
        this.data = data;
    }
}
