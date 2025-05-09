package my.study.animal.feature;

public interface Flyable {
//    void fly();

    default void fly() {
        System.out.println("fly...");
    }
}
