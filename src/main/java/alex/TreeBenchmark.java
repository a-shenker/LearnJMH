package alex;

import it.unimi.dsi.fastutil.longs.Long2LongRBTreeMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class TreeBenchmark {
  private final static int SAMPLE_SIZE = 100_000;
  private final static ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
  private static final long[] KEYS = genRandomLongArray();
  private static final long[] VALUES = genRandomLongArray();

  private static long[] genRandomLongArray() {
    long beforeT = System.nanoTime();
    final long[] retVal = new long[SAMPLE_SIZE];
    for (int i = 0; i < SAMPLE_SIZE; i++) {
        retVal[i] = RANDOM.nextLong();
    }
    System.out.println("\nTook " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - beforeT) +
        " millis to generate sample data of size " + SAMPLE_SIZE + " result size " + retVal.length);
    return retVal;
  }

  static Long2LongRBTreeMap genRandomRBTreeMap() {
    return new Long2LongRBTreeMap(KEYS, VALUES);
  }
  static TreeMap<Long,Long> genRandomTreeMap() {
    final TreeMap<Long,Long> map = new TreeMap<>();
    for (int i = 0; i < KEYS.length; i++) {
      map.put(KEYS[i], VALUES[i]);
    }
    return map;
  }

  @State(Scope.Benchmark)
  public static class BMarkState {
    @Param({"true"})
    public boolean removeLargest;

    @Param({"true"})
    public boolean removeOnly;

    public Long2LongRBTreeMap fastUtilMap;
    TreeMap<Long,Long> treeMap;

    @Setup(Level.Invocation)
    public void init() {
      fastUtilMap = genRandomRBTreeMap();
      treeMap = genRandomTreeMap();
    }
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void testFastUtilMap(final BMarkState params, final Blackhole sink) {
    final Long2LongRBTreeMap map;
    if (params.removeOnly) {
      map = params.fastUtilMap;
    } else {
      map = genRandomRBTreeMap();
      for (long key : KEYS) sink.consume(map.get(key));
    }
    if (params.removeLargest) {
      while (!map.isEmpty()) {
        map.remove(map.lastLongKey());
      }
    } else {
      for (long key : KEYS) map.remove(key);
    }
    sink.consume(map);
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void testDefaultMap(final BMarkState params, final Blackhole sink) {
    final TreeMap<Long,Long> map;
    if (params.removeOnly) {
      map = params.treeMap;
    } else {
      map = genRandomTreeMap();
      for (long key : KEYS) sink.consume(map.get(key));
    }
    if (params.removeLargest) {
      while (!map.isEmpty()) {
        map.pollLastEntry();
      }
    } else {
      for (long key : KEYS) map.remove(key);
    }
    sink.consume(map);
  }
}