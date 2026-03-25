package com.webbol;

/**
 * MIME 타입 감지 모듈
 * 파일 확장자를 기반으로 MIME 타입 매핑
 * COBOL 의 mime-types.cbl 에 해당
 */
public class MimeTypes {
    
    /** 기본 MIME 타입 (알 수 없는 확장자) */
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";
    
    /**
     * 파일 경로를 기반으로 MIME 타입을 반환합니다.
     * 
     * @param filePath 파일 경로
     * @return MIME 타입 문자열
     */
    public static String getMimeType(String filePath) {
        if (filePath == null) {
            return DEFAULT_MIME_TYPE;
        }
        
        // 마지막 점 (.) 위치 찾아 확장자 추출
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex >= filePath.length() - 1) {
            return DEFAULT_MIME_TYPE;  // 확장자 없음
        }
        
        String extension = filePath.substring(lastDotIndex + 1).toLowerCase();
        
        if ("html".equals(extension) || "htm".equals(extension)) {
            return "text/html";
        } else if ("css".equals(extension)) {
            return "text/css";
        } else if ("js".equals(extension)) {
            return "application/javascript";
        } else if ("json".equals(extension)) {
            return "application/json";
        } else if ("xml".equals(extension)) {
            return "application/xml";
        } else if ("txt".equals(extension)) {
            return "text/plain";
        } else if ("png".equals(extension)) {
            return "image/png";
        } else if ("jpg".equals(extension) || "jpeg".equals(extension)) {
            return "image/jpeg";
        } else if ("gif".equals(extension)) {
            return "image/gif";
        } else if ("svg".equals(extension)) {
            return "image/svg+xml";
        } else if ("ico".equals(extension)) {
            return "image/x-icon";
        } else if ("pdf".equals(extension)) {
            return "application/pdf";
        } else {
            return DEFAULT_MIME_TYPE;
        }
    }
}
