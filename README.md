# fgroup
***Fgroup*** is a java utility to seek and capture for groups of files using GLOB-like patterns.

[![Build Status](https://travis-ci.com/peasoupio/fgroup.svg?branch=release%2F0.1-beta)](https://travis-ci.com/peasoupio/fgroup)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fgroup&metric=alert_status)](https://sonarcloud.io/dashboard?id=fgroup)

##  :sparkles: Features

| Feature | Implemented |
| --- | :---: |
| Current point location    | Yes :+1:  |   
| Parent starting point  | Yes :+1:  | 
| Root starting point  | Yes :+1:  |
| Tree starting point  | Yes :+1:  |
| Allow 2-spaces indent | Not yet :-1: |
| Capture patterns on different indents | Not yet :-1: | 

### Install
#### Prerequisites
* Java 11 or newer.  

#### Using Maven
```xml
<dependency>
    <groupId>io.peasoup</groupId>
    <artifactId>fgroup</artifactId>
    <version>0.1</version>
</dependency>
```

#### Using the CLI (available)
```
fgroup, version:  0.1.

Usage:
    fgroup [-d <dir>] <config>

Options:
    -d, --directory Root directory. 
                    By default it uses the current 
                    process directory

Parameters:
    <config>        Configuration file wherein instructions are
                    given to fgroup.
    <dir>           Specific root directory.
                    It will override the default value.
```

### How it works
***Fgroup*** uses a file text as its configurations and then tries to find file matching them.

The configuration text file work like this:
* Each line implies a new file pattern using GLOB-like syntax;
* Each indent means a subdirectory. An indent is a `\t` character OR 4 spaces.
* Only starting points are allowed at the start of a line;
  **IMPORTANT** starting point token can be written at the first indent.
* There are 4 starting points: 
    * Current starting point, token is `"./"`. It starts at the current directory where ***fgroup** is being used at. Equivalent to `System.getProperty("user.dir")`;
    * Parent starting point, token is `"../"`. It starts from the parent directory of the current one;
    * Root starting point, token is `"^/"`. It starts at the root of the current drive;
    * Tree starting point, token is `"*/"`. From the current directory, it will use all its parent directory, including the root.
* To capture matching files, use a capture pattern with the following syntax: `(PATTERN as 'PATTERNNAME')`;
* You can have has many capture pattern as you want;  
  **IMPORTANT**: All capture patterns has to be on the same indent, on individual lines.  
  **IMPORTANT 2**: You can't capture from multiple indents (yet). Each capture patterns has to happen on the same indent for the same capture group.
* A capture group includes only a single starting point with multiple files and captures patterns.
* You can have has many capture groups as you want;
* Commenting is available using the `#` character.

Here is an example (from ***INV***):
```
# Capture Groovy class files from the /src directory.
./
    src
        (*.groovy as 'groovyFiles')

# Capture Groovy test (Junit) files from the /test directory.
./
    test
        (*.groovy as 'groovyTestFiles')

# Capture different flavors of INV files.
./
    vars
        (*.groovy as 'invFiles')
        (*.yml as 'invFiles')
        (*.yaml as 'invFiles')

# Capture different flavors of REPO files. Expect only one.
./
    (repo.groovy as 'repoFile')
    (repo.yml as 'repoFile')
    (repo.yaml as 'repoFile')
```
>1. There are 4 capturing groups;
>1. Each capture group uses the current starting point using the `./` token;
>1. Since `src` and `test` are two different directories, even if the capture pattern is similar, they have to be defined in two separate capture group.
>1. `./`, and `vars/` are using multiple capture patterns with the same pattern name. 

Here is how you would the configuration above (used as `fgroup.txt`):
```java
import io.peasoup.fgroup.FileMatches;
import io.peasoup.fgroup.FileSeeker;

Path configFile = Path.of(tmpFolder, "fgroup.txt");
FileSeeker fileSeeker = new FileSeeker(configFile.toString());

for (FileMatches.FileMatchRecord match : fileMatches.get("groovyFiles")) {
    System.out.println("Here is one of my Groovy files: " + match.getCurrent());
}

```



  