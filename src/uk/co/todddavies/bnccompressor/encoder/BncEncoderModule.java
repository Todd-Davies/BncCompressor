package uk.co.todddavies.bnccompressor.encoder;

import com.google.inject.AbstractModule;

import edu.uchicago.lowasser.flaginjection.Flags;

public final class BncEncoderModule extends AbstractModule {
  @Override
  protected void configure() {
    install(Flags.flagBindings(BncEncoderFlags.class));
  }
}
