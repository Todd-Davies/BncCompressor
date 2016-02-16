package uk.co.todddavies.bnccompressor;

public final class WordTag {

  public final String word, tag;
  
  private WordTag(String word, String tag) {
    this.word = word;
    this.tag = tag;
  }
  
  public String toString() {
    return String.format("(%s,%s)", word, tag);
  }
  
  public static WordTag from(String bncLine) {
    int index = bncLine.indexOf("!!");
    return new WordTag(bncLine.substring(index + 2), bncLine.substring(0, index));
  }
  
  public static WordTag from(String word, String tag) {
    return new WordTag(word, tag);
  }  
}
