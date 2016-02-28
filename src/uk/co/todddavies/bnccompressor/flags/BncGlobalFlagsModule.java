package uk.co.todddavies.bnccompressor.flags;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;

import edu.uchicago.lowasser.flaginjection.Flags;

public final class BncGlobalFlagsModule extends AbstractModule {
  
  @Override
  protected void configure() {
    install(Flags.flagBindings(BncGlobalFlags.class));
  }
  
  @Provides
  @Quiet
  boolean provideQuiet() {
    return BncGlobalFlags.isQuiet();
  }
    
  @BindingAnnotation @Retention(RetentionPolicy.RUNTIME)
  public @interface Quiet {}
}
