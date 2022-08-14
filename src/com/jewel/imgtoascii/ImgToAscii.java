package com.jewel.imgtoascii;

import java.io.IOException;
import java.net.URL;

import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.widget.TextView;

public class ImgToAscii extends AndroidNonvisibleComponent {

  public ImgToAscii(ComponentContainer container) {
    super(container.$form());
  }

  @SimpleFunction(description = "Set quality as int from 1-5. Set very small text size to view the full ascii image.")
  public void Create(String path, int quality, boolean isColor, AndroidViewComponent textView) {
    TextView view = (TextView) textView.getView();
    if (path.startsWith("http://") || path.startsWith("https://")) {
      new Thread(() -> {
        try {
          Bitmap bitmap = BitmapFactory.decodeStream(new URL(path).openStream());
          form.$context().runOnUiThread(() -> {
            ConvertView(view, quality, isColor, bitmap);
          });
        } catch (IOException e) {
          Failed(e.getMessage());
        }
      }).start();
    } else {
      Bitmap bitmap = BitmapFactory.decodeFile(path);
      ConvertView(view, quality, isColor, bitmap);
    }
  }

  private void ConvertView(TextView textView, int quality, boolean isColor, Bitmap bitmap) {
    new Img2Ascii()
        .bitmap(bitmap)
        .quality(quality)
        .color(isColor)
        .convert(new Img2Ascii.Listener() {
          @Override
          public void onProgress(int percentage) {
            Progress(percentage);
          }

          @Override
          public void onResponse(Spannable text) {
            textView.setText(text);
            Completed();
          }
        });
  }

  @SimpleEvent(description = "")
  public void Progress(int percentage) {
    EventDispatcher.dispatchEvent(this, "Progress", percentage);
  }

  @SimpleEvent(description = "")
  public void Completed() {
    EventDispatcher.dispatchEvent(this, "Completed");
  }

  @SimpleEvent(description = "")
  public void Failed(String message) {
    EventDispatcher.dispatchEvent(this, "Failed", message);
  }
}
