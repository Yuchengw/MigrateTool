========================================================
MigrateTool
copyright (c) 2013, Yucheng Wang,  Vijay Swamidass
Authors: Yucheng Wang
========================================================
Description:

MigrateTool is an extensible org objects migrate tool for Salesforce.com. 
It is implemented by Java, which keeps consistent with Salesforce.com.

========================================================
Features:

- could migrate object's data from one org to another org
- could insert user-defined log to specific org
- could query object info from a log
- logger based application
- flexible disign, easy to add new features

=========================================================
System requirements:

- Standard JAVA JDK
- Standard ANT 

=========================================================
Installation and Build:

- Enter into build directory
- ant clean (remove patch directory)
- ant compile (generate patch directory in bin)
- ant jar (generate patch directory in bin by build time)
- ant all (ant compile plus ant jar)
- ant doc (generate javadoc in doc directory)

=========================================================
Usage
- 1. clone latest release into your local directory
  git clone https://github.com/Yuchengw/MigrateTool.git 
- 2. Go to build directory, then enter following command:
  ant jar OR ant all to get tool.jar in bin directory
- 3. Go to bin directory, go to patch_jar_LATESTTIME 
  then enter following command:
  java -jar tool.jar -h [--help]
- 4. Review the sample xml and csv files in bkp directory 
  to see how to configure
- 5. Copy XML configuration file into patch directory
- 6. Run the command 
  e.g., java -jar tool.jar -m -v  

=========================================================
BUG FIXES
- Please contact yucheng.wang@salesforce.com to address any 
  bugs you find, thanks!
=========================================================






