package uk.co.todddavies.bnccompressor.tagmapping;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;

import edu.uchicago.lowasser.flaginjection.Flag;
import edu.uchicago.lowasser.flaginjection.FlagsClass;

class MapGeneratorFlags extends FlagsClass {

  private static Path path;
  
  @Inject
  private MapGeneratorFlags(
      @Flag(name="outPath", description="The path to write the output mapping to") String path) {
    MapGeneratorFlags.path = Paths.get(path);
  }

  static Path getPath() {
    return path;
  }  
}
