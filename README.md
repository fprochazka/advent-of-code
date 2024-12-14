# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.28ms         1.43ms         0.90ms
  1.    2.       input:         1.36ms         1.52ms         0.98ms
  2.    1.       input:         0.59ms         0.64ms         0.52ms
  2.    2.       input:         0.96ms         1.38ms         0.80ms
  3.    1.       input:         0.41ms         0.43ms         0.40ms
  3.    2.       input:         0.46ms         0.48ms         0.45ms
  4.    1.       input:         8.17ms         9.85ms         7.77ms
  4.    2.       input:         1.42ms         2.14ms         1.06ms
  5.    1.       input:         0.92ms         1.00ms         0.86ms
  5.    2.       input:         1.23ms         1.30ms         1.15ms
  6.    1.       input:         0.79ms         1.28ms         0.64ms
  6.    2.       input:        32.40ms        33.97ms        30.30ms
  7.    1.       input:         5.25ms         5.42ms         5.07ms
  7.    2.       input:       129.03ms       130.47ms       128.00ms
  8.    1.       input:         0.45ms         0.61ms         0.35ms
  8.    2.       input:         0.43ms         0.63ms         0.35ms
  9.    1.       input:         1.44ms         1.58ms         1.34ms
  9.    1. perf_test_1:         2.50ms         4.36ms         2.12ms
  9.    1. perf_test_2:        10.40ms        10.82ms        10.01ms
  9.    2.       input:         9.44ms         9.71ms         9.10ms
  9.    2. perf_test_1:        17.53ms        20.93ms        16.55ms
  9.    2. perf_test_2:       119.95ms       181.28ms       105.19ms
 10.    1.       input:         0.82ms         0.86ms         0.77ms
 10.    2.       input:         0.71ms         0.80ms         0.64ms
 11.    1.       input:         0.36ms         0.41ms         0.33ms
 11.    2.       input:        16.08ms        20.28ms        13.22ms
 12.    1.       input:        16.64ms        23.95ms        13.82ms
 12.    2.       input:        17.77ms        20.71ms        14.92ms
 13.    1.       input:         0.59ms         1.63ms         0.31ms
 13.    2.       input:         0.35ms         0.62ms         0.27ms
 14.    1.       input:         0.46ms         0.66ms         0.39ms

Sum of average times for normal inputs: 249.798ms
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
