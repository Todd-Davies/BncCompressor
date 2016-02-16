package uk.co.todddavies.bnccompressor.encoder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Injector;

import edu.uchicago.lowasser.flaginjection.Flags;
import uk.co.todddavies.bnccompressor.WordTag;
import uk.co.todddavies.bnccompressor.bnc.BncIterator;
import uk.co.todddavies.bnccompressor.bnc.BncModule;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.TagMap;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.WordMap;

public final class BncEncoder {

  private static final Charset CHARSET = Charset.defaultCharset();
  private static final String NEW_SENTENCE = String.format("--%n");
  
  private final BncIterator bncIterator;
  private final ImmutableMap<String, Long> wordMap, tagMap;
  
  public static void main(String[] args) {    
    // Create the injector
    Injector injector = Flags.bootstrapFlagInjector(args,
        BncEncoder.class.getName(),
        ImmutableList.<String>of(
            "uk.co.todddavies.bnccompressor.encoder",
            "uk.co.todddavies.bnccompressor.bnc",
            "uk.co.todddavies.bnccompressor.mapping.reader"),
        new BncEncoderModule(),
        new BncModule(),
        new MappingReaderModule());
    
    BncEncoder bncEncoder = injector.getInstance(BncEncoder.class);
    bncEncoder.encode();
  }
  
  @Inject
  private BncEncoder(BncIterator iterator,
      @WordMap ImmutableMap<String, Long> wordMap,
      @TagMap ImmutableMap<String, Long> tagMap) {
    this.bncIterator = iterator;
    this.wordMap = wordMap;
    this.tagMap = tagMap;
  }
  
  public void encode() {
    try (BufferedWriter writer = Files.newWriter(BncEncoderFlags.getOutputFile(), CHARSET)) {
      while(bncIterator.hasNext()) {
        for(WordTag wt : bncIterator.next()) {
          writer.write(String.format("%d %d%n", wordMap.get(wt.word), tagMap.get(wt.tag)));
        }
        writer.write(NEW_SENTENCE);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}
