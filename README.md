# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         0.86ms         0.90ms         0.83ms
  1.    2.       input:         0.90ms         0.91ms         0.89ms
  2.    1.       input:         0.63ms         0.67ms         0.57ms
  2.    2.       input:         1.39ms         2.74ms         0.85ms
  3.    1.       input:         0.39ms         0.41ms         0.36ms
  3.    2.       input:         0.43ms         0.45ms         0.42ms
  4.    1.       input:        10.14ms        12.21ms         9.26ms
  4.    2.       input:         2.41ms         3.01ms         1.45ms
  5.    1.       input:         0.76ms         0.80ms         0.69ms
  5.    2.       input:         1.10ms         1.12ms         1.08ms
  6.    1.       input:         0.96ms         1.00ms         0.93ms
  6.    2.       input:        37.08ms        39.46ms        34.40ms
  7.    1.       input:         5.14ms         5.33ms         4.89ms
  7.    2.       input:       124.00ms       126.07ms       121.59ms
  8.    1.       input:         0.35ms         0.36ms         0.34ms
  8.    2.       input:         0.34ms         0.38ms         0.28ms
  9.    1.       input:         1.39ms         1.42ms         1.35ms
  9.    1. perf_test_1:         2.24ms         2.30ms         2.19ms
  9.    1. perf_test_2:        11.94ms        16.17ms        10.36ms
  9.    2.       input:         9.37ms         9.90ms         8.93ms
  9.    2. perf_test_1:        17.17ms        18.00ms        16.60ms
  9.    2. perf_test_2:       118.17ms       161.92ms       100.90ms
 10.    1.       input:         0.77ms         0.81ms         0.73ms
 10.    2.       input:         0.67ms         0.72ms         0.62ms
 11.    1.       input:         0.25ms         0.25ms         0.23ms
 11.    2.       input:        12.91ms        16.13ms        10.56ms
 12.    1.       input:        15.99ms        16.69ms        14.34ms
 12.    2.       input:        18.10ms        19.54ms        17.14ms
 13.    1.       input:         0.28ms         0.29ms         0.27ms
 13.    2.       input:         0.27ms         0.28ms         0.26ms
 14.    1.       input:         0.37ms         0.38ms         0.37ms
 15.    1.       input:         0.90ms         0.91ms         0.89ms
 15.    2.       input:         1.46ms         1.51ms         1.42ms
 16.    1.       input:         7.69ms         8.14ms         6.54ms
 16.    2.       input:        28.71ms        30.64ms        27.16ms
 17.    1.       input:         0.13ms         0.14ms         0.12ms
 17.    2.       input:         0.12ms         0.13ms         0.10ms
 18.    1.       input:         2.09ms         2.18ms         2.03ms
 18.    2.       input:        10.23ms        10.99ms         9.75ms
 19.    1.       input:         6.01ms         6.67ms         4.76ms
 19.    2.       input:         6.62ms         7.06ms         6.17ms
 20.    1.       input:         7.46ms         8.03ms         7.10ms
 20.    2.       input:       147.70ms       161.62ms       138.49ms

Sum of average times for normal inputs: 466.363ms
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
