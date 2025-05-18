package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoffeeMaker {
    @Value("${barista}")
    private String barista;

    @Value("${brew.count}")
    private Integer brewCount;

    @Autowired
    @Qualifier("espressoMachine")
    private CoffeeMachine coffeeMachine;

    public void setCoffeeMachine(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    @PostConstruct
    public void makeCoffee() {
        System.out.println(coffeeMachine.brew());
    }

    @Autowired
    private List<CoffeeMachine> coffeeMachines;

    @PostConstruct
    public void makeCoffees() {
        System.out.println("barista " + barista);
        for (int i = 0; i < brewCount; i++) {
            for (CoffeeMachine coffeeMachine : coffeeMachines) {
                System.out.println(coffeeMachine.brew());
            }
        }
    }
}
