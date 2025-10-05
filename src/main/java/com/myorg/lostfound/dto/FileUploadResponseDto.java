package com.myorg.lostfound.dto;

/**
 * DTO for file upload responses
 */
public class FileUploadResponseDto {
    
    private Long lostItemId;
    private String imagePath;
    private String fileName;
    private Long fileSize;
    private String contentType;
    private String uploadUrl;
    
    public FileUploadResponseDto() {}
    
    public FileUploadResponseDto(Long lostItemId, String imagePath, String fileName, 
                               Long fileSize, String contentType, String uploadUrl) {
        this.lostItemId = lostItemId;
        this.imagePath = imagePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.uploadUrl = uploadUrl;
    }
    
    public Long getLostItemId() {
        return lostItemId;
    }
    
    public void setLostItemId(Long lostItemId) {
        this.lostItemId = lostItemId;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public String getUploadUrl() {
        return uploadUrl;
    }
    
    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
}

