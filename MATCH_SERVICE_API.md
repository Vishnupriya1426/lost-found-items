# MatchService API Documentation

## Overview
The MatchService provides intelligent matching between lost and found items using a scoring algorithm that considers text similarity, location proximity, and date relevance.

## Scoring Algorithm
The match score is calculated using the following formula:
```
Match Score = 0.6 × Text Similarity + 0.3 × Location Score + 0.1 × Date Score
```

### Score Components:
- **Text Similarity (60%)**: Jaccard similarity between tokenized descriptions
- **Location Score (30%)**: Location matching based on exact match, containment, or word overlap
- **Date Score (10%)**: Time-based scoring that decreases with days difference

## API Endpoints

### 1. Find Matches (POST)
**Endpoint**: `POST /api/matches/find`

**Request Body**:
```json
{
  "lostItemId": 1,
  "locationFilter": "Downtown",
  "daysBefore": 7,
  "daysAfter": 3
}
```

**Response**:
```json
{
  "success": true,
  "message": "Matches found successfully",
  "data": [
    {
      "foundItemId": 5,
      "foundItemTitle": "Black iPhone 12",
      "foundItemDescription": "Found a black iPhone near the coffee shop",
      "foundItemCategory": "Electronics",
      "foundItemLocation": "Downtown Coffee Shop",
      "foundItemDate": "2024-01-15T10:30:00",
      "foundByUserName": "John Doe",
      "foundByUserEmail": "john@example.com",
      "foundByUserPhone": "123-456-7890",
      "matchScore": 0.85,
      "textSimilarity": 0.8,
      "locationScore": 0.9,
      "dateScore": 0.7
    }
  ],
  "count": 1
}
```

### 2. Find Matches by Lost Item ID (GET)
**Endpoint**: `GET /api/matches/lost-item/{lostItemId}`

**Example**: `GET /api/matches/lost-item/1`

### 3. Find Matches with Location Filter (GET)
**Endpoint**: `GET /api/matches/lost-item/{lostItemId}/location/{location}`

**Example**: `GET /api/matches/lost-item/1/location/Downtown`

### 4. Find Matches with Date Range (GET)
**Endpoint**: `GET /api/matches/lost-item/{lostItemId}/date-range?daysBefore=7&daysAfter=3`

## Text Similarity Algorithm
1. **Tokenization**: Split text into words
2. **Normalization**: Convert to lowercase, remove punctuation
3. **Filtering**: Remove words shorter than 3 characters
4. **Jaccard Similarity**: Calculate intersection over union of word sets

## Location Scoring
- **Exact Match**: 1.0
- **Containment**: 0.8 (one location contains the other)
- **Word Overlap**: Jaccard similarity of location words

## Date Scoring
- **Same Day**: 1.0
- **1 Day Difference**: 0.9
- **3 Days Difference**: 0.7
- **7 Days Difference**: 0.5
- **14 Days Difference**: 0.3
- **30+ Days Difference**: 0.0

## Usage Examples

### Basic Match Search
```bash
curl -X POST http://localhost:8080/api/matches/find \
  -H "Content-Type: application/json" \
  -d '{"lostItemId": 1}'
```

### Location-Filtered Search
```bash
curl -X GET "http://localhost:8080/api/matches/lost-item/1/location/Downtown"
```

### Date-Range Search
```bash
curl -X GET "http://localhost:8080/api/matches/lost-item/1/date-range?daysBefore=7&daysAfter=3"
```

## Features
- ✅ Returns top 10 matches sorted by score
- ✅ Configurable location and date filters
- ✅ Detailed scoring breakdown
- ✅ User information for found items
- ✅ RESTful API design
- ✅ Input validation
- ✅ Error handling
