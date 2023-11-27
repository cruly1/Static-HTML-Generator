import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ImageGenerator {
    private String path;

    public ImageGenerator(String path) {
        this.path = path;
    }

    public void generateImage(String elem, String rootPath, List<List<String>> lista) {
        File f = new File(elem);
        String fileName = f.getName().substring(0, f.getName().lastIndexOf("."));
        String absolutePath = Utils.getParentPath(elem);
        StringBuilder sb = new StringBuilder();

        sb.append(Utils.htmlHead());
        sb.append(imageHtmlBody(rootPath, fileName, elem, lista.get(0), lista.get(1)));

        generateHtmlFile(sb.toString(), fileName, absolutePath);
    }

    public String imageHtmlBody(String rootPath, String fileName, String elem, List<String> images, List<String> directories) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTmp = new StringBuilder();
        int depth = Utils.getDepth(this.path, rootPath);

        sbTmp.append("../".repeat(depth));
        sb.append("<h1 style=\"border-bottom: 2px solid black;\"><a href=\"%sindex.html\">Home Page</a></h1>\n<hr>\n\n".formatted(sbTmp.toString()));
        sb.append("<h2 style=\"border-bottom: 2px solid black;\">Directories:</h2>");

        if (depth != 0) {
            sb.append("<h1\"><a href=../index.html>^^</a></h1>");
        }

        for (String dir : directories) {
            File f = new File(dir);
            if (Utils.isSubFolder(dir, Utils.getParentPath(this.path))) {
                sb.append("<h4><a href=\"" + f.getName() + "/index.html" + "\">" + f.getName() + "</a></h4>");
            }
        }

        sb.append("<h2 style=\"border-bottom: 2px solid black;\">Current Image:</h2>");

        List<String> currentDirImages = Utils.getCurrentDirImages(this.path, images);
        int currentIndex = currentDirImages.indexOf(this.path);
        String nextElem = "";
        String prevElem = "";

        if (currentDirImages.size() != 1) {
            if (currentIndex == currentDirImages.size() - 1) {
                prevElem = currentDirImages.get(currentIndex - 1);
            } else if (currentIndex == 0) {
                nextElem = currentDirImages.get(currentIndex + 1);
            } else {
                nextElem = currentDirImages.get(currentIndex + 1);
                prevElem = currentDirImages.get(currentIndex - 1);
            }
        }

        if (!nextElem.isEmpty()) {
            File f1 = new File(nextElem);
            nextElem = f1.getName().substring(0, f1.getName().lastIndexOf(".")) + ".html";
        }
        if (!prevElem.isEmpty()){
            File f2 = new File(prevElem);
            prevElem = f2.getName().substring(0, f2.getName().lastIndexOf(".")) + ".html";
        }

        sb.append(fileLinker(fileName + "." + Utils.getExtension(elem), nextElem, prevElem));
        sb.append("""
            </body>
            </html>
            """);

        return sb.toString();
    }

    private void generateHtmlFile(String sourceCode, String elem, String absolutePath) {
        try (PrintWriter writer = new PrintWriter(absolutePath + "/" + elem + ".html")) {
            writer.write(sourceCode);
            writer.close();
        } catch (IOException e) {
            System.err.println("[ERROR]" + e);
        } catch (Exception e) {
            System.err.println("[ERROR]" + e);
        }
    }

    private String fileLinker(String current, String next, String previous) {
        StringBuilder sb = new StringBuilder("<div>");

        if (next.isEmpty() && previous.isEmpty()) {
            sb.append("<h3>" + "&nbsp;".repeat(5) + current + "</h3>");
            sb.append("<img src=\"./"+ current + "\"  style=\"width: 20%; height: 20%;\"></a>");
        } else if (!next.equals("") && !previous.equals("")) {
            sb.append("<h3><a href=\"./" + previous + "\"> << </a>" + current + "<a href=\"./" + next + "\"> >> </a></h3>");
            sb.append("<a href=\"./" + next + "\"><img src=\"./"+ current + "\"  style=\"width: 20%; height: 20%;\"></a>");
        } else if (previous.isEmpty()) {
            sb.append("<h3>" + "&nbsp;".repeat(5) + current + " <a href=\"./" + next + "\"> >> </a></h3>");
            sb.append("<a href=\"./" + next + "\"><img src=\"./"+ current + "\"  style=\"width: 20%; height: 20%;\"></a>");
        } else if (next.isEmpty()) {
            sb.append("<h3> <a href=\"./" + previous + "\"> << </a> " + current + "</h3>");
            sb.append("<img src=\"./"+ current + "\"  style=\"width: 20%; height: 20%;\"></a>");
        }
        sb.append("</div>");

        return sb.toString();
    }
}

/*

next empty
prev empty
mind2 empty
egyik sem empty

 */