package uk.co.todddavies.bnccompressor.tagmapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.file.Path;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;

import edu.uchicago.lowasser.flaginjection.Flags;

public final class MapGeneratorModule extends AbstractModule {
  
  @Override
  protected void configure() {
    install(Flags.flagBindings(MapGeneratorFlags.class));
  }
  
  @Provides
  @OutPath
  Path outputPathProvider() {
    return MapGeneratorFlags.getPath();
  }
    
  @BindingAnnotation @Retention(RetentionPolicy.RUNTIME)
  public @interface OutPath {}
}
