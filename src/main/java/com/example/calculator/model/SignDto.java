package com.example.calculator.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignDto {
    private String sign;

    public SignDto(String sign) {
        this.sign = sign;
    }
}
