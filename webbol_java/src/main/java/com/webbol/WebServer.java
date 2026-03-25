package com.webbol;

import com.webbol.config.ServerConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 웹 서버 메인 클래스
 * COBOL 의 webserver.cbl 에 해당
 */
public class WebServer {
    
    /** 요청 카운터 (스레드 안전) */
    private static final AtomicLong requestCount = new AtomicLong(0);
    
    /**
     * 메인 엔트리포인트
     */
    public static void main(String[] args) {
        System.out.println("COBOL Web Server Starting... (Java Port)");
        System.out.println("Press Ctrl+C to stop");
        System.out.println(" ");
        
        int port = ServerConfig.SERVER_PORT;
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // SO_REUSEADDR 옵션은 Java 에서 기본적으로 설정됨
            serverSocket.setReuseAddress(true);
            
            System.out.println("Server listening on port " + port);
            
            // 메인 서버 루프
            while (true) {
                acceptLoop(serverSocket);
            }
            
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * 클라이언트 연결을 수락하고 처리합니다.
     */
    private static void acceptLoop(ServerSocket serverSocket) {
        try {
            // 클라이언트 연결 대기 및 수락
            Socket clientSocket = serverSocket.accept();
            
            try {
                // 요청 카운터 증가
                long count = requestCount.incrementAndGet();
                
                // 요청 처리
                handleRequest(clientSocket, count);
                
            } finally {
                // 클라이언트 소켓 닫기
                clientSocket.close();
            }
            
        } catch (IOException e) {
            System.err.println("Accept failed: " + e.getMessage());
        }
    }
    
    /**
     * HTTP 요청을 읽고 응답을 보냅니다.
     */
    private static void handleRequest(Socket clientSocket, long requestNum) throws IOException {
        // 요청 읽기
        byte[] requestBuffer = new byte[ServerConfig.REQUEST_BUFFER_SIZE];
        int bytesRead = clientSocket.getInputStream().read(requestBuffer);
        
        if (bytesRead <= 0) {
            return;  // 데이터 없거나 연결 종료
        }
        
        // 요청 헤더 끝 찾기 (CRLF CRLF)
        String request = new String(requestBuffer, 0, bytesRead, StandardCharsets.UTF_8);
        int headerEnd = findHeaderEnd(request);
        
        // 요청 로깅
        System.out.println("Request #" + requestNum + ":");
        if (headerEnd > 0 && headerEnd <= bytesRead) {
            System.out.println(request.substring(0, headerEnd));
        } else {
            System.out.println(request.substring(0, Math.min(200, request.length())));
        }
        
        // HTTP 핸들러로 요청 처리
        byte[] response = HttpHandler.handleRequest(request);
        
        // 응답 전송
        if (response != null && response.length > 0) {
            clientSocket.getOutputStream().write(response);
            clientSocket.getOutputStream().flush();
        }
    }
    
    /**
     * HTTP 헤더의 끝 (CRLF CRLF) 을 찾습니다.
     */
    private static int findHeaderEnd(String request) {
        int crlfCrlfIndex = request.indexOf("\r\n\r\n");
        if (crlfCrlfIndex >= 0) {
            return crlfCrlfIndex;
        }
        return 0;  // 헤더 끝을 찾을 수 없음
    }
}
