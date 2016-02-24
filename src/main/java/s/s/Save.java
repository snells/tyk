package s.s;
import s.Page;
import s.Year;


import com.gluonhq.charm.down.common.PlatformFactory;

import java.io.*;
import java.util.ArrayList;

public class Save {
    public File path;
    public Save() {
        try {
            path = PlatformFactory.getPlatform().getPrivateStorage();
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1); }
    }


    public boolean save(String name, Year year) {
        FileOutputStream fo = null;
        ObjectOutputStream os = null;
        boolean ret = true;
        File file = new File(path, name);
        try {
            fo = new FileOutputStream(file, false);
            os = new ObjectOutputStream(fo);
            os.writeObject(year);
        } catch (Exception e) {
            ret = false;
            e.printStackTrace();
             }
        try {
            os.close();
            fo.close();
        } catch(Exception e) {}
        return ret;
    }
    public Year load(String name) {
        Year y = null;
        File file = new File(path, name);

        FileInputStream fi = null;
        ObjectInputStream oi = null;
        try {
            fi = new FileInputStream(file);
            oi = new ObjectInputStream(fi);
            y = (Year)oi.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            oi.close();
            fi.close();
        } catch(Exception e) {}
        return y;
    }

    public ArrayList<Page> loadOwn() {
        File f = new File(path + "own");
        ArrayList<Page> pages = new ArrayList<>();
        try {
            FileInputStream fi = new FileInputStream(f);
            ObjectInputStream os = new ObjectInputStream(fi);
            pages = (ArrayList<Page>)os.readObject();
            os.close();
            fi.close();
        } catch (Exception e) { }
        return pages;
    }

    public boolean saveOwn(ArrayList<Page> pages) {
        File f = new File(path + "own");
        try {
            FileOutputStream fo = new FileOutputStream(f, false);
            ObjectOutputStream os = new ObjectOutputStream(fo);
            os.writeObject(pages);
            os.close();
            fo.close();
        } catch (Exception e) { return false; }
        return true;
    }
}
