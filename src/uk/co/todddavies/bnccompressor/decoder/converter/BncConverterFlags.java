package uk.co.todddavies.bnccompressor.decoder.converter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;

import edu.uchicago.lowasser.flaginjection.Flag;
import edu.uchicago.lowasser.flaginjection.FlagsClass;

class BncConverterFlags extends FlagsClass {
  
  private static Path outputPath;
  
  @Inject
  private BncConverterFlags(
      @Flag(name="outputFile", description="The path to write the converted file to") String outputPath) {
    BncConverterFlags.outputPath = Paths.get(outputPath);
  }

  static File getOutputFile() {
    return outputPath.toFile();
  }
}
