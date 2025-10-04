# File Upload API Documentation

## Overview
The File Upload API provides endpoints for uploading, managing, and serving images for lost items in the Lost & Found system.

## Features
- ✅ **Image Upload**: Upload JPG/PNG images for lost items
- ✅ **File Validation**: Size limit (5MB) and type validation
- ✅ **Secure Storage**: Files stored in `/uploads` directory with unique names
- ✅ **Database Integration**: Updates `LostItem.imagePath` field
- ✅ **Image Serving**: Serve uploaded images via HTTP endpoints
- ✅ **File Management**: Delete images and update database
- ✅ **Error Handling**: Comprehensive validation and error responses

## API Endpoints

### 1. Upload Image for Lost Item
**Endpoint**: `POST /api/lost/{id}/image`

**Content-Type**: `multipart/form-data`

**Parameters**:
- `id` (path): Lost item ID
- `file` (form-data): Image file (JPG/PNG, max 5MB)

**Request Example**:
```bash
curl -X POST http://localhost:8080/api/lost/1/image \
  -F "file=@/path/to/image.jpg"
```

**Response Success** (200):
```json
{
  "success": true,
  "message": "Image uploaded successfully",
  "data": {
    "lostItemId": 1,
    "imagePath": "uploads/lost_1_a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg",
    "fileName": "image.jpg",
    "fileSize": 1048576,
    "contentType": "image/jpeg"
  }
}
```

**Response Error** (400):
```json
{
  "success": false,
  "message": "Validation error: File size exceeds maximum allowed size of 5MB",
  "data": null
}
```

### 2. Delete Image for Lost Item
**Endpoint**: `DELETE /api/lost/{id}/image`

**Parameters**:
- `id` (path): Lost item ID

**Request Example**:
```bash
curl -X DELETE http://localhost:8080/api/lost/1/image
```

**Response Success** (200):
```json
{
  "success": true,
  "message": "Image deleted successfully",
  "data": {
    "lostItemId": 1
  }
}
```

**Response Error** (404):
```json
{
  "success": false,
  "message": "No image found for this lost item",
  "data": null
}
```

### 3. Get Upload Information
**Endpoint**: `GET /api/upload/info`

**Request Example**:
```bash
curl -X GET http://localhost:8080/api/upload/info
```

**Response** (200):
```json
{
  "success": true,
  "message": "Upload information retrieved successfully",
  "data": {
    "maxFileSize": 5242880,
    "maxFileSizeMB": 5,
    "allowedTypes": ["image/jpeg", "image/jpg", "image/png"],
    "allowedExtensions": [".jpg", ".jpeg", ".png"]
  }
}
```

### 4. Serve Uploaded Images
**Endpoint**: `GET /api/images/{filename}`

**Parameters**:
- `filename` (path): Image filename

**Request Example**:
```bash
curl -X GET http://localhost:8080/api/images/lost_1_a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg
```

**Response**: Binary image data with appropriate content-type headers

## File Validation Rules

### Size Limits
- **Maximum file size**: 5MB (5,242,880 bytes)
- **Spring Boot limit**: Configured in `application.properties`

### Allowed File Types
- **Content Types**: `image/jpeg`, `image/jpg`, `image/png`
- **File Extensions**: `.jpg`, `.jpeg`, `.png`
- **Case Insensitive**: Extensions are checked case-insensitively

### File Naming
- **Format**: `lost_{itemId}_{uuid}.{extension}`
- **Example**: `lost_1_a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg`
- **Uniqueness**: UUID ensures unique filenames

## Configuration

### Application Properties
```properties
# File Upload Configuration
app.upload.dir=uploads
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB
```

### Directory Structure
```
project-root/
├── uploads/                    # Upload directory
│   ├── lost_1_uuid1.jpg       # Lost item images
│   ├── lost_2_uuid2.png
│   └── ...
└── src/
    └── main/
        └── java/
            └── com/myorg/lostfound/
                ├── controller/
                │   └── FileUploadController.java
                ├── service/
                │   └── FileUploadService.java
                └── config/
                    └── FileUploadConfig.java
```

## Error Handling

### Validation Errors (400)
- File is required
- File size exceeds 5MB limit
- Invalid file type (not JPG/PNG)
- Invalid file extension
- File name is required

### Not Found Errors (404)
- Lost item not found
- Image file not found

### Server Errors (500)
- File system errors
- Database errors
- Unexpected exceptions

## Usage Examples

### JavaScript (Frontend)
```javascript
// Upload image
const uploadImage = async (lostItemId, file) => {
  const formData = new FormData();
  formData.append('file', file);
  
  const response = await fetch(`/api/lost/${lostItemId}/image`, {
    method: 'POST',
    body: formData
  });
  
  const result = await response.json();
  return result;
};

// Delete image
const deleteImage = async (lostItemId) => {
  const response = await fetch(`/api/lost/${lostItemId}/image`, {
    method: 'DELETE'
  });
  
  const result = await response.json();
  return result;
};

// Display image
const imageUrl = `/api/images/${imagePath}`;
```

### HTML Form
```html
<form enctype="multipart/form-data">
  <input type="file" name="file" accept=".jpg,.jpeg,.png" />
  <button type="submit">Upload Image</button>
</form>
```

### cURL Examples
```bash
# Upload image
curl -X POST http://localhost:8080/api/lost/1/image \
  -F "file=@/path/to/image.jpg"

# Delete image
curl -X DELETE http://localhost:8080/api/lost/1/image

# Get upload info
curl -X GET http://localhost:8080/api/upload/info

# View image
curl -X GET http://localhost:8080/api/images/lost_1_uuid.jpg
```

## Security Considerations

### File Validation
- **Type Checking**: Both MIME type and file extension validation
- **Size Limits**: Prevents large file uploads
- **Path Security**: Prevents directory traversal attacks

### File Storage
- **Unique Names**: UUID-based naming prevents conflicts
- **Directory Isolation**: Files stored in dedicated upload directory
- **Access Control**: Images served through controlled endpoints

### Error Handling
- **No Sensitive Information**: Error messages don't expose system details
- **Graceful Degradation**: Service continues if file operations fail

## Integration with Lost Items

### Database Updates
- **Image Path Storage**: `LostItem.imagePath` field updated with relative path
- **Automatic Cleanup**: Old images deleted when new ones uploaded
- **Null Handling**: Image path set to null when image deleted

### API Consistency
- **Response Format**: Consistent with other API endpoints
- **Error Handling**: Same error response structure
- **Status Codes**: Standard HTTP status codes used

## Performance Considerations

### File Handling
- **Streaming**: Files processed as streams to minimize memory usage
- **Efficient Copying**: Uses `StandardCopyOption.REPLACE_EXISTING`
- **Directory Creation**: Upload directory created only when needed

### Database Operations
- **Single Query**: Only one database update per upload
- **Transaction Safety**: File operations and database updates are atomic
- **Error Recovery**: Failed operations don't leave orphaned files

## Testing

### Manual Testing
1. **Upload Valid Image**: Test with JPG/PNG files under 5MB
2. **Upload Invalid File**: Test with oversized or wrong type files
3. **Delete Image**: Test image deletion functionality
4. **View Image**: Test image serving endpoint
5. **Error Cases**: Test with non-existent lost items

### Automated Testing
```java
@Test
public void testImageUpload() {
    // Test valid image upload
    // Test file validation
    // Test database update
    // Test file storage
}
```
