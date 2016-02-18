package uk.co.todddavies.bnccompressor.bnc;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Stack;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import uk.co.todddavies.bnccompressor.WordTag;
import uk.co.todddavies.bnccompressor.bnc.BncModule.BncPath;

final class BncIteratorImpl implements BncIterator {

  private Stack<Path> bncDirectories;
  private BncReader currentReader;
  
  @Inject
  private BncIteratorImpl(@BncPath Path bncPath) {
    bncDirectories = new Stack<>();
    bncDirectories.push(bncPath);
    currentReader = null;
  }
  
  @Override
  public void close() throws IOException {
    if (currentReader != null) {
      currentReader.close();
      currentReader = null;
      bncDirectories.clear();;
    }
  }

  @Override
  public boolean hasNext() {
    return (currentReader != null && currentReader.hasNext()) || !bncDirectories.isEmpty();
  }

  @Override
  public ImmutableList<WordTag> next() {
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
        System.out.printf("Opening %s\n", nextPath.toUri());
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
