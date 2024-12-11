# advent-of-code

## Snapshot of `./gradlew benchmarkJvm`:

```
 day  task                   [average]          [min]          [max]
  1.    1.       input:         5.81ms         1.68ms        38.83ms
  1.    2.       input:         2.69ms         1.74ms         7.36ms
  2.    1.       input:         2.52ms         0.75ms         8.90ms
  2.    2.       input:         3.79ms         1.52ms        15.22ms
  3.    1.       input:         2.57ms         0.60ms        17.14ms
  3.    2.       input:         1.52ms         0.77ms         5.87ms
  4.    1.       input:        34.32ms        17.65ms        97.86ms
  4.    2.       input:         6.26ms         3.41ms        23.20ms
  5.    1.       input:         2.92ms         1.19ms        11.21ms
  5.    2.       input:         3.65ms         1.59ms         7.90ms
  6.    1.       input:         3.74ms         1.92ms        13.83ms
  6.    2.       input:        56.29ms        24.91ms       175.43ms
  7.    1.       input:        12.75ms         7.24ms        54.09ms
  7.    2.       input:       261.36ms       217.83ms       489.59ms
  8.    1.       input:         2.16ms         0.76ms        11.48ms
  8.    2.       input:         1.44ms         0.83ms         3.34ms
  9.    1.       input:         5.56ms         2.06ms        26.80ms
  9.    1. perf_test_1:         6.13ms         3.55ms        15.92ms
  9.    1. perf_test_2:        32.61ms        19.21ms        54.51ms
  9.    2.       input:        27.17ms        14.66ms        85.26ms
  9.    2. perf_test_1:        37.39ms        29.17ms        60.51ms
  9.    2. perf_test_2:       191.85ms       170.68ms       223.86ms
 10.    1.       input:         2.90ms         1.58ms        10.17ms
 10.    2.       input:         2.32ms         1.52ms         5.42ms
 11.    1.       input:         0.98ms         0.56ms         3.12ms
 11.    2.       input:        36.18ms        27.48ms        54.51ms

Sum of min for normal inputs: 332.242ms
```

## Snapshot of `./gradlew benchmarkNative`:

```
 day  task                   [average]          [min]          [max]
  1.    1.       input:         1.46ms         1.14ms         2.46ms
  1.    2.       input:         1.56ms         1.17ms         3.35ms
  2.    1.       input:         2.12ms         1.79ms         2.89ms
  2.    2.       input:         4.22ms         3.68ms         6.47ms
  3.    1.       input:         1.19ms         1.08ms         1.63ms
  3.    2.       input:         1.50ms         1.39ms         1.82ms
  4.    1.       input:        64.86ms        48.08ms       119.40ms
  4.    2.       input:         7.16ms         6.14ms         9.97ms
  5.    1.       input:         1.88ms         1.74ms         2.77ms
  5.    2.       input:         2.94ms         2.66ms         4.19ms
  6.    1.       input:         3.82ms         3.24ms         6.80ms
  6.    2.       input:       119.02ms        89.53ms       178.09ms
  7.    1.       input:        20.88ms        18.61ms        31.09ms
  7.    2.       input:       860.62ms       831.67ms       888.63ms
  8.    1.       input:         0.68ms         0.63ms         0.90ms
  8.    2.       input:         0.70ms         0.68ms         0.75ms
  9.    1.       input:         6.70ms         6.42ms         7.27ms
  9.    1. perf_test_1:        11.60ms        11.26ms        12.96ms
  9.    1. perf_test_2:        68.43ms        61.76ms        88.67ms
  9.    2.       input:        27.83ms        26.97ms        29.17ms
  9.    2. perf_test_1:        62.53ms        54.06ms       102.81ms
  9.    2. perf_test_2:       421.12ms       300.03ms       530.06ms
 10.    1.       input:        12.15ms         2.91ms        93.57ms
 10.    2.       input:         3.60ms         2.87ms         6.60ms
 11.    1.       input:         2.50ms         0.94ms        14.56ms
 11.    2.       input:        62.07ms        49.43ms       140.80ms

Sum of min for normal inputs: 1.102755s
```

lol?
