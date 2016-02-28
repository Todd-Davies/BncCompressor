package uk.co.todddavies.bnccompressor.endtoend.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Modules;

import edu.uchicago.lowasser.flaginjection.Flags;
import uk.co.todddavies.bnccompressor.flags.BncGlobalFlagsModule;
import uk.co.todddavies.bnccompressor.mapping.builder.MappingBuilder;
import uk.co.todddavies.bnccompressor.mapping.builder.MappingBuilderModule;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.TagMap;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.WordMap;
import uk.co.todddavies.bnccompressor.test.FakeBncIteratorModule;

public class MapGeneratorReaderTest {
  
  private static final String TEMP_TAG_MAP_PATH = "C:\\temp\\tag-map";
  private static final String TEMP_WORD_MAP_PATH = "C:\\temp\\word-map";
  
  private Injector createInjector() {
    String[] args = {"-quiet", "false",
        "-tagMapFile", TEMP_TAG_MAP_PATH, "-wordMapFile", TEMP_WORD_MAP_PATH,
        "-outputTagMapFile", TEMP_TAG_MAP_PATH, "-outputWordMapFile", TEMP_WORD_MAP_PATH,
        "-collapseTags", "false"};
    return Flags.bootstrapFlagInjector(args,
        MapGeneratorReaderTest.class.getName(),
        ImmutableList.<String>of(
            "uk.co.todddavies.bnccompressor.mapping",
            "uk.co.todddavies.bnccompressor.flags"),
        Modules.override(new FakeBncIteratorModule()).with(new MappingReaderModule()),
        new MappingBuilderModule(),
        new BncGlobalFlagsModule());
  }
  
  @Test
  public void testCreateInjector() {
    try {
      createInjector();
    } catch (Exception e) {
      fail("No exception expected: " + e.getMessage());
    }
  }
  
  @Test
  public void testMapGenRead() {
    Injector inj = createInjector();
    // Create the generator and generate the map
    MappingBuilder builder = inj.getInstance(MappingBuilder.class);
    builder.buildMap();
    builder.writeMaps();
    // Obtain the read tag and word maps
    TypeLiteral<ImmutableMap<String, Long>> mapType = new TypeLiteral<ImmutableMap<String, Long>>() {};
    ImmutableMap<String, Long> tagMap = inj.getInstance(Key.get(mapType, TagMap.class));
    ImmutableMap<String, Long> wordMap = inj.getInstance(Key.get(mapType, WordMap.class));
    // Make sure they're the same
    assertEquals(tagMap.keySet(), FakeBncIteratorModule.TAG_MAP.keySet());
    assertEquals(ImmutableSet.builder().addAll(tagMap.values()).build(),
        ImmutableSet.builder().addAll(FakeBncIteratorModule.TAG_MAP.values()).build());
    assertEquals(wordMap.keySet(), FakeBncIteratorModule.WORD_MAP.keySet());
    assertEquals(ImmutableSet.builder().addAll(wordMap.values()).build(),
        ImmutableSet.builder().addAll(FakeBncIteratorModule.WORD_MAP.values()).build());
  }
}
