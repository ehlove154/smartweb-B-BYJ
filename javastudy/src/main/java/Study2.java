public class Study2 {
    public static void main(String[] args) {
//        Food chicken = new Food();
//
//        // 비정적인(static이 없는) 메서드를 객체를 통해 접근
//        System.out.println(chicken.getName()); //(올바른 예)
//
//        // 비정적인(static이 없는) 메서드를 타입을 통해 접근
//        System.out.println(Food.getName()); // (올바르지 않은 예 / 불가능)
//
//        // 정적인(static이 있는) 메서드를 객체를 통해 접근
//        System.out.println(chicken.getKcal()); // (올바르지 않은 예)
//
//        // 정적인(static이 있는) 메서드를 타입을 통해 접근
//        System.out.println(Food.getKcal()); //(올바른 예)

        Food hamburger = new Food("버거", 560);
//        hamburger.name = "슈비버거";
        System.out.println(hamburger);

        Food pizza = new Food("피자", 1600);
//        pizza.name = "하와이안피자";
        System.out.println(pizza);

    }
}