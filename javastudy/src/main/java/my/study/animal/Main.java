package my.study.animal;

import my.study.animal.component.Animal;
import my.study.animal.component.Gender;
import my.study.animal.component.inheritance.*;
import my.study.animal.feature.Flyable;

public class Main {
    public static void main(String[] args) {
//        Animal dog = new Animal();
//        dog.setSpecies("mixed-poodle");
//        dog.setName("half moon");
//        dog.setAge(7);
//        dog.setGender("M");
//        dog.eat();
//        dog.move();
//
//        Animal cat = new Animal();
//        cat.setSpecies("russian blue");
//        cat.setName("blue");
//        cat.setAge(2);
//        cat.setGender("M");
//        cat.eat();
//        cat.move();

//        MixedPoodle mixedPoodle = new MixedPoodle("M");
//        mixedPoodle.setName("Oreo");
//        mixedPoodle.eat();
//
//        NorwegianForest norwegianForest = new NorwegianForest("F");
//        norwegianForest.setName("Nori");
//        norwegianForest.eat();

//        Animal[] animals = {
//                new MixedPoodle("M"),
//                new NorwegianForest("F"),
//                new NorwegianForest("F"),
//                new Hamster("F"),
//                new MixedPoodle("M"),
//                new MixedPoodle("M"),
//                new Hamster("F"),
//                new NorwegianForest("F"),
//                new MixedPoodle("M"),
//                new Hamster("F"),
//                new Hamster("F"),
//                new Hamster("F"),
//                new NorwegianForest("F"),
//                new MixedPoodle("M")
//        };
//        int mixedPoodleCount = 0;
//        int norwegianForestCount = 0;
//        int hamsterCount = 0;
//
//        for (Animal animal : animals) {
//            if (animal instanceof MixedPoodle) {
//                mixedPoodleCount++;
//            } else if (animal instanceof NorwegianForest) {
//                norwegianForestCount++;
//            } else if (animal instanceof Hamster) {
//                hamsterCount++;
//            }
//        }
//
//        System.out.println("MixedPoodle: " + mixedPoodleCount);
//        System.out.println("NorwegianForest: " + norwegianForestCount);
//        System.out.println("Hamster: " + hamsterCount);

//        MixedPoodle halfMoon = new MixedPoodle(Gender.MALE);
//        MixedPoodle moon = new MixedPoodle(Gender.FEMALE);
//        MixedPoodle oreo = new MixedPoodle(Gender.FEMALE);
//
//        System.out.println(halfMoon.getGender().getDisplayText());


//        Animal[] animals = {
//                new Chicken(Gender.MALE),
//                new Eagle(Gender.FEMALE),
//                new Hamster(Gender.MALE),
//                new NorwegianForest(Gender.FEMALE),
//                new MixedPoodle(Gender.MALE),
//                new Sparrow(Gender.MALE)
//        };
//        for (Animal animal : animals) {
//            if (!(animal instanceof Flyable)) {
//                System.out.println(animal.getSpecies() + " is not flyable.");
//            } else {
//                ((Flyable) animal).fly();
//            }
//        }


//        for (Animal animal : animals) {
//            String toPrint = String.format("%s은(는) 날수 %s.", animal.getSpecies(), animal instanceof Flyable ? "있다" : "없다");
//            System.out.println(toPrint);
//        }

    }
}
