#!/bin/bash

BASE_URL="http://localhost:8080"

echo "=== AUTHOR APPLICATION WORKFLOW TEST ==="
echo

# Step 1: Register a reader user
echo "1. Registering a reader user..."
REGISTER_RESPONSE=$(curl -s -X POST $BASE_URL/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Aspiring Author",
    "email": "aspiring@example.com", 
    "password": "password123",
    "age": 26,
    "bio": "I want to become an author"
  }')
echo "Registration: $REGISTER_RESPONSE"
echo

# Step 2: Login as reader
echo "2. Logging in as reader..."
LOGIN_RESPONSE=$(curl -s -X POST $BASE_URL/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "aspiring@example.com",
    "password": "password123"
  }')
echo "Login: $LOGIN_RESPONSE"

READER_TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Reader Token: $READER_TOKEN"
echo

# Step 3: Submit author application
echo "3. Submitting author application..."
APPLICATION_RESPONSE=$(curl -s -X POST $BASE_URL/api/author-applications \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $READER_TOKEN" \
  -d '{
    "reason": "I have been writing stories for 5 years and would love to share my work on this platform. I have experience in fiction writing and believe I can contribute quality content."
  }')
echo "Application Response: $APPLICATION_RESPONSE"
echo

# Step 4: Check my applications
echo "4. Checking my applications..."
curl -s -X GET $BASE_URL/api/author-applications/my-applications \
  -H "Authorization: Bearer $READER_TOKEN"
echo
echo

# Step 5: Register an admin user
echo "5. Registering admin user..."
ADMIN_REGISTER=$(curl -s -X POST $BASE_URL/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Admin User",
    "email": "admin@example.com", 
    "password": "admin123",
    "age": 35,
    "bio": "System Administrator"
  }')
echo "Admin Registration: $ADMIN_REGISTER"
echo

# Step 6: Login as admin
echo "6. Logging in as admin..."
ADMIN_LOGIN=$(curl -s -X POST $BASE_URL/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "admin123"
  }')
echo "Admin Login: $ADMIN_LOGIN"

ADMIN_TOKEN=$(echo $ADMIN_LOGIN | grep -o '"token":"[^"]*' | cut -d'"' -f4)
ADMIN_ID=$(echo $ADMIN_LOGIN | grep -o '"id":[0-9]*' | cut -d':' -f2)
echo "Admin Token: $ADMIN_TOKEN"
echo "Admin ID: $ADMIN_ID"
echo

echo "=== MANUAL STEPS REQUIRED ==="
echo "To complete the workflow:"
echo "1. Go to H2 Console: $BASE_URL/h2-console"
echo "2. Connect: jdbc:h2:mem:testdb, user: sa, password: password"
echo "3. Run: UPDATE users SET role = 'ADMIN' WHERE id = $ADMIN_ID;"
echo "4. Then test admin endpoints:"
echo
echo "# Get pending applications (as admin)"
echo "curl -X GET $BASE_URL/api/author-applications/admin/pending \\"
echo "  -H \"Authorization: Bearer $ADMIN_TOKEN\""
echo
echo "# Approve application (replace {APP_ID} with actual ID)"
echo "curl -X PUT $BASE_URL/api/author-applications/admin/{APP_ID}/approve \\"
echo "  -H \"Authorization: Bearer $ADMIN_TOKEN\""
echo
echo "# Get application stats"
echo "curl -X GET $BASE_URL/api/author-applications/admin/stats \\"
echo "  -H \"Authorization: Bearer $ADMIN_TOKEN\""
echo
echo "# After approval, test story creation with reader token"
echo "curl -X POST $BASE_URL/api/stories \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Bearer $READER_TOKEN\" \\"
echo "  -d '{"
echo "    \"title\": \"My First Story\","
echo "    \"content\": \"This is my first story as a new author!\","
echo "    \"tags\": [\"debut\", \"fiction\"],"
echo "    \"contextTime\": \"SHORT\""
echo "  }'"