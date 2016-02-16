package uk.co.todddavies.bnccompressor.encoder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;

import edu.uchicago.lowasser.flaginjection.Flag;
import edu.uchicago.lowasser.flaginjection.FlagsClass;

class BncEncoderFlags extends FlagsClass {
  
  private static Path outputPath;
  
  @Inject
  private BncEncoderFlags(
      @Flag(name="outputFile", description="The path to write the bnc to") String outputPath) {
    BncEncoderFlags.outputPath = Paths.get(outputPath);
  }

  static File getOutputFile() {
    return outputPath.toFile();
  }
}
