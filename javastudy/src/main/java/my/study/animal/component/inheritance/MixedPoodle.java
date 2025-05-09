package my.study.animal.component.inheritance;

import my.study.animal.component.Animal;
import my.study.animal.component.Gender;

public class MixedPoodle extends Animal {
    public MixedPoodle(Gender gender) {
        super("Mixed-Poodle", gender); // 부모 생성자 호출
    }
}
