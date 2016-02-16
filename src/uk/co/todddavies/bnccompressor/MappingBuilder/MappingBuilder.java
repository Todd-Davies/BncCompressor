package uk.co.todddavies.bnccompressor.MappingBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Injector;

import edu.uchicago.lowasser.flaginjection.Flags;
import uk.co.todddavies.bnccompressor.BncHelper;
import uk.co.todddavies.bnccompressor.BncReader;
import uk.co.todddavies.bnccompressor.WordTag;

public final class MappingBuilder {
  
  private static final Charset CHARSET = Charset.defaultCharset();
  
  private long wordCount = 1, tagCount = 1;
  private HashMap<String, Long> wordMap, tagMap;
  
  @Inject
  private MappingBuilder() {
    wordMap = new HashMap<>();
    tagMap = new HashMap<>();
  }
  
  public static void main(String[] args) {    
    // Create the injector
    Injector injector = Flags.bootstrapFlagInjector(args,
        MappingBuilder.class.getName(),
        ImmutableList.<String>of("uk.co.todddavies.bnccompressor"),
        new MappingBuilderModule());
    
    MappingBuilder mappingBuilder = injector.getInstance(MappingBuilder.class);
    mappingBuilder.buildMap();
    log(String.format("Writing words to %s", MappingBuilderFlags.getWordFile().toURI()));
    writeMap(MappingBuilderFlags.getWordFile(), mappingBuilder.wordMap);
    log(String.format("Writing tags to %s", MappingBuilderFlags.getTagFile().toURI()));
    writeMap(MappingBuilderFlags.getTagFile(), mappingBuilder.tagMap);
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

  public void buildMap() {
    for(Path bncFile : BncHelper.getBncFiles(MappingBuilderFlags.getPath())) {
      log(String.format("Processing %s", bncFile.toUri()));
      try(BncReader reader = new BncReader(bncFile)) {
        while(reader.hasNext()) {
          for (WordTag wordTag : reader.next()) {
             if (!wordMap.containsKey(wordTag.word)) wordMap.put(wordTag.word, wordCount++);
             String tag = MappingBuilderFlags.shouldCollapseTags() ? wordTag.tag.substring(0,2) : wordTag.tag;
             if (!tagMap.containsKey(tag)) tagMap.put(tag, tagCount++);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
 }
