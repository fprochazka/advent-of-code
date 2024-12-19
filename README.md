# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.39ms         1.84ms         0.90ms
  1.    2.       input:         1.40ms         1.88ms         0.97ms
  2.    1.       input:         0.68ms         0.72ms         0.60ms
  2.    2.       input:         1.01ms         1.09ms         0.88ms
  3.    1.       input:         0.59ms         1.89ms         0.42ms
  3.    2.       input:         0.50ms         0.58ms         0.47ms
  4.    1.       input:         9.92ms        11.59ms         9.36ms
  4.    2.       input:         1.51ms         2.29ms         1.15ms
  5.    1.       input:         1.00ms         1.06ms         0.95ms
  5.    2.       input:         1.33ms         1.53ms         1.29ms
  6.    1.       input:         0.77ms         1.24ms         0.64ms
  6.    2.       input:        32.53ms        39.55ms        28.04ms
  7.    1.       input:         5.35ms         5.59ms         5.11ms
  7.    2.       input:       132.00ms       132.79ms       130.86ms
  8.    1.       input:         0.37ms         0.47ms         0.34ms
  8.    2.       input:         0.36ms         0.53ms         0.32ms
  9.    1.       input:         1.48ms         1.57ms         1.42ms
  9.    1. perf_test_1:         2.60ms         4.29ms         2.28ms
  9.    1. perf_test_2:        12.44ms        19.91ms        10.80ms
  9.    2.       input:         9.39ms         9.51ms         9.09ms
  9.    2. perf_test_1:        17.88ms        20.03ms        16.29ms
  9.    2. perf_test_2:       106.36ms       110.29ms       104.30ms
 10.    1.       input:         1.00ms         2.36ms         0.80ms
 10.    2.       input:         0.75ms         1.10ms         0.64ms
 11.    1.       input:         0.36ms         0.38ms         0.35ms
 11.    2.       input:        16.74ms        17.83ms        13.74ms
 12.    1.       input:        18.70ms        28.25ms        16.88ms
 12.    2.       input:        19.56ms        22.08ms        17.13ms
 13.    1.       input:         0.32ms         0.36ms         0.29ms
 13.    2.       input:         0.30ms         0.37ms         0.26ms
 14.    1.       input:         0.37ms         0.42ms         0.34ms
 15.    1.       input:         0.82ms         0.94ms         0.78ms
 15.    2.       input:         1.66ms         1.75ms         1.57ms
 16.    1.       input:         8.71ms         9.84ms         8.01ms
 16.    2.       input:        27.02ms        35.12ms        23.37ms
 17.    1.       input:         0.16ms         0.19ms         0.13ms
 17.    2.       input:         0.22ms         0.38ms         0.14ms
 18.    1.       input:         2.51ms         3.73ms         1.74ms
 18.    2.       input:        12.40ms        15.07ms        11.28ms
 19.    1.       input:         6.78ms         8.91ms         5.18ms
 19.    2.       input:        10.62ms        11.26ms         8.89ms

Sum of average times for normal inputs: 330.557ms
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
