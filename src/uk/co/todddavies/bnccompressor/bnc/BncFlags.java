package uk.co.todddavies.bnccompressor.bnc;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nullable;

import com.google.inject.Inject;

import edu.uchicago.lowasser.flaginjection.Flag;
import edu.uchicago.lowasser.flaginjection.FlagsClass;

public class BncFlags extends FlagsClass {

  private static Path bncPath;
  private static int stripLong = -1, stripShort = -1;
  
  @Inject
  private BncFlags(
      @Flag(name="bncPath", description="The path to the BNC") String bncPath,
      @Nullable
      @Flag(name="stripLong", optional=true, description="The length of which to strip long sentences") Integer stripLong,
      @Nullable
      @Flag(name="stripShort", optional=true, description="The length of which to strip short sentences") Integer stripShort) {
    BncFlags.bncPath = Paths.get(bncPath);
    if (stripLong != null) {
      BncFlags.stripLong = stripLong;
    }
    if (stripShort != null) {
      BncFlags.stripShort = stripShort;
    }
  }

  static Path getBncPath() {
    return bncPath;
  }
 
  static int getStripLong() {
    return stripLong;
  }
  
  static boolean shouldStripLong() {
    return stripLong > -1;
  }
  
  static int getStripShort() {
    return stripShort;
  }
  
  static boolean shouldStripShort() {
    return stripShort > -1;
  }
}
