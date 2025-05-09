import java.util.Scanner;

public class StudyString {
    public static void main(String[] args) {
//        char[] cs = {'H', 'e', 'l', 'l', 'o'};
//        String S = String.copyValueOf(cs);
//        System.out.println(S);

//        String s = "Hello";
//        char c = s.charAt(4);
//        System.out.println(c);

//        int pop = 51_684_564;
//        String message = "획득한 점수는 " + score + "점 입니다.";
//        System.out.println(message);
//        String message = String.format("대한민국의 인구는 %,d명 입니다.", pop);
//        System.out.println(message);

//        System.out.println(String.format("%,10d 원 - 감자", 3840));
//        System.out.println(String.format("%,10d 원 - 토마토", 5600));
//        System.out.println(String.format("%,10d 원 - 껌", 500));
//        System.out.println(String.format("%,10d 원 - 쌀 20kg", 78000));

//        String name = "백유정";
//        int age = 31;
//        double eyeSight = 0.8;
//
//        String toPrint = String.format("이름은 %s, 나이는 %d살, 시력은 %.1f이다.", name, age, eyeSight);
//        System.out.println(toPrint);

//        String toPrint = String.join("-", "2025", "04", "04");
//        System.out.println(toPrint);

//        int year = 2025;
//        int month = 4;
//        int day = 4;
//
//        String toPrint = String.join("-",
//                String.valueOf(year),
//                String.valueOf(month),
//                String.valueOf(day));
//        System.out.println(toPrint);

//        String s1 = "Hello";
//        String s2 = "World";
//        String s3 = s1.concat(s2);
//
//        System.out.println(s1);
//        System.out.println(s2);
//        System.out.println(s3);

//        System.out.println("Hello".contains("Hell"));
//        System.out.println("Hello".startsWith("He"));
//        System.out.println("Hello".endsWith("lO"));

//        String s1 = new String("School");
//        String s2 = new String("School");
//
//        System.out.println(s1.equals(s2));

//        String str = "abcdeabcde";
//
//        System.out.println(str.indexOf("cde"));
//        System.out.println(str.lastIndexOf("bcd"));
//        System.out.println(str.length());

//        System.out.println("ㅋ".repeat(100));

//        String chat = "아니 우리 정글";
//        String rep = chat.replace("아니", "뭐요");
//
//        System.out.println(chat);
//        System.out.println(rep);

//        String contact = "공일공-1234-5678";
//        contact = contact.replaceAll("[^0-9]", "");
//        System.out.println(contact);

//        String s1 = "   Hello   ";
//        String sStrip = s1.strip();
//        String sStripL = s1.stripLeading();
//        String sStripT = s1.stripTrailing();
//
//        System.out.println(s1);
//        System.out.println(sStrip);
//        System.out.println(sStripL);
//        System.out.println(sStripT);

//        String msg = "abcdefghijk";
//        System.out.println(msg.substring(2, 8));

//        String msg = "hello world";
//        char [] cs = msg.toCharArray();
//        for (char c : cs) {
//            System.out.println(c);
//        }

        String at = "2025.04.07";
        String[] array = at.split(".");

        System.out.println(array.length);
        for (String s : array) {
            System.out.println(s);
        }
    }
}
