package com.example.calculator.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerDto {
    private double result;
    private String equation;

    public AnswerDto(Double result) {
        this.result = result;
    }

    public AnswerDto(Double result, String equation){
        this.result = result;
        this.equation = equation;
    }

    @Override
    public String toString(){
        String val = String.valueOf(this.result);
        return val;
    }
}
