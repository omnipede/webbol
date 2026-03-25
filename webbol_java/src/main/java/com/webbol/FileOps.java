package com.webbol;

import com.webbol.config.ServerConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 파일 읽기 연산 모듈
 * COBOL 의 file-ops.cbl 에 해당
 */
public class FileOps {
    
    /** 파일 읽기 성공 */
    public static final int FILE_READ_OK = 0;
    
    /** 파일 읽기 실패 (파일이 없거나 읽을 수 없음) */
    public static final int FILE_READ_ERROR = 1;
    
    /** 파일이 너무 큼 (버퍼 초과) */
    public static final int FILE_TOO_LARGE = 2;
    
    /**
     * 파일을 읽어 바이트 배열로 반환합니다.
     * 
     * @param filePath 파일 경로
     * @return 파일 콘텐츠 바이트 배열 (실패 시 null)
     */
    public static FileReadResult readFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            
            if (!Files.exists(path) || !Files.isReadable(path)) {
                return new FileReadResult(null, FILE_READ_ERROR);
            }
            
            long fileSize = Files.size(path);
            
            // 파일 크기가 버퍼를 초과하는지 확인
            if (fileSize > ServerConfig.BUFFER_SIZE) {
                return new FileReadResult(null, FILE_TOO_LARGE);
            }
            
            byte[] content = Files.readAllBytes(path);
            return new FileReadResult(content, FILE_READ_OK);
            
        } catch (IOException e) {
            return new FileReadResult(null, FILE_READ_ERROR);
        }
    }
    
    /**
     * 파일 읽기 결과를 담는 레코드 클래스
     */
    public static class FileReadResult {
        private final byte[] content;
        private final int returnCode;
        
        public FileReadResult(byte[] content, int returnCode) {
            this.content = content;
            this.returnCode = returnCode;
        }
        
        public byte[] getContent() {
            return content;
        }
        
        public int getReturnCode() {
            return returnCode;
        }
    }
}
