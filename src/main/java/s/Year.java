package  s;

import java.io.Serializable;
import java.util.ArrayList;

public class Year implements Serializable {
    public static final long serialVersionUID = 333L;
    public ArrayList<Page> pages;
    public int year;
    public Year() {}
    public Year(int y, ArrayList<Page> p) {
        pages = p;
        year = y;
    }
}
