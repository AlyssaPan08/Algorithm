import java.util.Map;
import java.util.HashMap;

public class Translator {
    private static Map<String, Map<String, String>> dict;//root word, <lang, word>
    private static Map<String, String> roots; //word, root word
    
    public Translator() {
        dict = new HashMap<>();
        roots = new HashMap<>();
    }
    public static void add(String inputLang, String inputWord, String outputLang, String outputWord) {
        init(inputLang, inputWord); //initialize each word for union find
        init(outputLang, outputWord);
        String root1 = find(inputWord);
        String root2 = find(outputWord);
        if (!root1.equals(root2)) {
            roots.put(root2, root1); //union
            dict.get(root1).putAll(dict.get(root2));
            dict.remove(root2);
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
        }
    }
    private static String find(String word) {
        if (word == null || word.equals("")) return null;
        String root = roots.get(word);
        if (root == null) return null;
        if (!word.equals(root)) roots.put(word, find(root));
        return roots.get(word);
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
