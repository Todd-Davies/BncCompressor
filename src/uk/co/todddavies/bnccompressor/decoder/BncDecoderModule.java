package uk.co.todddavies.bnccompressor.decoder;

import com.google.inject.AbstractModule;

import edu.uchicago.lowasser.flaginjection.Flags;
import uk.co.todddavies.bnccompressor.bnc.BncIterator;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule;

public final class BncDecoderModule extends AbstractModule {
  @Override
  protected void configure() {
    install(Flags.flagBindings(BncDecoderFlags.class));
    bind(BncIterator.class).to(BncDecoder.class);
  }
}
