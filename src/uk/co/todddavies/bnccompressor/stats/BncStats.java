package uk.co.todddavies.bnccompressor.stats;

import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Injector;

import edu.uchicago.lowasser.flaginjection.Flags;
import uk.co.todddavies.bnccompressor.TokenTag;
import uk.co.todddavies.bnccompressor.bnc.BncIterator;
import uk.co.todddavies.bnccompressor.bnc.BncModule;
import uk.co.todddavies.bnccompressor.flags.BncGlobalFlagsModule;

public final class BncStats {

  private final BncIterator iterator;
  
  @Inject
  private BncStats(BncIterator iterator) {
    this.iterator = iterator;
  }
  
  public static void main(String[] args) {
    // Create the injector
    Injector injector = Flags.bootstrapFlagInjector(args,
        BncStats.class.getName(),
        ImmutableList.<String>of(
            "uk.co.todddavies.bnccompressor.bnc",
            "uk.co.todddavies.bnccompressor.flags"),
        new BncModule(),
        new BncGlobalFlagsModule());
    
    BncStats stats = injector.getInstance(BncStats.class);
    stats.getSentenceLengthStats();
  }
  
  private void getSentenceLengthStats() {
    TreeMap<Integer, Integer> lengthCounts = new TreeMap<>();
    while(iterator.hasNext()) {
      ImmutableList<TokenTag> sentence = iterator.next();
      Integer nextSize = sentence.size();
      if (lengthCounts.containsKey(nextSize)) {
        lengthCounts.put(nextSize, lengthCounts.get(nextSize) + 1);
      } else {
        lengthCounts.put(nextSize, 1);
      }
    }
    for (Entry<Integer, Integer> size : lengthCounts.entrySet()) {
      System.out.printf("%d\t%d%n", size.getKey(), size.getValue());
    }
  }
  
}
