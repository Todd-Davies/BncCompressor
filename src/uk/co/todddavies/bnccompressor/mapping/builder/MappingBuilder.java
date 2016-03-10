package uk.co.todddavies.bnccompressor.mapping.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Injector;

import edu.uchicago.lowasser.flaginjection.Flags;
import uk.co.todddavies.bnccompressor.TokenTag;
import uk.co.todddavies.bnccompressor.bnc.BncIterator;
import uk.co.todddavies.bnccompressor.bnc.BncModule;
import uk.co.todddavies.bnccompressor.flags.BncGlobalFlagsModule;

public final class MappingBuilder {
  
  private static final Charset CHARSET = Charset.defaultCharset();
  
  private long wordCount = 1, tagCount = 1;
  private final HashMap<String, Long> wordMap, tagMap;
  private final BncIterator iterator;
  
  @Inject
  private MappingBuilder(BncIterator iterator) {
    wordMap = new HashMap<>();
    tagMap = new HashMap<>();
    this.iterator = iterator;
  }
  
  public static void main(String[] args) {    
    // Create the injector
    Injector injector = Flags.bootstrapFlagInjector(args,
        MappingBuilder.class.getName(),
        ImmutableList.<String>of(
            "uk.co.todddavies.bnccompressor.bnc.flags",
            "uk.co.todddavies.bnccompressor.bnc",
            "uk.co.todddavies.bnccompressor.mapping.builder"),
        new BncModule(),
        new BncGlobalFlagsModule(),
        new MappingBuilderModule());
    
    MappingBuilder mappingBuilder = injector.getInstance(MappingBuilder.class);
    mappingBuilder.buildMap();
    mappingBuilder.writeMaps();
    log("Done!");
  }
  
  private static void writeMap(File outputFile, Map<String, Long> map) {
    try (BufferedWriter writer = Files.newWriter(outputFile, CHARSET)) {
      for (Entry<String, Long> entry : map.entrySet()) {
        writer.write(String.format("%s %d%n", entry.getKey(), entry.getValue()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private static void log(String text) {
    if (!MappingBuilderFlags.isQuiet()) {
      System.out.println(text);
    }
  }
  
  public void writeMaps() {
    log(String.format("Writing words to %s", MappingBuilderFlags.getWordFile().toURI()));
    writeMap(MappingBuilderFlags.getWordFile(), wordMap);
    log(String.format("Writing tags to %s", MappingBuilderFlags.getTagFile().toURI()));
    writeMap(MappingBuilderFlags.getTagFile(), tagMap);
  }

  public void buildMap() {
    log("Building word and tag maps...");
    while(iterator.hasNext()) {
      for (TokenTag wordTag : iterator.next()) {
        if (!wordMap.containsKey(wordTag.word)) wordMap.put(wordTag.word, wordCount++);
        String tag = MappingBuilderFlags.shouldCollapseTags() ? wordTag.tag.substring(0,2) : wordTag.tag;
        if (!tagMap.containsKey(tag)) tagMap.put(tag, tagCount++);
      }
    }
  }
  
 }
