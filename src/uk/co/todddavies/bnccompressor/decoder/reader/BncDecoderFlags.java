package uk.co.todddavies.bnccompressor.decoder.reader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;

import edu.uchicago.lowasser.flaginjection.Flag;
import edu.uchicago.lowasser.flaginjection.FlagsClass;

class BncDecoderFlags extends FlagsClass {
  
  private static Path bncInputPath;
  
  @Inject
  private BncDecoderFlags(
      @Flag(name="inputFile", description="The path to read the encoded bnc from") String inputPath) {
    BncDecoderFlags.bncInputPath = Paths.get(inputPath);
  }

  static File getInputFile() {
    return bncInputPath.toFile();
  }
}
