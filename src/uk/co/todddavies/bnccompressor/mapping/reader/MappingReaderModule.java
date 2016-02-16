package uk.co.todddavies.bnccompressor.mapping.reader;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Scanner;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;

import edu.uchicago.lowasser.flaginjection.Flags;

public final class MappingReaderModule extends AbstractModule {
  
  private static ImmutableMap<String, Long> wordMap, tagMap;
  
  private static ImmutableMap<String, Long> readFile(File file) {
    try(Scanner wordsInput = new Scanner(file)) {
      ImmutableMap.Builder<String, Long> words = ImmutableMap.builder();
      while(wordsInput.hasNextLine()) {
        String[] line = wordsInput.nextLine().split(" ");
        words.put(line[0], Long.parseLong(line[1]));
      }
      return words.build();
    } catch (IOException e) {
      // Convert to runtime exception
      throw new RuntimeException(e);
    }
  }
  
  private static void readMaps() {
    wordMap = readFile(MappingReaderFlags.getWordFile());
    tagMap = readFile(MappingReaderFlags.getTagFile());
  }
  
  @Override
  protected void configure() {
    install(Flags.flagBindings(MappingReaderFlags.class));
  }
  
  @WordMap
  @Provides
  ImmutableMap<String, Long> provideWordMap() {
    if (wordMap == null) {
      readMaps();
    }
    return wordMap;
  }
  
  @TagMap
  @Provides
  ImmutableMap<String, Long> provideTagMap() {
    if (tagMap == null) {
      readMaps();
    }
    return tagMap;
  }
  
  @BindingAnnotation @Retention(RetentionPolicy.RUNTIME)
  public @interface WordMap {}
  
  @BindingAnnotation @Retention(RetentionPolicy.RUNTIME)
  public @interface TagMap {}
}
