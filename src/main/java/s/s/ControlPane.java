package s.s;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import s.Page;

import java.util.ArrayList;
import java.util.Arrays;

public class ControlPane extends HBox {
    private Control ctrl;
    public boolean search = false;
    private ContainerPane root;
    private ViewPane vp;
    Button go;
    TextField tf;
    public ControlPane(Control c, ViewPane vp) {
        this.vp = vp;
        ctrl = c;
        root = Vars.root;
        this.setPrefSize(Vars.width, 49);

        go = new Button("Search");
        go.setOnMouseClicked(event -> {
            ret();
            search = true;
        });

        go.setPrefWidth(80);
        tf = new TextField();
        tf.getProperties().put("vkType", "numeric");
        setWidth(Vars.width);
//        tf.setPrefWidth(Vars.width - 81);
//        tf.setMinWidth(Vars.width - 81);
        tf.setOnKeyPressed(e -> {
            if(e.getEventType() == e.KEY_RELEASED)
                return;
            KeyCode k = e.getCode();
            if(k == KeyCode.ENTER) ret();
            else if(k == KeyCode.BACK_SPACE) del();
            else if(k == KeyCode.SPACE) space();
            else tf.appendText(e.getCharacter());
            tf.forward();
        });

        this.getChildren().addAll(tf, go);
    }

    private void ret() {
        if(tf.getText().length() == 0) {
            Vars.pages = new ArrayList<>((Vars.state == ViewPane.VIEW_CNT.ALL) ? Vars.allPages : Vars.ownPages);
            search = false;
        }
        else if(search) {
            search = false;
            Vars.pages = ctrl.getPages();
            Vars.pages = ctrl.find(new ArrayList<String>(Arrays.asList(tf.getText().split(" "))));
        }
        else {
            Vars.pages = ctrl.find(new ArrayList<String>(Arrays.asList(tf.getText().split(" "))));
            search = true;
        }
            //viewPane.setPages(ctrl.find(new ArrayList<String>(Arrays.asList(tf.getText().split(" ")))));
        Vars.updateReady = true;
        tf.setText("");
    }

    public void esc() {
        vp.lv.getSelectionModel().clearSelection();
        if(vp.flag) {
            vp.swap();
            vp.requestFocus();
            return;
        }
        if(search) {
            Vars.pages = new ArrayList<Page>(Vars.allPages);
            search = false;
            Vars.updateReady = true;
            vp.requestFocus();
        }
        else {
            if (Vars.root.flag)
                System.exit(0);
            else
                Vars.root.swap();
        }

    }
    private void del() {
        String s = tf.getText();
        if(s.length() == 0)
            return;
        int c = tf.getCaretPosition();
        tf.setText(s.substring(0, s.length()));
        tf.positionCaret(c - 1);
    }
    private void space() {
        tf.appendText(" ");
    }

    public void setWidth(int w) {
        tf.setPrefWidth(w / 2);
        tf.setMaxWidth(w / 2);
        tf.setMinWidth(w / 2);
    }
}
