
package s;



import java.io.Serializable;
import java.util.ArrayList;

public class Page implements Serializable{
    public static final long serialVersionUID = 333L;
    public String name;
    public ArrayList<String> data;

    public Page(String n, ArrayList<String> d) {
          name = n;
        data = d;
    }

    @Override
    public String toString() {
        return name;
    }
}

