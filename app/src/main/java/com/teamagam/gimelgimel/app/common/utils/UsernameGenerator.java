package com.teamagam.gimelgimel.app.common.utils;

import java.util.Random;

public class UsernameGenerator {

  private static final String SPACE = " ";
  private static final Random rand = new Random();
  private String[] mAnimals;
  private String[] mAdjectives;

  public UsernameGenerator(String[] animals, String[] adjectives) {
    mAnimals = animals;
    mAdjectives = adjectives;
  }

  private static <T> T pickRandom(T[] array) {
    return array[rand.nextInt(array.length)];
  }

  private static String capitalizeFirstLetters(String phrase) {
    StringBuilder builder = new StringBuilder();
    String[] words = phrase.split(SPACE);
    for (String word : words) {
      String capWord = capitalizeFirstLetter(word);
      appendWordWithSpace(builder, capWord);
    }
    return builder.toString().trim();
  }

  private static String capitalizeFirstLetter(String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1);
  }

  private static void appendWordWithSpace(StringBuilder builder, String word) {
    builder.append(word).append(SPACE);
  }

  public String generateUsername() {
    return capitalizeFirstLetters(pickRandomUsername());
  }

  private String pickRandomUsername() {
    return pickRandomAdjective() + SPACE + pickRandomAnimal();
  }

  private String pickRandomAnimal() {
    return pickRandom(mAnimals);
  }

  private String pickRandomAdjective() {
    return pickRandom(mAdjectives);
  }
}