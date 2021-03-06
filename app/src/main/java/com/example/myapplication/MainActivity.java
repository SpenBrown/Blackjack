package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {


    //STARTING CARD PLACEMENTS
    private ImageView playerCard0;
    private ImageView playerCard1;
    private ImageView playerCard2;
    private ImageView playerCard3;
    private ImageView playerCard4;
    private ImageView playerCard5;
    private ImageView playerCard6;

    private ImageView playerCardDown;

    private ImageView dealerCard0;
    private ImageView dealerCard1;
    private ImageView dealerCard2;
    private ImageView dealerCard3;
    private ImageView dealerCard4;
    private ImageView dealerCard5;
    private ImageView dealerCard6;

    //HAND VALUES
    private TextView playerCount;
    private TextView dealerCount;
    private TextView shoeCurrentCount;
    private TextView analyzeCount;

    //BUTTONS
    private Button hitButton;
    private Button standButton;
    private Button doubleDownButton;
    private Button splitButton;
    private Button startButton;
    private Button redealButton;
    private Button reshuffleButton;

    //OTHER ACTIVITIES
    private ImageButton settingsActivity;
    private ImageButton analyzeActivity;
    private ImageButton showShoeButton;

    //DECK OBJECT
    private Deck singleDeck;

    //PLAYER AND DEALER OBJECT
    private Player player1;
    private Dealer dealer1;

    //MISC
    private int playerCardPos = 2;
    private int dealerCardPos = 2;
    private AnalyzeCount currentCount = new AnalyzeCount(0);


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CREATE OBJECTS
        singleDeck = new Deck(1);
        Collections.shuffle(singleDeck.myDeck);
        shoeCurrentCount = findViewById(R.id.shoeCount);
        shoeCurrentCount.setText("Cards left: " + Integer.toString(singleDeck.myDeck.size()));  //fixme:test
        player1 = new Player(singleDeck);
        dealer1 = new Dealer(singleDeck);

        //SET CARD FACES

        playerCard0 = findViewById(R.id.playerCardPos0);
        playerCard1 = findViewById(R.id.playerCardPos1);
        playerCard2 = findViewById(R.id.playerCardPos2);
        playerCard3 = findViewById(R.id.playerCardPos3);
        playerCard4 = findViewById(R.id.playerCardPos4);
        playerCard5 = findViewById(R.id.playerCardPos5);
        playerCard6 = findViewById(R.id.playerCardPos6);

        playerCardDown = findViewById(R.id.playerDoubleDownCard);

        dealerCard0 = findViewById(R.id.dealerCardPos0);
        dealerCard1 = findViewById(R.id.dealerCardPos1);
        dealerCard2 = findViewById(R.id.dealerCardPos2);
        dealerCard3 = findViewById(R.id.dealerCardPos3);
        dealerCard4 = findViewById(R.id.dealerCardPos4);
        dealerCard5 = findViewById(R.id.dealerCardPos5);
        dealerCard6 = findViewById(R.id.dealerCardPos6);

        //SET CARD COUNT STARTING AT 0
        playerCount = findViewById(R.id.playerCount);
        dealerCount = findViewById(R.id.dealerCount);
        analyzeCount = findViewById(R.id.analyzeCount);
        analyzeCount.setText("Count: " + Integer.toString(currentCount.getValue()));

        //SET BUTTONS
        hitButton = findViewById(R.id.buttonHit);
        standButton = findViewById(R.id.buttonStand);
        doubleDownButton = findViewById(R.id.buttonDD);
        splitButton = findViewById(R.id.buttonSplit);
        startButton = findViewById(R.id.buttonStart);
        redealButton = findViewById(R.id.buttonRedeal);
        reshuffleButton = findViewById(R.id.buttonReshuffle);

        //SET THE PAGE BUTTONS
        settingsActivity = findViewById(R.id.imageSettingButton);
        analyzeActivity = findViewById(R.id.imageAnalyzeButton);
        showShoeButton = findViewById(R.id.gameShoeButton);

        //when the player clicks the START button
        startButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                int playerLeft = singleDeck.myDeck.get(0).getValue();
                int playerRight = singleDeck.myDeck.get(1).getValue();

                player1.playerHit(playerCard0, currentCount);
                player1.playerHit(playerCard1, currentCount);
                playerCount.setText(Integer.toString(player1.getPlayerHandValue()));

                dealer1.dealerHit(dealerCard0, currentCount);
                dealerCount.setText(Integer.toString(dealer1.getDealerHandValue()));
                dealer1.dealerHitBottom(dealerCard1);

                if (dealer1.dealerHas21() || player1.playerHas21()) {
                    endHand();
                } else {

                    startButton.setVisibility(View.GONE);
                    hitButton.setVisibility(View.VISIBLE);
                    standButton.setVisibility(View.VISIBLE);
                    if (playerLeft == playerRight) {
                        splitButton.setVisibility(View.VISIBLE);
                    }
                    if (player1.getPlayerHandValue() <= 11) {
                        doubleDownButton.setVisibility(View.VISIBLE);
                    }
                }
                analyzeCount.setText("Count: " + Integer.toString(currentCount.getValue()));
                shoeCurrentCount.setText("Cards left: " + Integer.toString(singleDeck.myDeck.size()));  //fixme:test
/*
                //SharedPreferences sharedPreferencesPro = getSharedPreferences(" ", MODE_PRIVATE);
                SharedPreferences sharedPreferencesDD = getSharedPreferences(" ", MODE_PRIVATE);
                SharedPreferences sharedPreferencesSpl = getSharedPreferences(" ", MODE_PRIVATE);
                //final boolean probVal = sharedPreferencesPro.getBoolean(getString(R.string.Probability), false);
                final boolean DDVal = sharedPreferencesDD.getBoolean(getString(R.string.double_down), false);
                final boolean SplVal = sharedPreferencesSpl.getBoolean(getString(R.string.split), false);

                if ((DDVal == true) && (SplVal == true)) {
                    doubleDownButton.setVisibility(View.VISIBLE);
                    splitButton.setVisibility(View.VISIBLE);
                } else if ((DDVal == true) && (SplVal == false)) {
                    doubleDownButton.setVisibility(View.VISIBLE);
                    splitButton.setVisibility(View.GONE);
                } else if ((DDVal == false) && (SplVal == true)) {
                    doubleDownButton.setVisibility(View.GONE);
                    splitButton.setVisibility(View.VISIBLE);
                } else {
                    doubleDownButton.setVisibility(View.GONE);
                    splitButton.setVisibility(View.GONE);
                }
                */
            }

        });


        //When player uses hit
        hitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                doubleDownButton.setVisibility(View.GONE);
                splitButton.setVisibility(View.GONE);

                switch (playerCardPos) {
                    case 2:
                        player1.playerHit(playerCard2, currentCount);
                        analyzeCount.setText("Count: " + Integer.toString(currentCount.getValue()));
                        playerCount.setText(Integer.toString(player1.getPlayerHandValue()));
                        playerCardPos++;
                        break;
                    case 3:
                        player1.playerHit(playerCard3, currentCount);
                        analyzeCount.setText("Count: " + Integer.toString(currentCount.getValue()));
                        playerCount.setText(Integer.toString(player1.getPlayerHandValue()));
                        playerCardPos++;
                        break;
                    case 4:
                        player1.playerHit(playerCard4, currentCount);
                        analyzeCount.setText("Count: " + Integer.toString(currentCount.getValue()));
                        playerCount.setText(Integer.toString(player1.getPlayerHandValue()));
                        playerCardPos++;
                        break;
                    case 5:
                        player1.playerHit(playerCard5, currentCount);
                        analyzeCount.setText("Count: " + Integer.toString(currentCount.getValue()));
                        playerCount.setText(Integer.toString(player1.getPlayerHandValue()));
                        playerCardPos++;
                        break;
                    case 6:
                        player1.playerHit(playerCard6, currentCount);
                        playerCardPos++;
                        break;
                }
                if (player1.isPlayerBust()) {
                    dealerCard1.setImageResource(dealer1.getDealerBottomCard().getDrawable());
                    if (dealer1.getDealerBottomCard().getValue() <= 6 && dealer1.getDealerBottomCard().getValue() >= 2) {
                        currentCount.add();
                    }else if (dealer1.getDealerBottomCard().getValue() >= 10 || dealer1.getDealerBottomCard().getValue() == 1) {
                        currentCount.sub();
                    }
                    analyzeCount.setText("Count: " + Integer.toString(currentCount.getValue()));
                    dealerCount.setText(Integer.toString(dealer1.getDealerHandValue()));
                    hitButton.setVisibility(View.GONE);
                    standButton.setVisibility(View.GONE);

                    if (singleDeck.myDeck.size() < 20) {
                        redealButton.setVisibility(View.GONE);
                        reshuffleButton.setVisibility(View.VISIBLE);

                    } else {
                        redealButton.setVisibility(View.VISIBLE);
                    }
                }
                analyzeCount.setText("Count: " + Integer.toString(currentCount.getValue()));
                shoeCurrentCount.setText("Cards left: " + Integer.toString(singleDeck.myDeck.size()));  //fixme:test

            }
        });

        splitButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Split functionality in development!", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        //When the player clicks the DOUBLE DOWN button
        doubleDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hitButton.setVisibility(View.GONE);
                standButton.setVisibility(View.GONE);
                splitButton.setVisibility(View.GONE);
                doubleDownButton.setVisibility(View.GONE);

                player1.playerDoubleDown(playerCardDown, currentCount);
                analyzeCount.setText("Count: " + Integer.toString(currentCount.getValue()));
                shoeCurrentCount.setText("Cards left: " + Integer.toString(singleDeck.myDeck.size()));  //fixme:test
                playerCount.setText(Integer.toString(player1.getPlayerHandValue()));
                standButton.performClick();


            }
        });

        //when the player clicks the STAND button
        standButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hitButton.setVisibility(View.GONE);
                standButton.setVisibility(View.GONE);
                splitButton.setVisibility(View.GONE);
                doubleDownButton.setVisibility(View.GONE);

                dealerCard1.setImageResource(dealer1.getDealerBottomCard().getDrawable());

                if (dealer1.getDealerBottomCard().getValue() <= 6 && dealer1.getDealerBottomCard().getValue() >= 2) {
                    currentCount.add();
                }else if (dealer1.getDealerBottomCard().getValue() >= 10 || dealer1.getDealerBottomCard().getValue() == 1) {
                    currentCount.sub();
                }
                analyzeCount.setText("Count: " + Integer.toString(currentCount.getValue()));
                dealerCount.setText(Integer.toString(dealer1.getDealerHandValue()));

                while (dealer1.getDealerHandValue() < 17) {
                    switch (dealerCardPos) {
                        case 2:
                            dealer1.dealerHit(dealerCard2, currentCount);
                            dealerCount.setText(Integer.toString(dealer1.getDealerHandValue()));
                            dealerCardPos++;
                            break;
                        case 3:
                            dealer1.dealerHit(dealerCard3, currentCount);
                            dealerCount.setText(Integer.toString(dealer1.getDealerHandValue()));
                            dealerCardPos++;
                            break;
                        case 4:
                            dealer1.dealerHit(dealerCard4, currentCount);
                            dealerCount.setText(Integer.toString(dealer1.getDealerHandValue()));
                            dealerCardPos++;
                            break;
                        case 5:
                            dealer1.dealerHit(dealerCard5, currentCount);
                            dealerCount.setText(Integer.toString(dealer1.getDealerHandValue()));
                            dealerCardPos++;
                            break;
                        case 6:
                            dealer1.dealerHit(dealerCard6, currentCount);
                            dealerCount.setText(Integer.toString(dealer1.getDealerHandValue()));
                            dealerCardPos++;
                            break;
                    }
                }
                if (singleDeck.myDeck.size() < 20) {
                    redealButton.setVisibility(View.GONE);
                    reshuffleButton.setVisibility(View.VISIBLE);

                } else {
                    redealButton.setVisibility(View.VISIBLE);
                    shoeCurrentCount.setText("Cards left: " + Integer.toString(singleDeck.myDeck.size()));  //fixme:test
                }
                analyzeCount.setText("Count: " + Integer.toString(currentCount.getValue()));

            }
        });

        // restart the board after a hand is finished
        redealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBoard();
                player1.playerReset();
                dealer1.dealerReset();
                redealButton.setVisibility(View.GONE);
                startButton.performClick();
            }
        });

        //reshuffle the deck
        reshuffleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clearBoard();
                player1.playerReset();
                dealer1.dealerReset();
                shoeReset();
                reshuffleButton.setVisibility(View.GONE);
                startButton.performClick();

            }
        });


        //shows what is left in the shoe when clicked
        showShoeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        //when the player clicks the settings icon
        settingsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextPage = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(nextPage);
            }
        });

        //when the player clicks the graph icon
        analyzeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextPage = new Intent(MainActivity.this, AnalyzeActivity.class);
                startActivity(nextPage);
            }
        });

    }

    private void shoeReset() {
        //CREATE OBJECTS
        singleDeck = new Deck(1);
        Collections.shuffle(singleDeck.myDeck);
        shoeCurrentCount = findViewById(R.id.shoeCount);
        shoeCurrentCount.setText("Cards left: " + Integer.toString(singleDeck.myDeck.size()));  //fixme:test
        player1 = new Player(singleDeck);
        dealer1 = new Dealer(singleDeck);
        currentCount = new AnalyzeCount(0);
    }

    private void clearBoard() {
        //SET CARD FACES
        playerCard0.setImageResource(R.drawable.gray_back);
        playerCard1.setImageResource(R.drawable.gray_back);
        playerCard2.setVisibility(View.GONE);
        playerCard3.setVisibility(View.GONE);
        playerCard4.setVisibility(View.GONE);
        playerCard5.setVisibility(View.GONE);
        playerCard6.setVisibility(View.GONE);

        playerCardDown.setVisibility(View.GONE);

        dealerCard0.setImageResource(R.drawable.gray_back);
        dealerCard1.setImageResource(R.drawable.gray_back);
        dealerCard2.setVisibility(View.GONE);
        dealerCard3.setVisibility(View.GONE);
        dealerCard4.setVisibility(View.GONE);
        dealerCard5.setVisibility(View.GONE);
        dealerCard6.setVisibility(View.GONE);


        playerCardPos = 2;
        dealerCardPos = 2;
        //SET CARD COUNT STARTING AT
        playerCount.setText("0");
        dealerCount.setText("0");
        analyzeCount.setText("0");

    }

    private void endHand() {
        dealerCard1.setImageResource(dealer1.getDealerBottomCard().getDrawable());
        if (dealer1.getDealerBottomCard().getValue() <= 6 && dealer1.getDealerBottomCard().getValue() >= 2) {
            currentCount.add();
        }else if (dealer1.getDealerBottomCard().getValue() >= 10 || dealer1.getDealerBottomCard().getValue() == 1) {
            currentCount.sub();
        }
        analyzeCount.setText("Count: " + Integer.toString(currentCount.getValue()));
        dealerCount.setText(Integer.toString(dealer1.getDealerHandValue()));

        redealButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE);
        hitButton.setVisibility(View.GONE);
        standButton.setVisibility(View.GONE);
        splitButton.setVisibility(View.GONE);
        doubleDownButton.setVisibility(View.GONE);
    }
};

class AnalyzeCount {
    public Integer value;
    
    AnalyzeCount(Integer value) {
        this.value = value;
    }
    public void add() {value++;}
    public void sub() {value--;}
    public Integer getValue() {return value;}
}

