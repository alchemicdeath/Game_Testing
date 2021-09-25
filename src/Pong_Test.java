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
import javafx.scene.control.*;
import javafx.util.Duration;

public class Pong_Test extends Application
{
    // Variables //
    int player1Score = 0, player2Score = 0,
        time = 0,
        directX = 1, directY = 1;
    Text player1ScoreText, player2ScoreText,
         timeText;

    ///// Screen Values /////
    final int WIDTH = 800, HEIGHT = 600;

    /// Ball and Paddle Values ///
    final int BALL_RADIUS = 10,
              PADDLE_W = 20, PADDLE_H = 100;

    ////// Create Ball and Paddles //////
    private Circle ball = new Circle(BALL_RADIUS);
    private Rectangle player1Paddle = new Rectangle(PADDLE_W, PADDLE_H);
    private Rectangle player2Paddle = new Rectangle(PADDLE_W, PADDLE_H);

    // Line Background //
    private Line lineVrt = new Line((WIDTH / 2.0), 0, (WIDTH / 2.0), HEIGHT);
    private Line lineHrz = new Line(0, 100, WIDTH,
            100);

    private Timeline timeline = new Timeline();
    private boolean running = true;

    ////////// Game Content //////////
    private Parent createContent()
    {
        // Core set up //
        Pane root = new Pane();
        root.setPrefSize(WIDTH, HEIGHT);

        /// Create paddles ///
        paddles(player1Paddle, 0, (HEIGHT / 2 - PADDLE_W));
        paddles(player2Paddle, WIDTH - PADDLE_W, (HEIGHT / 2) - PADDLE_W);

        //// Create the score texts ////
        player1ScoreText = texts(300, 50, player1Score);
        player2ScoreText = texts(450, 50, player2Score);

        // set the ball and line color
        ball.setFill(Color.WHITE);
        lineVrt.setStroke(Color.WHITE);
        lineHrz.setStroke(Color.WHITE);

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
            playerBorders();


        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        gameStart();

        /// Add all to pane ///
        root.getChildren().addAll(ball,player1Paddle,player2Paddle,
                lineHrz, lineVrt, player1ScoreText, player2ScoreText);
        return root;
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
            directX = 1;
        }
        if (ball.getTranslateX() == WIDTH)
        {
            directX = -1;
        }
    }

    ////// Player Borders /////
    void playerBorders()
    {
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
        ///// Player 2 /////
        if (ball.getBoundsInParent().intersects(player2Paddle.getBoundsInParent()))
        {
            System.out.println("LEFT");
            directX = -1;
        }

    }

    ///// Text ////
    Text texts(int x, int y, int score)
    {
        Text text = new Text(x, y, "Score: " + score);
        text.setFill(Color.WHITE);
        return text;
    }

    /// Paddles ///
    Rectangle paddles(Rectangle paddle,int x, int y)
    {
        paddle.setTranslateX(x);        // X location
        paddle.setTranslateY(y);        // Y Location
        paddle.setFill(Color.WHITE);    // Color
        return paddle;
    }

    void gameStart()
    {
        ball.setTranslateX((WIDTH / 2.0) + 100);
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
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
