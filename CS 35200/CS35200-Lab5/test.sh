NUMLOOPS=0                                 
MAXLOOPS=50                                 # number of tests
while [ "$NUMLOOPS" -le "$MAXLOOPS" ]
do
  COUNT=1                
  MAXCOUNT=10                               # number of ints per test
  while [ "$COUNT" -le "$MAXCOUNT" ]; do
    numbers[$COUNT]=$RANDOM
    let "COUNT += 1"
  done
  sorted=($(echo ${numbers[*]} | tr " " "\n" | sort -n))
  rm -f ./correct_output.txt
  # output sorted array since our output is 1 element per line
  echo "Sorted:" >> ./correct_output.txt
  for x in "${sorted[@]}"
  do
    echo "$x" >> ./correct_output.txt
  done
  echo "${numbers[*]}" > input.txt
  rm -f ./my_output.txt
  ./sort < input.txt > my_output.txt
  DIFF=$(diff ./correct_output.txt ./my_output.txt)
  if [ "$DIFF" != "" ]
  then
    echo "Failed"
  else
    echo "Success"
  fi
  let "NUMLOOPS += 1"
  rm -f ./correct_output.txt
  rm -f ./my_output.txt
  rm -f ./input.txt
done
