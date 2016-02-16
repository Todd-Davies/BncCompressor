package uk.co.todddavies.bnccompressor.mapping.builder;

import java.nio.file.Path;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import edu.uchicago.lowasser.flaginjection.Flags;

public final class MappingBuilderModule extends AbstractModule {
  
  @Override
  protected void configure() {
    install(Flags.flagBindings(MappingBuilderFlags.class));
  }
  
  @Provides
  Path provideBncPath() {
    return MappingBuilderFlags.getPath();
  }
}
