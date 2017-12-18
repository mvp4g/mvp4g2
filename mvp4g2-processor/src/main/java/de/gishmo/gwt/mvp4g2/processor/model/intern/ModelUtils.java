package de.gishmo.gwt.mvp4g2.processor.model.intern;

import java.util.Collection;

import static java.util.Objects.isNull;

public class ModelUtils {

  public static String stringify(Collection<String> values) {
    String stringValue = "";
    if (!isNull(values)) {
      for (String value : values) {
        stringValue += value + ",";
      }
      if (stringValue.contains(",")) {
        stringValue = stringValue.substring(0,
                                            stringValue.lastIndexOf(","));
      }
    }
    return stringValue;
  }

}