# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]          [max]         [last]
  1.    1.       input:         5.76ms        38.08ms         1.65ms
  1.    2.       input:         3.23ms         7.28ms         1.80ms
  2.    1.       input:         2.83ms        10.13ms         0.81ms
  2.    2.       input:         3.63ms        13.88ms         1.41ms
  3.    1.       input:         2.35ms        16.03ms         0.61ms
  3.    2.       input:         1.51ms         6.45ms         0.76ms
  4.    1.       input:        30.39ms       100.28ms        18.05ms
  4.    2.       input:         6.12ms        20.82ms         3.52ms
  5.    1.       input:         3.00ms        11.73ms         1.17ms
  5.    2.       input:         3.75ms         8.08ms         1.63ms
  6.    1.       input:         4.45ms        20.54ms         1.94ms
  6.    2.       input:        62.89ms       169.87ms        32.96ms
  7.    1.       input:        13.93ms        54.85ms         8.70ms
  7.    2.       input:       301.94ms       539.29ms       236.01ms
  8.    1.       input:         2.56ms        13.48ms         0.76ms
  8.    2.       input:         1.56ms         3.92ms         0.83ms
  9.    1.       input:         5.74ms        28.44ms         2.19ms
  9.    1. perf_test_1:         5.56ms        16.18ms         3.68ms
  9.    1. perf_test_2:        26.46ms        38.70ms        24.14ms
  9.    2.       input:        23.95ms        97.78ms        15.38ms
  9.    2. perf_test_1:        33.12ms        49.18ms        29.49ms
  9.    2. perf_test_2:       179.89ms       218.02ms       174.75ms
 10.    1.       input:         2.78ms        10.50ms         2.39ms
 10.    2.       input:         2.04ms         5.04ms         1.52ms
 11.    1.       input:         0.91ms         2.81ms         0.58ms
 11.    2.       input:        34.13ms        54.71ms        29.99ms

Sum of last for normal inputs: 364.671732ms
```

## Snapshot of `./gradlew benchmarkNative`:

```
 day  task                   [average]          [max]         [last]
  1.    1.       input:         1.51ms         3.42ms         1.23ms
  1.    2.       input:         1.35ms         1.85ms         1.26ms
  2.    1.       input:         1.93ms         2.63ms         1.83ms
  2.    2.       input:         4.73ms        12.47ms         3.83ms
  3.    1.       input:         1.14ms         1.45ms         1.07ms
  3.    2.       input:         1.63ms         2.77ms         1.42ms
  4.    1.       input:        73.26ms       143.40ms        64.64ms
  4.    2.       input:         7.35ms        13.55ms         6.56ms
  5.    1.       input:         1.99ms         2.84ms         1.85ms
  5.    2.       input:         3.07ms         4.22ms         2.79ms
  6.    1.       input:         5.38ms        19.84ms         4.01ms
  6.    2.       input:       134.71ms       230.52ms       135.60ms
  7.    1.       input:        21.66ms        38.73ms        38.73ms
  7.    2.       input:       878.53ms      1016.99ms      1016.99ms
  8.    1.       input:         0.63ms         0.69ms         0.62ms
  8.    2.       input:         0.68ms         0.70ms         0.69ms
  9.    1.       input:         6.91ms         8.60ms         6.92ms
  9.    1. perf_test_1:        11.77ms        14.07ms        11.97ms
  9.    1. perf_test_2:        67.78ms        96.75ms        69.98ms
  9.    2.       input:        29.98ms        43.32ms        32.95ms
  9.    2. perf_test_1:        57.26ms        61.25ms        56.58ms
  9.    2. perf_test_2:       348.22ms       409.80ms       300.61ms
 10.    1.       input:         2.99ms         3.45ms         2.96ms
 10.    2.       input:         3.00ms         3.83ms         2.84ms
 11.    1.       input:         0.99ms         1.10ms         0.94ms
 11.    2.       input:        77.59ms       170.66ms        52.29ms

Sum of last for normal inputs: 1.382048680s
```

lol?
