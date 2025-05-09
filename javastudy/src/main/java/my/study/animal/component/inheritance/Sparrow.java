package my.study.animal.component.inheritance;

import my.study.animal.component.Gender;
import my.study.animal.feature.Flyable;

public class Sparrow extends Bird implements Flyable {
    public Sparrow(Gender gender) {
        super("Sparrow", gender);
    }
}
