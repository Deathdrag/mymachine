package com.deathrow.mymachine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class ProgressBar extends JPanel {
 
  JProgressBar pbar;
  static final int MY_MINIMUM=0;
  static final int MY_MAXIMUM=100;
 
  public ProgressBar() {
     pbar = new JProgressBar();
     pbar.setMinimum(MY_MINIMUM);
     pbar.setMaximum(MY_MAXIMUM);
     add(pbar);
  }
 
  public void updateBar(int newValue) {
    pbar.setValue(newValue);
  }
}



