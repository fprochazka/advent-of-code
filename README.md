# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.34ms         1.81ms         0.89ms
  1.    2.       input:         1.46ms         2.38ms         0.93ms
  2.    1.       input:         0.64ms         0.79ms         0.61ms
  2.    2.       input:         0.99ms         1.08ms         0.95ms
  3.    1.       input:         0.44ms         0.52ms         0.43ms
  3.    2.       input:         0.67ms         2.12ms         0.47ms
  4.    1.       input:         7.90ms         8.54ms         7.45ms
  4.    2.       input:         1.43ms         2.20ms         1.03ms
  5.    1.       input:         1.29ms         3.06ms         1.03ms
  5.    2.       input:         1.58ms         2.31ms         1.41ms
  6.    1.       input:         0.89ms         1.81ms         0.66ms
  6.    2.       input:        31.25ms        35.80ms        27.20ms
  7.    1.       input:         5.38ms         7.60ms         4.95ms
  7.    2.       input:       128.81ms       131.99ms       125.77ms
  8.    1.       input:         0.46ms         0.75ms         0.33ms
  8.    2.       input:         0.49ms         0.74ms         0.35ms
  9.    1.       input:         1.57ms         2.03ms         1.38ms
  9.    1. perf_test_1:         2.58ms         4.05ms         2.20ms
  9.    1. perf_test_2:        10.96ms        12.86ms        10.30ms
  9.    2.       input:        10.03ms        14.01ms         9.27ms
  9.    2. perf_test_1:        19.60ms        28.47ms        16.90ms
  9.    2. perf_test_2:       111.57ms       124.06ms       106.87ms
 10.    1.       input:         0.83ms         0.90ms         0.78ms
 10.    2.       input:         0.73ms         0.92ms         0.68ms
 11.    1.       input:         0.37ms         0.37ms         0.36ms
 11.    2.       input:        16.17ms        16.82ms        15.71ms
 12.    1.       input:        15.06ms        16.15ms        13.35ms
 12.    2.       input:        18.10ms        21.35ms        15.24ms
 13.    1.       input:         0.34ms         0.43ms         0.30ms
 13.    2.       input:         0.30ms         0.38ms         0.27ms
 14.    1.       input:         0.40ms         0.58ms         0.35ms

Sum of average times for normal inputs: 248.93ms
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
