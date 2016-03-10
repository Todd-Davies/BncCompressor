package uk.co.todddavies.bnccompressor.bnc;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Stack;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import uk.co.todddavies.bnccompressor.TokenTag;
import uk.co.todddavies.bnccompressor.bnc.BncModule.BncPath;
import uk.co.todddavies.bnccompressor.flags.BncGlobalFlagsModule.Quiet;

final class BncIteratorImpl implements BncIterator {

  private final Stack<Path> bncDirectories;
  private final boolean quiet;
  private BncReader currentReader;
  
  private ImmutableList<TokenTag> nextSentence = null;
  
  @Inject
  private BncIteratorImpl(@BncPath Path bncPath, @Quiet boolean quiet) {
    bncDirectories = new Stack<>();
    bncDirectories.push(bncPath);
    currentReader = null;
    this.quiet = quiet;
    next();
  }
  
  @Override
  public void close() throws IOException {
    if (currentReader != null) {
      currentReader.close();
      currentReader = null;
      bncDirectories.clear();
    }
  }

  @Override
  public boolean hasNext() {
    return nextSentence != null;
  }

  @Override
  public ImmutableList<TokenTag> next() {
    ImmutableList<TokenTag> sentence = null;
    while(((currentReader != null && currentReader.hasNext()) || !bncDirectories.isEmpty())
        && sentence == null) {
      sentence = nextSentence();
      if (BncFlags.shouldStripLong()) {
        if (sentence.size() > BncFlags.getStripLong()) {
          sentence = null;
        }
      }
      if (sentence != null && BncFlags.shouldStripShort()) {
        if (sentence.size() < BncFlags.getStripShort()) {
          sentence = null;
        }
      }
    }
    ImmutableList<TokenTag> output = nextSentence;
    nextSentence = sentence;
    return output;
  }
  
  private ImmutableList<TokenTag> nextSentence() {
    if (currentReader == null || !currentReader.hasNext()) {
      try {
        Path nextPath = null;
        while(nextPath == null || nextPath.toFile().isDirectory()) {
          if (bncDirectories.isEmpty()) return null;
          else nextPath = bncDirectories.pop();
          if (nextPath.toFile().isDirectory()) {
            try(DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(nextPath)) {
              for (Path file : stream) {
                bncDirectories.add(file);
              }
            }
          }
        }
        if (currentReader != null) currentReader.close();
        if (!quiet) {
          System.out.printf("Opening %s\n", nextPath.toUri());
        }
        currentReader = new BncReader(nextPath);
      } catch (IOException e) {
        // Convert to runtime exception to be caught higher up
        throw new RuntimeException(e);
      }
    }
    return currentReader.next();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
