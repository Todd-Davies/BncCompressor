package uk.co.todddavies.bnccompressor.bnc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.file.Path;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;

import edu.uchicago.lowasser.flaginjection.Flags;

public final class BncModule extends AbstractModule {
  
  @Override
  protected void configure() {
    install(Flags.flagBindings(BncFlags.class));
    bind(BncIterator.class).to(BncIteratorImpl.class);
  }
  
  @Provides
  @BncPath
  Path bncPathProvider() {
    return BncFlags.getBncPath();
  }
    
  @BindingAnnotation @Retention(RetentionPolicy.RUNTIME)
  public @interface BncPath {}
}
