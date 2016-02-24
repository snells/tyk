package s.s;


import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import s.Page;

import java.util.ArrayList;

public class ViewPane extends StackPane {
    public boolean flag = false;
    private boolean longPress = false;
    public ListView<Page> lv;
    ContextMenu cm = new ContextMenu();
    MenuItem save = new MenuItem("Save course");
    MenuItem dummy = new MenuItem("");
    MenuItem delAll = new MenuItem("Del all");
    MenuItem del = new MenuItem("Del");
    public TextArea ta;
    public enum VIEW_CNT { ALL, OWN };
    public VIEW_CNT state = VIEW_CNT.ALL;
    private Page selected = null;
    private Control ctrl;
    private ControlPane cp;
    boolean cmFlag = false;

    public ViewPane(Control c) {
        ctrl = c;
        this.setPrefSize(Vars.width, Math.min(Vars.height * 0.95, Vars.height - 50));
        lv = new ListView<>(FXCollections.observableArrayList());

        lv.setMinSize(this.getMaxWidth(), this.getMaxHeight());

        lv.getStyleClass().add("max-size");
        ta = new TextArea();
        lv.prefWidthProperty().bind(this.widthProperty());
        lv.prefHeightProperty().bind(this.heightProperty());
        ta.prefWidthProperty().bind(this.widthProperty());
        ta.prefHeightProperty().bind(this.heightProperty());
        ta.getStyleClass().add("max-size");
        ta.setEditable(false);
        ta.setWrapText(true);
        ta.setVisible(flag);


        /*
        lv.setOnMouseReleased(e -> {
            if(cm.isShowing())
                return;
            if(selected != null)
                swap();

        });
        lv.setOnMousePressed(e -> {
            if(e.getEventType() != e.MOUSE_PRESSED)
            if(cm.isShowing()) {
                cm.hide();
                this.requestFocus();
                lv.getSelectionModel().clearSelection();
                e.consume();
                return;
            }

            if(lv.getSelectionModel().isEmpty())
                selected = null;
            else {
                selected = lv.getSelectionModel().getSelectedItem();
                ArrayList<String> ret = new ArrayList<String>(selected.data);
                if(ret.size() == 0)
                    return;
                ret.add(selected.name);
                setText(ret);
            }
            //e.consume();
        });
        */

        lv.setOnMouseClicked(e -> {
            if(longPress) {
                longPress = false;
                return;
            }

            if(cmFlag) {
                cm.hide();
                lv.getSelectionModel().clearSelection();
                cmFlag = false;
                return;
            }
            if(lv.getSelectionModel().isEmpty())
                return;
            selected = lv.getSelectionModel().getSelectedItem();
            ArrayList<String> ret = new ArrayList<String>(selected.data);
            if(ret.size() == 0)
                return;
            ret.add(selected.name);
            setText(ret);
            swap();
            e.consume();

        });
        lv.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                if (cm.isShowing()) {
                    cm.hide();
                    this.requestFocus();
                    lv.getSelectionModel().clearSelection();
                    selected = null;
                    cp.esc();
                }
                else {
                    lv.getSelectionModel().clearSelection();
                    this.requestFocus();
                    Vars.root.cp.esc();
                }
                e.consume();
            }
        });


        lv.setContextMenu(cm);
        Util.addHoldHandler(lv, Duration.seconds(0.5), e -> {
            longPress = true;
            cmFlag = true;
            e.consume();
            cm.show(lv, e.getSceneX(), e.getSceneY());
        });
        cm.getItems().add(save);
        cm.getItems().add(del);
        cm.getItems().add(dummy);
        cm.getItems().add(delAll);
        cm.setHideOnEscape(true);
        save.setOnAction(e -> {
            cm.hide();
            c.savePage(lv.getSelectionModel().getSelectedItem());
            lv.getSelectionModel().clearSelection();
                    //selected);
            cmFlag = false;
            selected = null;
            lv.requestFocus();
            e.consume();
        });
        del.setOnAction(e -> {
            cm.hide();
            c.delPage(lv.getSelectionModel().getSelectedItem());
            lv.getSelectionModel().clearSelection();
                    //selected);
            cmFlag = false;
            selected = null;
            lv.requestFocus();
            e.consume();
        });

        delAll.setOnAction(e -> {
            cm.hide();
            if(Vars.state == VIEW_CNT.ALL) {
                ctrl.delAll();
                Vars.allPages = new ArrayList<Page>();
                Vars.pages = new ArrayList<Page>();
            }
            else {
                ctrl.saveOwn(new ArrayList<Page>());
                Vars.ownPages = new ArrayList<Page>();
                Vars.pages = Vars.ownPages;
            }
            Vars.updateReady = true;
        });

        this.getChildren().add(lv);
        this.getChildren().add(ta);



    }

    public void setAll() {
        state = VIEW_CNT.ALL;
        Vars.state = VIEW_CNT.ALL;
        Vars.pages = Vars.allPages;
        Vars.updateReady = true;

        del.setVisible(false);
        save.setVisible(true);
        delAll.setVisible(true);
    }
    public void setOwn() {
        state = VIEW_CNT.OWN;

        Vars.state = VIEW_CNT.OWN;
        Vars.pages = Vars.ownPages;
        Vars.updateReady = true;

        save.setVisible(false);
        del.setVisible(true);
        delAll.setVisible(true);
    }

    public void swap() {
        flag = !flag;
        ta.setVisible(flag);
        lv.setVisible(!flag);
    }

    public boolean back() {
        if(flag) {
            swap();
            return true;
        }
        return false;
    }

    public void setText(ArrayList<String> strs) {

        int l = strs.size();
        String val = strs.get(l - 1) + "\n\n\n";
        strs.remove(l - 1);

        boolean flag = false;
        for(String s : strs) {
            val = val + s + "\n";
            if (flag)
                val += "\n";
            flag = !flag;
        }
        ta.setText(val);
    }

    public void setPages(ArrayList<Page> pages) {
        lv.setItems(FXCollections.observableArrayList(pages));
    }
}
