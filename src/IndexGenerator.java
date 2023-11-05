import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/*
metódusok:
- konstruktor
- generateIndex
- listEveryFileAndDirectory
- generateHtmlFile
- cleanDir
- cleanPath
- cleanFileName
 */

public class IndexGenerator {
    private String path;
    private List<String> images;
    private List<String> directories;

    public IndexGenerator(String path, List<List<String>> lista) {
        this.path = path;
        this.images = lista.get(0);
        this.directories = lista.get(1);
    }

    /*
    az elkészített html fájlt és a képeket, valamint a könyvtárakat fogja majd kilistázni
    ezen felül meghívja a html generáló metódust, mely elkészíti a könyvtárban az index.html fájlt
     */
    public void generateIndex(String rootPath) {
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

        sb.append(listEveryFileAndDirectory(rootPath));

        sb.append("""
                </body>
                </html>
                """);

        generateHtmlFile(sb.toString());
    }

    /*
    kilistázza az adott könyvtárban található összes fényképet
    és összes könyvtárat
    majd ez alapján fűzi StringBuilderbe a html forráskódokat
    ezáltal ki lesz listázva minden szükséges fájl a könyvtárunkban
     */
    private String listEveryFileAndDirectory(String rootPath) {
        StringBuilder sb = new StringBuilder();

        StringBuilder sb2 = new StringBuilder();

        int depth = Utils.getDepth(this.path, rootPath);
        for (int i = 0; i < depth; i++) {
            sb2.append("../");
        }

        sb.append("<h1 style=\"border-bottom: 2px solid black;\"><a href = \"%sindex.html\">Home Page</a></h1>\n<hr>\n\n".formatted(sb2.toString()));

        sb.append("<h2 style=\"border-bottom: 2px solid black;\">Directories:</h2>");

        if (!(this.path.equals(rootPath))) {
            sb.append("<h1\"><a href=../index.html>..</a></h1>");
        }

        for (String dir : directories) {
            if (Utils.isSubFolder(dir, this.path)) {
                sb.append("<h4><a href=\"" + cleanDir(dir) + "/index.html" + "\">" + cleanDir(dir) + "</a></h4>");
            }
        }

        sb.append("<h2 style=\"border-bottom: 2px solid black;\">Images:</h2>");

        for (String image : images) {
            if (Utils.getAbsPath(image).equals(this.path)) {
                String fileLink = Utils.fileOrDirectoryName(image) + ".html";
                String fileName = Utils.fileOrDirectoryName(image);
                sb.append("<h4><a href=\"" + fileLink + "\">" + fileName + "</a></h4>");
            }
        }

        return sb.toString();
    }

    private void generateHtmlFile(String sourceCode) {
        try (FileWriter writer = new FileWriter(this.path + "/index.html")) {
            writer.write(sourceCode);
            System.out.println(this.path);
        } catch (IOException e) {
            System.err.println("Hiba történt a fájl létrehozása közben: " + e.getMessage());
        }
    }

    /*
    random fos metódusok amelyek kiszedték a fájl / könyvtár nevét,
    kiterjesztését és még fasz tudja mire volt jó
    currently működik szóval ne merészelj hozzányúlni, a jövőbeli éned ígyis anyázik neked
     */
    private String cleanDir(String s) {
        String[] tmp = s.split("/");

        return tmp[tmp.length - 1];
    }
}
