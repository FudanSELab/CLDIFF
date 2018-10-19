## This is readme about CLDiff

### TODOs
* A lot of refactoring work need to be done.
* Detailed documentation of project

### How to start
1. start CLDiff in command line mode:
    * Run CLDiff in main.CLDIFFCmd.java
    * As you can see in CLDIFFCmd.run(),CLDiff accepts three arguments: repository path(ends with "/.git"), commit id and CLDiff cache directory(/foo/bar).
2. start CLDiff with CLDiff-Web
    * Deploy [CLDiff-Web](https://github.com/FudanSELab/CLDIFF-WEB) to web container(e.g. Tomcat)
    * In CLDiff, run main.CLDIFFServerOffline.java. Remember to pass two arguments: repository path(ends with "/.git") and CLDiff cache directory(/foo/bar).

### Description
CLDiff aims to generate code differences whose granularity  is in between the existing code differencing and code change summarization methods.
See our paper in [ACM digital library](https://dl.acm.org/citation.cfm?id=3238219)

### Web GUI
Visualization for CLDiff can be found [here](https://github.com/FudanSELab/CLDIFF-WEB).
### Supported languages
Currently it only supports java files.


### Documentation
Currently we have documenting plans on documentation about how to use CLDiff.
If you have any other requests, please contact me.

### Contact
Please feel free to contact me if you have any requests. My email address is kfhuang16 atat fudan.edu.cn