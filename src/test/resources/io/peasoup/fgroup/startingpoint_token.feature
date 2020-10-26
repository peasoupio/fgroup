Feature: What is my starting point token ?
  Caller wants to know the name of the starting
  point.

  Scenario Outline: Token is valid and has a name
    Given token is "<token>"
    When I ask whether it's valid and has a name
    Then I should be told its name "<name>"

    Examples:
      | token          | name    |
      | ^/             | root    |
      | */             | tree    |
      | ./             | current |
      | ../            | parent  |
      | anything else! | null    |

