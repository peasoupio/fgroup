Feature: What are the files I am seeking ?
  Caller wants to know which file should be seeked from a configuration file

  Scenario Outline: Seeking is valid
    Given seeker configuration file is "<configFile>"
    When I set the user.dir to the test resources folder
    Then I should be told the seeker report file "<seekerReportFile>"

    Examples:
      | configFile                       | seekerReportFile                 |
      # Current starting point
      | /fileseeker/configs/config1.txt  | /fileseeker/reports/report1.txt  |
      | /fileseeker/configs/config2.txt  | /fileseeker/reports/report2.txt  |
      | /fileseeker/configs/config3.txt  | /fileseeker/reports/report3.txt  |
      | /fileseeker/configs/config4.txt  | /fileseeker/reports/report4.txt  |
      | /fileseeker/configs/config5.txt  | /fileseeker/reports/report5.txt  |
      | /fileseeker/configs/config6.txt  | /fileseeker/reports/report6.txt  |
      # Parent starting point
      | /fileseeker/configs/config10.txt | /fileseeker/reports/report10.txt |
      | /fileseeker/configs/config11.txt | /fileseeker/reports/report11.txt |
      # Tree starting point
      | /fileseeker/configs/config20.txt | /fileseeker/reports/report20.txt |
      # Empty results and errors configurations
      | /fileseeker/configs/config90.txt | /fileseeker/reports/report90.txt |
      | /fileseeker/configs/config91.txt | /fileseeker/reports/report91.txt |
      | /fileseeker/configs/config92.txt | /fileseeker/reports/report92.txt |
      | /fileseeker/configs/config93.txt | /fileseeker/reports/report93.txt |
      # Other peasoup project
      | /fileseeker/configs/inv.txt      | /fileseeker/reports/inv.txt      |
