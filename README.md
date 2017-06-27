# NASA-api

The goal for this API testing project is to validate that it meets expectations for functionality, reliability, performance, and security of https://api.nasa.gov/planetary/sounds.  
Normally the testing is conducted against a Mock server or Development environment before it is available the the public in a Production
environment.  For the sake of this project I am assume this API is under a test environment and not yet released to the public.

Technologies used: Java, MVN, TestNG

# To run import zip or clone into Eclipse/IDE
From Command line, navigate to root project directory and execute "mvn test".
Frome Eclipse Right click Project root folder and run as Maven Test.

# Test Cases:
* testValidRequest: Test Valid request and verify response code
* testEmptySearchRequest: Test empty search request and verify response code and limit value in response
* testInvalidSearchRequest: Test search request that should not return results and verify response code and limit value in response	
* testLargeLimit: Test large limit parameter value
* testSmallLimit: Test small limit parameter value
* testInvalidLimit: Test invalid limit parameter value
* testMissingParameters: Both Q and Limit values are missing.  Default values used by API
* testInvalidKey: Verify invalid key used receives the correct unauthorized response code
* testEmptyKey: Validate status code, code, and error message response for Missing Key
* testRequestResponseTime: Verify response time for request is within expected range

# Known issues with API
* API does not respect q search parameter. Any values for q returns the same response.  If this is fixed or changed, testEmptySearchRequest and testInvalidSearchRequest will catch this.
* API data is static and limited to 64 items.  This allows the verification of count parameter to reach an upper limit.  Also response time testing of extreme limit size is not done due to known issue.
* Description is null for various results.  Will need to refer to schema specification to validate if this is valid or not.
* Rate limiting error on frequent API usage does not allow for scalability or performance testing.  

# To Do:
Schema Validation of JSON 
