package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoffeeConfig {
    // default bean name is same as method name
    @Bean("espressoMachineAlpha")
    public CoffeeMachine espressoMachine() {
        return new EspressoMachine();
    }

    @Bean("dripCoffeeMachineAlpha")
    public CoffeeMachine dripCoffeeMachine() {
        return new DripCoffeeMachine();
    }
}
