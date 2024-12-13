# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.24ms         1.46ms         0.93ms
  1.    2.       input:         1.55ms         3.80ms         0.99ms
  2.    1.       input:         0.59ms         1.02ms         0.46ms
  2.    2.       input:         0.85ms         0.94ms         0.73ms
  3.    1.       input:         0.48ms         0.57ms         0.44ms
  3.    2.       input:         0.70ms         2.00ms         0.51ms
  4.    1.       input:         8.15ms         9.27ms         7.77ms
  4.    2.       input:         1.87ms         3.29ms         1.30ms
  5.    1.       input:         0.71ms         1.19ms         0.58ms
  5.    2.       input:         0.98ms         1.30ms         0.87ms
  6.    1.       input:         0.98ms         1.32ms         0.82ms
  6.    2.       input:        27.83ms        30.91ms        26.43ms
  7.    1.       input:         5.17ms         7.10ms         4.73ms
  7.    2.       input:       148.43ms       156.42ms       144.77ms
  8.    1.       input:         0.50ms         0.74ms         0.39ms
  8.    2.       input:         0.45ms         0.69ms         0.37ms
  9.    1.       input:         1.49ms         1.89ms         1.39ms
  9.    1. perf_test_1:         2.35ms         2.48ms         2.21ms
  9.    1. perf_test_2:        11.46ms        13.68ms        10.43ms
  9.    2.       input:         9.59ms        10.24ms         9.05ms
  9.    2. perf_test_1:        18.89ms        22.64ms        16.75ms
  9.    2. perf_test_2:       116.51ms       153.15ms       105.25ms
 10.    1.       input:         1.03ms         1.15ms         0.84ms
 10.    2.       input:         0.90ms         1.00ms         0.69ms
 11.    1.       input:         0.35ms         0.39ms         0.34ms
 11.    2.       input:        17.35ms        21.15ms        13.75ms
 12.    1.       input:        15.03ms        18.63ms        13.80ms
 12.    2.       input:        17.27ms        24.61ms        14.58ms
 13.    1.       input:         0.34ms         0.42ms         0.29ms
 13.    2.       input:         0.29ms         0.38ms         0.26ms

Sum of average times for normal inputs: 264.094ms
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
