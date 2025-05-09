package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> list1 = null;
        System.out.println(Arrays.toString(listToArray(list1)));

        List<String> list2 = new ArrayList<>();
        System.out.println(Arrays.toString(listToArray(list2)));

        List<String> list3 = new ArrayList<>();
        list3.add("apple");
        list3.add("banana");
        list3.add("cherry");
        System.out.println(Arrays.toString(listToArray(list3))); // 1번


        String[] array1 = null;
        System.out.println(arrayToList(array1));

        String[] array2 = {};
        System.out.println(arrayToList(array2));

        String[] array3 = {"apple", "banana"};
        System.out.println(arrayToList(array3)); // 2번


        System.out.println(countName("박현식", "하병현", "장희훈", "안상윤", "권상준", "박석훈", "하태희", "안영준", "권문숙", "장힘찬", "박한길")); // {박 : 3, 하 : 2, 장 : 2, . . .} 순서 무관 /* 3번 */

        Map<String, String[]> students = new HashMap<>();
        students.put("1반", new String[] {"한하윤", "문해민", "백용기"});
        students.put("2반", new String[] {"이희성", "홍원일", "김현정"});
        students.put("3반", new String[] {"설기하", "설상민", "서우성"});

        Map<String , Integer> scores = new HashMap<>();
        scores.put("한하윤", 87); scores.put("문해민", 96); scores.put("백용기", 33);
        scores.put("이희성", 45); scores.put("홍원일", 68); scores.put("김현정", 88);
        scores.put("설기하", 65); scores.put("설상민", 24); scores.put("서우성", 98);
        System.out.println(averageByClass(students, scores));
         // { 1반 : 72, 2반 : 57, 3반 : 62.3333333 } 순서 무관 /* 4번 */
    }

    public static String[] listToArray(List<String> list) {
        /* 전달받은 List를 문자열 배열로 변환하여 반환하는 메서드 listToArray 를 완성
        * 매개변수 List가 null이거나 길이가 0일 경우 길이가 0인 문자열 배열(String[])을 반환
        * 단, List 객체가 가진 toArray 메서드를 사용하지 말고 문제를 해결 */

        if (list == null || list.size() == 0) { // isEmpty() == true
            return new String[0];
        }

        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public static List<String> arrayToList(String[] array) {
        /* 전달받은 array를 문자열을 인자로 가지는 List 객체로 변환하여 반환하는 메서드 arrayToList를 완성
        * 매개변수 array가 null이거나 길이가 0일 경우 길이가 0인 List<String> 객체를 반환
        * 단, Arrays 클래스를 사용하지 말고 문제를 해결 */
        if (array == null || array.length == 0) {
            return new ArrayList<>();
        }

        List<String> list = new ArrayList<>();
        for (String s : array) {
            list.add(s);
        }
        return list;
    }

    public static Map<String, Integer> countName(String ... names) {
        /* 이름(성 + 이름)을 인자로 가지는 가변인자 names에 대해 성씨별로 그 수를 가지는 Map<String, Integer> 객체를 반환하는 메서드 countName을 완성
        * 단, names가 null이거나 길이가 0일 경우 쌍을 가지지 않는 Map<String, Integer> 객체를 반환
        * 추가로, 성은 반드시 제일 앞 한 글자인 것으로 간주 */
        if (names == null || names.length == 0) {
            return new HashMap<>();
        }
        Map<String, Integer> map = new HashMap<>();
        for (String name : names) {
            if (name != null && name.length() > 0) {
                String lastName = name.substring(0, 1);
                map.put(lastName, map.getOrDefault(lastName, 0) + 1);
            }
        }
        return map;
    }

    public static Map<String , Double> averageByClass(Map<String , String[]> students, Map<String , Integer> scores) {
        /* 매개변수 students는 Map<학반, 학생 이름 배열> 이고, 매개변수 scores는 Map<학생 이름, 점수> 일때, Map<학반, 평균 점수>를 반환할 수 있는 averageByClass 메서드를 완성
        * 학반은 "n반"(1반, 2반, 3반, ...) 형식을 가지는 것으로 하고, 학생 이름의 경우 동명이인은 없는 것으로 함. 추가로 students 및 scores의 size는 0보다 크고 항상 같으며 null이지 않음 */
     Map<String , Double> result = new HashMap<>();
     for(String $class : students.keySet()) {
         double average = 0D;
         String[] names = students.get($class);
         for (String name : names) {
             average += scores.get(name);
         }
         average /= names.length;
         result.put($class, average);
     }
     return result;
    }
}