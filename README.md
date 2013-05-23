========================================================
MigrateTool
copyright (c) 2013, Yucheng Wang,  Vijay Swamidass
https://github.com/Yuchengw/MigrateTool
Authors: Yucheng Wang
========================================================
Description:

MigrateTool is an extensible org objects migrate tool for Salesforce.com. 
It is implemented by Java, which keeps consistent with Salesforce.com.

========================================================
Features:

- could migrate object's info from one org to another org, 
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
- please see the sample xml files in bkp directory 

=========================================================
BUG FIXES
- Please contact yucheng.wang@salesforce.com to address any 
  bugs you find, thanks!
=========================================================






