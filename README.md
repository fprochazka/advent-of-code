# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.23ms         1.52ms         0.88ms
  1.    2.       input:         1.31ms         1.54ms         1.00ms
  2.    1.       input:         0.50ms         0.56ms         0.46ms
  2.    2.       input:         0.81ms         1.11ms         0.73ms
  3.    1.       input:         0.49ms         0.65ms         0.43ms
  3.    2.       input:         0.56ms         0.79ms         0.51ms
  4.    1.       input:         9.72ms        10.30ms         9.40ms
  4.    2.       input:         1.82ms         2.58ms         1.46ms
  5.    1.       input:         0.68ms         0.90ms         0.57ms
  5.    2.       input:         0.96ms         1.13ms         0.87ms
  6.    1.       input:         1.04ms         1.33ms         0.89ms
  6.    2.       input:        33.19ms        34.95ms        31.01ms
  7.    1.       input:         5.05ms         5.36ms         4.60ms
  7.    2.       input:       152.35ms       154.16ms       147.26ms
  8.    1.       input:         0.53ms         0.70ms         0.44ms
  8.    2.       input:         0.48ms         0.88ms         0.38ms
  9.    1.       input:         1.68ms         2.14ms         1.55ms
  9.    1. perf_test_1:         2.58ms         2.73ms         2.52ms
  9.    1. perf_test_2:        12.31ms        13.65ms        11.76ms
  9.    2.       input:         9.88ms        10.76ms         8.82ms
  9.    2. perf_test_1:        18.62ms        20.86ms        17.32ms
  9.    2. perf_test_2:       121.02ms       178.85ms       107.74ms
 10.    1.       input:         1.08ms         1.23ms         0.88ms
 10.    2.       input:         0.98ms         1.30ms         0.75ms
 11.    1.       input:         0.38ms         0.42ms         0.35ms
 11.    2.       input:        21.26ms        56.53ms        14.96ms
 12.    1.       input:        16.67ms        26.98ms        13.94ms
 12.    2.       input:        17.64ms        19.97ms        16.02ms
 13.    1.       input:         0.34ms         0.41ms         0.30ms
 13.    2.       input:         0.29ms         0.42ms         0.26ms

Sum of average times for normal inputs: 280.913ms
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
