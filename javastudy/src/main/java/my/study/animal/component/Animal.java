package my.study.animal.component;
// 변수 -> 속성 : 이름, 종, 무게 . . .
// 메서드 -> 동작 : 움직임(like verb)
public class Animal {
    private final String species; // 종 final로 지정시 set은 필요없음
    private final Gender gender; // 성별 (F or M or N)
    private String name; // 이름
    private int age; // 나이

    protected Animal(String species, Gender gender) {
        this.species = species;
        this.gender = gender;
    }

    public void eat() {
        System.out.println(this.name + " was ate.");
    }

    public void move() {
        System.out.println(this.name + " was moved.");
    }

    public String getSpecies() {
        return species;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException(age + " must be a positive integer.");
        }
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }
}
