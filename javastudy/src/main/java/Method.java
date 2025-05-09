import java.util.Arrays;

public class Method {
    public static void main(String[] args) {
//        System.out.println(sum(1, 2, 3, 4, 5, 6, 7, 8, 9));
        System.out.println(max()); // 0
        System.out.println(max(Double.NaN, 57, -6)); // 57
        System.out.println(max(56, -66, 8, -35, 99, 546));
        System.out.println(max(Double.NaN));

        System.out.println();

        System.out.println(round(3.141592, 2)); // 3.14
        System.out.println(round(Double.NaN, 10)); // NaN
        System.out.println(round(Double.NEGATIVE_INFINITY, 10));

        System.out.println();

        System.out.println(Arrays.toString(lotto()));

        System.out.println();

        System.out.println(getRadius(684513));
        System.out.println(getRadius(484312018));

        System.out.println();

        System.out.println(rootOfQuadEq(654, 816, 15));
        System.out.println(rootOfQuadEq(1, -3, 2));
        System.out.println(rootOfQuadEq(1, -2, 1));
        System.out.println(rootOfQuadEq(1, 1, 1));
    }

    public static double max(double... nums) {
        /* 매개변수 가변인자 nums가 가지는 값중 가장 큰 값을 반환하는 메서드 max를 완성
         * 단, nums가 null이거나 길이가 0이라면 0.0을 반환. 추가로 NaN은 검사대상에서 제외 */
        double max = Double.NaN;

        if (nums == null || nums.length == 0) {
            return 0.0;
        }

        for (double num : nums) {
            if (Double.isNaN(num)) {
                continue;
            }
            if (Double.isNaN(max) || num > max) {
                max = num;
            }
        }
        return Double.isNaN(max) ? 0 : max;

//        for (int i = 0; i < nums.length; i++) {
//            if (nums[i] > max) {
//                max = nums[i];
//            } else if (Double.isNaN(nums[i])) {
//                max = 0;
//            }
//        }
//        return max;
    }

    public static double round(double num, int digit) {
        /* 매개변수 num에 대해 소수점 digit 자리까지 반올림해서 변환하는 메서드 round를 완성
         * 단, num이 무한하거나 NaN일 경우 그대로 반환. 추가로 digit이 10보다 크다면 10을 할당하여 사용, 0보다 작다면 0을 할당하여 사용 */

        if (Double.isInfinite(num) || Double.isNaN(num)) {
            return num;
        }

        // 1.
        if (digit < 0) {
            digit = 0;
        } else if (digit > 10) {
            digit = 10;
        }

        final double factor = Math.pow(10, digit);
        num *= factor;
        num = Math.round(num);
        num /= factor;

        return num;


//        // 2.
//        digit = digit > 10 ? 10 : digit;
//
//        // 3.
//        digit = Math.min(digit, 10);
//        digit = Math.max(digit, 0);
    }

    public static int[] lotto() {
        int[] result = new int[6];
        /* 1이상 45이하의 무작위 정수 6개를 인자로 가지는 배열을 반환하는 메서드 lotto를 완성. 단, 인자는 중복되면 안됨.
         * 실수를 정수로 강제 형변환하면 실수부가 버려지게 됨을 참고 (int) 3.14 -> 3 */
        int count = 0;

        while (count < 6) {
            int random = (int) (Math.random() * 45) + 1;

            boolean isDuplicate = false;
            for (int i = 0; i < count; i++) {
                if (random == result[i]) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                result[count] = random;
                count++;
            }
        }
        return result;
    }

    public static double getRadius(double area) {
        /* 어떠한 (정)원의 넓이가 매개변수 area로 주어질때 해당 원의 반지름을 반환하는 메서드 getRadius를 완성
         * 원의 넓이를 구하는 공식은 πr^2
         * 단, area가 0이하일 경우 NaN을 반환*/

        if (area <= 0) {
            return Float.NaN;
        } else {
            return Math.sqrt(area / Math.PI);
        }
    }

    public static int rootOfQuadEq(double a, double b, double c) {
        /* 이차방정식 y = ax^2 + bx + c 꼴에서 각 a, b. c가 매개변수로 주어질때 해당 이차방정식이 (서로다른) 실근을 가진다면 1을 , 중근을 가진다면 0을, 허근을 가진다면 -1을 반환하는 메서드 rootOfQuadEq를 완성
         * 이차방정식의 근의 공식은 (-b ± √(b^2 -4ac)) / 2a */
        double root = Math.pow(b, 2) - 4 * a * c;

        if (root > 0) {
            return 1;
        } else if (root == 0){
            return 0;
        }
        return -1;
    }

}

//    public static int sum(int... nums) {
//        int sum = 0;
//        for (int num : nums) {
//            sum += num;
//        }
//        return sum;
//    }
