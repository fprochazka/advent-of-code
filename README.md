# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.18ms         1.23ms         1.15ms
  1.    2.       input:         1.12ms         1.15ms         1.08ms
  2.    1.       input:         0.62ms         0.66ms         0.57ms
  2.    2.       input:         0.89ms         0.98ms         0.83ms
  3.    1.       input:         0.37ms         0.38ms         0.36ms
  3.    2.       input:         0.42ms         0.43ms         0.41ms
  4.    1.       input:         9.37ms         9.82ms         9.07ms
  4.    2.       input:         2.09ms         2.81ms         1.38ms
  5.    1.       input:         0.72ms         0.76ms         0.68ms
  5.    2.       input:         1.01ms         1.07ms         0.95ms
  6.    1.       input:         0.86ms         0.97ms         0.71ms
  6.    2.       input:        32.39ms        36.57ms        29.76ms
  7.    1.       input:         5.12ms         5.29ms         4.90ms
  7.    2.       input:       121.57ms       122.22ms       120.99ms
  8.    1.       input:         0.34ms         0.37ms         0.31ms
  8.    2.       input:         0.36ms         0.48ms         0.27ms
  9.    1.       input:         1.52ms         1.82ms         1.39ms
  9.    1. perf_test_1:         2.19ms         2.32ms         2.12ms
  9.    1. perf_test_2:        11.37ms        14.07ms        10.33ms
  9.    2.       input:         9.10ms        10.32ms         7.60ms
  9.    2. perf_test_1:        16.61ms        18.17ms        15.54ms
  9.    2. perf_test_2:       115.56ms       159.24ms       100.62ms
 10.    1.       input:         0.72ms         0.80ms         0.69ms
 10.    2.       input:         0.60ms         0.64ms         0.58ms
 11.    1.       input:         0.24ms         0.26ms         0.23ms
 11.    2.       input:        11.10ms        13.40ms        10.14ms
 12.    1.       input:        15.06ms        17.03ms        13.17ms
 12.    2.       input:        15.59ms        17.85ms        14.81ms
 13.    1.       input:         0.27ms         0.28ms         0.25ms
 13.    2.       input:         0.23ms         0.25ms         0.23ms
 14.    1.       input:         0.34ms         0.35ms         0.33ms
 15.    1.       input:         0.79ms         0.80ms         0.78ms
 15.    2.       input:         1.26ms         1.32ms         1.23ms
 16.    1.       input:         7.24ms        12.11ms         5.49ms
 16.    2.       input:        26.22ms        26.40ms        25.98ms
 17.    1.       input:         0.11ms         0.12ms         0.11ms
 17.    2.       input:         0.13ms         0.19ms         0.10ms
 18.    1.       input:         1.39ms         1.44ms         1.37ms
 18.    2.       input:         8.71ms         8.78ms         8.64ms
 19.    1.       input:         4.72ms         4.74ms         4.70ms
 19.    2.       input:         5.49ms         5.50ms         5.47ms
 20.    1.       input:         4.58ms         5.77ms         4.10ms
 20.    2.       input:        25.80ms        28.11ms        24.07ms

Sum of average times for normal inputs: 319.628ms
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
