package com.myorg.lostfound.controller;

import com.myorg.lostfound.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for file upload operations
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * Upload image for a lost item
     */
    @PostMapping("/lost/{id}/image")
    public ResponseEntity<Map<String, Object>> uploadLostItemImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String imagePath = fileUploadService.uploadLostItemImage(id, file);
            
            response.put("success", true);
            response.put("message", "Image uploaded successfully");
            response.put("data", Map.of(
                "lostItemId", id,
                "imagePath", imagePath,
                "fileName", file.getOriginalFilename(),
                "fileSize", file.getSize(),
                "contentType", file.getContentType()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", "Validation error: " + e.getMessage());
            response.put("data", null);
            
            return ResponseEntity.badRequest().body(response);
            
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "File upload failed: " + e.getMessage());
            response.put("data", null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Unexpected error: " + e.getMessage());
            response.put("data", null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Delete image for a lost item
     */
    @DeleteMapping("/lost/{id}/image")
    public ResponseEntity<Map<String, Object>> deleteLostItemImage(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean deleted = fileUploadService.deleteLostItemImage(id);
            
            if (deleted) {
                response.put("success", true);
                response.put("message", "Image deleted successfully");
                response.put("data", Map.of("lostItemId", id));
            } else {
                response.put("success", false);
                response.put("message", "No image found for this lost item");
                response.put("data", null);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Failed to delete image: " + e.getMessage());
            response.put("data", null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Unexpected error: " + e.getMessage());
            response.put("data", null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get file upload information
     */
    @GetMapping("/upload/info")
    public ResponseEntity<Map<String, Object>> getUploadInfo() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            FileUploadService.FileInfo fileInfo = fileUploadService.getFileInfo();
            
            response.put("success", true);
            response.put("message", "Upload information retrieved successfully");
            response.put("data", Map.of(
                "maxFileSize", fileInfo.getMaxFileSize(),
                "maxFileSizeMB", fileInfo.getMaxFileSize() / (1024 * 1024),
                "allowedTypes", fileInfo.getAllowedTypes(),
                "allowedExtensions", fileInfo.getAllowedExtensions()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get upload info: " + e.getMessage());
            response.put("data", null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Serve uploaded images
     */
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = determineContentType(filename);
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Determine content type based on file extension
     */
    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            default:
                return "application/octet-stream";
        }
    }
}

