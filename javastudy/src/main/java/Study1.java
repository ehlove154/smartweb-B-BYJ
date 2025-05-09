import java.util.ArrayList;
import java.util.List;

public class Study1 {
    public static void main(String[] args) {
//        for (int i = 97; i <= 122; i++) {
//            char c = (char) i;
//            System.out.print(c);
//        }
//
//        System.out.println();
//
//        for (char c = 'a'; c <= 'z'; c++) {
//            System.out.print(c);
//    }
//        System.out.println(sum(9118, 654));

        System.out.println(sum(new int[] {5, 8, 99, -5, 21, 6, -11}));  // 1번 문제
        System.out.println(sum(null)); // 1번 문제
        System.out.println(areCapitals(new char[]{'h', 'I', 'T', 'l'})); // 2번
        System.out.println(areCapitals(new char[]{'H','E','L','L','O'})); // 2번
        String[] filteredNames = filterNames(new String[] {"백유정", "양관식", "오애순", "양금명"}, new int[] {69, 65, 60, 50}); // 3 번
        for (String name : filteredNames) {
            System.out.println(name);
        }
        System.out.println(validatePassword("qwerty123"));
    }

    //public static int sum(int a, int b) {
//    return a + b;
//}
    public static int sum(int[] nums) { // 1번 문제
//     전달받은 점수 배열 nums가 가지는 인자들의 합을 반환하는 메서드 sum을 완성하시오.
//     단, 매개변수 nums가 null인 경우 0을 반환

        if (nums == null) {
            return 0;
        }
        int total = 0;
        for (int i = 0; i < nums.length; i++) {
            total += nums[i];
        }
        return total;
    }

    public static boolean areCapitals(char[] cs) { // 2번 문제
        // 전달받은 문자 배열 cs가 가지는 인자들이 모두 알파벳 대문자인가의 여부를 반환하는 매서드 areCapitals를 완성하세요.
        // 단, 매개변수 cs가 null인 경우 false를 반환
        if (cs == null) {
            return false;
        }
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] < 65 || cs[i] > 90) {
                return false;
            }
        }
        return true;
}

    public static String[] filterNames (String[] names, int[] scores) { // 3번 문제
        // 매개변수 names는 응시생의 이름을 순서대로 담은 문자열 배열이고 scores는 점수를 순서대로 담은 정수 배열
        // 합격을 위한 최소 점수가 70점이라고 할때 합격한 사람의 이름을 인자로 가지는 문자열 배열을 반환하는 메서드 filterNames를 완성
        // 단. 매개변수인 names와 scores 배열의 길이는 1이상이고 항상 같으며 null이지 않음. 점수는 반드시 0이상 100이하의 값만 전달됨.
        // 합격자가 없는 경우 길이가 0인 문자열 배열을 반환

//        List<String> filteredNames = new ArrayList<String>();
//
//        for (int i = 0; i < scores.length; i++) {
//            if (scores[i] >= 70) {
//                filteredNames.add(names[i]);
//            }
//        }
//        return filteredNames.toArray(new String[0]);


        int passed = 0;
        for (int i = 0; i < names.length; i++) {
            int score = scores[i];
            if (score >= 70) {
                passed++;
            }
        }
        if (passed == 0) {
            return new String[0];
        }

        String[] result = new String[passed];
        int index = 0;
        for (int i = 0; i < names.length; i++) {
            int score = scores[i];
            if (score >= 70) {
                result[index++] = names[i];
            }
        }
        return result;
    }

    public static int validatePassword(String password) { // 4번 문제
        // 문자열 객체가 가지는 toCharArray() 메서드는 문자열이 가지는 문자를 인자로 가지는 문자 배열(char[])을 반환
        // ex "Hello".toCharArray()는 ['H', 'l', 'l', 'o']를 반환
        if (password == null) return -1;
        char[] cs = password.toCharArray();
        // 전달받은 비밀번호 문자열에 대해 보안성 점수(0~5점, 취약~안전)를 반환하는 로직을 담은 매서드 validatePassword를 작성
        // - 비밀번호는 4장 이상, 알파벳을 포함. 특수문자는 필수가 아니지만 ! @ # $ 이외의 특수문자를 사용 하면 안됨. 해당 규격을 벗어날 경우 -1점 반환
        // - 길이 (-1~2점) :
        //  * 3자 이하 : 올바르지 않은 형식임으로 -1점 반환
        //  * 4자 이상 ~ 6자 미만 : 0점
        //  * 6자 이상 ~ 8자 미만 : 1점
        //  * 8자 이상 : 2점
        // - 영어 대/소문자(0~1점) :
        //  * 영어 없음 : 올바르지 않은 형식임으로 -1점 반환
        //  * 영어 소문자만/대문자만 있음 : 0점
        //  * 영어 대소문자가 섞여 있음 : 1점
        // - 특수 문자(0~2점) :
        //  * 허용되는 특수문자 : ! @ # $
        //  * 허용되지 않는 특수문자가 포함 : 올바르지 않은 형식임으로 -1점 반환
        //  * 허용되는 특수문자 0자 : 0점
        //  * 허용되는 특수문자 1자 : 1점
        //  * 허용되는 특수문자 2자 이상(똑같은 특수문자도 개수로 인정) : 2점

        int total = 0;
        int lengthScore;
        if (cs.length < 4) return -1;
        else if (cs.length < 6) lengthScore = 0;
        else if (cs.length < 8) lengthScore = 1;
        else lengthScore = 2;
        total += lengthScore;

        boolean hasLower = false;
        boolean hasUpper = false;

        for (char c : cs) {
            if (c >= 65 && c <= 90) hasUpper = true;
            if (c >= 97 && c <= 122) hasLower = true;
        }

        if (!hasLower && !hasUpper) return -1;
        if (hasLower && hasUpper) total += 1;

        String specialCharacters = "!@#$";
        int specialCount = 0;

        for (char c : cs) {
            if (specialCharacters.indexOf(c) >= 0) specialCount++;
            else if (!((c >= 65 && c <= 90) || (c >= 97 && c <= 122) || (c >= 48 && c <= 57))) return -1;
        }

        if (specialCount == 1) total += 1;
        else if (specialCount >= 2) total += 2;

        return total;
    }
}