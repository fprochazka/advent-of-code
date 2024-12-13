# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.35ms         1.75ms         1.00ms
  1.    2.       input:         1.21ms         1.47ms         0.95ms
  2.    1.       input:         0.57ms         0.61ms         0.55ms
  2.    2.       input:         0.89ms         0.98ms         0.81ms
  3.    1.       input:         0.48ms         0.64ms         0.44ms
  3.    2.       input:         0.50ms         0.55ms         0.47ms
  4.    1.       input:         8.74ms        10.22ms         8.19ms
  4.    2.       input:         1.65ms         2.36ms         1.32ms
  5.    1.       input:         0.63ms         0.85ms         0.56ms
  5.    2.       input:         0.92ms         0.99ms         0.84ms
  6.    1.       input:         0.97ms         1.36ms         0.80ms
  6.    2.       input:        27.71ms        31.61ms        24.89ms
  7.    1.       input:         6.06ms         7.86ms         5.51ms
  7.    2.       input:       174.05ms       193.34ms       169.74ms
  8.    1.       input:         0.47ms         0.65ms         0.37ms
  8.    2.       input:         0.45ms         0.69ms         0.37ms
  9.    1.       input:         1.37ms         1.44ms         1.34ms
  9.    1. perf_test_1:         2.24ms         2.41ms         2.18ms
  9.    1. perf_test_2:         9.93ms        10.52ms         9.69ms
  9.    2.       input:         9.79ms        12.52ms         9.31ms
  9.    2. perf_test_1:        18.64ms        21.73ms        16.81ms
  9.    2. perf_test_2:       116.27ms       176.48ms       105.02ms
 10.    1.       input:         1.07ms         1.27ms         0.86ms
 10.    2.       input:         0.95ms         1.24ms         0.75ms
 11.    1.       input:         0.39ms         0.58ms         0.36ms
 11.    2.       input:        19.20ms        33.19ms        15.64ms
 12.    1.       input:        17.44ms        28.62ms        13.63ms
 12.    2.       input:        16.85ms        18.14ms        16.19ms
 13.    1.       input:       337.65ms       344.71ms       329.26ms
 13.    2.       input:       271.82ms       275.10ms       266.67ms

Sum of average times for normal inputs: 903.157ms
```

## Snapshot of `./gradlew benchmarkNative`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         0.64ms         0.66ms         0.62ms
  1.    2.       input:         0.62ms         0.64ms         0.61ms
  2.    1.       input:         1.21ms         1.28ms         0.90ms
  2.    2.       input:         2.11ms         2.19ms         1.84ms
  3.    1.       input:         0.58ms         0.60ms         0.56ms
  3.    2.       input:         0.81ms         0.84ms         0.79ms
  4.    1.       input:        32.90ms        95.49ms        24.04ms
  4.    2.       input:         4.46ms        12.10ms         3.16ms
  5.    1.       input:         0.94ms         0.98ms         0.89ms
  5.    2.       input:         1.46ms         1.57ms         1.37ms
  6.    1.       input:         1.77ms         1.99ms         1.66ms
  6.    2.       input:        87.16ms       123.98ms        63.33ms
  7.    1.       input:        11.82ms        25.13ms         9.72ms
  7.    2.       input:       444.79ms       469.64ms       427.37ms
  8.    1.       input:         0.35ms         0.52ms         0.32ms
  8.    2.       input:         0.32ms         0.34ms         0.31ms
  9.    1.       input:         4.06ms         4.32ms         3.92ms
  9.    1. perf_test_1:         6.77ms         7.09ms         6.61ms
  9.    1. perf_test_2:        40.18ms        42.98ms        38.61ms
  9.    2.       input:        16.36ms        16.94ms        16.12ms
  9.    2. perf_test_1:        33.82ms        43.97ms        31.23ms
  9.    2. perf_test_2:       208.76ms       340.24ms       180.13ms
 10.    1.       input:         1.47ms         1.51ms         1.41ms
 10.    2.       input:         1.42ms         1.48ms         1.33ms
 11.    1.       input:         0.51ms         0.52ms         0.48ms
 11.    2.       input:        32.67ms        52.39ms        26.76ms
 12.    1.       input:        29.30ms        52.52ms        24.82ms
 12.    2.       input:        29.81ms        36.07ms        27.99ms

Sum of average times for normal inputs: 707.555ms
```

lol?
