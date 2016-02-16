package uk.co.todddavies.bnccompressor.mapping.reader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;

import edu.uchicago.lowasser.flaginjection.Flag;
import edu.uchicago.lowasser.flaginjection.FlagsClass;

public class MappingReaderFlags extends FlagsClass {

  private static Path tagPath, wordPath;
  
  @Inject
  private MappingReaderFlags(
      @Flag(name="tagMapFile", description="The path to read the output tag mapping from") String tagPath,
      @Flag(name="wordMapFile", description="The path to read the word mapping from") String wordPath) {
    MappingReaderFlags.tagPath = Paths.get(tagPath);
    MappingReaderFlags.wordPath = Paths.get(wordPath);
  }

  static File getTagFile() {
    return tagPath.toFile();
  }
  
  static File getWordFile() {
    return wordPath.toFile();
  }
}
