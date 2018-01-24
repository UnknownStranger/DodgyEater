package sample;
//January 19-20, 2018
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("resources/fxml/sample.fxml"));
        Scene scene = new Scene(root,600,600, Color.BLACK);
        primaryStage.setTitle("Dodgy Eater: A Snake Alternative");
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("sample/Snake.png")));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            int lastPressed;
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        if((lastPressed != 38) && (lastPressed != 40) && !Controller.gameOver){
                            Controller.setDirection(38);
                            lastPressed = 38;
                        }
                        break;
                    case DOWN:
                        if((lastPressed != 38) && (lastPressed != 40) && !Controller.gameOver){
                            Controller.setDirection(40);
                            lastPressed = 40;
                        }
                        break;
                    case LEFT:
                        if((lastPressed != 37) && (lastPressed != 39) && !Controller.gameOver) {
                            Controller.setDirection(37);
                            lastPressed = 37;
                        }
                        break;
                    case RIGHT:
                        if((lastPressed != 37) && (lastPressed != 39) && !Controller.gameOver) {
                            Controller.setDirection(39);
                            lastPressed = 39;
                        }
                        break;

                    case Y:
                        if(Controller.gameOver){
                            Controller.restart();
                        }
                }
            }
        });

    }



    public static void main(String[] args) {
        launch(args);

    }
}
