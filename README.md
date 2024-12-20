# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.14ms         1.17ms         1.11ms
  1.    2.       input:         1.14ms         1.15ms         1.12ms
  2.    1.       input:         0.67ms         0.76ms         0.63ms
  2.    2.       input:         0.96ms         1.10ms         0.90ms
  3.    1.       input:         0.40ms         0.41ms         0.39ms
  3.    2.       input:         0.45ms         0.47ms         0.44ms
  4.    1.       input:         9.78ms        10.04ms         9.63ms
  4.    2.       input:         1.78ms         2.72ms         1.43ms
  5.    1.       input:         0.71ms         0.75ms         0.69ms
  5.    2.       input:         0.99ms         1.05ms         0.95ms
  6.    1.       input:         0.76ms         0.95ms         0.70ms
  6.    2.       input:        31.73ms        33.70ms        30.22ms
  7.    1.       input:         5.04ms         5.20ms         4.92ms
  7.    2.       input:       120.35ms       120.55ms       120.17ms
  8.    1.       input:         0.33ms         0.36ms         0.30ms
  8.    2.       input:         0.38ms         0.46ms         0.34ms
  9.    1.       input:         1.46ms         1.64ms         1.40ms
  9.    1. perf_test_1:         2.18ms         2.31ms         2.10ms
  9.    1. perf_test_2:        10.34ms        10.40ms        10.26ms
  9.    2.       input:         8.98ms         9.55ms         7.56ms
  9.    2. perf_test_1:        16.44ms        17.14ms        15.96ms
  9.    2. perf_test_2:       104.73ms       117.09ms        98.83ms
 10.    1.       input:         0.74ms         0.81ms         0.71ms
 10.    2.       input:         0.62ms         0.66ms         0.60ms
 11.    1.       input:         0.24ms         0.25ms         0.23ms
 11.    2.       input:        10.31ms        10.60ms        10.11ms
 12.    1.       input:        17.13ms        28.81ms        12.84ms
 12.    2.       input:        15.78ms        18.13ms        14.51ms
 13.    1.       input:         0.27ms         0.28ms         0.27ms
 13.    2.       input:         0.25ms         0.26ms         0.25ms
 14.    1.       input:         0.35ms         0.35ms         0.34ms
 15.    1.       input:         0.83ms         0.88ms         0.80ms
 15.    2.       input:         1.31ms         1.37ms         1.20ms
 16.    1.       input:         6.26ms         7.24ms         5.58ms
 16.    2.       input:        26.70ms        35.18ms        23.44ms
 17.    1.       input:         0.13ms         0.14ms         0.12ms
 17.    2.       input:         0.15ms         0.20ms         0.10ms
 18.    1.       input:         1.60ms         2.21ms         1.38ms
 18.    2.       input:         9.09ms        10.23ms         8.61ms
 19.    1.       input:         4.99ms         5.79ms         4.68ms
 19.    2.       input:         5.44ms         5.47ms         5.42ms
 20.    1.       input:         3.86ms         3.91ms         3.82ms
 20.    2.       input:        12.68ms        14.01ms        11.47ms

Sum of average times for normal inputs: 305.761ms
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
