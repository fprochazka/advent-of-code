# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]          [max]         [last]
  1.    1.       input:         5.80ms        35.60ms         1.70ms
  1.    2.       input:         2.55ms         6.64ms         1.74ms
  2.    1.       input:         2.52ms         8.05ms         1.19ms
  2.    2.       input:         3.20ms        12.10ms         1.81ms
  3.    1.       input:         2.28ms        15.62ms         0.61ms
  3.    2.       input:         1.38ms         5.21ms         0.77ms
  4.    1.       input:        28.78ms        96.78ms        17.90ms
  4.    2.       input:         5.36ms        18.70ms         3.40ms
  5.    1.       input:         2.96ms        11.88ms         1.18ms
  5.    2.       input:         3.58ms         8.84ms         1.62ms
  6.    1.       input:         3.84ms        16.27ms         1.94ms
  6.    2.       input:        54.21ms       160.60ms        29.09ms
  7.    1.       input:        13.49ms        52.44ms         7.34ms
  7.    2.       input:       296.71ms       487.35ms       210.91ms
  8.    1.       input:         2.37ms        14.74ms         0.78ms
  8.    2.       input:         1.95ms         5.81ms         0.86ms
  9.    1.       input:         6.23ms        26.37ms         2.08ms
  9.    1. perf_test_1:         5.35ms        15.36ms         3.51ms
  9.    1. perf_test_2:        23.43ms        40.74ms        19.54ms
  9.    2.       input:        25.12ms        84.24ms        15.70ms
  9.    2. perf_test_1:        36.99ms        65.12ms        30.91ms
  9.    2. perf_test_2:       183.22ms       215.27ms       174.20ms
 10.    1.       input:         2.53ms        10.60ms         1.57ms
 10.    2.       input:         1.93ms         5.12ms         1.53ms
 11.    1.       input:         4.38ms        35.81ms         0.57ms
 11.    2.       input:        31.70ms        53.97ms        28.98ms
 12.    1.       input:        30.27ms        81.75ms        22.34ms
 12.    2.       input:        25.56ms        42.36ms        21.90ms

Sum of last for normal inputs: 377.478432ms
```

## Snapshot of `./gradlew benchmarkNative`:

```
 day  task                   [average]          [max]         [last]
  1.    1.       input:         1.37ms         2.16ms         1.15ms
  1.    2.       input:         1.31ms         1.84ms         1.20ms
  2.    1.       input:         2.01ms         2.78ms         1.90ms
  2.    2.       input:         4.55ms         6.14ms         3.96ms
  3.    1.       input:         1.22ms         1.69ms         1.11ms
  3.    2.       input:         1.59ms         1.85ms         1.42ms
  4.    1.       input:        67.69ms       160.06ms        49.88ms
  4.    2.       input:         6.62ms         9.80ms         6.27ms
  5.    1.       input:         1.89ms         2.65ms         1.87ms
  5.    2.       input:         4.50ms        20.01ms         2.65ms
  6.    1.       input:         3.35ms         3.43ms         3.43ms
  6.    2.       input:       110.24ms       147.74ms       118.87ms
  7.    1.       input:        19.39ms        22.82ms        22.82ms
  7.    2.       input:       822.23ms       879.14ms       879.14ms
  8.    1.       input:         0.67ms         0.75ms         0.66ms
  8.    2.       input:         0.84ms         1.79ms         0.70ms
  9.    1.       input:         7.39ms         9.34ms         9.34ms
  9.    1. perf_test_1:        12.63ms        17.44ms        11.62ms
  9.    1. perf_test_2:        72.21ms        90.31ms        63.79ms
  9.    2.       input:        28.86ms        35.88ms        27.19ms
  9.    2. perf_test_1:        63.58ms       107.91ms        90.98ms
  9.    2. perf_test_2:       409.33ms       566.03ms       465.16ms
 10.    1.       input:         2.99ms         3.19ms         2.99ms
 10.    2.       input:         3.61ms         6.72ms         2.95ms
 11.    1.       input:         0.97ms         0.99ms         0.97ms
 11.    2.       input:        67.06ms       119.21ms        50.17ms
 12.    1.       input:        57.26ms       145.46ms        39.56ms
 12.    2.       input:        46.42ms        85.31ms        39.94ms

Sum of last for normal inputs: 1.270143274s
```

lol?
