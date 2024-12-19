# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         0.82ms         0.84ms         0.80ms
  1.    2.       input:         0.89ms         0.91ms         0.88ms
  2.    1.       input:         0.63ms         0.64ms         0.61ms
  2.    2.       input:         0.98ms         1.00ms         0.95ms
  3.    1.       input:         0.39ms         0.41ms         0.38ms
  3.    2.       input:         0.47ms         0.48ms         0.46ms
  4.    1.       input:         9.34ms         9.53ms         9.01ms
  4.    2.       input:         1.07ms         1.09ms         1.05ms
  5.    1.       input:         0.77ms         0.78ms         0.75ms
  5.    2.       input:         1.09ms         1.11ms         1.07ms
  6.    1.       input:         0.61ms         0.62ms         0.59ms
  6.    2.       input:        32.50ms        32.87ms        31.89ms
  7.    1.       input:         5.04ms         5.37ms         4.90ms
  7.    2.       input:       129.83ms       131.53ms       127.49ms
  8.    1.       input:         0.32ms         0.38ms         0.29ms
  8.    2.       input:         0.31ms         0.35ms         0.25ms
  9.    1.       input:         1.42ms         1.45ms         1.39ms
  9.    1. perf_test_1:         2.31ms         2.38ms         2.25ms
  9.    1. perf_test_2:        10.68ms        10.81ms        10.45ms
  9.    2.       input:         9.49ms         9.55ms         9.37ms
  9.    2. perf_test_1:        17.56ms        18.36ms        17.00ms
  9.    2. perf_test_2:       105.16ms       109.11ms       102.93ms
 10.    1.       input:         0.80ms         0.85ms         0.76ms
 10.    2.       input:         0.68ms         0.78ms         0.64ms
 11.    1.       input:         0.33ms         0.34ms         0.33ms
 11.    2.       input:        16.08ms        16.73ms        15.77ms
 12.    1.       input:        16.97ms        17.29ms        16.40ms
 12.    2.       input:        18.70ms        19.14ms        18.48ms
 13.    1.       input:         0.28ms         0.29ms         0.27ms
 13.    2.       input:         0.25ms         0.26ms         0.24ms
 14.    1.       input:         0.33ms         0.34ms         0.33ms
 15.    1.       input:         0.74ms         0.76ms         0.73ms
 15.    2.       input:         1.15ms         1.23ms         1.10ms
 16.    1.       input:         8.67ms        10.57ms         8.02ms
 16.    2.       input:        27.05ms        34.19ms        24.39ms
 17.    1.       input:         0.14ms         0.15ms         0.12ms
 17.    2.       input:         0.13ms         0.13ms         0.13ms
 18.    1.       input:         2.59ms         2.61ms         2.57ms
 18.    2.       input:        12.95ms        14.71ms        12.13ms
 19.    1.       input:         6.73ms         7.12ms         6.48ms
 19.    2.       input:         6.24ms         6.45ms         6.13ms

Sum of average times for normal inputs: 316.759ms
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
