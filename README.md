# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.31ms         1.79ms         0.98ms
  1.    2.       input:         1.42ms         2.05ms         1.07ms
  2.    1.       input:         0.62ms         1.02ms         0.49ms
  2.    2.       input:         0.96ms         1.55ms         0.77ms
  3.    1.       input:         0.45ms         0.63ms         0.42ms
  3.    2.       input:         0.51ms         0.80ms         0.45ms
  4.    1.       input:         9.85ms        12.13ms         9.14ms
  4.    2.       input:         1.48ms         2.11ms         1.15ms
  5.    1.       input:         0.91ms         0.95ms         0.86ms
  5.    2.       input:         1.22ms         1.28ms         1.13ms
  6.    1.       input:         0.80ms         1.32ms         0.65ms
  6.    2.       input:        34.76ms        38.41ms        32.06ms
  7.    1.       input:         5.29ms         5.59ms         5.02ms
  7.    2.       input:       130.48ms       132.40ms       127.91ms
  8.    1.       input:         0.34ms         0.35ms         0.32ms
  8.    2.       input:         0.37ms         0.57ms         0.32ms
  9.    1.       input:         1.45ms         1.47ms         1.42ms
  9.    1. perf_test_1:         2.26ms         2.37ms         2.13ms
  9.    1. perf_test_2:        10.93ms        13.06ms        10.31ms
  9.    2.       input:         9.48ms         9.68ms         9.27ms
  9.    2. perf_test_1:        19.20ms        26.49ms        16.74ms
  9.    2. perf_test_2:       120.82ms       171.23ms       107.71ms
 10.    1.       input:         0.84ms         0.96ms         0.76ms
 10.    2.       input:         0.70ms         0.80ms         0.64ms
 11.    1.       input:         0.37ms         0.39ms         0.36ms
 11.    2.       input:        16.59ms        17.41ms        15.72ms
 12.    1.       input:        18.59ms        26.15ms        16.57ms
 12.    2.       input:        18.69ms        21.45ms        16.80ms
 13.    1.       input:         0.32ms         0.36ms         0.29ms
 13.    2.       input:         0.30ms         0.40ms         0.27ms
 14.    1.       input:         0.37ms         0.43ms         0.34ms
 15.    1.       input:         0.79ms         0.80ms         0.76ms
 15.    2.       input:         1.68ms         1.86ms         1.64ms
 16.    1.       input:         8.54ms        10.40ms         7.89ms
 16.    2.       input:        29.72ms        40.77ms        26.58ms
 17.    1.       input:         0.15ms         0.18ms         0.13ms
 17.    2.       input:         0.23ms         0.47ms         0.13ms
 18.    1.       input:         2.33ms         2.78ms         1.70ms
 18.    2.       input:        12.80ms        17.49ms        11.15ms
 19.    1.       input:         0.00ms         0.00ms         0.00ms
 19.    2.       input:         0.00ms         0.00ms         0.00ms

Sum of average times for normal inputs: 314.682ms
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
