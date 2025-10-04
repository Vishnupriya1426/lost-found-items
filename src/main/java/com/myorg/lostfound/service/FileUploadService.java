package com.myorg.lostfound.service;

import com.myorg.lostfound.model.LostItem;
import com.myorg.lostfound.repository.LostItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for handling file uploads
 */
@Service
public class FileUploadService {
    
    @Autowired
    private LostItemRepository lostItemRepository;
    
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png");
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png");
    
    /**
     * Upload image for a lost item
     */
    public String uploadLostItemImage(Long lostItemId, MultipartFile file) throws IOException {
        // Validate file
        validateFile(file);
        
        // Check if lost item exists
        Optional<LostItem> lostItemOpt = lostItemRepository.findById(lostItemId);
        if (!lostItemOpt.isPresent()) {
            throw new IllegalArgumentException("Lost item with ID " + lostItemId + " not found");
        }
        
        LostItem lostItem = lostItemOpt.get();
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String uniqueFilename = "lost_" + lostItemId + "_" + UUID.randomUUID().toString() + extension;
        
        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Update lost item with image path
        String relativePath = uploadDir + "/" + uniqueFilename;
        lostItem.setImagePath(relativePath);
        lostItemRepository.save(lostItem);
        
        return relativePath;
    }
    
    /**
     * Delete image for a lost item
     */
    public boolean deleteLostItemImage(Long lostItemId) throws IOException {
        Optional<LostItem> lostItemOpt = lostItemRepository.findById(lostItemId);
        if (!lostItemOpt.isPresent()) {
            return false;
        }
        
        LostItem lostItem = lostItemOpt.get();
        String imagePath = lostItem.getImagePath();
        
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            // Delete file from filesystem
            Path filePath = Paths.get(imagePath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            
            // Update lost item
            lostItem.setImagePath(null);
            lostItemRepository.save(lostItem);
            return true;
        }
        
        return false;
    }
    
    /**
     * Validate uploaded file
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
        
        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of 5MB");
        }
        
        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Invalid file type. Only JPG and PNG files are allowed");
        }
        
        // Check file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("File name is required");
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Invalid file extension. Only .jpg, .jpeg, and .png files are allowed");
        }
    }
    
    /**
     * Get file extension from filename
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }
    
    /**
     * Get file info for validation
     */
    public FileInfo getFileInfo() {
        return new FileInfo(MAX_FILE_SIZE, ALLOWED_TYPES, ALLOWED_EXTENSIONS);
    }
    
    /**
     * File information class
     */
    public static class FileInfo {
        private final long maxFileSize;
        private final List<String> allowedTypes;
        private final List<String> allowedExtensions;
        
        public FileInfo(long maxFileSize, List<String> allowedTypes, List<String> allowedExtensions) {
            this.maxFileSize = maxFileSize;
            this.allowedTypes = allowedTypes;
            this.allowedExtensions = allowedExtensions;
        }
        
        public long getMaxFileSize() {
            return maxFileSize;
        }
        
        public List<String> getAllowedTypes() {
            return allowedTypes;
        }
        
        public List<String> getAllowedExtensions() {
            return allowedExtensions;
        }
    }
}
