package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

/**
 * Well, i18n...
 */
public class I18N {

  private static I18NBundle bundle;

  public static I18NBundle getBundle() {
    if(bundle == null) {
      FileHandle baseFileHandle = Gdx.files.internal("i18n/bundle");
      Locale locale = new Locale(Locale.getDefault().getLanguage());
      bundle = I18NBundle.createBundle(baseFileHandle, locale);
    }
    return bundle;
  }
}
