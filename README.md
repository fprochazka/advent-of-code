# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         0.83ms         0.84ms         0.81ms
  1.    2.       input:         0.90ms         0.93ms         0.88ms
  2.    1.       input:         0.54ms         0.55ms         0.53ms
  2.    2.       input:         0.81ms         0.83ms         0.80ms
  3.    1.       input:         0.38ms         0.38ms         0.38ms
  3.    2.       input:         0.45ms         0.46ms         0.44ms
  4.    1.       input:         8.70ms         9.83ms         8.25ms
  4.    2.       input:         1.12ms         1.15ms         1.10ms
  5.    1.       input:         0.69ms         0.71ms         0.68ms
  5.    2.       input:         0.97ms         0.98ms         0.96ms
  6.    1.       input:         0.50ms         0.53ms         0.49ms
  6.    2.       input:        26.84ms        28.47ms        25.56ms
  7.    1.       input:         4.64ms         4.71ms         4.55ms
  7.    2.       input:       122.18ms       124.94ms       120.93ms
  8.    1.       input:         0.31ms         0.36ms         0.26ms
  8.    2.       input:         0.32ms         0.33ms         0.30ms
  9.    1.       input:         1.42ms         1.45ms         1.41ms
  9.    1. perf_test_1:         2.21ms         2.30ms         2.17ms
  9.    1. perf_test_2:        11.14ms        12.30ms        10.63ms
  9.    2.       input:         9.24ms         9.45ms         8.88ms
  9.    2. perf_test_1:        16.77ms        17.59ms        16.19ms
  9.    2. perf_test_2:       121.39ms       173.59ms       102.26ms
 10.    1.       input:         0.78ms         0.81ms         0.73ms
 10.    2.       input:         0.67ms         0.69ms         0.64ms
 11.    1.       input:         0.26ms         0.27ms         0.25ms
 11.    2.       input:        13.89ms        15.20ms        11.02ms
 12.    1.       input:        15.14ms        19.63ms        12.82ms
 12.    2.       input:        16.42ms        18.62ms        14.70ms
 13.    1.       input:         0.30ms         0.34ms         0.28ms
 13.    2.       input:         0.25ms         0.25ms         0.24ms
 14.    1.       input:         0.34ms         0.35ms         0.34ms
 15.    1.       input:         0.79ms         0.82ms         0.77ms
 15.    2.       input:         1.34ms         1.41ms         1.22ms
 16.    1.       input:         6.82ms         7.31ms         5.52ms
 16.    2.       input:        27.81ms        31.72ms        25.57ms
 17.    1.       input:         0.16ms         0.23ms         0.13ms
 17.    2.       input:         0.17ms         0.21ms         0.11ms
 18.    1.       input:         1.95ms         2.46ms         1.44ms
 18.    2.       input:         9.44ms        10.07ms         8.87ms
 19.    1.       input:         4.93ms         5.08ms         4.82ms
 19.    2.       input:         5.46ms         5.61ms         5.37ms

Sum of average times for normal inputs: 287.735ms
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
