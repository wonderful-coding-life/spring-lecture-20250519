package com.example.demo;

import org.springframework.stereotype.Component;

@Component("espressoMachine")
public class EspressoMachine implements CoffeeMachine {
    @Override
    public String brew() {
        return "Brewing coffee with Espresso Machine";
    }
}
