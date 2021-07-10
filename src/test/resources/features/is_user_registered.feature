#Feature: unidentified user can access the register url
#  Scenario: Customer can register through POST with email and password
#    When user gives the email "test@test.com"
#    And  user gives the password "12345"
#    And  user makes a POST to "/register"
#    And  there is no user registered with this email
#    Then the client receives status code 201
#    And  the application sends an email to the user email