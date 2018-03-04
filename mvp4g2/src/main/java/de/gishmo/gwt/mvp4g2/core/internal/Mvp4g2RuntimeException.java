package de.gishmo.gwt.mvp4g2.core.internal;

@SuppressWarnings("serial")
public class Mvp4g2RuntimeException
  extends RuntimeException {

  public Mvp4g2RuntimeException() {
    super();
  }

  public Mvp4g2RuntimeException(String message) {
    super(message);
  }

  public Mvp4g2RuntimeException(String message,
                                Throwable cause) {
    super(message,
          cause);
  }

  public Mvp4g2RuntimeException(Throwable cause) {
    super(cause);
  }

  public Mvp4g2RuntimeException(String message,
                                Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
    super(message,
          cause,
          enableSuppression,
          writableStackTrace);
  }
}
