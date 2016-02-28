package uk.co.todddavies.bnccompressor.tagmapping;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Injector;

import edu.uchicago.lowasser.flaginjection.Flags;
import uk.co.todddavies.bnccompressor.WordTag;
import uk.co.todddavies.bnccompressor.bnc.BncIterator;
import uk.co.todddavies.bnccompressor.decoder.reader.BncDecoderModule;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.TagMap;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.WordMap;

public final class TagMapGenerator {
    
  private static final Charset CHARSET = Charset.defaultCharset();
  
  private final ImmutableMap<String, Long> wordMap, tagMap;
  private final Table<Long, Long, Integer> map;
  private final BncIterator iterator;
  
  @Inject
  private TagMapGenerator(BncIterator iterator,
      @WordMap ImmutableMap<String, Long> wordMap,
      @TagMap ImmutableMap<String, Long> tagMap) {
    map = HashBasedTable.create();
    this.iterator = iterator;
    this.wordMap = wordMap;
    this.tagMap = tagMap;
  }
  
  public static void main(String[] args) {    
    // Create the injector
    Injector injector = Flags.bootstrapFlagInjector(args,
        TagMapGenerator.class.getName(),
        ImmutableList.<String>of(
            "uk.co.todddavies.bnccompressor.mapping.reader",
            "uk.co.todddavies.bnccompressor.tagmapping",
            "uk.co.todddavies.bnccompressor.decoder.reader"),
        new MappingReaderModule(),
        new BncDecoderModule(),
        new MapGeneratorModule());
    
    TagMapGenerator mapGen = injector.getInstance(TagMapGenerator.class);
    System.out.println("Building word and tag maps...");
    mapGen.buildMap();
    System.out.println("Writing map...");
    mapGen.writeMap();
    System.out.println("Done!");
  }

  public void buildMap() {
    while(iterator.hasNext()) {
      for (WordTag wordTag : iterator.next()) {
        Map<Long, Integer> row = map.row(wordMap.get(wordTag.word));
        Long tagKey = tagMap.get(wordTag.tag);
        if (row.containsKey(tagKey)) {
          row.put(tagKey, row.get(tagKey) + 1);
        } else {
          row.put(tagKey, 1);
        }
      }
    }
  }
  
  public void writeMap() {
    try (BufferedWriter writer = Files.newWriter(MapGeneratorFlags.getPath().toFile(), CHARSET)) {
      // Unknown word; unknown tag
      writer.write(String.format("%d %d%n", -1, -1));
      for (Long wordId : map.rowKeySet()) {
        Entry<Long, Integer> min = null;
        for (Entry<Long, Integer> entry : map.row(wordId).entrySet()) {
            if (min == null || min.getValue() < entry.getValue()) {
                min = entry;
            }
        }
        writer.write(String.format("%d %d%n", wordId, min.getKey()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
