package com.hfad.simon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartScreen extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start_screen);
  }

  public void onClickStartGame(View view) {
    Intent intent = new Intent(this, GameScreen.class);
    startActivity(intent);
  }

  public void onClickHighScores(View view) {
    Intent intent = new Intent(this, HighScores.class);
    startActivity(intent);
  }
}
