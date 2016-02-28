package uk.co.todddavies.bnccompressor.flags;

import javax.annotation.Nullable;

import com.google.inject.Inject;

import edu.uchicago.lowasser.flaginjection.Flag;
import edu.uchicago.lowasser.flaginjection.FlagsClass;

class BncGlobalFlags extends FlagsClass {

  private static boolean quiet = false;
  
  @Inject
  private BncGlobalFlags(
      @Nullable
      @Flag(name="quiet", description="Print to stdout or not") Boolean quiet) {
    if (quiet != null) {
      BncGlobalFlags.quiet = quiet;
    }
  }

  static boolean isQuiet() {
    return quiet;
  }
}
