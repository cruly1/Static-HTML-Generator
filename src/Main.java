import java.util.List;

public class Main {
    public static void main(String[] args) {
        String rootPath = Utils.checkArguments(args);
        List<List<String>> filesAndFolders = Utils.listEveryImageAndDirectory(rootPath);
        filesAndFolders.get(1).add(rootPath);
        Utils.startGenerators(filesAndFolders, rootPath);
    }
}
