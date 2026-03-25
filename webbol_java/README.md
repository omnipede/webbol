# Webbol Java

A minimal static web server ported from COBOL to Java.

## Features

- Serves static files from the current directory
- Automatic MIME type detection for common file extensions
- HTTP status codes: 200 (OK), 403 (Forbidden), 404 (Not Found), 413 (Payload Too Large)
- Path traversal attack prevention
- Request logging to console
- Defaults to `index.html` for root path requests

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Building

Clone or download the repository, then compile:

```bash
cd webbol_java
mvn clean package
```

This will create `target/webbol-1.0.0.jar` and `target/webbol-1.0.0-jar-with-dependencies.jar`.

## Usage

Start the server from the `webbol_java` directory:

```bash
# Using Maven
mvn exec:java -Dexec.mainClass="com.webbol.WebServer"

# Or using the executable JAR
java -jar target/webbol-1.0.0-jar-with-dependencies.jar
```

The server will start on port 8080 and serve files from the current directory.

### Example

```bash
# Start the server
java -jar target/webbol-1.0.0-jar-with-dependencies.jar

# In another terminal, test it
curl http://localhost:8080/
```

### Accessing the Server

Once running, you can access files via:

- `http://localhost:8080/` - serves `index.html` from the current directory
- `http://localhost:8080/filename.html` - serves the specified file
- `http://localhost:8080/path/to/file.txt` - serves files from subdirectories

Press `Ctrl+C` to stop the server.

## Configuration

To change the server port, edit `src/main/java/com/webbol/config/ServerConfig.java`:

```java
public static final int SERVER_PORT = 8080;  // Change this value
```

Then recompile with `mvn clean package`.

## Project Structure

```
webbol_java/
├── pom.xml                    # Maven build configuration
├── README.md                  # This file
├── index.html                 # Sample HTML file
└── src/main/java/com/webbol/
    ├── WebServer.java         # Main server entry point
    ├── HttpHandler.java       # HTTP request/response handling
    ├── PathUtils.java         # Path validation and sanitization
    ├── UrlDecoder.java        # URL decoding (%XX → characters)
    ├── MimeTypes.java         # MIME type detection
    ├── FileOps.java           # File reading operations
    └── config/
        └── ServerConfig.java  # Server configuration constants
```

## Supported MIME Types

- HTML: `text/html`
- CSS: `text/css`
- JavaScript: `application/javascript`
- JSON: `application/json`
- XML: `application/xml`
- Plain text: `text/plain`
- PNG: `image/png`
- JPEG: `image/jpeg`
- GIF: `image/gif`
- SVG: `image/svg+xml`
- ICO: `image/x-icon`
- PDF: `application/pdf`

## Security Features

- Path traversal prevention: Blocks requests containing `..` sequences
- Directory access restriction: Only serves files from the current directory and subdirectories
- Safe file handling: Validates all paths before file system access

## Limitations

- Single-threaded: Handles one request at a time (same as COBOL original)
- No SSL/TLS support
- Maximum file size: 64KB
- No caching or compression
- No range requests or partial content support

## Troubleshooting

**Port already in use:**
```
java.net.BindException: Address already in use
```
Another process is using port 8080. Either stop that process or change the port in `ServerConfig.java`.

**Permission denied:**
Ensure the files you're trying to serve have read permissions.

**File not found (404):**
Verify the file exists in the current directory where the server is running. File paths are case-sensitive.

## License

This project is released into the public domain. Use it however you'd like.

## Acknowledgments

This is a Java port of the original COBOL web server ([webbol](../webbol)).
