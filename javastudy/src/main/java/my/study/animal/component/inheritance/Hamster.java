package my.study.animal.component.inheritance;

import my.study.animal.component.Animal;
import my.study.animal.component.Gender;

public class Hamster extends Animal {
    public Hamster(Gender gender) {
        super("Hamster", gender);
    }
}
