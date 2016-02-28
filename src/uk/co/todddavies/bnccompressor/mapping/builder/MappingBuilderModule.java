package uk.co.todddavies.bnccompressor.mapping.builder;

import com.google.inject.AbstractModule;

import edu.uchicago.lowasser.flaginjection.Flags;

public final class MappingBuilderModule extends AbstractModule {
  
  @Override
  protected void configure() {
    install(Flags.flagBindings(MappingBuilderFlags.class));
  }
}
