import java.lang.reflect.Array;

public class Practice20250404 {
    public static void main(String[] args) {
        System.out.println(removeConsecutiveSpaces("  hello       nice    to   see        you"));
        System.out.println(removeConsecutiveSpaces(null));

        System.out.println(filterMessage("아니 우리 정글 ㅈㄴ")); // 아니 우리 정글 **
        System.out.println(filterMessage("ㅎㄴ 사사사사 ㅅㅁㅌ"));

        System.out.println(toISODate(587, 6, 13)); // 0587-06-13
        System.out.println(toISODate(1999, 18, 6)); // null
        System.out.println(toISODate(2025,4,4));

        System.out.println(toCamelCase("Student Name Array"));
        System.out.println(toCamelCase("으아아악hello바나나bye애플orange"));
        System.out.println(toCamelCase("안녕? Article count가 필요해"));
    }

    public static String removeConsecutiveSpaces(String input) {
        /* 전달된 문자열 input에서 선/후행 공백을 모두 제거하고, 연속된 공백을 모두 단일 공백으로 치환하여 반환하는 메서드 removeConsecutiveSpaces를 완성
         * input이 "  hello       nice    to   see        you"일때 반환값은 "hello nice to see you"
         * 만약에 input이 null이라면 빈문자열을 반환 */

        if (input == null) {
            return "";
        }
        input = input.strip();
        return input.replaceAll("\\s+", " ");
    }

    public static String filterMessage(String message) {
        /* 전달된 문자열 message  에서 아래에 주어진 문자열 배열이 가지는 인자를 모두 각 인자가 가지는 길이에 맞는 에스터리스크(*)로 치환한 문자열로 반환하는 메서드 filterMessage 메서드를 완성 (가령, "ㅍㄱㅁㅈ" => "****") */
        final String[] slangs = {"ㅎㅍ", "ㅈㄴ", "ㅎㄴ", "ㅅㅁㅌ", "ㅍㄱㅁㅈ"};

        for (String slang : slangs) {
            if (message.contains(slang)) {
                String replace = "*".repeat(slang.length());
                message = message.replace(slang, replace);
            }
        }
        return message;
    }

    public static String toISODate(int year, int month, int day) {
        /* 정수인 년, 월, 일이 주어지면 ISO 형식을 가지는 날짜 (yyyy-mm-dd) 문자열을 반환하는 메서드 toISODate를 완성하시오.
         * 각 매개변수 year, month, day에 전달할 수 있는 인자의 범위에 대한 제한은 없으며 유효한 값은 각각 0~9999, 1~12, 1~31 임으로, 그 외의 값이 전달 되었을 때에는 null을 반환
         * 단, 윤년, 윤달 및 짧은 달, 긴달은 고려하지 않아도 되고, 년도는 네자리, 월, 일은 두자리로 맞추되 자리가 부족할 경우 자리수를 맞춰 앞자리에 "0"으로 채움 */

        String date = String.format("%04d-%02d-%02d", year, month, day);

        if (year < 0 || year > 9999 || month < 0 || month > 12 || day < 0 || day > 31) {
            return null;
        }
        return date;
    }

    public static String toCamelCase(String input) {
        /* 전달된 문자열 input을 카멜 케이스화하여 반환하는 메서드 toCamelCase를 완성.
         * 단, 이가 가지는 내용 중 영어 알파벳이 아닌 내용은 반환값에서 제외. 구분 대상으로 하는 값은 공백( )
         * 가령 input의 내용이 "Student Name Array"라면 반환값은 "studentNameArray" 여야함
         * 마찬가지로 input이 "안녕? Article count가 필요해"라면 반환값은 "articleCount"
         * 당연히 인간적 사고로 단어간 구분이 가능하더라도 공백이 없다면 케이싱되지 않아도 괜찮음
         * 가령 "으아아악hello바나나bye애플orange"에 대한 반환값은 중간에 공백이 없으므로 "hellobyeorange"
         * 만약에 input이 null이라면 빈문자열을 반환 */
        if (input == null) {
            return "";
        }

        input = input.trim();
        while (input.contains("  ")) {
            input = input.replace("  ", " ");
        }

        String result = "";
        char[] inputArray = input.toCharArray();
        for (int i = 0; i < inputArray.length; i++) {
            char c = inputArray[i];
            if (result.length() == 0) {
                if (c >= 'A' && c <= 'Z') {
                    result += (char) (c + 32);
                } else if (c >= 'a' && c <= 'z') {
                    result += c;
                }
            } else {
                if (inputArray[i - 1] == ' ') {
                    if (c >= 'A' && c <= 'Z') {
                        result += c;
                    } else if (c >= 'a' && c <= 'z') {
                        result += (char) (c - 32);
                    }
                } else {
                    if (c >= 'A' && c <= 'Z') {
                        result += (char) (c - 32);
                    } else if (c >= 'a' && c <= 'z') {
                        result += c;
                    }
                }
            }
        }

        return result;

//        input = input.replaceAll("[^a-zA-Z ]", "").strip();
//
//        if (input.isEmpty()) {
//            return "";
//        }
//
//        String[] words = input.split("\\s+");
//        StringBuilder result = new StringBuilder();
//
//        for (int i = 0; i < words.length; i++) {
//            String word = words[i].toLowerCase();
//
//            if (i == 0) {
//                result.append(word);
//            } else {
//                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
//            }
//        }
//        return result.toString();
//        char[] inputArray = input.toCharArray();
//        char[] outArray = new char[inputArray.length];
//        for (int i = 0; i < inputArray.length; i++) {
//            if (inputArray[i] >= 'A' || inputArray[i] <= 'Z' || inputArray[i] >= 'a' || inputArray[i] <= 'z') {
//                outArray += inputArray;
//            }
//        }
    }
}