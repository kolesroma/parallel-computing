# Parallel Computing
## Priority
### Bounce
This is a program for simulating the movement of billiard balls. The movement of each ball is reproduced in a separate thread. Upon entering the pocket, the ball disappears, and the corresponding thread completes its work. Red balls are created with a higher priority of the thread in which they move faster than blue ones. 
### Counter
This is a program that performs parallel arithmetic operations. Different types of synchronization is used.
### Symbol
This is a program that prints characters in parallel. Synchronized access is used for the correct operation of threads when working simultaneously. The output one-by-one to the console was achieved.
## Matrix
This is a program that multiplies matrices in parallel. Algorithms used:
- Naive
- Striped
- Fox

For matrix 1000*1000 (multiplied by itself)

|              | Naive implementation | Fox algorithm | Striped algorithm |
|--------------|----------------------|---------------|-------------------|
| Time, millis | 5511                 | 4295          | 3368              |
| Cluster size | -                    | 500           | 500               |
