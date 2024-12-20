package aoc.measure;

import aoc.utils.Resource;
import aoc.y2024.Aoc2024Day01Kt;
import aoc.y2024.Aoc2024Day02Kt;
import aoc.y2024.Aoc2024Day03Kt;
import aoc.y2024.Aoc2024Day04Kt;
import aoc.y2024.Aoc2024Day05Kt;
import aoc.y2024.Aoc2024Day06Kt;
import aoc.y2024.Aoc2024Day07Kt;
import aoc.y2024.Aoc2024Day08Kt;
import aoc.y2024.Aoc2024Day09Kt;
import aoc.y2024.Aoc2024Day10Kt;
import aoc.y2024.Aoc2024Day11Kt;
import aoc.y2024.Aoc2024Day12Kt;
import aoc.y2024.Aoc2024Day13Kt;
import aoc.y2024.Aoc2024Day14Kt;
import aoc.y2024.Aoc2024Day15Kt;
import aoc.y2024.Aoc2024Day16Kt;
import aoc.y2024.Aoc2024Day17Kt;
import aoc.y2024.Aoc2024Day18Kt;
import aoc.y2024.Aoc2024Day19Kt;
import aoc.y2024.Aoc2024Day20Kt;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Aoc2024Bench
{

    private final Resource inDay01 = Resource.named("aoc2024/day01/input.txt");

    @Benchmark
    public void day01_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day01Kt.day01(inDay01).getResult1()
        );
    }

    @Benchmark
    public void day01_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day01Kt.day01(inDay01).getResult2()
        );
    }

    private final Resource inDay02 = Resource.named("aoc2024/day02/input.txt");

    @Benchmark
    public void day02_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day02Kt.day02(inDay02).getResult1()
        );
    }

    @Benchmark
    public void day02_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day02Kt.day02(inDay02).getResult2()
        );
    }

    private final Resource inDay03 = Resource.named("aoc2024/day03/input.txt");

    @Benchmark
    public void day03_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day03Kt.day03(inDay03).getResult1()
        );
    }

    @Benchmark
    public void day03_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day03Kt.day03(inDay03).getResult2()
        );
    }

    private final Resource inDay04 = Resource.named("aoc2024/day04/input.txt");

    @Benchmark
    public void day04_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day04Kt.day04(inDay04).getResult1()
        );
    }

    @Benchmark
    public void day04_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day04Kt.day04(inDay04).getResult2()
        );
    }

    private final Resource inDay05 = Resource.named("aoc2024/day05/input.txt");

    @Benchmark
    public void day05_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day05Kt.day05(inDay05).getResult1()
        );
    }

    @Benchmark
    public void day05_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day05Kt.day05(inDay05).getResult2()
        );
    }

    private final Resource inDay06 = Resource.named("aoc2024/day06/input.txt");

    @Benchmark
    public void day06_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day06Kt.day06(inDay06).getResult1()
        );
    }

    @Benchmark
    public void day06_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day06Kt.day06(inDay06).getResult2()
        );
    }

    private final Resource inDay07 = Resource.named("aoc2024/day07/input.txt");

    @Benchmark
    public void day07_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day07Kt.day07(inDay07).getResult1()
        );
    }

    @Benchmark
    public void day07_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day07Kt.day07(inDay07).getResult2()
        );
    }

    private final Resource inDay08 = Resource.named("aoc2024/day08/input.txt");

    @Benchmark
    public void day08_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day08Kt.day08(inDay08).getResult1()
        );
    }

    @Benchmark
    public void day08_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day08Kt.day08(inDay08).getResult2()
        );
    }

    private final Resource inDay09 = Resource.named("aoc2024/day09/input.txt");

    @Benchmark
    public void day09_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day09Kt.day09(inDay09).getResult1()
        );
    }

    @Benchmark
    public void day09_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day09Kt.day09(inDay09).getResult2()
        );
    }

    private final Resource inDay10 = Resource.named("aoc2024/day10/input.txt");

    @Benchmark
    public void day10_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day10Kt.day10(inDay10).getResult1()
        );
    }

    @Benchmark
    public void day10_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day10Kt.day10(inDay10).getResult2()
        );
    }

    private final Resource inDay11 = Resource.named("aoc2024/day11/input.txt");

    @Benchmark
    public void day11_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day11Kt.day11(inDay11).getResult1()
        );
    }

    @Benchmark
    public void day11_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day11Kt.day11(inDay11).getResult2()
        );
    }

    private final Resource inDay12 = Resource.named("aoc2024/day12/input.txt");

    @Benchmark
    public void day12_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day12Kt.day12(inDay12).getResult1()
        );
    }

    @Benchmark
    public void day12_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day12Kt.day12(inDay12).getResult2()
        );
    }

    private final Resource inDay13 = Resource.named("aoc2024/day13/input.txt");

    @Benchmark
    public void day13_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day13Kt.day13(inDay13).getResult1()
        );
    }

    @Benchmark
    public void day13_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day13Kt.day13(inDay13).getResult2()
        );
    }

    private final Resource inDay14 = Resource.named("aoc2024/day14/input.txt");

    @Benchmark
    public void day14_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day14Kt.day14(inDay14).getResult1()
        );
    }

//    @Benchmark
//    public void day14_task2(Blackhole bh)
//    {
//        bh.consume(
//            Aoc2024Day14Kt.day14(inDay14).getResult2()
//        );
//    }

    private final Resource inDay15 = Resource.named("aoc2024/day15/input.txt");

    @Benchmark
    public void day15_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day15Kt.day15(inDay15).getResult1()
        );
    }

    @Benchmark
    public void day15_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day15Kt.day15(inDay15).getResult2()
        );
    }

    private final Resource inDay16 = Resource.named("aoc2024/day16/input.txt");

    @Benchmark
    public void day16_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day16Kt.day16(inDay16).getResult1()
        );
    }

    @Benchmark
    public void day16_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day16Kt.day16(inDay16).getResult2()
        );
    }

    private final Resource inDay17 = Resource.named("aoc2024/day17/input.txt");

    @Benchmark
    public void day17_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day17Kt.day17(inDay17).getResult1()
        );
    }

    @Benchmark
    public void day17_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day17Kt.day17(inDay17).getResult2()
        );
    }

    private final Resource inDay18 = Resource.named("aoc2024/day18/input.txt");

    @Benchmark
    public void day18_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day18Kt.day18(inDay18).getResult1()
        );
    }

    @Benchmark
    public void day18_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day18Kt.day18(inDay18).getResult2()
        );
    }

    private final Resource inDay19 = Resource.named("aoc2024/day19/input.txt");

    @Benchmark
    public void day19_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day19Kt.day19(inDay19).getResult1()
        );
    }

    @Benchmark
    public void day19_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day19Kt.day19(inDay19).getResult2()
        );
    }

    private final Resource inDay20 = Resource.named("aoc2024/day20/input.txt");

    @Benchmark
    public void day20_task1(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day20Kt.day20(inDay20).getResult1()
        );
    }

    @Benchmark
    public void day20_task2(Blackhole bh)
    {
        bh.consume(
            Aoc2024Day20Kt.day20(inDay20).getResult2()
        );
    }

}
