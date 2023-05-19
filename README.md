# Parallel Computing

## Group Method of Data Handling (GMDH)
The Group Method of Data Handling (GMDH) is a statistical technique for data prediction and modeling. GMDH is particularly useful for handling high-dimensional and nonlinear datasets in various fields like machine learning and time series analysis.
### This implementation provides GMDH speedup = 1.5 using Fork Join Framework
| Quantity of based functions | Parallel algorithm time, ms | Sequential algorithm time, ms | Speedup |
|-----------------------------|-----------------------------|-------------------------------|---------|
| 5                           | 186                         | 169                           | -       |
| 6                           | 239                         | 277                           | 1.159   |
| 7                           | 610                         | 735                           | 1.205   |
| 8                           | 2532                        | 3733                          | 1.474   |
| 9                           | 18503                       | 25576                         | 1.382   |
| 10                          | 185351                      | 278343                        | 1.501   |

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

## Lockers
### Bank
This is a bank program that works incorrect. Money is leaking. Used `synchronizedList` and lockers so program works correct.
### Journal
Used `ConcurrentLinkedQueue` and `LinkedBlockingQueue`. Implement an electronic group journal that stores grades in one discipline for three groups of students. Every week, the lecturer and his 3 assistants give grades in the discipline on a 100-point scale.
### Producer
Used `Guarded blocks`. Threads often have to coordinate their actions. The most common coordination idiom is the guarded block. Such a block begins by polling a condition that must be true before the block can proceed. There are a number of steps to follow in order to do this correctly.

## Fork Join
Work stealing was introduced in Java with the aim of reducing contention in multithreaded applications. This is done using the fork/join framework.
### Statistics
Used `ForkJoinPool` and `RecursiveAction` to build a statistical text analysis algorithm and determine the characteristics of the random variable "word length in characters". 
### Same words
Have got a folder as input. Children files are opened in new threads. Used `ForkJoinPool` and `RecursiveAction` to develop and implement an algorithm for finding common words in text documents.
### Find file
Same words are found in `Folder` as input. Some files in folder are expected to contain the `KEYWORD`. So files that meet the requirements are found.