import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private Utils() {}

    public static void checkArguments(String[] path) {
        if (path.length != 1) {
            System.err.println("Hiba! Adja meg az elérési útvonalat!");
            System.exit(1);
        }

        if (!new File(path[0]).isDirectory()) {
            System.err.println("Hiba! Hibás elérési útvonal!");
            System.exit(2);
        }
    }

    /*
    eldönti egy fájlról, hogy elfogadott kiterjesztést használ e
     */
    public static boolean validImageExtensions(String file) {
        String[] extensions = {"jpg", "jpeg", "png"};

        for (int i = 0; i < extensions.length; i++) {
            if (file.endsWith(extensions[i])) {
                return true;
            }
        }

        return false;
    }

    /*
    bejárja a metódus az egész mappaszerkezetet és egy listába menti a képeket,
    egy külön listába menti a könyvtárakat elérési úttal együtt
    majd ezt téríti vissza
     */
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
                } else if (Utils.validImageExtensions(file.getName())) {
                    images.add(file.getAbsolutePath());
                }
            }
        }

        return List.of(images, directories);
    }


    /*
    végigmegy az összes fájlon a gyökérkönyvtáron belül
    minden almappán, azoknak a tartalmán és azok alapján példányosít
     */
    public static void varazsMetodus(List<List<String>> lista, String rootPath) {
        for (String image : lista.get(0)) {
            new ImageGenerator(image, lista).sourceCode(image, rootPath);
        }

        for (String dir : lista.get(1)) {
            new IndexGenerator(dir, lista).generateIndex(rootPath);
        }
    }

    /*
    2023. 11. 02.
    21:29
    MIT TETTEM?

    majd holnapra elromlik
    a lényegi működése ugye, hogy ha megadunk egy elérési útvonalat, az tartalmazni fog '/' jeleket
    és mivel minden elérés után töröltem a '/' karaktereket, de az összes előtt meghagytam
    ezért hogyha az egyik pontosan 1 darab '/' jellel tartalmaz többet mint a másik,
    akkor biztosan almappája az egyik a másiknak
     */
    public static boolean isSubFolder(String s, String currentDir) {
        if (s.split("/").length == currentDir.split("/").length + 1) {
            return true;
        }

        return false;
    }
}
