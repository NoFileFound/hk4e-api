package org.httpsrv.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Webstatic {
    @GetMapping(value = {"webstatic/**", "sdk-public/**"})
    public ResponseEntity<?> SendResourceFiles(jakarta.servlet.http.HttpServletRequest request) {
        try {
            Resource resource = new ClassPathResource(request.getRequestURI());
            if (!resource.exists()) {
                return ResponseEntity.status(404).body(Collections.singletonMap("error", "Not Found"));
            }

            Path path = resource.getFile().toPath();
            if (Files.isRegularFile(path)) {
                String contentType = Files.probeContentType(path);
                if (contentType != null && contentType.startsWith("text")) {
                    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(Files.readString(path));
                } else {
                    return ResponseEntity.ok().contentType(contentType != null ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM).body(Files.readAllBytes(path));
                }
            } else {
                return ResponseEntity.status(404).body(Collections.singletonMap("error", "Not Found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Internal Server Error"));
        }
    }
}