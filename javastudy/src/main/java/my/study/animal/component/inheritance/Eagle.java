package my.study.animal.component.inheritance;

import my.study.animal.component.Gender;
import my.study.animal.feature.Flyable;

public class Eagle extends Bird implements Flyable {
    public Eagle(Gender gender) {
        super("Eagle", gender);
    }

    @Override
    public void fly() {
        System.out.println(this.getSpecies() + " is flying!");
    }
}
