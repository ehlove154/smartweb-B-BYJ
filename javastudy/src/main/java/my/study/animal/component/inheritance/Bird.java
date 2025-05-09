package my.study.animal.component.inheritance;

import my.study.animal.component.Animal;
import my.study.animal.component.Gender;

public class Bird extends Animal {
    protected Bird(String species, Gender gender) {
        super(species, gender);
    }
}
