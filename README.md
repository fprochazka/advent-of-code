# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         0.83ms         0.86ms         0.81ms
  1.    2.       input:         0.89ms         0.94ms         0.86ms
  2.    1.       input:         0.53ms         0.56ms         0.52ms
  2.    2.       input:         0.80ms         0.83ms         0.78ms
  3.    1.       input:         0.37ms         0.38ms         0.36ms
  3.    2.       input:         0.43ms         0.44ms         0.42ms
  4.    1.       input:         8.41ms         8.57ms         8.34ms
  4.    2.       input:         1.10ms         1.14ms         1.04ms
  5.    1.       input:         0.69ms         0.70ms         0.68ms
  5.    2.       input:         0.98ms         1.03ms         0.95ms
  6.    1.       input:         0.49ms         0.52ms         0.48ms
  6.    2.       input:        28.60ms        30.30ms        27.41ms
  7.    1.       input:         5.02ms         5.16ms         4.87ms
  7.    2.       input:       121.14ms       124.10ms       119.84ms
  8.    1.       input:         0.29ms         0.34ms         0.26ms
  8.    2.       input:         0.35ms         0.38ms         0.32ms
  9.    1.       input:         1.37ms         1.40ms         1.34ms
  9.    1. perf_test_1:         2.11ms         2.20ms         2.06ms
  9.    1. perf_test_2:        12.73ms        18.52ms        10.03ms
  9.    2.       input:         8.90ms         9.46ms         8.64ms
  9.    2. perf_test_1:        15.96ms        16.96ms        15.49ms
  9.    2. perf_test_2:       109.47ms       127.48ms       101.78ms
 10.    1.       input:         0.76ms         0.79ms         0.71ms
 10.    2.       input:         0.62ms         0.64ms         0.60ms
 11.    1.       input:         0.34ms         0.35ms         0.32ms
 11.    2.       input:        14.24ms        16.20ms        12.58ms
 12.    1.       input:        14.86ms        15.37ms        13.99ms
 12.    2.       input:        16.40ms        17.14ms        15.72ms
 13.    1.       input:         0.28ms         0.30ms         0.25ms
 13.    2.       input:         0.24ms         0.25ms         0.24ms
 14.    1.       input:         0.34ms         0.34ms         0.33ms
 15.    1.       input:         0.78ms         0.78ms         0.77ms
 15.    2.       input:         1.24ms         1.30ms         1.19ms
 16.    1.       input:         6.14ms         6.53ms         5.93ms
 16.    2.       input:        30.56ms        38.39ms        27.10ms
 17.    1.       input:         0.12ms         0.13ms         0.11ms
 17.    2.       input:         0.13ms         0.17ms         0.10ms
 18.    1.       input:         1.95ms         2.73ms         1.68ms
 18.    2.       input:        13.01ms        13.65ms        12.41ms
 19.    1.       input:         5.02ms         5.15ms         4.88ms
 19.    2.       input:         5.50ms         5.67ms         5.33ms

Sum of average times for normal inputs: 293.718ms
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
