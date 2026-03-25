package com.webbol.config;

/**
 * 서버 설정 상수
 * COBOL 의 config.cpy 에 해당
 */
public class ServerConfig {
    
    /** TCP 포트 번호 (기본 HTTP 개발 포트) */
    public static final int SERVER_PORT = 8080;
    
    /** 최대 대기 연결 수 */
    public static final int MAX_CONNECTIONS = 10;
    
    /** 파일 콘텐츠 및 HTTP 응답 최대 크기 (64KB) */
    public static final int BUFFER_SIZE = 65536;
    
    /** 파일 경로 최대 길이 (버퍼 오버플로 방지) */
    public static final int MAX_PATH_LEN = 512;
    
    /** 요청 버퍼 크기 (8KB) */
    public static final int REQUEST_BUFFER_SIZE = 8192;
    
    /** 응답 버퍼 크기 (64KB) */
    public static final int RESPONSE_BUFFER_SIZE = 65536;
    
    private ServerConfig() {
        // 유틸리티 클래스는 인스턴스화 방지
    }
}
