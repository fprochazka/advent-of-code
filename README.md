# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         0.85ms         0.89ms         0.82ms
  1.    2.       input:         0.91ms         0.93ms         0.90ms
  2.    1.       input:         0.62ms         0.63ms         0.58ms
  2.    2.       input:         0.95ms         0.99ms         0.86ms
  3.    1.       input:         0.38ms         0.38ms         0.37ms
  3.    2.       input:         0.44ms         0.46ms         0.43ms
  4.    1.       input:         9.33ms         9.79ms         9.01ms
  4.    2.       input:         1.04ms         1.08ms         1.01ms
  5.    1.       input:         0.76ms         0.77ms         0.71ms
  5.    2.       input:         1.08ms         1.12ms         1.01ms
  6.    1.       input:         0.60ms         0.62ms         0.59ms
  6.    2.       input:        33.10ms        33.84ms        32.26ms
  7.    1.       input:         5.11ms         5.29ms         4.99ms
  7.    2.       input:       127.01ms       129.04ms       124.40ms
  8.    1.       input:         0.30ms         0.32ms         0.29ms
  8.    2.       input:         0.34ms         0.35ms         0.32ms
  9.    1.       input:         1.46ms         1.53ms         1.41ms
  9.    1. perf_test_1:         2.25ms         2.30ms         2.17ms
  9.    1. perf_test_2:        10.73ms        10.85ms        10.57ms
  9.    2.       input:         9.48ms         9.65ms         9.24ms
  9.    2. perf_test_1:        19.72ms        28.72ms        16.22ms
  9.    2. perf_test_2:       106.08ms       115.90ms       100.79ms
 10.    1.       input:         0.75ms         0.77ms         0.75ms
 10.    2.       input:         0.68ms         0.76ms         0.65ms
 11.    1.       input:         0.34ms         0.35ms         0.33ms
 11.    2.       input:        16.63ms        17.43ms        16.24ms
 12.    1.       input:        16.62ms        17.00ms        16.35ms
 12.    2.       input:        17.98ms        18.33ms        17.62ms
 13.    1.       input:         0.27ms         0.28ms         0.27ms
 13.    2.       input:         0.25ms         0.26ms         0.23ms
 14.    1.       input:         0.33ms         0.34ms         0.33ms
 15.    1.       input:         0.74ms         0.74ms         0.73ms
 15.    2.       input:         1.13ms         1.22ms         1.07ms
 16.    1.       input:         7.83ms         7.92ms         7.73ms
 16.    2.       input:        27.06ms        32.92ms        24.75ms
 17.    1.       input:         0.13ms         0.13ms         0.13ms
 17.    2.       input:         0.13ms         0.13ms         0.12ms
 18.    1.       input:         2.60ms         2.62ms         2.56ms
 18.    2.       input:        12.70ms        14.45ms        11.89ms
 19.    1.       input:         6.79ms         6.98ms         6.64ms
 19.    2.       input:         6.48ms         6.62ms         6.41ms

Sum of average times for normal inputs: 313.188ms
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
