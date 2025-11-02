package alex;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@State(Scope.Benchmark)
public class ClassSelectorBenchmark {
   private static final byte BYTE = 0;
   private static final byte SHORT = 1;
   private static final byte INT = 2;
   private static final byte LONG = 3;
   private static final byte FLOAT = 4;
   private static final byte DOUBLE = 5;
   private static final byte BOOLEAN = 6;
   private static final byte CHAR = 7;
   private static final byte BYTE_OBJ = 8;
   private static final byte SHORT_OBJ = 9;
   private static final byte INT_OBJ = 10;
   private static final byte LONG_OBJ = 11;
   private static final byte FLOAT_OBJ = 12;
   private static final byte DOUBLE_OBJ = 13;
   private static final byte BOOLEAN_OBJ = 14;
   private static final byte CHAR_OBJ = 15;
   private static final byte STRING = 16;
   private static final byte LOCAL_DATE = 17;
   private static final byte LOCAL_DATE_DATE = 18;
   private final static Class<?>[] TYPES = new Class<?>[19];
   static {
      TYPES[BYTE] = byte.class;
      TYPES[SHORT] = short.class;
      TYPES[INT] = int.class;
      TYPES[LONG] = long.class;
      TYPES[FLOAT] = float.class;
      TYPES[DOUBLE] = double.class;
      TYPES[BOOLEAN] = boolean.class;
      TYPES[CHAR] = char.class;
      TYPES[BYTE_OBJ] = Byte.class;
      TYPES[SHORT_OBJ] = Short.class;
      TYPES[INT_OBJ] = Integer.class;
      TYPES[LONG_OBJ] = Long.class;
      TYPES[FLOAT_OBJ] = Float.class;
      TYPES[DOUBLE_OBJ] = Double.class;
      TYPES[BOOLEAN_OBJ] = Boolean.class;
      TYPES[CHAR_OBJ] = Character.class;
      TYPES[STRING] = String.class;
      TYPES[LOCAL_DATE] = LocalDate.class;
      TYPES[LOCAL_DATE_DATE] = LocalDateTime.class;
   }
   private final Map<Class<?>, Runnable> _setters;
   public ClassSelectorBenchmark() {
      final Object2ObjectMap<Class<?>, Runnable> tmp = new Object2ObjectArrayMap<>(TYPES.length);
      tmp.put(int.class, () -> _counts[INT]++);
      tmp.put(long.class, () -> _counts[LONG]++);
      tmp.put(double.class, () -> _counts[DOUBLE]++);
      tmp.put(float.class, () -> _counts[FLOAT]++);
      tmp.put(short.class, () -> _counts[SHORT]++);
      tmp.put(byte.class, () -> _counts[BYTE]++);
      tmp.put(boolean.class, () -> _counts[BOOLEAN]++);
      tmp.put(char.class, () -> _counts[CHAR]++);
      tmp.put(Integer.class, () -> _counts[INT_OBJ]++);
      tmp.put(Long.class, () -> _counts[LONG_OBJ]++);
      tmp.put(Double.class, () -> _counts[DOUBLE_OBJ]++);
      tmp.put(Float.class, () -> _counts[FLOAT_OBJ]++);
      tmp.put(Short.class, () -> _counts[SHORT_OBJ]++);
      tmp.put(Byte.class, () -> _counts[BYTE_OBJ]++);
      tmp.put(Boolean.class, () -> _counts[BOOLEAN_OBJ]++);
      tmp.put(Character.class, () -> _counts[CHAR_OBJ]++);
      tmp.put(String.class, () -> _counts[STRING]++);
      tmp.put(LocalDate.class, () -> _counts[LOCAL_DATE]++);
      tmp.put(LocalDateTime.class, () -> _counts[LOCAL_DATE_DATE]++);
      tmp.defaultReturnValue(() -> {throw new IllegalArgumentException("Unknown class");});
      _setters = tmp; //Object2ObjectMaps.unmodifiable(tmp);
   }
   private final int[] _counts = new int[TYPES.length];
   private final ThreadLocalRandom _random = ThreadLocalRandom.current();
   private Class<?> randomClass() {
      return TYPES[_random.nextInt(19)];
   }
   private void clear() {
      Arrays.fill(_counts, 0);
   }

