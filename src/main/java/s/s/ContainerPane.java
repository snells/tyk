package s.s;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import s.Page;


public class ContainerPane extends StackPane {
    public boolean flag = true;
    private VBox pageView = new VBox();
    private StackPane menuView = new StackPane();
    ViewPane vp = new ViewPane(Vars.ctrl);
    public ControlPane cp = new ControlPane(Vars.ctrl, vp);
    Button courseBut = new Button("Cources");
    Button saved = new Button("Saved courses");
    Text info = new Text(Vars.text);
    Button dlData = new Button("Download courses");
    Button startDl = new Button("start");
    TextField insertYear = new TextField("2015");
    Text yearText = new Text("-  2016");

    public void setDefault() {
        courseBut.setVisible(true);
        saved.setVisible(true);
        info.setVisible(true);
        dlData.setVisible(true);
        yearText.setVisible(false);
        insertYear.setVisible(false);
        startDl.setVisible(false);
    }

    public void showDl() {
        yearText.setVisible(true);
        insertYear.setVisible(true);
        startDl.setVisible(true);
    }



    public ContainerPane() {
        setDefault();
        pageView.setVisible(false);
        menuView.setAlignment(Pos.TOP_CENTER);
        Util.butPos(courseBut, 0, 20, 200);
        Util.butPos(saved, 0, 70, 200);
        Util.butPos(dlData, 0, 120, 200);
        insertYear.setMaxWidth(50);
        insertYear.setTranslateX(-75);
        insertYear.setTranslateY(170);
        insertYear.getProperties().put("vkType", "numeric");
        yearText.setTranslateY(175);
        yearText.setTranslateX(-25);
        Util.butPos(startDl, 75, 170, 50);
        info.setTranslateY(220);
        info.setWrappingWidth(300);
        info.setTextAlignment(TextAlignment.CENTER);


        Task<Void> yearInfoTask = new Task<Void>() {
            @Override public Void call() {
                while(true) {
                    try {
                        int y = Integer.parseInt(insertYear.getText());
                        Vars.insertYear = y;
                        updateMessage("-  " + (y + 1));
                    } catch (Exception e) {
                        updateMessage("-  Error");
                    }
                    try { Thread.sleep(50); } catch (Exception e){}
                }
            }};
        Thread yearInfoT = new Thread(yearInfoTask);
        yearInfoT.setDaemon(true);
        yearText.textProperty().bind(yearInfoTask.messageProperty());
        yearInfoT.start();


        courseBut.setOnMouseClicked(e -> {
            vp.setAll();
            swap();
            vp.lv.requestFocus();
        });
        saved.setOnMouseClicked(e -> {
            vp.setOwn();
            swap();
            vp.lv.requestFocus();
        });
        dlData.setOnMouseClicked(e -> {
            showDl();
            Vars.text = "Insert year";
        });


        vp.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("change " + newSceneWidth);
                cp.setWidth(newSceneWidth.intValue());
            }
        });

        this.setOnKeyPressed(e ->{
            KeyCode k = e.getCode();
            if(e.KEY_RELEASED == e.getEventType())
                return;
            if(k == KeyCode.ESCAPE) {
                cp.esc();
                e.consume();
            }
        });

        startDl.setOnMouseClicked(e -> {
            try {
                if(insertYear.getText().length() == 0)
                    return;
                else
                     Vars.insertYear = Integer.parseInt(insertYear.getText());
            } catch (Exception ex) {
                Vars.text = "Error with inputted value";
                return;
            }
            setDefault();
            Task<Void> task = new Task<Void>() {
                @Override public Void call() {
                    Vars.text = "Donwloading year " + Vars.insertYear;
                    Vars.ctrl.dlYear(Vars.insertYear);
                    return null;
                }
            };
            Thread t = new Thread(task);
            t.setDaemon(true);
            t.start();
        });

        Task task = new Task<Void>() {
            @Override public Void call() {
                while(true) {
                    updateMessage(Vars.text);
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {}
                }
            }
        };
        info.textProperty().unbind();
        info.textProperty().bind(task.messageProperty());
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();




        menuView.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {

                if (newSceneWidth.intValue() < 500) {
                    menuView.setAlignment(Pos.TOP_CENTER);
                    Util.butPos(courseBut, 0, 20, 200);
                    Util.butPos(saved, 0, 70, 200);
                    Util.butPos(dlData, 0, 120, 200);
                    insertYear.setTranslateX(-75);
                    insertYear.setTranslateY(170);
                    yearText.setTranslateY(175);
                    yearText.setTranslateX(-25);
                    Util.butPos(startDl, 75, 170, 50);
                    info.setTranslateY(220);

                } else {
                    menuView.setAlignment(Pos.CENTER);
                    Util.butPos(courseBut, 0, -50, 200);
                    Util.butPos(saved, 0, 0, 200);
                    Util.butPos(dlData, 0, 50, 200);
                    insertYear.setTranslateX(-75);
                    insertYear.setTranslateY(120);
                    yearText.setTranslateY(122);
                    yearText.setTranslateX(-25);
                    Util.butPos(startDl, 55, 120, 90);
                    info.setTranslateY(170);
                }
            }
        });


        Task<ObservableList<Page>> vpTask = new Task<ObservableList<Page>>() {
            @Override
            public ObservableList<Page> call() {
                while (true) {
                    if (Vars.updateReady) {
                        updateValue(FXCollections.observableArrayList(Vars.pages));
                        Vars.updateReady = false;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {}
                }
            }
        };
        vp.lv.itemsProperty().bind(vpTask.valueProperty());
        Thread vpt = new Thread(vpTask);
        vpt.setDaemon(true);
        vpt.start();



        pageView.getChildren().addAll(cp, vp);
        menuView.getChildren().addAll(courseBut, saved, dlData, insertYear, yearText, startDl, info);
        this.getChildren().addAll(menuView, pageView);
    }


    public void esc() {
        if(flag)
            System.exit(0);
        else
            cp.esc();
    }
    
    public void swap() {
        pageView.setVisible(flag);
        menuView.setVisible(!flag);
        flag = !flag;
    }


}
