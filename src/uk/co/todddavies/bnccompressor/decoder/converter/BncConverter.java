package uk.co.todddavies.bnccompressor.decoder.converter;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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
import uk.co.todddavies.bnccompressor.decoder.reader.BncDecoderModule;
import uk.co.todddavies.bnccompressor.encoder.BncEncoder;
import uk.co.todddavies.bnccompressor.flags.BncGlobalFlagsModule;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.TagMap;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.WordMap;

final class BncConverter {
  
  private static final Charset CHARSET = Charset.defaultCharset();
  private static final String NEW_SENTENCE = String.format("--%n");
  
  public static void main(String[] args) {
    // Create the injector
    Injector injector = Flags.bootstrapFlagInjector(args,
        BncEncoder.class.getName(),
        ImmutableList.<String>of(
            "uk.co.todddavies.bnccompressor.flags",
            "uk.co.todddavies.bnccompressor.decoder.reader",
            "uk.co.todddavies.bnccompressor.decoder.converter",
            "uk.co.todddavies.bnccompressor.mapping.reader"),
        new MappingReaderModule(),
        new BncConverterModule(),
        new BncDecoderModule(),
        new BncGlobalFlagsModule());
    
    BncConverter converter = injector.getInstance(BncConverter.class);
    converter.convert();
  }
  
  private final BncIterator bncIterator;
  
  @Inject
  private BncConverter(@TagMap ImmutableMap<String, Long> tagMap,
      @WordMap ImmutableMap<String, Long> wordMap,
      BncIterator bncIterator) throws FileNotFoundException {
    this.bncIterator = bncIterator;
  }
  
  public void convert() {
    try (BufferedWriter writer = Files.newWriter(BncConverterFlags.getOutputFile(), CHARSET)) {
      while(bncIterator.hasNext()) {
        for(WordTag wt : bncIterator.next()) {
          writer.write(String.format("%s %s%n", wt.tag, wt.word));
        }
        writer.write(NEW_SENTENCE);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
