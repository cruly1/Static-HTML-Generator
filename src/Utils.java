import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private Utils() {}

    public static void checkArguments(String[] path) {
        if (path.length != 1 && path.length !=2) {
            System.err.println("[ERROR] Please provide a directory!");
            System.exit(1);
        }

        if (path.length == 2 && path[1].equals("-clean")) {
            cleaner(path[0]);
        }

        if (!new File(path[0]).isDirectory()) {
            System.err.println("[ERROR] Wrong or unreachable directory!");
            System.exit(2);
        }
    }

    private static void cleaner(String rootPath) {
        File root = new File(rootPath);
        File[] list = root.listFiles();

        if (list == null) return ;

        for (File f : list) {
            if (f.isDirectory()) {
                cleaner(f.getAbsolutePath());
            }
            else if (f.toString().endsWith(".html")){
                f.delete();
            }
        }
    }

    public static List<List<String>> findImagesAndDirectories(String path) {
        List<String> images = new ArrayList<>();
        List<String> directories = new ArrayList<>();
        File dir = new File(path);
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    directories.add(file.getAbsolutePath());
                    List<List<String>> subLists = findImagesAndDirectories(file.getAbsolutePath());
                    images.addAll(subLists.get(0));
                    directories.addAll(subLists.get(1));
                } else if (file.getName().endsWith("jpg")
                        || file.getName().endsWith("png")
                        || file.getName().endsWith("jpeg")) {
                    images.add(file.getAbsolutePath());
                }
            }
        }

        return List.of(images, directories);
    }

    public static void startGenerators(List<List<String>> lista, String rootPath) {
        for (String image : lista.get(0)) {
            new ImageGenerator(image, lista).sourceCode(image, rootPath);
        }

        for (String dir : lista.get(1)) {
            new IndexGenerator(dir, lista).generateIndex(rootPath);
        }
    }

    public static boolean isSubFolder(String subFolder, String currentDir) {
        String[] sub = subFolder.split("/");
        String[] curr = currentDir.split("/");

        return sub[sub.length - 2].equals(curr[curr.length - 1]);
    }

    public static String fileOrDirectoryName(String s) {
        String[] dirs = s.split("/");
        String tmp = dirs[dirs.length - 1];

        if (tmp.contains(".")) {
            return tmp.substring(0, tmp.lastIndexOf("."));
        }

        return tmp;
    }

    public static int getDepth(String currentPath, String rootPath) {
        if (currentPath.contains(".")) {
            return currentPath.split("/").length - rootPath.split("/").length - 1;
        }
        return currentPath.split("/").length - rootPath.split("/").length;
    }

    public static String getAbsPath(String s) {
        return s.substring(0, s.lastIndexOf("/"));
    }

    public static List<String> getCurrentDirImages(String currentPath, List<String> images) {
        List<String> result = new ArrayList<>();

        for (String image : images) {
            if (Utils.getAbsPath(image).equals(Utils.getAbsPath(currentPath))) {
                result.add(image);
            }
        }

        return result;
    }

    public static String htmlHead() {
        StringBuilder sb = new StringBuilder();

        sb.append("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Static HTML Generator - Project</title>
                </head>
                <body>""");

        return sb.toString();
    }
}
