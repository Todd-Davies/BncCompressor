package uk.co.todddavies.bnccompressor.test;

import java.io.IOException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

import uk.co.todddavies.bnccompressor.TokenTag;
import uk.co.todddavies.bnccompressor.bnc.BncIterator;
//import uk.co.todddavies.bnccompressor.endtoend.coding.EndToEndEncoderDecodeTest.FakeBncIterator;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.TagMap;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.WordMap;

public final class FakeBncIteratorModule extends AbstractModule {
  public final ImmutableList<ImmutableList<TokenTag>> FAKE_BNC = ImmutableList.of(
      ImmutableList.of(
          TokenTag.from("NN1!!wait"),
          TokenTag.from("PUN!!,"),
          TokenTag.from("NN1!!wait"),
          TokenTag.from("PUN!!,"),
          TokenTag.from("NN1!!wait"),
          TokenTag.from("PUN!!!")),
      ImmutableList.of(
          TokenTag.from("CJC!!And"),
          TokenTag.from("PUN!!,"),
          TokenTag.from("DTQ!!what"),
          TokenTag.from("VBZ!!'s"),
          TokenTag.from("AT0!!the"),
          TokenTag.from("AJ0!!other"),
          TokenTag.from("PNI!!one"),
          TokenTag.from("PUN!!.")));
  
  public static final ImmutableMap<String, Long> TAG_MAP = ImmutableMap.<String, Long>builder()
      .put("AJ0",1L).put("CJC",2L).put("DTQ",3L).put("NN1",4L)
      .put("PNI",5L).put("PUN",6L).put("VBZ",7L).put("AT0",8L).build();
  
  public static final ImmutableMap<String, Long> WORD_MAP = ImmutableMap.<String, Long>builder()
      .put("!",1L).put("'s",2L).put(",",3L).put(".",4L).put("And",5L)
      .put("one",6L).put("other",7L).put("the",8L).put("wait",9L).put("what", 10L).build();
  
  private static class FakeBncIterator implements BncIterator {
    private final ImmutableList<ImmutableList<TokenTag>> fakeBnc;
    
    private int index = 0;
    FakeBncIterator(ImmutableList<ImmutableList<TokenTag>> fakeBnc) {
      this.fakeBnc = fakeBnc;
    }
    
    @Override
    public boolean hasNext() {
      return index < fakeBnc.size();
    }
    @Override
    public ImmutableList<TokenTag> next() {
      if (hasNext()) {
        return fakeBnc.get(index++);
      } else return null;
    }

    @Override public void remove() { throw new UnsupportedOperationException(); }
    @Override public void close() throws IOException {}
  }
  
  @Override
  protected void configure() {
    TypeLiteral<ImmutableMap<String, Long>> mapType = new TypeLiteral<ImmutableMap<String, Long>>() {};
    bind(mapType).annotatedWith(TagMap.class).toInstance(TAG_MAP);
    bind(mapType).annotatedWith(WordMap.class).toInstance(WORD_MAP);
  }
  
  @Provides
  BncIterator provideFakeBncIterator() {
    return new FakeBncIterator(FAKE_BNC);
  }
}
