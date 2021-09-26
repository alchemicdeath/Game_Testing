package Platformer;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application
{
    // maps keys
    private final HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private final ArrayList<Node> platforms = new ArrayList<>();

    private final Pane appRoot = new Pane();
    private final Pane gameRoot = new Pane();
    private final Pane uiRoot = new Pane();

    private Node player;
    private Point2D playerVelocity = new Point2D(0,0);
    private boolean canJump = true;

    private int levelWidth;
    private void initContent()
    {
        Rectangle bg = new Rectangle(1280, 720);

        levelWidth = LevelData.Level1[0].length() * 60;

        for (int i = 0; i < LevelData.Level1.length; i++)
        {
            String line = LevelData.Level1[i];
            for (int j = 0; j < line.length(); j++)
            {
                // reads the level sheet
                switch (line.charAt(j))
                {
                    case '0':   // adds nothing
                        break;
                    case '1':   // adds block
                        Node platform = createEntity(j * 60, i * 60, 60, 60,
                                Color.BROWN);
                        platforms.add(platform);
                        break;
                }
            }
        }
        player = createEntity(0, 600, 40, 40, Color.BLUE);  // create player

        player.translateXProperty().addListener((obs, old, newValue) ->
        {
            int offset = newValue.intValue();

            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
            }
        });
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }

    private void update()
    {
        if (isPressed(KeyCode.SPACE) && player.getTranslateY() >= 5) {
            jumpPlayer();
        }

        if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) {
            movePlayerX(-5);
        }

        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5) {
            movePlayerX(5);
        }

        if (playerVelocity.getY() < 10) {
            playerVelocity = playerVelocity.add(0, 1);
        }

        movePlayerY((int)playerVelocity.getY());
    }

    private void movePlayerX(int value)
    {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++)
        {
            for (Node platform : platforms)
            {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent()))
                {
                    if (movingRight)
                    {
                        // if the player is on the ground allows moving
                        if (player.getTranslateX() + 40 == platform.getTranslateX())
                        {
                            return;
                        }
                    }
                    else
                    {
                        if (player.getTranslateX() == platform.getTranslateX() + 60)
                        {
                            return;
                        }
                    }
                }
            }
            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
        }
    }

    private void movePlayerY(int value)
    {
        boolean movingDown = value > 0;

        for (int i = 0; i < Math.abs(value); i++)
        {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects
                                                 (platform.getBoundsInParent()))
                {
                    if (movingDown)
                    {
                        // if the player's location is equal to that of the
                        // platform it will stand.
                        if (player.getTranslateY() + 40 == platform.getTranslateY())
                        {
                            player.setTranslateY(player.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else
                    {
                        // Prevents player from jumping in/through a block
                        if (player.getTranslateY() == platform.getTranslateY()
                            + 60)
                        {
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }

    // checks if player can perform a jump and how high
    private void jumpPlayer()
    {
        if (canJump)
        {
            // effects player jump height
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }

    private Node createEntity(int x, int y, int w, int h, Color color)
    {
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);

        gameRoot.getChildren().add(entity);
        return entity;
    }

    private boolean isPressed(KeyCode key)
    {
        return keys.getOrDefault(key, false);
    }


    @Override
    public void start(Stage primaryStage)
    {
        initContent();

        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setTitle("Platformer");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now)
            {
                update();
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
