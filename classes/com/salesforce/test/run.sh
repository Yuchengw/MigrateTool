#!/bin/bash

ARG_ERROR=65
LIB_PATH=../../../../lib/bulkapi/force-wsc-27.0.0.jar:../../../../lib/bulkapi/partner.jar 
A_PATH=../../../../lib/bulkapi/force-wsc-27.0.0.jar
B_PATH=../../../../lib/bulkapi/partner.jar 
CLASS_PATH=../../../../classes/
JAVA_PATH=java


if [ $# -ne 1 ]
	then echo "./run.sh <java File Name>"
	exit $ARG_ERROR
fi

	$JAVA_PATH  -cp $LIB_PATH:$CLASS_PATH $1 -jar $B_PATH

