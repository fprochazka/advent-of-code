# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.32ms         1.57ms         1.03ms
  1.    2.       input:         1.38ms         1.57ms         0.99ms
  2.    1.       input:         0.51ms         0.67ms         0.47ms
  2.    2.       input:         0.77ms         0.87ms         0.74ms
  3.    1.       input:         0.47ms         0.53ms         0.44ms
  3.    2.       input:         0.51ms         0.52ms         0.50ms
  4.    1.       input:         9.61ms        10.94ms         9.26ms
  4.    2.       input:         1.81ms         2.69ms         1.42ms
  5.    1.       input:         0.69ms         1.03ms         0.57ms
  5.    2.       input:         0.93ms         1.05ms         0.88ms
  6.    1.       input:         1.06ms         1.37ms         0.90ms
  6.    2.       input:        34.90ms        37.04ms        31.80ms
  7.    1.       input:         5.60ms         6.33ms         5.37ms
  7.    2.       input:       173.31ms       181.87ms       168.39ms
  8.    1.       input:         0.55ms         1.01ms         0.38ms
  8.    2.       input:         0.55ms         1.39ms         0.37ms
  9.    1.       input:         1.88ms         3.40ms         1.55ms
  9.    1. perf_test_1:         2.61ms         2.80ms         2.48ms
  9.    1. perf_test_2:        13.22ms        21.36ms        11.49ms
  9.    2.       input:        10.79ms        12.97ms         9.62ms
  9.    2. perf_test_1:        19.43ms        24.54ms        17.28ms
  9.    2. perf_test_2:       118.27ms       190.03ms       106.24ms
 10.    1.       input:         1.05ms         1.22ms         0.85ms
 10.    2.       input:         0.88ms         1.03ms         0.71ms
 11.    1.       input:         0.35ms         0.39ms         0.34ms
 11.    2.       input:        16.32ms        18.15ms        14.14ms
 12.    1.       input:        15.91ms        18.84ms        14.22ms
 12.    2.       input:        17.39ms        22.63ms        15.83ms
 13.    1.       input:         0.31ms         0.38ms         0.28ms
 13.    2.       input:         0.29ms         0.37ms         0.25ms
 14.    1.       input:         0.58ms         0.69ms         0.51ms

Sum of average times for normal inputs: 299.736ms
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
