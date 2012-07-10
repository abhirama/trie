package com.abhyrama;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * An implementation of the Trie data structure.
 */
public class Trie {
  private Map<Character, Node> root = new HashMap<Character, Node>();

  /**
   * Adds the passed in word to the Trie.
   * @param word word to be added to the Trie.
   */
  public void add(String word) {
    char[] chars = word.toCharArray();

    //Get the first letter in the word and see whether it exists as an entry in the root.
    char first = chars[0];

    Node current;
    if (root.containsKey(first)) {
      //Root already contains the letter, hence treat that as the current element.
      current = root.get(first);
    } else {
      //Letter is not already present as an entry in the root, hence create it and add it to root.
      Node node = new Node(first, false);
      root.put(first, node);
      current = node;
    }

    //Iterate over the other letters.
    for (int i = 1; i < chars.length; ++i) {
      char aChar = chars[i];

      if (current.children.containsKey(aChar)) {
        //This letter is already present in the tree as a child. Hence, just update the current to point to the node
        //represented by this letter.
        current = current.children.get(aChar);
      } else {
        //This letter is not present as a child of the current element, hence add it.
        Node node = new Node(aChar, false);
        current.children.put(aChar, node);
        current = node;
      }
    }

    //Our word ends here, mark the current node as the terminal node.
    current.ends = true;
  }

  /**
   * Checks as to whether the the sequence of letters exists in the Trie or not.
   * @param sequence the sequence to be searched in the Trie.
   * @return a boolean indicating whether this sequence is present in the Trie or not.
   */
  public boolean contains(String sequence) {
    char[] chars = sequence.toCharArray();

    char first = chars[0];

    //If the first letter is not present as a key in the root, there is no chance of this sequence being present in the trie.
    if (!root.containsKey(first)) {
      return false;
    }

    Node current = root.get(first);

    for (int i = 1; i < chars.length; ++i) {
      char aChar = chars[i];

      //This letter is not present in the trie, hence the trie does not have this sequence.
      if (!current.children.containsKey(aChar)) {
        return false;
      }

      current = current.children.get(aChar);
    }

    //We came out of the loop, it means that all the letters are there in the trie, in the appropriate order.
    return true;
  }

  /**
   * Get a list of all words in the Trie with the given prefix.
   * @param prefix prefix of the words to be searched.
   * @return a list of all words which have the passed in prefix.
   */
  public List<String> get(String prefix) {
    List<String> words = new LinkedList<String>();

    char[] chars = prefix.toCharArray();

    char first = chars[0];

    //First letter is not present as a key in the root, hence there cannot be any words in this trie corresponding to the
    //prefix.
    if (!root.containsKey(first)) {
      return words;
    }

    Node current = root.get(first);

    for (int i = 1; i < chars.length; ++i) {
      char aChar = chars[i];

      //No entry in the tire for this letter, hence there cannot be any words in the trie for the passed in prefix.
      if (!current.children.containsKey(aChar)) {
        return words;
      }

      current = current.children.get(aChar);
    }

    //At this point, we can be sure that there are words in the trie corresponding to the passed in prefix. Now plow down
    //the depths of the tree and form all the words corresponding to the prefix.
    return formWords(current, prefix, words);
  }

  private List<String> formWords(Node node, String prefix, List<String> words) {
    //A word can be formed at this point, hence add it to the container.
    if (node.ends) {
      words.add(prefix);

      //This branch of the tree ends here as there are no more children. Hence return.
      if (node.children.size() == 0) {
        return words;
      }
    }

    //Recursively go through all the children of the node and form words.
    for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
      Node _node = entry.getValue();
      formWords(entry.getValue(), prefix + _node.letter, words);
    }

    return words;
  }
  
  private static class Node {
    public char letter;
    public boolean ends;
    
    public Node(char letter, boolean ends) {
      this.letter = letter;
      this.ends = ends;
    }
    
    public Map<Character, Node> children = new HashMap<Character, Node>();
  }

  public static void main(String[] args) throws FileNotFoundException {
    Trie trie = new Trie();

    //File from this link - http://www.lextutor.ca/freq/lists_download/
    Scanner scanner = new Scanner(new File("1000_families.txt"));
    
    while (scanner.hasNext()) {
      String word = scanner.next().toLowerCase();
      trie.add(word);
    }
    
    System.out.println(trie.get(" "));
  }
}
