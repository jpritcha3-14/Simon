package com.hfad.simon;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class GameScreen extends Activity {

  ArrayList<Integer> pattern = new ArrayList<>();
  Iterator<Integer> patternIterator = null;
  Button previousButton = null;
  Random random = new Random();
  TextView scoreText;
  //boolean showingColorPattern = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game_screen);

    // Add an initial button to the pattern and show it.
    pattern.add(getRanodmColor());
    showColorPatternDelayed(500);

    // Set the initial score text and set the background to black
    scoreText = (TextView) findViewById(R.id.score);
    View root = scoreText.getRootView();
    root.setBackgroundColor(getResources().getColor(android.R.color.black));
    scoreText.setText(getString(R.string.score) + (pattern.size()-1));
  }


  public void onClickGetButton(View view) {

    if (patternIterator == null) {
      patternIterator = pattern.iterator();
    }

      // Play corresponding sound when correct button in pattern is pressed
      if (patternIterator.next() == view.getId()) {
        MakeSound.buttonSound(view.getId());

      // If the incorrect button is pressed, play fail sound, clear pattern, and restart the game.
      } else {
        MakeSound.buttonSound(-1);
        pattern.clear();
        pattern.add(getRanodmColor());
        patternIterator = pattern.iterator();
        showColorPatternDelayed(1500);
      }

    // If the correct pattern has been entered, add a button to the pattern, restart the iterator, and show the pattern.
    if (!patternIterator.hasNext()) {
      pattern.add(getRanodmColor());
      patternIterator = pattern.iterator();
      scoreText.setText(getString(R.string.score) + (pattern.size()-1));
      showColorPatternDelayed(1000);

    }
  }

  public void showColorPattern() {
    scoreText.setText(getString(R.string.score) + (pattern.size()-1));
    final Handler handler = new Handler();
    for (int i = 0; i <= pattern.size(); i++) {
      if (i == 0) {
        int buttonId = pattern.get(i);
        Button button = (Button) findViewById(buttonId);
        handler.postDelayed(new Blink(getPushedButtonColor(button), button), 500 * i);
        previousButton = button;
      } else if (i < pattern.size()) {
        int buttonId = pattern.get(i);
        Button button = (Button) findViewById(buttonId);
        handler.postDelayed(new Blink(getPushedButtonColor(button), button, previousButton, getButtonColor(previousButton)), 500 * i);
        previousButton = button;
      } else {
        handler.postDelayed(new Blink(previousButton, getButtonColor(previousButton)), 500 * i);
      }
    }
  }

  private void showColorPatternDelayed(int delay) {
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        showColorPattern();
      }
    }, delay);
  }

  private int getButtonColor(Button button) {
    if (button.equals(findViewById(R.id.red))) {
      return R.drawable.red;
    } else if (button.equals(findViewById(R.id.blue))) {
      return R.drawable.blue;
    } else if (button.equals(findViewById(R.id.green))) {
      return R.drawable.green;
    } else {
      return R.drawable.yellow;
    }
  }

  private int getPushedButtonColor(Button button) {
    if (button.equals(findViewById(R.id.red))) {
      return R.drawable.red_pushed;
    } else if (button.equals(findViewById(R.id.blue))) {
      return R.drawable.blue_pushed;
    } else if (button.equals(findViewById(R.id.green))) {
      return R.drawable.green_pushed;
    } else {
      return R.drawable.yellow_pushed;
    }
  }

  private int getRanodmColor() {
    int color = random.nextInt(4);
    switch (color) {
      case 0: return R.id.red;
      case 1: return R.id.blue;
      case 2: return R.id.green;
      default : return R.id.yellow;
    }
  }

}
