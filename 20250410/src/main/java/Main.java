import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
        System.out.println(sum("17", "3")); // 20
        System.out.println(sum("56", null)); // null
        System.out.println(sum("ㅋ", null)); // null
        System.out.println(sum(null, "ㅋ")); // null
        System.out.println(sum("56", "ㅋ")); // INVALID
        System.out.println(sum("2000000000", "2000000000")); // FLOW
        System.out.println(sum("-2000000000", "-2000000000")); // FLOW
    }

    public static String sum(String a, String b) {
        /* 전달받은 문자열 a와 b를 정수로 변환하여 그 정수를 합하고 다시 문자열로 변환하여 반환하는 메서드 sum을 완성
         * a,b 중 하나 이상이 null일 경우 "NULL",
         * a,b 중 하나 이상을 정수로 변환할 수 없는 경우 "INVALID",
         * 두 수의 합이 정수의 범위에 대해 오버/언더플로우가 발생하는 경우, "FLOW"를 반환 */

        if (a == null || b == null) {
            return "NULL";
        }
        try {
            int aNum = Integer.parseInt(a);
            int bNum = Integer.parseInt(b);
            int sum = Math.addExact(aNum, bNum);
            return String.valueOf(sum);
        } catch (NumberFormatException e) {
            return "INVALID";
        } catch (ArithmeticException e) {
            return "FLOW";
        }

//        int aNum, bNum;
//
//        try {
//            aNum = Integer.parseInt(a);
//            bNum = Integer.parseInt(b);
//        } catch (NumberFormatException e) {
//            return "INVALID";
//        }
//        long sum = (long)aNum + (long)bNum;
//        if (sum > Integer.MAX_VALUE || sum < Integer.MIN_VALUE) {
//            return "FLOW";
//        }
//        return String.valueOf((int)sum);
    }
}