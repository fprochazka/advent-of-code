# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.28ms         1.49ms         0.97ms
  1.    2.       input:         1.27ms         1.50ms         0.97ms
  2.    1.       input:         0.54ms         0.60ms         0.51ms
  2.    2.       input:         0.83ms         0.95ms         0.80ms
  3.    1.       input:         0.43ms         0.50ms         0.41ms
  3.    2.       input:         0.47ms         0.49ms         0.45ms
  4.    1.       input:         9.15ms        11.06ms         8.58ms
  4.    2.       input:         1.47ms         2.23ms         1.05ms
  5.    1.       input:         0.90ms         0.97ms         0.85ms
  5.    2.       input:         1.23ms         1.30ms         1.15ms
  6.    1.       input:         0.79ms         1.20ms         0.64ms
  6.    2.       input:        34.24ms        37.58ms        31.54ms
  7.    1.       input:         5.39ms         6.09ms         5.03ms
  7.    2.       input:       130.74ms       133.16ms       128.01ms
  8.    1.       input:         0.36ms         0.41ms         0.34ms
  8.    2.       input:         0.35ms         0.39ms         0.31ms
  9.    1.       input:         1.46ms         1.54ms         1.42ms
  9.    1. perf_test_1:         2.36ms         2.74ms         2.23ms
  9.    1. perf_test_2:        11.14ms        12.95ms        10.40ms
  9.    2.       input:         9.85ms        11.93ms         9.14ms
  9.    2. perf_test_1:        17.46ms        18.62ms        16.53ms
  9.    2. perf_test_2:       109.08ms       116.15ms       106.33ms
 10.    1.       input:         0.84ms         1.06ms         0.77ms
 10.    2.       input:         0.69ms         0.73ms         0.64ms
 11.    1.       input:         0.34ms         0.36ms         0.33ms
 11.    2.       input:        17.58ms        42.15ms        12.57ms
 12.    1.       input:        16.47ms        19.54ms        13.67ms
 12.    2.       input:        17.10ms        20.45ms        15.55ms
 13.    1.       input:         0.34ms         0.43ms         0.29ms
 13.    2.       input:         0.28ms         0.31ms         0.26ms
 14.    1.       input:         0.38ms         0.44ms         0.34ms
 15.    1.       input:         0.83ms         0.92ms         0.79ms
 15.    2.       input:         1.70ms         1.76ms         1.65ms
 16.    1.       input:         7.72ms        10.82ms         6.13ms
 16.    2.       input:        23.41ms        25.21ms        21.72ms

Sum of average times for normal inputs: 288.43ms
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
