Feature: What is my parsed configuration ?
  Caller wants to parse configuration

  Scenario Outline: Parsing is valid
    Given configuration file is "<configFile>"
    When I set the user.dir "<userDir>" to determine the parsing context
    Then I should be told the filter chain "<filterChain>"

    Examples:
      | configFile           | userDir                     | filterChain                                                                         |
    # Simple configuration files
      | /parser/config1.txt  | c:/Windows/System32         | [ROOT] [c:]/Windows/System32/(*.dll)                                                |
      | /parser/config2.txt  | c:/Windows                  | [TREE] [c:,c:/Windows]/System32/(*.dll)                                             |
      | /parser/config3.txt  | c:/Windows                  | [CURRENT] [c:/Windows]/System32/(*.dll)                                             |
      | /parser/config4.txt  | c:/Windows/System32/drivers | [PARENT] [c:/Windows/System32]/(*.dll)                                              |
    # Complex configuration files
      | /parser/config10.txt | c:/Windows/System32         | [ROOT] [c:]/Windows/System\|System32/(*.dll)                                        |
      | /parser/config11.txt | c:/Windows/System32         | [ROOT] [c:]/Windows/System32/(*.dll)\n[ROOT] [c:]/Windows/System/(*.dll)   |
      | /parser/config12.txt | c:/Windows/System32         | [ROOT] [c:]/Windows/System32/(*.dll)                                                |