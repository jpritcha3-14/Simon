package com.hfad.simon;

import android.widget.Button;

public class Blink implements Runnable {

  Button current = null;
  Button previous = null;
  int colorPrevious = -1;
  int colorChange = -1;

  public Blink(int colorChange, Button current) {
    this.current = current;
    this.colorChange = colorChange;
  }

  public Blink(Button previous, int colorPrevious){
    this.previous = previous;
    this.colorPrevious = colorPrevious;
  }

  public Blink(int colorChange, Button current, Button previous, int colorPrevious) {
    this.current = current;
    this.previous = previous;
    this.colorPrevious = colorPrevious;
    this.colorChange = colorChange;
  }


  @Override
  public void run() {
   if (previous == null) {
      current.setBackgroundResource(colorChange);
      MakeSound.buttonSound(current.getId());
    } else if (current == null) {
      previous.setBackgroundResource(colorPrevious);
    } else {
      previous.setBackgroundResource(colorPrevious);
      current.setBackgroundResource(colorChange);
      MakeSound.buttonSound(current.getId());
    }

  }
}
