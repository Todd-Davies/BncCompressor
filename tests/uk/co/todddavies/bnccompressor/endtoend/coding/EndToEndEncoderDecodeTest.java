package uk.co.todddavies.bnccompressor.endtoend.coding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import edu.uchicago.lowasser.flaginjection.Flags;
import uk.co.todddavies.bnccompressor.TokenTag;
import uk.co.todddavies.bnccompressor.bnc.BncIterator;
import uk.co.todddavies.bnccompressor.decoder.reader.BncDecoder;
import uk.co.todddavies.bnccompressor.decoder.reader.BncDecoderModule;
import uk.co.todddavies.bnccompressor.encoder.BncEncoder;
import uk.co.todddavies.bnccompressor.encoder.BncEncoderModule;
import uk.co.todddavies.bnccompressor.flags.BncGlobalFlagsModule;
import uk.co.todddavies.bnccompressor.test.FakeBncIteratorModule;

public class EndToEndEncoderDecodeTest {  
  
  private final String TEMP_BNC_PATH = "C:\\temp\\tests";
  
  private Injector createInjector() {
    String[] args = {"-quiet", "false", "-outputFile", TEMP_BNC_PATH, "-inputFile", TEMP_BNC_PATH};
    return Flags.bootstrapFlagInjector(args,
        EndToEndEncoderDecodeTest.class.getName(),
        ImmutableList.<String>of(
            "uk.co.todddavies.bnccompressor.decoder.reader",
            "uk.co.todddavies.bnccompressor.encoder",
            "uk.co.todddavies.bnccompressor.flags"),
        new BncEncoderModule(),
        new BncDecoderModule(),
        new FakeBncIteratorModule(),
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
  public void testEncodeDecode() {
    Injector inj = createInjector();
    // Encode the bnc
    BncEncoder encoder = inj.getInstance(BncEncoder.class);
    encoder.encode();
    // Get a decoder
    BncIterator decoder = inj.getInstance(BncDecoder.class);
    // Get the fake bnc
    BncIterator fakeBnc = inj.getInstance(BncIterator.class);
    // Check they're the same
    while (decoder.hasNext() || fakeBnc.hasNext()) {
      if (decoder.hasNext() != fakeBnc.hasNext()) fail();
      else {
        Iterator<TokenTag> fake = fakeBnc.next().iterator(), decoded = decoder.next().iterator();
        while(fake.hasNext() && decoded.hasNext()) {
          assertEquals(fake.next(), decoded.next());
        }
        assertEquals(fake.hasNext(), decoded.hasNext());
      }
    }
  }
}
