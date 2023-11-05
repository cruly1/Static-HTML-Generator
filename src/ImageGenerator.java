import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
metódusok:
- konstruktor
- sourceCode
- generateHtmlFile
- cleanString
- getAbsolutePath
- getExtension
- dirName
- getCurrentDirImages
- fileLinker
 */

public class ImageGenerator {
    private String path;
    private List<String> images;
    private List<String> directories;

    public ImageGenerator(String path, List<List<String>> lista) {
        this.path = path;
        this.images = lista.get(0);
        this.directories = lista.get(1);
    }

    public void sourceCode(String elem, String rootPath) {
        String fileName = Utils.fileOrDirectoryName(elem);
        String absolutePath = Utils.getAbsPath(elem);
        StringBuilder sb = new StringBuilder();

        sb.append(
                """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Static HTML Generator - Project</title>
                </head>
                <body>""");

        StringBuilder sb2 = new StringBuilder();

        int depth = Utils.getDepth(this.path, rootPath);
        for (int i = 0; i < depth; i++) {
            sb2.append("../");
        }

        sb.append("<h1 style=\"border-bottom: 2px solid black;\"><a href = \"%sindex.html\">Home Page</a></h1>\n<hr>\n\n".formatted(sb2.toString()));

        sb.append("<h2 style=\"border-bottom: 2px solid black;\">Directories:</h2>");

        for (String dir : directories) {
            if (Utils.isSubFolder(dir, this.path)) {
                sb.append("<h4>" + dirName(dir) + "</h4>");
            }
        }

        sb.append("<h2 style=\"border-bottom: 2px solid black;\">Current Image:</h2>");
        
        List<String> teszt = getCurrentDirImages(images);
        int currentIndex = teszt.indexOf(this.path);

        String nextElem = "";
        String prevElem = "";

        if (currentIndex == teszt.size() - 1) {
            prevElem = teszt.get(currentIndex - 1);
        } else if (currentIndex == 0) {
            nextElem = teszt.get(currentIndex + 1);
        } else {
            nextElem = teszt.get(currentIndex + 1);
            prevElem = teszt.get(currentIndex - 1);
        }

        if (!nextElem.equals("")) {
            nextElem = Utils.fileOrDirectoryName(nextElem) + ".html";
        }
        if (!prevElem.equals("")){
            prevElem = Utils.fileOrDirectoryName(prevElem) + ".html";
        }

        sb.append(fileLinker(fileName + "." + getExtension(elem), nextElem, prevElem));

        sb.append("""
            </body>
            </html>
            """);

        generateHtmlFile(sb.toString(), fileName, absolutePath);
    }

    /*
    legenerálja a path directoryban fellelhető összes fénykép html forráskódját
     */
    private void generateHtmlFile(String sourceCode, String elem, String absolutePath) {
        try (FileWriter writer = new FileWriter(absolutePath + "/" + elem + ".html")) {
            writer.write(sourceCode);
            System.out.println(this.path);
        } catch (IOException e) {
            System.err.println("Hiba történt a fájl létrehozása közben: " + e.getMessage());
        }
    }

    /*
    az adott fénykép kiterjesztését adja vissza '.' nélkül
     */
    private String getExtension(String s) {
        String[] tmp = s.split("\\.");

        return tmp[tmp.length-1];
    }

    /*
    az adott könyvtár nevét adja vissza (csak a nevét)
     */
    private String dirName(String s) {
        String[] res = s.split("/");
        return res[res.length - 1];
    }

    /*
    kilistázza az adott könyvtárban lévő összes fényképet
     */
    private List<String> getCurrentDirImages(List<String> images) {
        List<String> result = new ArrayList<>();

        for (String image : images) {
            if (Utils.getAbsPath(image).equals(Utils.getAbsPath(this.path))) {
                result.add(image);
            }
        }

        return result;
    }

    /*
    megkapja a jelenlegi kép nevét kiterjesztéssel, illetve a következő és előző képet html kiterjesztéssel
    ezután megvizsgálja, hogy van e még előtte vagy utána kép és annak függvényében generálja a html forráskódot
     */
    private String fileLinker(String s, String next, String previous) {
        StringBuilder sb = new StringBuilder();

        if (!next.equals("") && !previous.equals("")) {
            sb.append("<h3><a href=\"./" + previous + "\"> << </a> &nbsp; <a href=\"./" + next + "\"> >> </a></h3>");
            sb.append("<a href=\"./" + next + "\"><img src=\"./"+ s + "\"  style=\"width: 20%; height: 20%;\"></a>");
        } else if (previous.equals("")) {
            sb.append("<h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href=\"./" + next + "\"> >> </a></h3>");
            sb.append("<a href=\"./" + next + "\"><img src=\"./"+ s + "\"  style=\"width: 20%; height: 20%;\"></a>");
        } else if (next.equals("")) {
            sb.append("<h3> <a href=\"./" + previous + "\"> << </a></h3>");
            sb.append("<img src=\"./"+ s + "\"  style=\"width: 20%; height: 20%;\"></a>");
        }

        return sb.toString();
    }
}
