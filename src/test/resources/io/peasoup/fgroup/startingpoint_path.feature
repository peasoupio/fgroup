Feature: What is my starting point path ?
  Caller wants to know the starting point path based
  on the desired token.

  Scenario Outline: Path is valid
    Given token is "<token>"
    When I set the user.dir "<userDir>" to determine the path context
    Then I should be told its paths "<expectedPaths>"

    Examples:
      | token          | userDir    | expectedPaths         |
      | ^/             | c:/my/dir/ | c:/                   |
      | */             | c:/my/dir/ | c:/, c:/my, c:/my/dir |
      | ./             | c:/my/dir/ | c:/my/dir             |
      | ../            | c:/my/dir/ | c:/my                 |
      | anything else! | c:/my/dir/ | null                  |


