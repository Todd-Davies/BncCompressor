package uk.co.todddavies.bnccompressor.decoder.rules;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Injector;

import edu.uchicago.lowasser.flaginjection.Flags;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.TagMap;
import uk.co.todddavies.bnccompressor.mapping.reader.MappingReaderModule.WordMap;
import uk.co.todddavies.bnccompressor.tagmapping.TagMapGenerator;

public final class RuleDecoder {

  public static void main(String[] args) {
    // Create the injector
    Injector injector = Flags.bootstrapFlagInjector(args,
        TagMapGenerator.class.getName(),
        ImmutableList.<String>of(
            "uk.co.todddavies.bnccompressor.mapping.reader",
            "uk.co.todddavies.bnccompressor.decoder.rules"),
        new MappingReaderModule());
    
    RuleDecoder decoder = injector.getInstance(RuleDecoder.class);
    decoder.decode(System.in);
  }

  private final ImmutableBiMap<Long, String> wordMap, tagMap;
  
  @Inject
  private RuleDecoder(@TagMap ImmutableMap<String, Long> tagMap,
      @WordMap ImmutableMap<String, Long> wordMap) throws FileNotFoundException {
    this.tagMap = ImmutableBiMap.<String, Long>builder().putAll(tagMap).build().inverse();
    this.wordMap = ImmutableBiMap.<String, Long>builder().putAll(wordMap).build().inverse();
  }
  
  private String parsePrecondition(String pre) {
    try {
      if (pre.length() >= 9) {
        boolean tag = true;
        if (pre.substring(0, 3).equals("TAG")) {
          tag = true;
        } else if (pre.substring(0, 4).equals("WORD")) {
          tag = false;
        } else {
          return "";
        }
        int lastIndexOfEquals = pre.lastIndexOf("=");
        long value = Long.parseLong(pre.substring(1 + lastIndexOfEquals));
        if (value == -1) return pre.substring(0, lastIndexOfEquals) + "SB";
        else return pre.substring(0, lastIndexOfEquals) + (tag ? (tagMap.containsKey(value) ? tagMap.get(value) : "UNK") : (wordMap.containsKey(value) ? wordMap.get(value) : "UNK"));
      }
    } catch(Exception e) {}
    return "";
  }
  
  void decode(InputStream in) {
    try (Scanner input = new Scanner(in)) {
      while(input.hasNextLine()) {
        try {
          String line = input.nextLine();
          String[] spaceSplit = line.split(" ");
          if (spaceSplit.length == 3) {
            if (!spaceSplit[1].equals("=")) continue;
            String[] preconds = spaceSplit[0].split(",");
            for (String precon : preconds) {
              System.out.printf(parsePrecondition(precon) + " ");
            }
            System.out.println("== " + tagMap.get(Long.parseLong(spaceSplit[2])));
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
