# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         0.87ms         0.88ms         0.85ms
  1.    2.       input:         0.93ms         0.96ms         0.91ms
  2.    1.       input:         0.65ms         0.68ms         0.58ms
  2.    2.       input:         0.98ms         1.04ms         0.86ms
  3.    1.       input:         0.39ms         0.39ms         0.39ms
  3.    2.       input:         0.45ms         0.47ms         0.44ms
  4.    1.       input:         9.15ms         9.33ms         8.97ms
  4.    2.       input:         1.10ms         1.15ms         1.08ms
  5.    1.       input:         0.77ms         0.80ms         0.73ms
  5.    2.       input:         1.09ms         1.12ms         1.02ms
  6.    1.       input:         0.59ms         0.61ms         0.57ms
  6.    2.       input:        32.94ms        33.51ms        32.15ms
  7.    1.       input:         5.34ms         5.51ms         5.17ms
  7.    2.       input:       130.05ms       130.37ms       129.79ms
  8.    1.       input:         0.32ms         0.36ms         0.29ms
  8.    2.       input:         0.30ms         0.34ms         0.24ms
  9.    1.       input:         1.41ms         1.43ms         1.38ms
  9.    1. perf_test_1:         2.22ms         2.28ms         2.19ms
  9.    1. perf_test_2:        10.48ms        10.55ms        10.38ms
  9.    2.       input:         9.06ms         9.24ms         8.92ms
  9.    2. perf_test_1:        16.97ms        17.42ms        16.35ms
  9.    2. perf_test_2:       108.56ms       114.58ms       105.70ms
 10.    1.       input:         0.79ms         0.86ms         0.75ms
 10.    2.       input:         0.70ms         0.78ms         0.62ms
 11.    1.       input:         0.34ms         0.34ms         0.33ms
 11.    2.       input:        15.36ms        16.46ms        13.11ms
 12.    1.       input:        17.41ms        19.39ms        15.66ms
 12.    2.       input:        18.34ms        19.15ms        17.46ms
 13.    1.       input:         0.28ms         0.28ms         0.27ms
 13.    2.       input:         0.25ms         0.26ms         0.25ms
 14.    1.       input:         0.34ms         0.35ms         0.34ms
 15.    1.       input:         0.77ms         0.78ms         0.76ms
 15.    2.       input:         1.22ms         1.33ms         1.15ms
 16.    1.       input:         8.10ms         8.61ms         7.85ms
 16.    2.       input:        27.61ms        32.91ms        22.96ms
 17.    1.       input:         0.13ms         0.14ms         0.12ms
 17.    2.       input:         0.15ms         0.21ms         0.11ms
 18.    1.       input:         2.37ms         2.62ms         1.68ms
 18.    2.       input:        12.46ms        13.39ms        12.09ms
 19.    1.       input:         5.98ms         6.77ms         5.20ms
 19.    2.       input:         6.12ms         6.52ms         5.72ms

Sum of average times for normal inputs: 315.09ms
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
