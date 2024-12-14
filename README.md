# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.61ms         2.99ms         0.88ms
  1.    2.       input:         1.70ms         3.96ms         0.94ms
  2.    1.       input:         0.86ms         2.29ms         0.52ms
  2.    2.       input:         1.25ms         2.79ms         0.80ms
  3.    1.       input:         0.53ms         0.69ms         0.45ms
  3.    2.       input:         0.58ms         0.83ms         0.48ms
  4.    1.       input:        10.17ms        14.46ms         8.79ms
  4.    2.       input:         1.65ms         2.47ms         1.12ms
  5.    1.       input:         1.16ms         2.21ms         0.89ms
  5.    2.       input:         1.57ms         2.22ms         1.30ms
  6.    1.       input:         0.94ms         2.17ms         0.65ms
  6.    2.       input:        32.28ms        37.75ms        28.34ms
  7.    1.       input:         5.08ms         5.51ms         4.86ms
  7.    2.       input:       132.32ms       138.40ms       128.21ms
  8.    1.       input:         0.49ms         0.80ms         0.37ms
  8.    2.       input:         0.52ms         0.92ms         0.38ms
  9.    1.       input:         1.65ms         2.51ms         1.37ms
  9.    1. perf_test_1:         3.02ms         4.91ms         2.24ms
  9.    1. perf_test_2:        11.75ms        15.85ms        10.55ms
  9.    2.       input:         9.99ms        15.21ms         8.96ms
  9.    2. perf_test_1:        18.35ms        20.10ms        16.88ms
  9.    2. perf_test_2:       109.88ms       124.49ms       103.85ms
 10.    1.       input:         0.84ms         0.88ms         0.81ms
 10.    2.       input:         0.71ms         0.74ms         0.66ms
 11.    1.       input:         0.34ms         0.36ms         0.33ms
 11.    2.       input:        14.35ms        17.73ms        12.78ms
 12.    1.       input:        13.87ms        16.28ms        12.99ms
 12.    2.       input:        15.74ms        18.97ms        14.47ms
 13.    1.       input:         0.36ms         0.52ms         0.31ms
 13.    2.       input:         0.33ms         0.50ms         0.27ms
 14.    1.       input:         0.64ms         2.16ms         0.39ms

Sum of average times for normal inputs: 251.515ms
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
