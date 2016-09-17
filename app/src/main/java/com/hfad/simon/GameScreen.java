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
import android.media.AudioTrack;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

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
    pattern.add(getRanodmColor());
    showColorPatternDelayed(500);
    scoreText = (TextView) findViewById(R.id.score);
    View root = scoreText.getRootView();
    root.setBackgroundColor(getResources().getColor(android.R.color.black));
    scoreText.setText(getString(R.string.score) + (pattern.size()-1));
  }

  public void onClickGetButton(View view) {
    Button button = (Button) view;

    if (patternIterator == null) {
      patternIterator = pattern.iterator();
    }

    // Play corresponding sound when correct button in pattern is pressed
      if (patternIterator.next() == view.getId()) {
        buttonSound(view.getId());

    // If the incorrect button is pressed, play fail sounds, clear pattern, and restart the game.
      } else {
        buttonSound(-1);
        pattern.clear();
        pattern.add(getRanodmColor());
        patternIterator = pattern.iterator();
        showColorPatternDelayed(1500);
      }

  // If the correct pattern has been entered, add a button to the pattern, restart the iterator, and show the pattern
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
        handler.postDelayed(new Blink(button), 500 * i);
        previousButton = button;
      } else if (i < pattern.size()) {
        int buttonId = pattern.get(i);
        Button button = (Button) findViewById(buttonId);
        handler.postDelayed(new Blink(button, previousButton, getButtonColor(previousButton)), 500 * i);
        previousButton = button;
      } else {
        handler.postDelayed(new Blink(previousButton, getButtonColor(previousButton)), 500 * i);
      }
    }
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

  private int getRanodmColor() {
    int color = random.nextInt(4);
    switch (color) {
      case 0: return R.id.red;
      case 1: return R.id.blue;
      case 2: return R.id.green;
      default : return R.id.yellow;
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


  public void buttonSound(int id) {

    Handler handler = new Handler();
    final int freq;
    final double duration;

    switch (id) {
      case R.id.blue:
        freq = 209;
        duration = 0.3;
        break;
      case R.id.yellow:
        freq = 252;
        duration = 0.3;
        break;
      case R.id.red:
        freq = 310;
        duration = 0.3;
        break;
      case R.id.green:
        freq = 415;
        duration = 0.3;
        break;
      default:
        freq = 44;
        duration = 1;
        break;
    }

    handler.post(new Runnable() {
      @Override
      public void run() {
        playSound(freq, duration);
      }
    });

  }

  private void playSound(double frequency, double duration) {
    // AudioTrack definition
    int mBufferSize = AudioTrack.getMinBufferSize(44100,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_8BIT);

    AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
        AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
        mBufferSize, AudioTrack.MODE_STREAM);

    // Sine or square wave
    int samples = (int) (duration*44100);
    double[] mSound = new double[samples];
    short[] mBuffer = new short[samples];
    for (int i = 0; i < mSound.length; i++) {
      mSound[i] = (Math.sin((2.0*Math.PI * i/(44100/frequency)))) > 0 ? 1 : 0;
      mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
    }

    //mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
    mAudioTrack.play();

    mAudioTrack.write(mBuffer, 0, mSound.length);
    mAudioTrack.stop();
    mAudioTrack.release();

  }


}
