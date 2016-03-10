package uk.co.todddavies.bnccompressor.bnc;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Scanner;

import com.google.common.collect.ImmutableList;

import uk.co.todddavies.bnccompressor.TokenTag;

final class BncReader implements Iterator<ImmutableList<TokenTag>>, Closeable {

  private static final String SENT_START = "<p>";
  
  private Scanner fileScanner;
  
  BncReader(Path filePath) throws FileNotFoundException {
    fileScanner = new Scanner(filePath.toFile());
    // Move the scanner past the first <p> tag
    if (fileScanner.hasNextLine()) {
      fileScanner.nextLine();
    }
  }
  
  @Override
  public boolean hasNext() {
    return fileScanner.hasNextLine();
  }

  @Override
  public ImmutableList<TokenTag> next() {
    ImmutableList.Builder<TokenTag> out = ImmutableList.builder();
    while(fileScanner.hasNextLine()) {
      String nextLine = fileScanner.nextLine();
      if (nextLine.substring(0, 3).equals(SENT_START)) {
        break;
      } else {
        out.add(TokenTag.from(nextLine));
      }
    }
    return out.build();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() throws IOException {
    fileScanner.close();
  }

}
