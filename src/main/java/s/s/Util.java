package s.s;

import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import s.Page;

import java.util.ArrayList;

/**
 * Created by s on 2/7/16.
 */
public class Util {
    public static ArrayList<String> strLst(String... strs) {
        ArrayList<String> s = new ArrayList<>();
        for(int i = 0; i < strs.length; i++)
            s.add(strs[i]);
        return s;
    }
    public static ArrayList<String> pageNames(ArrayList<Page> pages) {
        ArrayList<String> s = new ArrayList<>();
        for(Page p : pages)
            s.add(p.name);
        return s;
    }

    public static void addHoldHandler(Node node, Duration holdTime,
                                      EventHandler<MouseEvent> handler) {

        class Wrapper<T> { T content ; }
        Wrapper<MouseEvent> eventWrapper = new Wrapper<>();

        PauseTransition holdTimer = new PauseTransition(holdTime);
        holdTimer.setOnFinished(event -> handler.handle(eventWrapper.content));


        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            eventWrapper.content = event ;
            holdTimer.playFromStart();
        });
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> holdTimer.stop());
        node.addEventHandler(MouseEvent.DRAG_DETECTED, event -> holdTimer.stop());
    }

    public static <T extends Region> void butPos(T v, int x, int y, int w) {
        v.setPrefWidth(w);
        v.setTranslateX(x);
        v.setTranslateY(y);
    }

}
