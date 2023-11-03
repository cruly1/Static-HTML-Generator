import java.util.List;

public class Main {
    public static void main(String[] args) {
//        Utils.checkArguments(args);
//        String path = args[0];

        String rootPath = "/Users/komphone/prog/prog2/images";
        List<List<String>> varazsLista = Utils.findImagesAndDirectories(rootPath);
        varazsLista.get(1).add(rootPath);
        Utils.varazsMetodus(varazsLista, rootPath);
    }
}