   @State(Scope.Benchmark)
   public static class SampleSize {
      @Param("10000000")
      public int sampleSize;
   }

   @Benchmark
   @BenchmarkMode(Mode.AverageTime)
   @OutputTimeUnit(TimeUnit.MILLISECONDS)
   public void runFunctionalTest(final SampleSize param, final Blackhole sink) {
      clear();
      for (int i = 0; i < param.sampleSize; i ++) {
        _setters.get(randomClass()).run();
      }
      sink.consume(_counts);
      sanityCheck(param.sampleSize);
   }

   @Benchmark
   @BenchmarkMode(Mode.AverageTime)
   @OutputTimeUnit(TimeUnit.MILLISECONDS)
   public void runIfThenTest(final SampleSize param, final Blackhole sink) {
      clear();
      for (int i = 0; i < param.sampleSize; i ++) {
         final Class<?> clazz = randomClass();
         if (clazz == int.class) {
            _counts[INT]++;
         } else if (clazz == long.class) {
            _counts[LONG]++;
         } else if (clazz == double.class) {
            _counts[DOUBLE]++;
         } else if (clazz == float.class) {
            _counts[FLOAT]++;
         } else if (clazz == short.class) {
            _counts[SHORT]++;
         } else if (clazz == byte.class) {
            _counts[BYTE]++;
         } else if (clazz == boolean.class) {
            _counts[BOOLEAN]++;
         } else if (clazz == char.class) {
            _counts[CHAR]++;
         } else if (clazz == Integer.class) {
            _counts[INT_OBJ]++;
         } else if (clazz == Long.class) {
            _counts[LONG_OBJ]++;
         } else if (clazz == Double.class) {
            _counts[DOUBLE_OBJ]++;
         } else if (clazz == Float.class) {
            _counts[FLOAT_OBJ]++;
         } else if (clazz == Short.class) {
            _counts[SHORT_OBJ]++;
         } else if (clazz == Byte.class) {
            _counts[BYTE_OBJ]++;
         } else if (clazz == Boolean.class) {
            _counts[BOOLEAN_OBJ]++;
         } else if (clazz == Character.class) {
            _counts[CHAR_OBJ]++;
         } else if (clazz == String.class) {
            _counts[STRING]++;
         } else if (clazz == LocalDate.class) {
            _counts[LOCAL_DATE]++;
         } else if (clazz == LocalDateTime.class) {
            _counts[LOCAL_DATE_DATE]++;
         } else {
            throw new IllegalArgumentException("Unknown class");
         }
      }
      sink.consume(_counts);
      sanityCheck(param.sampleSize);
   }
   private void sanityCheck(final int sampleSize) {
     final int total = Arrays.stream(_counts).sum();
     if (total != sampleSize) {
       throw new IllegalStateException(
         String.format("Sanity check failed: total %d != sample size %d", total, sampleSize));
     }
   }

   private void printCounts() {
      log.info("Counts:");
      for (int i = 0; i < TYPES.length; i++) {
         log.info("  %s: %d%n", TYPES[i].getSimpleName(), _counts[i]);
      }
   }

   public static void main(final String[] argv) {
      final ClassSelectorBenchmark benchmark = new ClassSelectorBenchmark();
      final SampleSize ss = new SampleSize();
      ss.sampleSize = 100_000_000;
      final Blackhole sink  = new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous.");
      ClassSelectorBenchmark.log.info("Running if-then test...");
      long beforeT = System.nanoTime();
      benchmark.runIfThenTest(ss, sink);
      ClassSelectorBenchmark.log.info("If-then test took {}ms...", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - beforeT));
      benchmark.printCounts();
      ClassSelectorBenchmark.log.info("Running functional test...");
      beforeT = System.nanoTime();
      benchmark.runFunctionalTest(ss, sink);
      ClassSelectorBenchmark.log.info("Functional test took {}ms...", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - beforeT));
      benchmark.printCounts();
   }
}