import java.util.List;

public class Main {
    public static void main(String[] args) {
        Utils.checkArguments(args);
        String rootPath = args[0];

        if (args.length == 2 && args[1].equals("-clean")) return;

        List<List<String>> listAllElements = Utils.findImagesAndDirectories(rootPath);
        listAllElements.get(1).add(rootPath);
        Utils.startGenerators(listAllElements, rootPath);
    }
}
