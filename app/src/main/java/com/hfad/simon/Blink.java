package com.hfad.simon;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.widget.Button;

public class Blink implements Runnable {

  Button current = null;
  Button previous = null;
  int colorPrevious = -1;

  public Blink(Button current) {
    this.current = current;
  }

  public Blink(Button previous, int colorPrevious){
    this.previous = previous;
    this.colorPrevious = colorPrevious;
  }

  public Blink(Button current, Button previous, int colorPrevious) {
    this.current = current;
    this.previous = previous;
    this.colorPrevious = colorPrevious;
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


  @Override
  public void run() {
    if (previous == null) {
      current.setBackgroundResource(R.drawable.white);
      buttonSound(current.getId());
    } else if (current == null) {
      previous.setBackgroundResource(colorPrevious);
    } else {
      previous.setBackgroundResource(colorPrevious);
      current.setBackgroundResource(R.drawable.white);
      buttonSound(current.getId());
    }

  }
}
