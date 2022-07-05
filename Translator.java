import java.util.Map;
import java.util.HashMap;

public class Translator {
    private static final Map<String, Map<String, String>> dict = new HashMap<>();//root word, <lang, word>
    private static final Map<String, String> roots = new HashMap<>(); //word, root word
    private static final Map<String, Integer> rank = new HashMap<>(); //optimize union by rank
    
    public Translator() {}
    
    public static void add(String inputLang, String inputWord, String outputLang, String outputWord) {
        //put word1 and word2 to roots, dict and rank
        init(lang1, word1);
        init(lang2, word2);
        //get the root of word1 and word2
        String root1 = find(word1);
        String root2 = find(word2);
        //if root1 != root2 => union
        if (!root1.equals(root2)) {
            int rank1 = rank.get(root1);
            int rank2 = rank.get(root2);
            if (rank1 > rank2) {
                union(root2, root1);
            } else if (rank1 < rank2) {
                union(root1, root2);
            } else {
                union(root1, root2);
                rank.put(root2, rank2 + 1);
            }
        }
    }
    public static String get(String inputLang, String inputWord, String outputLang) {
        String root = find(inputWord);
        if (root == null) return "";
        return dict.get(root).get(outputLang);
    }
    private static void init(String lang, String word) {
        String s = roots.get(word);
        if (s == null) {
            roots.put(word, word);
            Map<String, String> entry = new HashMap<>();
            entry.put(lang, word);
            dict.put(word, entry);
            rank.put(word, 1);
        }
    }
    private static String find(String word) {
        if (word == null || word.equals("")) return null;
        String root = roots.get(word);
        if (root == null) return null;
        if (!word.equals(root)) roots.put(word, find(root));
        return roots.get(word);
    }
    private static void union(String child, String root) {
        roots.put(child, root);
        dict.get(root).putAll(dict.get(child));
        dict.remove(child);
        rank.remove(child);
    }
    
    public Map<String, String> getRoots() {
        return roots;
    }
    public Map<String, Map<String, String>> getDict() {
        return dict;
    }
    public static void main(String[] args) {
        Translator tl = new Translator();
        tl.add("EN", "Hello", "ES", "Hola");
        tl.add("FR", "Bonjour", "ES", "Hola");
        tl.add("EN", "Hello", "IT", "Ciao");
        tl.add("CN", "你好", "KR", "안녕하세요");
        tl.add("KR", "안녕하세요", "JP", "こんにちは");
        tl.add("GR", "Hallo", "JP", "こんにちは");
        tl.add("GR", "Hallo", "EN", "Bonjour");
        String[] langs = new String[] {"EN", "FR", "ES", "CN", "KR", "JP", "GR", "IT"};
        String[] words = new String[] {"Hello", "Bonjour", "Hola", "你好", "안녕하세요", "こんにちは", "Hallo", "Ciao"};

        System.out.println("Translate Hola into Different Languages:");
        String wordToTranslate = "Hola";
        for (String q : langs) System.out.printf("%s -> %s: %s\n", wordToTranslate, q, tl.get("ES", wordToTranslate, q));
        System.out.println();
        System.out.printf("dict  after Round1 queries: %s\n", tl.getDict());
        System.out.printf("roots after Round1 queries: %s\n", tl.getRoots());
        System.out.println();
        System.out.println("Translate different Language words into French:");
        String toLang = "FR";
        for (String s : words) System.out.printf("%s: %s <- %s\n", toLang, tl.get("ES", s, toLang), s);
        System.out.println();
        System.out.printf("dict  after Round2 queries: %s\n", tl.getDict());
        System.out.printf("roots after Round2 queries: %s\n", tl.getRoots());

    }
}
