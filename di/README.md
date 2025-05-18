# Dependency Injection

## Project
- New --> Project... --> Java Project with Gradle

## Dependency
- Tight Coupling vs. Loose Coupling
- CoffeeMachine interface for loose coupling
- DripCoffeeMachine, EspressoMachine implement CoffeeMachine interface
- CoffeeMaker has setter for coffee machine
- Main injects dependencies into CoffeeMaker using setter

## 의존성 주입의 장점
- 결합도 감소: 커피메이커는 커피머신 인터페이스에만 의존하므로 실제 구현 클래스인 드립커피머신이나 에스프레소머신과는 독립적입니다.
- 유연성 증가: 새로운 커피머신을 추가하려면 커피머신 인터페이스만 구현하면 됩니다.
- 테스트 용이성: 커피메이커를 테스트하기 위해서 목(Mock) 객체를 주입하여 테스트 할 수 있습니다.