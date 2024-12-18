# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.31ms         1.47ms         0.94ms
  1.    2.       input:         1.53ms         2.94ms         0.97ms
  2.    1.       input:         0.60ms         0.69ms         0.51ms
  2.    2.       input:         0.94ms         0.99ms         0.80ms
  3.    1.       input:         0.46ms         0.65ms         0.42ms
  3.    2.       input:         0.52ms         0.77ms         0.47ms
  4.    1.       input:         9.74ms        12.97ms         9.05ms
  4.    2.       input:         1.46ms         2.20ms         1.15ms
  5.    1.       input:         0.95ms         0.98ms         0.90ms
  5.    2.       input:         1.28ms         1.33ms         1.17ms
  6.    1.       input:         0.82ms         1.28ms         0.66ms
  6.    2.       input:        32.25ms        37.57ms        29.28ms
  7.    1.       input:         5.32ms         6.82ms         4.89ms
  7.    2.       input:       133.09ms       134.51ms       131.07ms
  8.    1.       input:         0.37ms         0.50ms         0.32ms
  8.    2.       input:         0.35ms         0.38ms         0.33ms
  9.    1.       input:         1.46ms         1.60ms         1.39ms
  9.    1. perf_test_1:         2.56ms         4.39ms         2.15ms
  9.    1. perf_test_2:        10.68ms        10.93ms        10.32ms
  9.    2.       input:         9.95ms        11.51ms         9.36ms
  9.    2. perf_test_1:        17.73ms        19.38ms        16.58ms
  9.    2. perf_test_2:       114.54ms       137.25ms       108.18ms
 10.    1.       input:         0.84ms         0.95ms         0.80ms
 10.    2.       input:         0.69ms         0.73ms         0.66ms
 11.    1.       input:         0.42ms         0.86ms         0.36ms
 11.    2.       input:        19.78ms        42.27ms        16.18ms
 12.    1.       input:        16.75ms        17.39ms        15.95ms
 12.    2.       input:        19.10ms        21.37ms        17.87ms
 13.    1.       input:         0.33ms         0.36ms         0.29ms
 13.    2.       input:         0.29ms         0.40ms         0.26ms
 14.    1.       input:         0.39ms         0.55ms         0.34ms
 15.    1.       input:         0.79ms         0.81ms         0.78ms
 15.    2.       input:         1.91ms         3.75ms         1.63ms
 16.    1.       input:         8.20ms         9.81ms         7.11ms
 16.    2.       input:        26.85ms        40.68ms        23.41ms
 17.    1.       input:         0.15ms         0.18ms         0.13ms
 17.    2.       input:         0.23ms         0.42ms         0.15ms
 18.    1.       input:         2.64ms         3.04ms         2.01ms
 18.    2.       input:        16.89ms        19.71ms        15.42ms

Sum of average times for normal inputs: 318.621ms
```

## Snapshot of `./gradlew benchmarkNative`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         0.60ms         0.62ms         0.56ms
  1.    2.       input:         0.61ms         0.64ms         0.57ms
  2.    1.       input:         1.19ms         1.29ms         0.94ms
  2.    2.       input:         2.10ms         2.19ms         1.84ms
  3.    1.       input:         0.63ms         0.84ms         0.56ms
  3.    2.       input:         1.01ms         2.59ms         0.76ms
  4.    1.       input:        27.91ms        39.17ms        24.60ms
  4.    2.       input:         4.39ms         9.79ms         3.29ms
  5.    1.       input:         1.20ms         3.33ms         0.89ms
  5.    2.       input:         1.68ms         3.64ms         1.36ms
  6.    1.       input:         9.31ms        69.18ms         1.71ms
  6.    2.       input:        93.55ms       138.22ms        64.01ms
  7.    1.       input:         9.89ms        10.21ms         9.67ms
  7.    2.       input:       449.43ms       470.37ms       430.59ms
  8.    1.       input:         0.33ms         0.37ms         0.32ms
  8.    2.       input:         0.33ms         0.34ms         0.32ms
  9.    1.       input:         4.06ms         4.26ms         3.90ms
  9.    1. perf_test_1:         6.98ms         8.40ms         6.56ms
  9.    1. perf_test_2:        42.86ms        56.46ms        36.49ms
  9.    2.       input:        16.66ms        18.60ms        15.98ms
  9.    2. perf_test_1:        34.02ms        41.21ms        30.98ms
  9.    2. perf_test_2:       211.23ms       269.54ms       181.10ms
 10.    1.       input:         1.61ms         1.92ms         1.49ms
 10.    2.       input:         1.51ms         1.57ms         1.42ms
 11.    1.       input:         0.51ms         0.54ms         0.49ms
 11.    2.       input:        32.96ms        73.37ms        26.13ms
 12.    1.       input:        30.61ms        55.73ms        24.22ms
 12.    2.       input:        30.24ms        35.26ms        27.02ms
 13.    1.       input:         0.43ms         0.59ms         0.40ms
 13.    2.       input:         0.39ms         0.62ms         0.34ms
 14.    1.       input:         0.54ms         0.56ms         0.51ms

Sum of average times for normal inputs: 723.69ms
```

lol?
