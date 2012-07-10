package com.abhyrama;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    
    char first = chars[0];

    Node current;
    if (root.containsKey(first)) {
      current = root.get(first);
    } else {
      Node node = new Node(first, false);
      root.put(first, node);
      current = node;
    }

    for (int i = 1; i < chars.length; ++i) {
      char aChar = chars[i];

      if (current.children.containsKey(aChar)) {
        current = current.children.get(aChar);
      } else {
        Node node = new Node(aChar, false);
        current.children.put(aChar, node);
        current = node;
      }
    }

    current.ends = true;
  }

  /**
   * Checks as to whether the word is present in the Trie or not.
   * @param word the word to be searched in the Trie.
   * @return a boolean indicating whether this word is present in the Trie or not.
   */
  public boolean contains(String word) {
    if (word.equals("")) {
      return false;
    }

    char[] chars = word.toCharArray();

    char first = chars[0];

    if (!root.containsKey(first)) {
      return false;
    }

    Node current = root.get(first);

    for (int i = 1; i < chars.length; ++i) {
      char aChar = chars[i];

      if (!current.children.containsKey(aChar)) {
        return false;
      }

      current = current.children.get(aChar);
    }

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

    if (!root.containsKey(first)) {
      return words;
    }

    Node current = root.get(first);
    
    String part = current.letter + "";

    for (int i = 1; i < chars.length; ++i) {
      char aChar = chars[i];

      if (!current.children.containsKey(aChar)) {
        return words;
      }

      current = current.children.get(aChar);
      part = part + current.letter;
    }

    return formWords(current, part, words);
  }

  private List<String> formWords(Node node, String part, List<String> words) {
    if (node.ends) {
      words.add(part);
      
      if (node.children.size() == 0) {
        return words;
      }
    }
    
    for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
      Node _node = entry.getValue();
      formWords(entry.getValue(), part + _node.letter, words);
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

  public static void main(String[] args) {
    Trie trie = new Trie();
    //trie.add("cat");
    trie.add("car");
    trie.add("c");
    trie.add("cart");
    trie.add("cartier");
    trie.add("carter");
    trie.add("cgombo");
    //trie.add("cam");
    System.out.println(trie.get("cart"));
  }
}
