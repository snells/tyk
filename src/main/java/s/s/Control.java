package s.s;


import s.Page;
import s.Year;

import java.io.File;
import java.util.ArrayList;

public class Control {
    String remote = "https://seafile.utu.fi/d/b5437514b5/files/";
    public ArrayList<Year> years;
    Save s = new Save();
    Parse p = new Parse();

    public ArrayList<Year> loadData() {
        years = new ArrayList<>();
        for(File f : s.path.listFiles()) {
            String name = f.getName();
            if(!name.contains("kurssi"))
                continue;
            Year x = s.load(name);
            if(x != null)
                years.add(x);
        }
        ArrayList<Page> ownPages = s.loadOwn();
        Vars.ownPages = ownPages;
        Vars.pages = new ArrayList<>();
        ArrayList<Page>  pages = new ArrayList<>();
        for(Year y : years)
            pages.addAll(y.pages);
        Vars.allPages = pages;
        return years;
    }

    public ArrayList<File> getFiles() {
        ArrayList<File> files = new ArrayList<>();
        for (File f : s.path.listFiles()) {
            if (!f.getName().contains("kurssi"))
                continue;
            files.add(f);
        }
        return files;
    }
    public void delAll() {
        ArrayList<File> files = getFiles();
        for (File f : files) {
            try {
                s.save(f.getName(), new Year(0, new ArrayList<Page>()));
                //f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dlYear(int year) {
        Vars.dl = true;
        File f = new File(s.path + "/kurssi" + year);
        boolean ret = p.getFile(remote + "?p=/kurssi" + year + "&dl=1", f);
        if(!ret) {
            return;
        }
        Vars.text = "Done";
        Year y = loadYear(year);
        Vars.allPages.addAll(y.pages);
        if(Vars.state == ViewPane.VIEW_CNT.ALL)
            Vars.pages = new ArrayList<>(Vars.allPages);
        Vars.updateReady = true;
    }
    public boolean saveYear(Year y){
        if(y == null)
            return false;
        return s.save("kurssi" + y.year, y);
    }
    public Year loadYear(int y) {
        return s.load("kurssi" + y);
    }

    public ArrayList<String> localYears() {
        ArrayList<String> l = new ArrayList<>();
        for(File f : s.path.listFiles())
            if(f.getName().contains("kurssi"))
                l.add(f.getName().substring(6));
        return l;
    }


    private boolean match(Page p, ArrayList<String> strs) {
        for(String s : strs)
            if(!p.name.contains(s))
                return false;
        return true;

    }

    public ArrayList<Page> find(ArrayList<String> strs) {
        ArrayList<Page> pgs = new ArrayList<>();
        for(Page p : Vars.pages) {
            if(match(p, strs))
                pgs.add(p);
        }
        return pgs;
    }

    public ArrayList<Page> getPages() {
        if(Vars.state == ViewPane.VIEW_CNT.ALL)
            return Vars.allPages;
        else
            return Vars.ownPages;
    }

    public void saveOwn(ArrayList<Page> p) {
        s.saveOwn(p);
    }
    public void savePage(Page p) {
        if(null == p)
            return;
        Vars.ownPages.add(p);
        s.saveOwn(Vars.ownPages);
        if(Vars.state == ViewPane.VIEW_CNT.OWN)
            Vars.pages = new ArrayList<>(Vars.ownPages);
        Vars.updateReady = true;
    }
    public void delPage(Page p) {
        if(null == p)
            return;
        ArrayList<Page> ret = new ArrayList<>();
        for(Page d : Vars.ownPages)
            if(!d.name.equals(p.name))
                ret.add(d);
        Vars.ownPages = new ArrayList<>(ret);
        if(Vars.state == ViewPane.VIEW_CNT.OWN)
            Vars.pages = new ArrayList<>(ret);
        Vars.updateReady = true;
        s.saveOwn(Vars.ownPages);
    }


}
