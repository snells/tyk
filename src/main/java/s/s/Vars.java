package s.s;

import s.Page;

import java.util.ArrayList;

public class Vars {
    public static double width;
    public static double height;
    public volatile static String text = "";
    public static ArrayList<Page> pages;
    public static ArrayList<Page> allPages;
    public static ArrayList<Page> ownPages;
    public static ViewPane.VIEW_CNT state = ViewPane.VIEW_CNT.ALL;
    public static boolean updateReady = false;
    public static Control ctrl = new Control();
    public static ContainerPane root;
    public static ArrayList<String> years;
    public static boolean dl = false;
    public static int insertYear = 2015;
    public Vars(double w, double h) {
        width = w;
        height = h;
    }
    public Vars() {}

}
