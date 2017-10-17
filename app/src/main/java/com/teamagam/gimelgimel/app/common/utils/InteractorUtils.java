package com.teamagam.gimelgimel.app.common.utils;

import com.teamagam.gimelgimel.domain.base.interactors.Interactor;

public class InteractorUtils {

  public static void execute(Interactor... interactors) {
    for (Interactor interactor : interactors) {
      execute(interactor);
    }
  }

  public static void execute(Iterable<Interactor> interactors) {
    for (Interactor interactor : interactors) {
      execute(interactor);
    }
  }

  public static void unsubscribe(Iterable<Interactor> interactors) {
    for (Interactor interactor : interactors) {
      unsubscribe(interactor);
    }
  }

  public static void unsubscribe(Interactor... interactors) {
    for (Interactor interactor : interactors) {
      unsubscribe(interactor);
    }
  }

  private static void execute(Interactor interactor) {
    if (interactor != null) {
      interactor.execute();
    }
  }

  private static void unsubscribe(Interactor interactor) {
    if (interactor != null) {
      interactor.unsubscribe();
    }
  }
}