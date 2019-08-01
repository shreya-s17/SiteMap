import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Web Crawler");
        Crawler.begin();
        PrintWriter out = new PrintWriter("output.txt");
        out.println(Crawler.jsonarray.toString());
        System.out.println(Crawler.jsonarray.toString());
    }
}
