package de.gishmo.gwt.mvp4g2.debugid.client;

/**
 * <p>Abstract class that provides function to set the debug id in a widget,</p>
 */
public abstract class AbstractDebugIdDriver<V>
  implements SimpleDebugIdDriver<V> {
//
//  /* Map: String ToolTipID, Liste der Widgets, die dieses FachId verwenden in dem View */
//  private Map<String, List<HasFachIdSupport>> widgetsWithFachIdSupport;

  public AbstractDebugIdDriver() {
    super();
//    this.widgetsWithFachIdSupport = new HashMap<>();
  }

//  /**
//   * Fuegt ein Widget mit FachId-Support mit dem uebergebenen Key
//   * der Liste der Widgets hinzu.
//   *
//   * @param FachId
//   *   ID der FachId
//   * @param widget
//   *   Widget mit FachIdSupport
//   */
//  protected void add(String fachId,
//                     HasFachIdSupport widget) {
//    List<HasFachIdSupport> widgetList = this.widgetsWithFachIdSupport.get(fachId);
//    if (widgetList == null) {
//      widgetList = new ArrayList<>();
//      this.widgetsWithFachIdSupport.put(fachId,
//                                        widgetList);
//    }
//    widgetList.add(widget);
//  }
//
//  /**
//   * Initialiert den Driver und laedt alle FachIds
//   *
//   * @param fachIdProvider
//   *   Klasse, die die Widgets mit den FachIds enthaelt
//   */
//  @Override
//  public void initialize(V fachIdProvider) {
//    initialize(fachIdProvider);
//  }

}
