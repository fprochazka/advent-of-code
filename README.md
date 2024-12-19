# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.35ms         2.02ms         0.91ms
  1.    2.       input:         1.34ms         1.86ms         0.96ms
  2.    1.       input:         0.64ms         0.70ms         0.58ms
  2.    2.       input:         0.98ms         1.08ms         0.87ms
  3.    1.       input:         0.43ms         0.46ms         0.42ms
  3.    2.       input:         0.48ms         0.49ms         0.48ms
  4.    1.       input:         9.18ms         9.53ms         9.01ms
  4.    2.       input:         1.55ms         2.50ms         1.12ms
  5.    1.       input:         1.02ms         1.13ms         0.94ms
  5.    2.       input:         1.52ms         3.11ms         1.25ms
  6.    1.       input:         0.85ms         1.43ms         0.62ms
  6.    2.       input:        32.29ms        35.49ms        28.91ms
  7.    1.       input:         5.46ms         7.14ms         5.13ms
  7.    2.       input:       132.62ms       135.07ms       130.61ms
  8.    1.       input:         0.37ms         0.44ms         0.35ms
  8.    2.       input:         0.37ms         0.45ms         0.33ms
  9.    1.       input:         1.50ms         1.98ms         1.41ms
  9.    1. perf_test_1:         2.53ms         4.35ms         2.21ms
  9.    1. perf_test_2:        10.94ms        11.44ms        10.45ms
  9.    2.       input:        10.36ms        13.12ms         9.35ms
  9.    2. perf_test_1:        18.60ms        21.13ms        17.34ms
  9.    2. perf_test_2:       118.92ms       195.41ms       104.81ms
 10.    1.       input:         0.87ms         1.07ms         0.78ms
 10.    2.       input:         0.70ms         0.74ms         0.65ms
 11.    1.       input:         0.38ms         0.48ms         0.35ms
 11.    2.       input:        19.74ms        38.31ms        16.30ms
 12.    1.       input:        17.60ms        18.43ms        16.93ms
 12.    2.       input:        20.71ms        22.87ms        18.18ms
 13.    1.       input:         0.35ms         0.55ms         0.28ms
 13.    2.       input:         0.31ms         0.38ms         0.26ms
 14.    1.       input:         0.40ms         0.51ms         0.32ms
 15.    1.       input:         0.83ms         1.01ms         0.75ms
 15.    2.       input:         1.84ms         2.68ms         1.64ms
 16.    1.       input:         8.44ms         9.91ms         7.91ms
 16.    2.       input:        26.99ms        34.66ms        23.02ms
 17.    1.       input:         0.15ms         0.21ms         0.11ms
 17.    2.       input:         0.27ms         0.50ms         0.13ms
 18.    1.       input:         2.42ms         4.90ms         1.74ms
 18.    2.       input:        12.06ms        12.73ms        11.21ms
 19.    1.       input:         6.87ms         9.04ms         5.14ms
 19.    2.       input:        10.31ms        12.85ms         8.96ms

Sum of average times for normal inputs: 333.526ms
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
