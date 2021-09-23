import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.util.Timer;
import java.util.TimerTask;

public class main extends Application
{
    int playerScore, time;
    Text playerScoreText, timeText;
    private Pane paneRoot = new Pane();
    Rectangle rect1, rect2;
    Line line1, line2;
    private double t = 0;

    private Parent createContent()
    {
        paneRoot.setPrefSize(800, 600);
        rect1 = rectangle(0, 200);
        rect2 = rectangle(750, 200);
        line1 = gameLines(0, 30, 800, 30, 0, 50);
        line2 = gameLines(50, 0, 50, 80, 400 - 50, 0);
        setPlayerScoreText();
        setTimeText();
        // Set the background to black
        paneRoot.setBackground(new Background
                              (new BackgroundFill(Color.BLACK, null, null)));

        paneRoot.getChildren().addAll(playerScoreText, rect1, rect2, line1,
                                      line2, timeText);

        AnimationTimer timer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                update();
            }
        };

        timer.start();
        return paneRoot;
    }

    private void update()
    {
        t += 0.016;

        if (t > 1)
        {
            t = 0;
            time++;
            timeText.setText("Time: " + time);
        }
    }

    // Player Rectangle
    Rectangle rectangle(int x, int y)
    {
        Rectangle rect = new Rectangle(50, 200);
        rect.setFill(Color.WHITE);
        rect.setX(x);
        rect.setY(y);
        return rect;
    }

    // Game Lines
    Line gameLines(int xStart, int yStart, int xEnd, int yEnd, int xLocation,
                   int yLocation)
    {
        Line line = new Line(xStart, yStart, xEnd, yEnd);
        line.setStroke(Color.WHITE);
        line.setTranslateX(xLocation);
        line.setTranslateY(yLocation);
        return line;
    }

    // Player Score Text
    private void setPlayerScoreText()
    {
        playerScoreText = new Text("Score: " + String.valueOf(playerScore));
        playerScoreText.setFill(Color.WHITE);
        playerScoreText.setX(200);
        playerScoreText.setY(50);
    }

    private void setTimeText()
    {
        // Game timer text
        timeText = new Text("Time:" + time);
        timeText.setFill(Color.WHITE);
        timeText.setX(400);
        timeText.setY(50);
    }


    @Override
    public void start(Stage primaryStage)
    {
       // set the scene
        Scene scene = new Scene(createContent());

        // Player score test: increments on space press
        scene.setOnKeyPressed(e ->
        {
            switch (e.getCode())
            {
                case SPACE:
                    playerScoreText.setText("Score: " + (playerScore + 1));
                    playerScore++;
                    break;
                case UP:
                    rect1.setY(rect1.getY() - 20);
                    break;
                case DOWN:
                    rect1.setY(rect1.getY() + 20);
                    break;
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();

        // Closes the program when the x is pressed.
        primaryStage.setOnCloseRequest(e -> System.exit(0));
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
