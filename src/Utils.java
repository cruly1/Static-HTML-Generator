import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    private Utils() {}

    public static String checkArguments(String[] path) {
        String valid = formatRootPath(path[0]);

        if (path.length != 1 && path.length !=2) {
            System.err.println("[ERROR] Please provide a directory!");
            System.exit(1);
        }

        if (path.length == 2 && path[1].equals("-clean")) {
            cleaner(valid);
            System.out.println("All HTML files deleted successfully!");
            System.exit(0);
        }

        if (!new File(valid).isDirectory()) {
            System.err.println("[ERROR] Wrong or unreachable directory!");
            System.exit(2);
        }

        return valid;
    }

    public static String formatRootPath(String root) {
        if (!root.startsWith("/")) {
            root = "/" + root;
        }

        if (root.endsWith("/")) {
            root = root.substring(0, root.length());
        }

        return root;
    }

    private static void cleaner(String rootPath) {
        File root = new File(rootPath);
        File[] list = root.listFiles();

        if (list == null) return ;

        for (File file : list) {
            if (file.isDirectory()) {
                cleaner(file.getAbsolutePath());
            }
            else if (file.getName().endsWith(".html")){
                file.delete();
            }
        }
    }

    public static List<List<String>> listEveryImageAndDirectory(String path) {
        List<String> images = new ArrayList<>();
        List<String> directories = new ArrayList<>();
        File dir = new File(path);
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    directories.add(file.getAbsolutePath());
                    List<List<String>> subLists = listEveryImageAndDirectory(file.getAbsolutePath());
                    images.addAll(subLists.get(0));
                    directories.addAll(subLists.get(1));
                } else if (isSupportedFileExtension(file)) {
                    images.add(file.getAbsolutePath());
                }
            }
        }
        Collections.sort(images);
        Collections.sort(directories);

        return List.of(images, directories);
    }

    public static String getExtension(String s) {
        return s.substring(s.lastIndexOf(".") + 1);
    }

    public static boolean isSupportedFileExtension(File file) {
        List<String> supportedExtensions = new ArrayList<>(List.of("jpg", "jpeg", "png", "gif"));
        boolean isSupported = supportedExtensions.contains(getExtension(file.getName()));
        return isSupported ? true : false;
    }

    public static void startGenerators(List<List<String>> lista, String rootPath) {
        for (String image : lista.get(0)) {
            new ImageGenerator(image).generateImage(image, rootPath, lista);
        }

        for (String dir : lista.get(1)) {
            new IndexGenerator(dir).generateIndex(rootPath, lista);
        }
    }

    public static boolean isSubFolder(String subDir, String parentDir) {
        boolean isSub = parentDir.endsWith(getParentPath(subDir));
        return isSub ? true : false;
    }

    public static String getParentPath(String s) {
        return s.substring(0, s.lastIndexOf("/"));
    }

    public static int getDepth(String currentPath, String rootPath) {
        if (currentPath.contains(".")) {
            return currentPath.split("/").length - rootPath.split("/").length - 1;
        }
        return currentPath.split("/").length - rootPath.split("/").length;
    }

    public static List<String> getCurrentDirImages(String currentPath, List<String> images) {
        return images.stream()
                .filter(image -> Utils.getParentPath(image).equals(Utils.getParentPath(currentPath)))
                .collect(Collectors.toList());
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
                """);

        return sb.toString();
    }
}
