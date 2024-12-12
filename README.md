# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
  day  task                   [average]          [max]         [last]
  1.    1.       input:         5.39ms        35.56ms         1.71ms
  1.    2.       input:         2.43ms         6.50ms         1.72ms
  2.    1.       input:         2.61ms         7.91ms         0.78ms
  2.    2.       input:         3.75ms        12.64ms         1.42ms
  3.    1.       input:         2.53ms        17.82ms         0.63ms
  3.    2.       input:         1.43ms         5.28ms         0.77ms
  4.    1.       input:        31.78ms        91.22ms        25.05ms
  4.    2.       input:         6.09ms        22.39ms         3.30ms
  5.    1.       input:         2.84ms        10.79ms         1.16ms
  5.    2.       input:         3.53ms         6.95ms         1.55ms
  6.    1.       input:         3.78ms        13.85ms         1.96ms
  6.    2.       input:        51.68ms       165.01ms        27.56ms
  7.    1.       input:        12.82ms        50.21ms         7.09ms
  7.    2.       input:       270.09ms       462.41ms       199.26ms
  8.    1.       input:         2.59ms        15.95ms         0.71ms
  8.    2.       input:         1.53ms         4.12ms         0.82ms
  9.    1.       input:         6.35ms        34.44ms         2.09ms
  9.    1. perf_test_1:         5.94ms        17.88ms         3.29ms
  9.    1. perf_test_2:        24.67ms        36.74ms        17.74ms
  9.    2.       input:        23.75ms        77.08ms        13.88ms
  9.    2. perf_test_1:        34.79ms        58.96ms        27.24ms
  9.    2. perf_test_2:       178.93ms       206.83ms       160.60ms
 10.    1.       input:         2.78ms        10.15ms         1.56ms
 10.    2.       input:         2.46ms         5.01ms         1.45ms
 11.    1.       input:         0.92ms         2.85ms         0.54ms
 11.    2.       input:        31.92ms        52.59ms        26.49ms
 12.    1.       input:        34.34ms        84.78ms        25.53ms
 12.    2.       input:        28.20ms        38.28ms        22.73ms

Sum of last for normal inputs: 369.774853ms
```

## Snapshot of `./gradlew benchmarkNative`:

```
 day  task                   [average]          [max]         [last]
  1.    1.       input:         1.37ms         2.16ms         1.15ms
  1.    2.       input:         1.31ms         1.84ms         1.20ms
  2.    1.       input:         2.01ms         2.78ms         1.90ms
  2.    2.       input:         4.55ms         6.14ms         3.96ms
  3.    1.       input:         1.22ms         1.69ms         1.11ms
  3.    2.       input:         1.59ms         1.85ms         1.42ms
  4.    1.       input:        67.69ms       160.06ms        49.88ms
  4.    2.       input:         6.62ms         9.80ms         6.27ms
  5.    1.       input:         1.89ms         2.65ms         1.87ms
  5.    2.       input:         4.50ms        20.01ms         2.65ms
  6.    1.       input:         3.35ms         3.43ms         3.43ms
  6.    2.       input:       110.24ms       147.74ms       118.87ms
  7.    1.       input:        19.39ms        22.82ms        22.82ms
  7.    2.       input:       822.23ms       879.14ms       879.14ms
  8.    1.       input:         0.67ms         0.75ms         0.66ms
  8.    2.       input:         0.84ms         1.79ms         0.70ms
  9.    1.       input:         7.39ms         9.34ms         9.34ms
  9.    1. perf_test_1:        12.63ms        17.44ms        11.62ms
  9.    1. perf_test_2:        72.21ms        90.31ms        63.79ms
  9.    2.       input:        28.86ms        35.88ms        27.19ms
  9.    2. perf_test_1:        63.58ms       107.91ms        90.98ms
  9.    2. perf_test_2:       409.33ms       566.03ms       465.16ms
 10.    1.       input:         2.99ms         3.19ms         2.99ms
 10.    2.       input:         3.61ms         6.72ms         2.95ms
 11.    1.       input:         0.97ms         0.99ms         0.97ms
 11.    2.       input:        67.06ms       119.21ms        50.17ms
 12.    1.       input:        57.26ms       145.46ms        39.56ms
 12.    2.       input:        46.42ms        85.31ms        39.94ms

Sum of last for normal inputs: 1.270143274s
```

lol?
