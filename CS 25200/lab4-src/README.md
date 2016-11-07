--- Part 1 ---
- Random combination of the letters A-C
- The print funtion, which is an infinite loop, was called before the threads. 

--- Part 3 ---
- count:

[   0 ][ hughe127@pod4-2 ][ Sun Mar 27 @ 23:07:00 ][ ~/cs252/lab4-src ]  
$ time ./count
Start Test. Final count should be 20000000

\>\>\>\>\>\> O.K. Final count is 20000000

real	0m1.600s
user	0m1.516s
sys	0m1.624s

- count\_spin w/o pthread\_yield()

[   0 ][ hughe127@pod4-2 ][ Sun Mar 27 @ 23:17:19 ][ ~/cs252/lab4-src ]  
$ time ./count\_spin
Start Test. Final count should be 20000000

>>>>>> O.K. Final count is 20000000

real	0m1.188s
user	0m2.284s
sys	0m0.000s

- count\_spin w/ pthread\_yield()

[   0 ][ hughe127@pod4-2 ][ Sun Mar 27 @ 23:19:19 ][ ~/cs252/lab4-src ]  
$ time ./count\_spin
Start Test. Final count should be 20000000

>>>>>> O.K. Final count is 20000000

real	0m0.377s
user	0m0.524s
sys	0m0.108s

- thr\_yield queues the threads, and they have to wait, which causes more user time
- thr\_yield causes system time because w/o the locks, they're locked in userland, but with thr\_yield, it takes system time to check for the thread to be done. 
