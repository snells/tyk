package s.s;


import javafx.concurrent.Task;
import javafx.scene.text.Text;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Parse {
    public boolean err = false;


    private Document getDoc(String url, int id) {

        Document d = null;
        try {
            d = Jsoup.connect(url)
                    .data("id", Integer.toString(id))
                    .userAgent("Mozilla")
                    .post();
        } catch (Exception e) {
            err = true;
        }
        return d;
    }

    public boolean getFile(String url, File out) {
        /*Document d = null;
        try {
            d = Jsoup.connect(url).ignoreContentType().execute().bodyAsBytes()
                    .data("p", name, "dl", "1")
                    .userAgent("Mozilla")
                    .post();
        } catch (Exception e) {}
        return d;
        */
        System.out.println(out);
        try {
            Connection.Response s = Jsoup.connect(url).ignoreContentType(true).maxBodySize(0).execute();
            System.out.println(s.contentType());
            if(!s.contentType().equals("application/octet-stream")) {
                Vars.text = "No file for that year";
                return false;
            }
            byte[] bytes = s.bodyAsBytes();
            FileOutputStream fo = new FileOutputStream(out);
            fo.write(bytes);
            fo.flush();
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            Vars.text = "Error while downloading";
            return false;
        }
        return true;
    }

    private String getName(Document d) {
        Elements e = d.select("div#header_marjapuuro");
        return e.first().ownText();
    }

    private ArrayList<String> getData(Document d) {
        Elements es = d.select("div.opsi_toteuma_kuvailu > *");
        ArrayList<String> data = new ArrayList<>();
        if (es.size() == 0)
            return data;
        boolean flag = false;
        int n = -1;
        for (Element e : es) {
            if (e.tagName().equals("h2")) {
                data.add(e.ownText());
                flag = false;
            } else if (flag)
                data.set(n, data.get(n) + "\n" + getInner(e.children()));
            else {
                data.add(getInner(e.children()));
                flag = true;
                n++;
            }
        }
        return data;
    }

    private String getInner(Elements es) {
        String t = "";
        for(Element e : es) {
            t += e.ownText() + "\n";
            if (e.children().size() > 0)
                for (Element a : e.children()) {
                    t += a.ownText() + "\n";
                    if (a.children().size() > 0)
                        for (Element b : a.children())
                            t = t + b.text() + "\n";
                }
        }
        return t;
    }

    public int lastPage = 0;

    public int findStart(String url, int start, int end) {
        int i = 0;
        int stop = 30;
        while(!testYear(url, i, 3 , start, end)) {
            stop--;
            if(stop == 0)
                return 0;
            i += 1000;
        }
        int lb = i - 1000;
        int ub = i;
        stop = 6;
        while(stop-- > 0) {
            int nlb = lb + ((ub - lb) / 2);
            if(testYear(url, nlb, 3, start, end))
                ub = nlb;
            else
                lb = nlb;
        }
        return ub;
    }

    public int findEnd(String url, int start, int end) {
        int i = 0;
        while(!testYear(url, i, 3, start, end))
            i += 1000;
        while(testYear(url, i, 3, start, end))
            i += 1000;
        return i - 1000;
    }
    public boolean testYear(String url, int id, int count , int start, int end) {
        String s = Integer.toString(start);
        String e = Integer.toString(end);
        for(int i = id; i < id + count; i++) {
            Document d = getDoc(url, id);
            if(checkErr())
                continue;
            String t = d.title();
            if(t.contains(s) && t.contains(e)) {
                return true;
            }
        }
        return false;
    }
    public boolean checkErr() {
        boolean e = err;
        err = false;
        return e;
    }

}
