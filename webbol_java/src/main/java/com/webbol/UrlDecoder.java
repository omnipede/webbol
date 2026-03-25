package com.webbol;

/**
 * URL 디코딩 모듈
 * %XX 인코딩된 문자를 실제 문자로 변환
 * COBOL 의 url-decode.cbl 에 해당
 */
public class UrlDecoder {
    
    /**
     * URL 인코딩된 경로를 디코딩합니다.
     * 예: %20 → 공백, %2F → /
     * 
     * @param input URL 인코딩된 입력 경로
     * @return 디코딩된 출력 경로
     */
    public static String decode(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        
        StringBuilder output = new StringBuilder(input.length());
        int i = 0;
        
        while (i < input.length()) {
            char ch = input.charAt(i);
            
            if (ch == '%' && i + 2 < input.length()) {
                // %XX 형식의 인코딩된 문자 디코딩
                String hexCode = input.substring(i + 1, i + 3);
                char decodedChar = decodeHexChar(hexCode);
                output.append(decodedChar);
                i += 3;
            } else {
                // 일반 문자는 그대로 복사
                output.append(ch);
                i++;
            }
        }
        
        return output.toString();
    }
    
    /**
     * 16 진수 코드를 문자로 디코딩합니다.
     * 
     * @param hexCode 2 자리 16 진수 문자열
     * @return 디코딩된 문자
     */
    private static char decodeHexChar(String hexCode) {
        if (hexCode == null) {
            return ' ';
        }
        
        switch (hexCode) {
            case "20": return ' ';   // 공백
            case "21": return '!';   // !
            case "22": return '"';   // "
            case "23": return '#';   // #
            case "24": return '$';   // $
            case "25": return '%';   // %
            case "26": return '&';   // &
            case "27": return '\'';  // '
            case "28": return '(';   // (
            case "29": return ')';   // )
            case "2B": case "2b": return '+';  // +
            case "2C": case "2c": return ',';  // ,
            case "2D": case "2d": return '-';  // -
            case "2E": case "2e": return '.';  // .
            case "2F": case "2f": return '/';  // /
            case "3A": case "3a": return ':';  // :
            case "3B": case "3b": return ';';  // ;
            case "3D": case "3d": return '=';  // =
            case "3F": case "3f": return '?';  // ?
            case "40": return '@';   // @
            case "5B": case "5b": return '[';  // [
            case "5D": case "5d": return ']';  // ]
            default: return ' ';    // 인식되지 않는 코드는 공백으로 처리
        }
    }
}
