package uk.co.todddavies.bnccompressor;

/**
 * Immutable class representing a token-tag pair
 */
public final class TokenTag {

  public final String word, tag;
  
  private TokenTag(String word, String tag) {
    this.word = word;
    this.tag = tag;
  }
  
  public String toString() {
    return String.format("(%s,%s)", word, tag);
  }
  
  public static TokenTag from(String bncLine) {
    int index = bncLine.indexOf("!!");
    return new TokenTag(bncLine.substring(index + 2), bncLine.substring(0, index));
  }
  
  public static TokenTag from(String word, String tag) {
    return new TokenTag(word, tag);
  }
  
  @Override
  public boolean equals(Object other) {
    if (other instanceof TokenTag) {
      TokenTag wt = (TokenTag) other;
      return word.equals(wt.word) && tag.equals(wt.tag); 
    } else return false;
  }
  
  @Override
  public int hashCode() {
	  return (word + tag).hashCode();
  }
}
