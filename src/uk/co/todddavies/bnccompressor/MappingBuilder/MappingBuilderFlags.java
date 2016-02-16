package uk.co.todddavies.bnccompressor.MappingBuilder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nullable;

import com.google.inject.Inject;

import edu.uchicago.lowasser.flaginjection.Flag;
import edu.uchicago.lowasser.flaginjection.FlagsClass;

public class MappingBuilderFlags extends FlagsClass {

  private static Path path, tagPath, wordPath;
  private static boolean quiet = false, collapseTags = false;
  
  @Inject
  private MappingBuilderFlags(@Flag(name="bncPath", description="The path to the bnc corpus") String path,
      @Flag(name="outputTagMapFile", description="The path to write the output tag mapping to") String tagPath,
      @Flag(name="outputWordMapFile", description="The path to write the output word mapping to") String wordPath,
      @Nullable
      @Flag(name="collapseTags", description="Only look at the first two characters of the tags") Boolean collapseTags,
      @Nullable
      @Flag(name="quiet", description="Print to stdout or not") Boolean quiet) {
    MappingBuilderFlags.path = Paths.get(path);
    MappingBuilderFlags.tagPath = Paths.get(tagPath);
    MappingBuilderFlags.wordPath = Paths.get(wordPath);
    if (quiet != null) {
      MappingBuilderFlags.quiet = quiet;
    }
    if (collapseTags != null) {
      MappingBuilderFlags.collapseTags = collapseTags;
    }
  }

  static Path getPath() {
    return path;
  }
  
  static File getTagFile() {
    return tagPath.toFile();
  }
  
  static File getWordFile() {
    return wordPath.toFile();
  }
  
  static boolean isQuiet() {
    return quiet;
  }
  
  static boolean shouldCollapseTags() {
    return collapseTags;
  }
  
}
