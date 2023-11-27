import java.util.List;

public class Main {
    public static void main(String[] args) {
        Utils.checkArguments(args);
        String rootPath = args[0];
        List<List<String>> filesAndFolders = Utils.listEveryImageAndDirectory(rootPath);
        filesAndFolders.get(1).add(rootPath);
        Utils.startGenerators(filesAndFolders, rootPath);
    }
}
