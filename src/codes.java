/**
    ////////// Key input ////////
    scene.setOnKeyPressed(e ->
    {
        switch (e.getCode())
        {
            case SPACE:
            playerScoreText.setText("Score: " + (playerScore + 1));
            playerScore++;
            break;
        }
    });

    //////// Update Text ////////
    'Item'.setText("Text");
*/