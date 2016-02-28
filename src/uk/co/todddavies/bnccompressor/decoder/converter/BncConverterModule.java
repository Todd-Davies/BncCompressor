package uk.co.todddavies.bnccompressor.decoder.converter;

import com.google.inject.AbstractModule;

import edu.uchicago.lowasser.flaginjection.Flags;

public final class BncConverterModule extends AbstractModule {
  @Override
  protected void configure() {
    install(Flags.flagBindings(BncConverterFlags.class));
  }
}
