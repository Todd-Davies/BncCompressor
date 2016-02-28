package uk.co.todddavies.bnccompressor.decoder.reader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;

import edu.uchicago.lowasser.flaginjection.Flags;
import uk.co.todddavies.bnccompressor.bnc.BncIterator;

public final class BncDecoderModule extends AbstractModule {
  @Override
  protected void configure() {
    install(Flags.flagBindings(BncDecoderFlags.class));
    bind(BncIterator.class).annotatedWith(BncDecoderIterator.class).to(BncDecoder.class);
  }
  
  @BindingAnnotation @Retention(RetentionPolicy.RUNTIME)
  public @interface BncDecoderIterator {}
}
