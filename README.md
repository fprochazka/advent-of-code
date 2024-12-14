# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]        [worst]         [best]
  1.    1.       input:         1.26ms         1.58ms         0.88ms
  1.    2.       input:         1.31ms         1.44ms         0.94ms
  2.    1.       input:         0.61ms         0.70ms         0.53ms
  2.    2.       input:         0.96ms         1.19ms         0.82ms
  3.    1.       input:         0.43ms         0.58ms         0.39ms
  3.    2.       input:         0.48ms         0.66ms         0.44ms
  4.    1.       input:         7.90ms         8.28ms         7.61ms
  4.    2.       input:         1.39ms         2.06ms         1.00ms
  5.    1.       input:         1.06ms         1.08ms         1.03ms
  5.    2.       input:         1.49ms         1.54ms         1.40ms
  6.    1.       input:         0.80ms         1.12ms         0.69ms
  6.    2.       input:        34.50ms        36.15ms        33.29ms
  7.    1.       input:         5.22ms         5.95ms         4.93ms
  7.    2.       input:       132.33ms       134.61ms       130.60ms
  8.    1.       input:         0.48ms         0.73ms         0.34ms
  8.    2.       input:         0.43ms         0.69ms         0.35ms
  9.    1.       input:         1.44ms         1.47ms         1.40ms
  9.    1. perf_test_1:         2.28ms         2.50ms         2.16ms
  9.    1. perf_test_2:        11.04ms        13.80ms        10.15ms
  9.    2.       input:         9.75ms        11.43ms         9.33ms
  9.    2. perf_test_1:        17.50ms        19.84ms        16.56ms
  9.    2. perf_test_2:       115.60ms       165.57ms       106.69ms
 10.    1.       input:         0.84ms         0.89ms         0.78ms
 10.    2.       input:         0.71ms         0.73ms         0.68ms
 11.    1.       input:         0.36ms         0.38ms         0.35ms
 11.    2.       input:        16.54ms        17.41ms        13.59ms
 12.    1.       input:        16.71ms        20.97ms        13.38ms
 12.    2.       input:        17.89ms        31.14ms        15.08ms
 13.    1.       input:         0.33ms         0.39ms         0.29ms
 13.    2.       input:         0.27ms         0.29ms         0.26ms
 14.    1.       input:         0.31ms         0.39ms         0.27ms

Sum of average times for normal inputs: 255.764ms
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
