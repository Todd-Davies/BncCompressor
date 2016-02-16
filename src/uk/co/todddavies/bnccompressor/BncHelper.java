package uk.co.todddavies.bnccompressor;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

import com.google.common.collect.ImmutableList;

public final class BncHelper {

  private BncHelper(){}

  /**
   * Returns a list of all the paths to BNC files in a directory.
   */
  public static ImmutableList<Path> getBncFiles(Path root) {
    return getBncFiles(root, ImmutableList.<Path>builder()).build();
  }
  
  private static ImmutableList.Builder<Path> getBncFiles(Path path, ImmutableList.Builder<Path> list) {
    try(DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(path)) {
      for (Path file : stream) {
        if (file.toFile().isDirectory()) {
          getBncFiles(file, list);
        } else {
          list.add(file);
        }
      }
    } catch (IOException e) {
      // Convert to runtime exception to be caught higher up
      throw new RuntimeException(e);
    }
    return list;
  }  
}
