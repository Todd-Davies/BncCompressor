package uk.co.todddavies.bnccompressor.decoder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import uk.co.todddavies.bnccompressor.WordTag;
import uk.co.todddavies.bnccompressor.bnc.BncIterator;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.TagMap;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.WordMap;

public final class BncDecoder implements BncIterator {

  private static final String NEW_SENTENCE = String.format("--");
  
  private final ImmutableBiMap<Long, String> wordMap, tagMap;
  private final Scanner fileScanner;
  
  @Inject
  private BncDecoder(@TagMap ImmutableMap<String, Long> tagMap,
      @WordMap ImmutableMap<String, Long> wordMap) throws FileNotFoundException {
    this.tagMap = ImmutableBiMap.<String, Long>builder().putAll(tagMap).build().inverse();
    this.wordMap = ImmutableBiMap.<String, Long>builder().putAll(wordMap).build().inverse();
    fileScanner = new Scanner(BncDecoderFlags.getInputFile());
  }

  @Override
  public boolean hasNext() {
    return fileScanner.hasNextLine();
  }

  @Override
  public ImmutableList<WordTag> next() {
    ImmutableList.Builder<WordTag> nextSentence = ImmutableList.builder(); 
    while(fileScanner.hasNextLine()) {
      String line = fileScanner.nextLine();
      if (line.equals(NEW_SENTENCE)) break;
      else {
        String[] wordTag = line.split(" ");
        nextSentence.add(WordTag.from(wordMap.get(Long.parseLong(wordTag[0])), tagMap.get(Long.parseLong(wordTag[1]))));
      }
    }
    return nextSentence.build();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() throws IOException {
    fileScanner.close();
  }
}
