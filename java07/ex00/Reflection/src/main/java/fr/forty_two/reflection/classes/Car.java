package fr.forty_two.reflection.classes;

import java.util.StringJoiner;

public class Car {
    private String model;
    private int hp;

    public Car() {
        this.model = "Default first name";
        this.hp = 0;
    }

    public Car(String model, int hp) {
        this.model = model;
        this.hp = hp;
    }

    public void repair(Long oilAmount, String spairParts) throws InterruptedException {
        System.out.println("Changing car oil...");
        Thread.sleep(350L);
        System.out.println("Car oil changed");
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
            .add("model='" + model + "'")
            .add("horsePower=" + hp)
            .toString();
    }

}
