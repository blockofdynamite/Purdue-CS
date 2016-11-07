#!/usr/bin/env bash
javac project3.java
java project3 < data/test_data_$1.txt > data/output$1.data.txt

if cmp -s data/output$1.data.txt data/expected_output$1_data.txt
then
   echo "Sanity Test Passed!"
else
   echo "Output Differs"
fi
