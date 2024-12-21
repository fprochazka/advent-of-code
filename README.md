# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.08ms         1.10ms         1.05ms
  1.    2.       input:         1.13ms         1.15ms         1.09ms
  2.    1.       input:         0.67ms         0.71ms         0.59ms
  2.    2.       input:         0.98ms         1.05ms         0.85ms
  3.    1.       input:         0.40ms         0.42ms         0.38ms
  3.    2.       input:         0.45ms         0.46ms         0.43ms
  4.    1.       input:         9.69ms         9.83ms         9.55ms
  4.    2.       input:         2.36ms         2.78ms         1.43ms
  5.    1.       input:         0.70ms         0.74ms         0.67ms
  5.    2.       input:         0.99ms         1.05ms         0.94ms
  6.    1.       input:         1.28ms         2.71ms         0.71ms
  6.    2.       input:        32.67ms        35.42ms        30.77ms
  7.    1.       input:         5.04ms         5.14ms         4.82ms
  7.    2.       input:       121.03ms       121.70ms       120.30ms
  8.    1.       input:         0.31ms         0.34ms         0.30ms
  8.    2.       input:         0.35ms         0.39ms         0.32ms
  9.    1.       input:         1.55ms         2.05ms         1.38ms
  9.    1. perf_test_1:         2.11ms         2.13ms         2.09ms
  9.    1. perf_test_2:        10.23ms        10.40ms        10.14ms
  9.    2.       input:         9.03ms         9.11ms         8.83ms
  9.    2. perf_test_1:        16.08ms        16.38ms        15.76ms
  9.    2. perf_test_2:       104.66ms       114.75ms       100.71ms
 10.    1.       input:         0.88ms         1.30ms         0.69ms
 10.    2.       input:         0.71ms         0.90ms         0.60ms
 11.    1.       input:         0.25ms         0.26ms         0.24ms
 11.    2.       input:        11.79ms        14.70ms        10.39ms
 12.    1.       input:        13.31ms        13.60ms        12.89ms
 12.    2.       input:        15.58ms        17.52ms        14.78ms
 13.    1.       input:         0.27ms         0.27ms         0.26ms
 13.    2.       input:         0.25ms         0.27ms         0.23ms
 14.    1.       input:         0.34ms         0.35ms         0.33ms
 15.    1.       input:         0.85ms         0.99ms         0.78ms
 15.    2.       input:         1.32ms         1.38ms         1.23ms
 16.    1.       input:         8.02ms        11.49ms         6.13ms
 16.    2.       input:        22.76ms        23.52ms        22.19ms
 17.    1.       input:         0.13ms         0.16ms         0.12ms
 17.    2.       input:         0.14ms         0.21ms         0.10ms
 18.    1.       input:         1.39ms         1.42ms         1.37ms
 18.    2.       input:         8.60ms         8.75ms         8.48ms
 19.    1.       input:         4.84ms         4.87ms         4.79ms
 19.    2.       input:         5.27ms         5.36ms         5.23ms
 20.    1.       input:         3.81ms         3.94ms         3.70ms
 20.    2.       input:        13.88ms        16.01ms        12.35ms
 21.    1.       input:         0.42ms         0.45ms         0.36ms
 21.    2.       input:         0.29ms         0.33ms         0.27ms

Sum of average times for normal inputs: 304.783ms
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
