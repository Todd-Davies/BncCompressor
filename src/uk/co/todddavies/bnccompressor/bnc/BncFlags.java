package uk.co.todddavies.bnccompressor.bnc;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;

import edu.uchicago.lowasser.flaginjection.Flag;
import edu.uchicago.lowasser.flaginjection.FlagsClass;

public class BncFlags extends FlagsClass {

  private static Path bncPath;
  
  @Inject
  private BncFlags(
      @Flag(name="bncPath", description="The path to the BNC") String bncPath) {
    BncFlags.bncPath = Paths.get(bncPath);
  }

  static Path getBncPath() {
    return bncPath;
  }
}
