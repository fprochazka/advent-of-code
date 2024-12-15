# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.28ms         1.45ms         0.91ms
  1.    2.       input:         1.53ms         2.84ms         1.04ms
  2.    1.       input:         0.57ms         0.79ms         0.50ms
  2.    2.       input:         0.83ms         0.91ms         0.79ms
  3.    1.       input:         0.43ms         0.45ms         0.41ms
  3.    2.       input:         0.50ms         0.62ms         0.47ms
  4.    1.       input:         9.41ms        11.94ms         8.72ms
  4.    2.       input:         1.61ms         2.23ms         1.16ms
  5.    1.       input:         1.02ms         1.08ms         0.98ms
  5.    2.       input:         1.40ms         1.46ms         1.35ms
  6.    1.       input:         0.79ms         1.19ms         0.61ms
  6.    2.       input:        32.24ms        34.11ms        28.67ms
  7.    1.       input:         5.20ms         5.33ms         5.01ms
  7.    2.       input:       130.36ms       138.50ms       127.56ms
  8.    1.       input:         0.47ms         0.70ms         0.37ms
  8.    2.       input:         0.39ms         0.64ms         0.34ms
  9.    1.       input:         1.48ms         1.96ms         1.39ms
  9.    1. perf_test_1:         2.52ms         3.31ms         2.23ms
  9.    1. perf_test_2:        10.76ms        11.21ms        10.60ms
  9.    2.       input:        10.07ms        11.66ms         8.71ms
  9.    2. perf_test_1:        18.32ms        22.91ms        17.02ms
  9.    2. perf_test_2:       112.53ms       143.49ms       105.42ms
 10.    1.       input:         0.82ms         0.89ms         0.79ms
 10.    2.       input:         0.68ms         0.72ms         0.65ms
 11.    1.       input:         0.34ms         0.35ms         0.33ms
 11.    2.       input:        14.58ms        18.35ms        13.30ms
 12.    1.       input:        15.33ms        18.22ms        13.64ms
 12.    2.       input:        17.36ms        25.69ms        14.89ms
 13.    1.       input:         0.35ms         0.43ms         0.29ms
 13.    2.       input:         0.29ms         0.38ms         0.25ms
 14.    1.       input:         0.37ms         0.46ms         0.33ms
 15.    1.       input:         0.99ms         2.67ms         0.77ms
 15.    2.       input:         1.74ms         1.87ms         1.64ms

Sum of average times for normal inputs: 252.404ms
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
