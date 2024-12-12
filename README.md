# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.31ms         1.71ms         0.99ms
  1.    2.       input:         1.32ms         1.70ms         0.98ms
  2.    1.       input:         0.53ms         0.60ms         0.45ms
  2.    2.       input:         0.92ms         1.49ms         0.73ms
  3.    1.       input:         0.48ms         0.56ms         0.44ms
  3.    2.       input:         0.52ms         0.59ms         0.49ms
  4.    1.       input:         8.44ms        10.75ms         7.95ms
  4.    2.       input:         1.77ms         2.36ms         1.30ms
  5.    1.       input:         0.67ms         0.88ms         0.55ms
  5.    2.       input:         0.96ms         1.15ms         0.85ms
  6.    1.       input:         1.02ms         1.45ms         0.84ms
  6.    2.       input:        27.88ms        31.67ms        25.69ms
  7.    1.       input:         6.15ms         7.88ms         5.67ms
  7.    2.       input:       171.18ms       175.64ms       167.68ms
  8.    1.       input:         0.51ms         0.69ms         0.41ms
  8.    2.       input:         0.48ms         0.72ms         0.38ms
  9.    1.       input:         1.49ms         2.08ms         1.37ms
  9.    1. perf_test_1:         2.67ms         4.90ms         2.24ms
  9.    1. perf_test_2:        11.21ms        13.67ms        10.15ms
  9.    2.       input:        10.04ms        12.27ms         9.52ms
  9.    2. perf_test_1:        18.00ms        20.99ms        16.63ms
  9.    2. perf_test_2:       112.48ms       121.43ms       107.35ms
 10.    1.       input:         1.06ms         1.23ms         0.85ms
 10.    2.       input:         0.89ms         1.06ms         0.73ms
 11.    1.       input:         0.35ms         0.37ms         0.33ms
 11.    2.       input:        15.84ms        19.01ms        13.57ms
 12.    1.       input:        15.53ms        20.19ms        12.73ms
 12.    2.       input:        18.64ms        23.90ms        14.64ms

Sum of average times for normal inputs: 287.969ms
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
