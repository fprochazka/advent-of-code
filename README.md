# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         0.86ms         0.91ms         0.84ms
  1.    2.       input:         0.91ms         0.92ms         0.91ms
  2.    1.       input:         0.62ms         0.65ms         0.55ms
  2.    2.       input:         0.95ms         1.00ms         0.86ms
  3.    1.       input:         0.40ms         0.44ms         0.37ms
  3.    2.       input:         0.44ms         0.45ms         0.42ms
  4.    1.       input:        10.57ms        12.34ms         9.91ms
  4.    2.       input:         2.33ms         2.68ms         1.43ms
  5.    1.       input:         0.75ms         0.79ms         0.70ms
  5.    2.       input:         1.06ms         1.10ms         1.01ms
  6.    1.       input:         0.92ms         1.02ms         0.74ms
  6.    2.       input:        35.63ms        38.15ms        34.06ms
  7.    1.       input:         5.94ms         6.05ms         5.73ms
  7.    2.       input:       150.48ms       151.09ms       150.03ms
  8.    1.       input:         0.37ms         0.52ms         0.30ms
  8.    2.       input:         0.42ms         0.55ms         0.36ms
  9.    1.       input:         1.55ms         1.94ms         1.41ms
  9.    1. perf_test_1:         2.33ms         2.59ms         2.16ms
  9.    1. perf_test_2:        10.56ms        10.70ms        10.48ms
  9.    2.       input:         9.58ms         9.96ms         9.27ms
  9.    2. perf_test_1:        19.57ms        26.91ms        16.61ms
  9.    2. perf_test_2:       105.56ms       112.41ms       101.17ms
 10.    1.       input:         0.79ms         0.90ms         0.72ms
 10.    2.       input:         0.71ms         0.75ms         0.64ms
 11.    1.       input:         0.27ms         0.28ms         0.26ms
 11.    2.       input:        15.53ms        17.50ms        14.62ms
 12.    1.       input:        15.87ms        16.62ms        14.06ms
 12.    2.       input:        20.43ms        26.65ms        18.13ms
 13.    1.       input:         0.29ms         0.29ms         0.28ms
 13.    2.       input:         0.27ms         0.30ms         0.25ms
 14.    1.       input:         0.37ms         0.38ms         0.36ms
 15.    1.       input:         0.91ms         0.93ms         0.90ms
 15.    2.       input:         1.53ms         1.61ms         1.47ms
 16.    1.       input:         8.32ms         8.43ms         8.00ms
 16.    2.       input:        29.36ms        30.67ms        28.16ms
 17.    1.       input:         0.12ms         0.15ms         0.10ms
 17.    2.       input:         0.12ms         0.13ms         0.11ms
 18.    1.       input:         2.03ms         2.29ms         1.45ms
 18.    2.       input:         9.81ms        10.51ms         8.91ms
 19.    1.       input:         6.84ms         7.90ms         6.41ms
 19.    2.       input:         6.09ms         6.22ms         5.77ms
 20.    1.       input:         6.28ms         6.84ms         5.13ms
 20.    2.       input:        93.22ms       102.27ms        83.79ms

Sum of average times for normal inputs: 442.955ms
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
