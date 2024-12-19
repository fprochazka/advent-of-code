# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.26ms         1.67ms         0.92ms
  1.    2.       input:         1.29ms         1.56ms         1.01ms
  2.    1.       input:         0.70ms         0.89ms         0.59ms
  2.    2.       input:         1.04ms         1.32ms         0.87ms
  3.    1.       input:         0.45ms         0.54ms         0.41ms
  3.    2.       input:         0.50ms         0.55ms         0.46ms
  4.    1.       input:        10.07ms        11.79ms         9.36ms
  4.    2.       input:         1.58ms         2.36ms         1.14ms
  5.    1.       input:         1.01ms         1.14ms         0.94ms
  5.    2.       input:         1.35ms         1.49ms         1.23ms
  6.    1.       input:         0.84ms         1.47ms         0.62ms
  6.    2.       input:        29.77ms        32.94ms        27.67ms
  7.    1.       input:         5.45ms         7.18ms         4.72ms
  7.    2.       input:       130.82ms       149.94ms       124.11ms
  8.    1.       input:         0.36ms         0.40ms         0.33ms
  8.    2.       input:         0.35ms         0.39ms         0.31ms
  9.    1.       input:         1.46ms         1.87ms         1.37ms
  9.    1. perf_test_1:         2.42ms         2.85ms         2.21ms
  9.    1. perf_test_2:        10.99ms        12.83ms        10.05ms
  9.    2.       input:        11.64ms        16.35ms         9.24ms
  9.    2. perf_test_1:        21.18ms        35.29ms        17.04ms
  9.    2. perf_test_2:       117.55ms       141.98ms       107.39ms
 10.    1.       input:         0.81ms         0.87ms         0.77ms
 10.    2.       input:         0.68ms         0.72ms         0.61ms
 11.    1.       input:         0.34ms         0.36ms         0.32ms
 11.    2.       input:        16.33ms        19.25ms        13.35ms
 12.    1.       input:        18.32ms        21.02ms        16.21ms
 12.    2.       input:        19.87ms        29.63ms        17.33ms
 13.    1.       input:         0.32ms         0.43ms         0.28ms
 13.    2.       input:         0.29ms         0.38ms         0.26ms
 14.    1.       input:         0.39ms         0.49ms         0.34ms
 15.    1.       input:         1.03ms         2.37ms         0.79ms
 15.    2.       input:         1.78ms         2.22ms         1.63ms
 16.    1.       input:         8.30ms         9.43ms         7.09ms
 16.    2.       input:        28.54ms        39.15ms        25.78ms
 17.    1.       input:         0.15ms         0.19ms         0.12ms
 17.    2.       input:         0.22ms         0.46ms         0.12ms
 18.    1.       input:         2.75ms         5.21ms         1.68ms
 18.    2.       input:        12.75ms        16.15ms        10.97ms
 19.    1.       input:         5.85ms         7.00ms         4.92ms
 19.    2.       input:         6.49ms         8.67ms         5.59ms

Sum of average times for normal inputs: 325.129ms
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
