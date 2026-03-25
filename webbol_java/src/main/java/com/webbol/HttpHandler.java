package com.webbol;

import com.webbol.config.ServerConfig;

import java.nio.charset.StandardCharsets;

/**
 * HTTP 요청 파서 및 응답 생성기 모듈
 * COBOL 의 http-handler.cbl 에 해당
 */
public class HttpHandler {
    
    /** HTTP 개행 문자 (CRLF) */
    private static final String CRLF = "\r\n";
    
    /**
     * HTTP 요청을 처리하고 응답을 생성합니다.
     * 
     * @param request HTTP 요청 문자열
     * @return HTTP 응답 바이트 배열
     */
    public static byte[] handleRequest(String request) {
        // HTTP 요청 파싱
        String method = extractMethod(request);
        String path = extractPath(request);
        
        System.out.println("Method: " + method);
        System.out.println("Path: " + path);
        
        // URL 디코딩
        String decodedPath = UrlDecoder.decode(path);
        System.out.println("Decoded path: " + decodedPath);
        
        // 경로 검증 및 sanitize
        String sanitizedPath = PathUtils.validateAndSanitize(decodedPath);
        System.out.println("Sanitized path: " + sanitizedPath);
        
        if (sanitizedPath == null) {
            System.out.println("Path validation failed - returning 403");
            return build403Response();
        }
        
        // 파일 읽기
        FileOps.FileReadResult result = FileOps.readFile(sanitizedPath);
        
        if (result.getReturnCode() == FileOps.FILE_TOO_LARGE) {
            System.out.println("File too large - returning 413");
            return build413Response();
        }
        
        if (result.getReturnCode() != FileOps.FILE_READ_OK) {
            System.out.println("File not found - returning 404");
            return build404Response();
        }
        
        // MIME 타입 결정
        String mimeType = MimeTypes.getMimeType(sanitizedPath);
        System.out.println("MIME type: " + mimeType);
        
        // 성공 응답 생성
        return build200Response(result.getContent(), mimeType);
    }
    
    /**
     * HTTP 요청에서 메서드를 추출합니다.
     */
    private static String extractMethod(String request) {
        if (request == null || request.isEmpty()) {
            return "";
        }
        
        int spaceIndex = request.indexOf(' ');
        if (spaceIndex > 0 && spaceIndex <= 10) {
            return request.substring(0, spaceIndex);
        }
        return "";
    }
    
    /**
     * HTTP 요청에서 경로를 추출합니다.
     */
    private static String extractPath(String request) {
        if (request == null || request.isEmpty()) {
            return "/";
        }
        
        // 첫 번째 공백 찾기 (메서드 끝)
        int firstSpace = request.indexOf(' ');
        if (firstSpace < 0) {
            return "/";
        }
        
        // 경로 시작 위치
        int pathStart = firstSpace + 1;
        
        // 두 번째 공백 찾기 (HTTP 버전 시작)
        int secondSpace = request.indexOf(' ', pathStart);
        if (secondSpace < 0) {
            return request.substring(pathStart);
        }
        
        String path = request.substring(pathStart, secondSpace);
        return path.isEmpty() ? "/" : path;
    }
    
    /**
     * HTTP 200 OK 응답을 생성합니다.
     */
    private static byte[] build200Response(byte[] content, String mimeType) {
        StringBuilder headers = new StringBuilder();
        headers.append("HTTP/1.1 200 OK").append(CRLF);
        headers.append("Content-Type: ").append(mimeType).append(CRLF);
        headers.append("Content-Length: ").append(content.length).append(CRLF);
        headers.append(CRLF);
        
        byte[] headerBytes = headers.toString().getBytes(StandardCharsets.UTF_8);
        byte[] response = new byte[headerBytes.length + content.length];
        
        System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
        System.arraycopy(content, 0, response, headerBytes.length, content.length);
        
        return response;
    }
    
    /**
     * HTTP 404 Not Found 응답을 생성합니다.
     */
    private static byte[] build404Response() {
        String body = "<html><body><h1>404 Not Found</h1></body></html>";
        return buildErrorResponse("404 Not Found", body);
    }
    
    /**
     * HTTP 403 Forbidden 응답을 생성합니다.
     */
    private static byte[] build403Response() {
        String body = "<html><body><h1>403 Forbidden</h1></body></html>";
        return buildErrorResponse("403 Forbidden", body);
    }
    
    /**
     * HTTP 413 Payload Too Large 응답을 생성합니다.
     */
    private static byte[] build413Response() {
        String body = "<html><body><h1>413 Payload Too Large</h1></body></html>";
        return buildErrorResponse("413 Payload Too Large", body);
    }
    
    /**
     * 에러 응답을 생성합니다.
     */
    private static byte[] buildErrorResponse(String status, String body) {
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 ").append(status).append(CRLF);
        response.append("Content-Type: text/html").append(CRLF);
        response.append("Content-Length: ").append(body.length()).append(CRLF);
        response.append(CRLF);
        response.append(body);
        
        return response.toString().getBytes(StandardCharsets.UTF_8);
    }
}
