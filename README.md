# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
day  task                   [average]        [worst]         [best]
  1.    1.       input:         0.82ms         0.82ms         0.81ms
  1.    2.       input:         0.89ms         0.92ms         0.86ms
  2.    1.       input:         0.55ms         0.62ms         0.49ms
  2.    2.       input:         0.86ms         0.95ms         0.76ms
  3.    1.       input:         0.38ms         0.38ms         0.37ms
  3.    2.       input:         0.44ms         0.46ms         0.42ms
  4.    1.       input:         8.54ms         8.70ms         8.42ms
  4.    2.       input:         1.18ms         1.31ms         1.08ms
  5.    1.       input:         0.73ms         0.77ms         0.68ms
  5.    2.       input:         1.07ms         1.13ms         0.94ms
  6.    1.       input:         0.73ms         0.76ms         0.62ms
  6.    2.       input:        29.85ms        32.22ms        28.18ms
  7.    1.       input:         5.17ms         5.30ms         4.96ms
  7.    2.       input:       121.38ms       122.34ms       120.74ms
  8.    1.       input:         0.31ms         0.34ms         0.27ms
  8.    2.       input:         0.29ms         0.35ms         0.25ms
  9.    1.       input:         1.39ms         1.42ms         1.38ms
  9.    1. perf_test_1:         2.29ms         2.49ms         2.17ms
  9.    1. perf_test_2:        10.74ms        10.98ms        10.59ms
  9.    2.       input:         9.35ms         9.43ms         9.23ms
  9.    2. perf_test_1:        17.14ms        18.35ms        16.49ms
  9.    2. perf_test_2:       123.22ms       177.74ms       101.70ms
 10.    1.       input:         0.78ms         0.83ms         0.73ms
 10.    2.       input:         0.67ms         0.71ms         0.64ms
 11.    1.       input:         0.34ms         0.39ms         0.33ms
 11.    2.       input:        16.02ms        17.78ms        12.88ms
 12.    1.       input:        17.73ms        22.31ms        14.97ms
 12.    2.       input:        17.79ms        18.74ms        16.99ms
 13.    1.       input:         0.27ms         0.28ms         0.27ms
 13.    2.       input:         0.26ms         0.27ms         0.26ms
 14.    1.       input:         0.34ms         0.36ms         0.33ms
 15.    1.       input:         0.81ms         0.83ms         0.79ms
 15.    2.       input:         1.33ms         1.39ms         1.29ms
 16.    1.       input:         7.33ms         8.10ms         6.57ms
 16.    2.       input:        25.98ms        27.20ms        24.66ms
 17.    1.       input:         0.13ms         0.13ms         0.12ms
 17.    2.       input:         0.15ms         0.21ms         0.12ms
 18.    1.       input:         2.37ms         2.93ms         1.76ms
 18.    2.       input:        12.68ms        13.87ms        11.91ms
 19.    1.       input:         5.82ms         6.77ms         4.94ms
 19.    2.       input:         6.25ms         6.57ms         5.47ms

Sum of average times for normal inputs: 300.971ms
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
