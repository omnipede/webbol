package com.webbol;

/**
 * 경로 검증 및 sanitize 모듈
 * 보안: 경로 탐색 공격 방지
 * COBOL 의 path-utils.cbl 에 해당
 */
public class PathUtils {
    
    /**
     * 경로를 검증하고 sanitize 합니다.
     * 
     * @param inputPath 원본 경로 (HTTP 요청에서 추출)
     * @return 검증된 경로. 유효하지 않으면 null 반환
     */
    public static String validateAndSanitize(String inputPath) {
        if (inputPath == null || inputPath.trim().isEmpty()) {
            return null;  // 빈 경로는 거부
        }
        
        String path = inputPath.trim();
        
        // 루트 경로 (/) 는 index.html 로 처리
        if (path.equals("/") || path.startsWith("/ ")) {
            return "index.html";
        }
        
        // 선행 슬래시 제거 (상대 경로로 변환)
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        
        // 절대 경로 차단 (보안 위험)
        if (path.startsWith("/")) {
            return null;
        }
        
        // ".." 시퀀스 검사 (경로 탐색 공격)
        if (path.contains("..")) {
            return null;
        }
        
        // "../" 로 시작하거나 정확히 ".." 인 경우 차단
        if (path.startsWith("../") || path.equals("..")) {
            return null;
        }
        
        return path;
    }
}
