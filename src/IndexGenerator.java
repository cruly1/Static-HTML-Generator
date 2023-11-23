import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class IndexGenerator {
    private String path;

    public IndexGenerator(String path) {
        this.path = path;
    }

    public void generateIndex(String rootPath, List<List<String>> lista) {
        StringBuilder sb = new StringBuilder();

        sb.append(Utils.htmlHead());
        sb.append(htmlBody(rootPath, lista.get(0), lista.get(1)));
        sb.append("""
                </body>
                </html>
                """);

        generateHtmlFile(sb.toString());
    }

    private String htmlBody(String rootPath, List<String> images, List<String> directories) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int depth = Utils.getDepth(this.path, rootPath);

        sb2.append("../".repeat(depth));
        sb.append("<h1 style=\"border-bottom: 2px solid black;\"><a href = \"%sindex.html\">Home Page</a></h1>\n<hr>\n\n".formatted(sb2.toString()));
        sb.append("<h2 style=\"border-bottom: 2px solid black;\">Directories:</h2>");

        if (!(this.path.equals(rootPath))) {
            sb.append("<h1\"><a href=../index.html>^^</a></h1>");
        }

        for (String dir : directories) {
            if (Utils.isSubFolder(dir, this.path)) {
                File f = new File(dir);
                sb.append("<h4><a href=\"" + f.getName() + "/index.html" + "\">" + f.getName() + "</a></h4>");
            }
        }

        sb.append("<h2 style=\"border-bottom: 2px solid black;\">Images:</h2>");

        for (String image : images) {
            if (Utils.getParentPath(image).equals(this.path)) {
                File f = new File(image);
                String fileLink = f.getName().substring(0, f.getName().lastIndexOf(".")) + ".html";
                String fileName = f.getName().substring(0, f.getName().lastIndexOf("."));
                sb.append("<h4><a href=\"" + fileLink + "\">" + fileName + "</a></h4>");
            }
        }

        return sb.toString();
    }

    private void generateHtmlFile(String sourceCode) {
        try (PrintWriter writer = new PrintWriter(this.path + "/index.html")) {
            writer.write(sourceCode);
            System.out.println(this.path);
            writer.close();
        } catch (IOException e) {
            System.err.println("[ERROR]" + e);
        } catch (Exception e) {
            System.err.println("[ERROR]" + e);
        }
    }
}
