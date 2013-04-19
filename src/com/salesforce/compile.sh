#!/bin/bash

ARG_ERROR=65
LIB_PATH=../../../lib/bulkapi/force-wsc-27.0.0.jar:../../../lib/bulkapi/partner.jar 
CLASS_PATH=../../../classes/
JAVA_PATH=/home/yucheng.wang/work/dev/tools/Linux/jdk/jdk1.6.0_31_x64/bin

if [ $# -ne 1 ]
	then echo "./compilesh <java File Name>"
	exit $ARG_ERROR
fi

{
	$JAVA_PATH/javac -Xlint -cp $LIB_PATH:$CLASS_PATH -d $CLASS_PATH $1
} > /dev/null 1>&1

