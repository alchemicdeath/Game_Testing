import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.util.Duration;

import java.util.Random;

public class Pong_Test extends Application
{
    // Score and direction variables //
    int player1Score = 0, player2Score = 0,
        directX = 1, directY = 1;
    Text player1ScoreText, player2ScoreText;

    ///// Screen Values /////
    final int WIDTH = 800, HEIGHT = 600;

    /// Ball and Paddle Values ///
    final int BALL_RADIUS = 10, PADDLE_W = 20, PADDLE_H = 100;

    ////// Create Ball and Paddles //////
    private final Circle ball = new Circle(BALL_RADIUS);
    private final Rectangle player1Paddle = new Rectangle(PADDLE_W, PADDLE_H);
    private final Rectangle player2Paddle = new Rectangle(PADDLE_W, PADDLE_H);

    // Line Background //
    private final Line lineV = new Line((WIDTH /2.0), 0, (WIDTH /2.0), HEIGHT);
    private final Line lineH = new Line(0, 100, WIDTH, 100);

    //// Timeline and run check ////
    private final Timeline timeline = new Timeline();
    private final boolean running = true;

    Random random = new Random();

    ////////// Game Content //////////
    private Parent createContent()
    {
        // Core set up //
        Pane root = new Pane();
        root.setPrefSize(WIDTH, HEIGHT);

        /// Create paddles ///
        paddles(player1Paddle, 0);
        paddles(player2Paddle, WIDTH - PADDLE_W);

        //// Create the score texts ////
        player1ScoreText = texts(300, player1Score);
        player2ScoreText = texts(450, player2Score);

        // set the ball and line color
        ball.setFill(Color.WHITE);
        lineV.setStroke(Color.WHITE);
        lineH.setStroke(Color.WHITE);

        //// Set background Color ////
        root.setBackground(new Background(new BackgroundFill
                                         (Color.BLACK, null, null)));

        KeyFrame frame = new KeyFrame(Duration.seconds(0.016), event ->
        {
            if (!running)
                return;

            // Start the ball movement
            ball.setTranslateX(ball.getTranslateX() + 5 * directX);
            ball.setTranslateY(ball.getTranslateY() + 5 * directY);

            ballBorders();
            paddleBorders();
            updateScore();
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        gameStart();

        /// Add all to pane ///
        root.getChildren().addAll(ball,player1Paddle,player2Paddle,
                lineH, lineV, player1ScoreText, player2ScoreText);
        return root;
    }

    void updateScore()
    {
        player1ScoreText.setText("Score: " + player1Score);
        player2ScoreText.setText("Score: " + player2Score);
    }

    //// Ball Borders ////
    void ballBorders()
    {
        if (ball.getTranslateY() == 100)
        {
            directY = 1;
        }
        if (ball.getTranslateY() == HEIGHT - BALL_RADIUS)
        {
            directY = -1;
        }
        if (ball.getTranslateX() == 0)
        {
            gameStart();
            player2Score = player2Score + 1;
            direction();
        }
        if (ball.getTranslateX() == WIDTH)
        {
            gameStart();
            player1Score = player1Score + 1;
            direction();
        }
    }

    /// Randomize Direction ///
    void direction()
    {
        int number = random.nextInt(2);
        if (number == 0)
        {
            directX = 1;
        }
        if (number == 1)
        {
            directX = -1;
        }
    }

    ///// Paddle Borders /////
    void paddleBorders()
    {
        ///// Player 1 /////
        if (ball.getBoundsInParent().intersects(player1Paddle.getBoundsInParent()))
        {
            System.out.println("LEFT");
            directX = 1;
        }

        if (player1Paddle.getBoundsInParent().intersects(lineH.getBoundsInParent()))
        {
            player1Paddle.setTranslateY(100);
        }
        if (player1Paddle.getTranslateY() >= 600 - PADDLE_H)
        {
            player1Paddle.setTranslateY(600 - PADDLE_H);
        }

        ///// Player 2 /////
        if (ball.getBoundsInParent().intersects(player2Paddle.getBoundsInParent()))
        {
            System.out.println("LEFT");
            directX = -1;
        }

        if (player2Paddle.getBoundsInParent().intersects(lineH.getBoundsInParent()))
        {
            player2Paddle.setTranslateY(100);
        }

        if (player2Paddle.getTranslateY() >= 600 - PADDLE_H)
        {
            player2Paddle.setTranslateY(600 - PADDLE_H);
        }
    }

    ///// Score Text ////
    Text texts(int x, int score)
    {
        Text text = new Text(x, 50, "Score: " + score);
        text.setFill(Color.WHITE);
        return text;
    }

    /// Paddles ///
    void paddles(Rectangle paddle, int x)
    {
        paddle.setTranslateX(x);                            // X location
        paddle.setTranslateY(HEIGHT / 2.0 - PADDLE_W);      // Y Location
        paddle.setFill(Color.WHITE);                        // Color
    }

    void gameStart()
    {
        ball.setTranslateX((WIDTH / 2.0));
        ball.setTranslateY((HEIGHT / 2.0) + 100);
        timeline.play();
    }

    @Override
    public void start(Stage primaryStage)
    {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(event ->
        {
            switch (event.getCode())
            {
                case W:
                    player1Paddle.setTranslateY(player1Paddle.getTranslateY() - 20);
                    break;
                case S:
                    player1Paddle.setTranslateY(player1Paddle.getTranslateY() + 20);
                    break;
                case UP:
                    player2Paddle.setTranslateY(player2Paddle.getTranslateY() - 20);
                    break;
                case DOWN:
                    player2Paddle.setTranslateY(player2Paddle.getTranslateY() + 20);
                    break;
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
