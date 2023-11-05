import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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

        sb.append(Utils.htmlHead());

        sb.append(htmlBody(rootPath, fileName, elem));

        generateHtmlFile(sb.toString(), fileName, absolutePath);
    }

    public String htmlBody(String rootPath, String fileName, String elem) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTmp = new StringBuilder();

        int depth = Utils.getDepth(this.path, rootPath);

        sbTmp.append("../".repeat(depth));

        sb.append("<h1 style=\"border-bottom: 2px solid black;\"><a href = \"%sindex.html\">Home Page</a></h1>\n<hr>\n\n".formatted(sbTmp.toString()));

        sb.append("<h2 style=\"border-bottom: 2px solid black;\">Directories:</h2>");

        if (depth != 0) {
            sb.append("<h1\"><a href=../index.html>..</a></h1>");
        }

        for (String dir : directories) {
            if (Utils.isSubFolder(dir, Utils.getAbsPath(this.path))) {
                sb.append("<h4><a href=\"" + Utils.fileOrDirectoryName(dir) + "/index.html" + "\">" + Utils.fileOrDirectoryName(dir) + "</a></h4>");
            }
        }

        sb.append("<h2 style=\"border-bottom: 2px solid black;\">Current Image:</h2>");

        List<String> currentDirImages = Utils.getCurrentDirImages(this.path, images);
        int currentIndex = currentDirImages.indexOf(this.path);

        String nextElem = "";
        String prevElem = "";

        if (currentIndex == currentDirImages.size() - 1) {
            prevElem = currentDirImages.get(currentIndex - 1);
        } else if (currentIndex == 0) {
            nextElem = currentDirImages.get(currentIndex + 1);
        } else {
            nextElem = currentDirImages.get(currentIndex + 1);
            prevElem = currentDirImages.get(currentIndex - 1);
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

        return sb.toString();
    }

    private void generateHtmlFile(String sourceCode, String elem, String absolutePath) {
        try (FileWriter writer = new FileWriter(absolutePath + "/" + elem + ".html")) {
            writer.write(sourceCode);
            System.out.println(this.path);
        } catch (IOException e) {
            System.err.println("[ERROR]" + e.getMessage());
        }
    }

    private String getExtension(String s) {
        String[] tmp = s.split("\\.");

        return tmp[tmp.length-1];
    }

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
