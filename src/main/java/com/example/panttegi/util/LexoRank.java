package com.example.panttegi.util;

public class LexoRank {
    public static String getMiddleRank(String before, String after) {
        if (before == null) before = "a"; // 최소값
        if (after == null) after = "zzzzzzzz"; // 최대값

        StringBuilder middle = new StringBuilder();
        int i = 0;

        while (true) {
            char beforeChar = i < before.length() ? before.charAt(i) : 'a';
            char afterChar = i < after.length() ? after.charAt(i) : 'z';

            // 동일한 문자일 경우, 그대로 중간값에 추가
            if (beforeChar == afterChar) {
                middle.append(beforeChar);
            } else {
                // 중간값 계산
                char midChar = (char)((beforeChar + afterChar) / 2);

                // 중간값이 beforeChar와 동일할 경우 처리
                if (midChar == beforeChar) {
                    middle.append(beforeChar).append('m'); // 새로운 문자 추가로 간격 확보
                } else {
                    middle.append(midChar);
                }
                break;
            }
            i++;
        }

        // 중간값 검증: 결과가 before 와 after 사이에 있는지 확인
        int safetyCounter = 100; // 최대 100회 패딩 추가 시도
        while ((middle.toString().compareTo(before) <= 0 || middle.toString().compareTo(after) >= 0) && safetyCounter > 0) {
            middle.append('m'); // 패딩 추가로 더 정밀한 위치 확보
            safetyCounter--;

            // 무한 루프 발생 시 예외 처리
            if (safetyCounter == 0) {
                return before + "m";
            }
        }

        return middle.toString();
    }
}
