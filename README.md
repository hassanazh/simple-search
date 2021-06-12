#Simple search application

### Requirement
This should read all the text files in the given directory, building an in memory representation of the files and their contents, and then give a command prompt at which interactive searches can be performed.

The search should take the words given on the prompt and return a list of the top 10 (maximum) matching filenames in rank order, giving the rank score for each match respectively.

### How to execute

- Clean `sbt clean`
- Compile `sbt compile`
- Test `sbt test`
- **Run** `sbt run [Folder Path] e.g. /foo/bar`
- Search keywords: Enter list of keywords separated by space
- To quite type `:quit`

##### Example
```
    sbt run /foo/bar
    4 files read in directory /foo/bar
    search> to be or not to be
    file1.txt : 100.0% file2.txt : 90.0% 
    search>  cats 
    no matches found 
    search>  :quit
    Thank you! 
```
