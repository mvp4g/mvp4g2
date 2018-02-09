package de.gishmo.gwt.mvp4g2.processor.model;

import de.gishmo.gwt.mvp4g2.processor.model.intern.ClassNameModel;
import de.gishmo.gwt.mvp4g2.processor.model.intern.IsMetaModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PresenterMetaModel
  implements IsMetaModel {

  private static final String KEY_PRESENTERS           = "presenters";
  private static final String KEY_PRESENTER            = ".presenter";
  private static final String KEY_IS_MULTIPLE          = ".isMultiple";
  private static final String KEY_IS_SHELL             = ".isShell";
  private static final String KEY_HANDLED_EVENTS       = ".handledEvents";
  private static final String KEY_VIEW_CLASS           = ".viewClass";
  private static final String KEY_VIEW_INTERFACE       = ".viewInterface";
  private static final String KEY_VIEW_CREATION_METHOD = ".viewCreationMethod";

  private Map<String, PresenterData> presenterDatas = new HashMap<>();

  public PresenterMetaModel() {
  }

  public PresenterMetaModel(Properties properties) {
    Arrays.stream(properties.getProperty(PresenterMetaModel.KEY_PRESENTERS)
                            .split("\\s*,\\s*"))
          .forEach(s -> {
            String handlerClassName = properties.getProperty(s + PresenterMetaModel.KEY_PRESENTER);
            this.presenterDatas.put(handlerClassName,
                                    new PresenterData(handlerClassName,
                                                      properties.getProperty(s + PresenterMetaModel.KEY_IS_MULTIPLE),
                                                      properties.getProperty(s + PresenterMetaModel.KEY_IS_SHELL),
                                                      properties.getProperty(s + PresenterMetaModel.KEY_VIEW_CLASS),
                                                      properties.getProperty(s + PresenterMetaModel.KEY_VIEW_INTERFACE),
                                                      properties.getProperty(s + PresenterMetaModel.KEY_VIEW_CREATION_METHOD),
                                                      properties.getProperty(s + PresenterMetaModel.KEY_HANDLED_EVENTS)
                                                                .split("\\s*,\\s*")));
          });
  }

//  public void add(String presenter,
//                  String isMultiple,
//                  String isShell,
//                  String viewClass,
//                  String viewInterface,
//                  String viewCreationMethod,
//                  String eventHandlers) {
//    this.presenterDatas.put(presenter,
//                            new PresenterData(presenter,
//                                              isMultiple,
//                                              isShell,
//                                              viewClass,
//                                              viewInterface,
//                                              viewCreationMethod,
//                                              eventHandlers));
//  }

  public void add(String presenter,
                  String isMultiple,
                  String isShell,
                  String viewClass,
                  String viewInterface,
                  String viewCreationMethod,
                  String... eventHandlers) {
    this.presenterDatas.put(presenter,
                            new PresenterData(presenter,
                                              isMultiple,
                                              isShell,
                                              viewClass,
                                              viewInterface,
                                              viewCreationMethod,
                                              eventHandlers));
  }

  public Set<String> getPresenterKeys() {
    return this.presenterDatas.keySet();
  }

  public PresenterData getPresenterData(String key) {
    return this.presenterDatas.get(key);
  }

  public Collection<PresenterData> getPresenterDatas() {
    return this.presenterDatas.values();
  }

  public Properties createPropertes() {
    Properties props = new Properties();
    props.setProperty(PresenterMetaModel.KEY_PRESENTERS,
                      String.join(",",
                                  this.presenterDatas.keySet()));
    this.presenterDatas.values()
                       .stream()
                       .forEach(data -> {
                         props.setProperty(data.getPresenter()
                                               .getClassName() + PresenterMetaModel.KEY_PRESENTER,
                                           data.getPresenter()
                                               .getClassName());
                         props.setProperty(data.getPresenter()
                                               .getClassName() + PresenterMetaModel.KEY_IS_MULTIPLE,
                                           data.getIsMultiple());
                         props.setProperty(data.getPresenter()
                                               .getClassName() + PresenterMetaModel.KEY_IS_SHELL,
                                           data.getIsShell());
                         props.setProperty(data.getPresenter()
                                               .getClassName() + PresenterMetaModel.KEY_VIEW_CLASS,
                                           data.getViewClass()
                                               .getClassName());
                         props.setProperty(data.getPresenter()
                                               .getClassName() + PresenterMetaModel.KEY_VIEW_INTERFACE,
                                           data.getViewInterface()
                                               .getClassName());
                         props.setProperty(data.getPresenter()
                                               .getClassName() + PresenterMetaModel.KEY_VIEW_CREATION_METHOD,
                                           data.getViewCreationMethod());
                         props.setProperty(data.getPresenter()
                                               .getClassName() + PresenterMetaModel.KEY_HANDLED_EVENTS,
                                           String.join(",",
                                                       data.handledEvents));
                       });
    return props;
  }

  public boolean isPresenter(String handlerClassName) {
    return this.presenterDatas.get(handlerClassName) != null;
  }

  public class PresenterData {

    private ClassNameModel presenter;
    private String         isMultiple;
    private ClassNameModel viewClass;
    private ClassNameModel viewInterface;
    private String         viewCreationMethod;
    private String         isShell;
    private List<String> handledEvents = new ArrayList<>();

    public PresenterData(String presenter,
                         String isMultiple,
                         String isShell,
                         String viewClass,
                         String viewInterface,
                         String viewCreationMethod,
                         String eventHandlers) {
      this(presenter,
           isMultiple,
           isShell,
           viewClass,
           viewInterface,
           viewCreationMethod,
           eventHandlers.split("\\s*,\\s*"));
    }

    public PresenterData(String presenter,
                         String isMultiple,
                         String isShell,
                         String viewClass,
                         String viewInterface,
                         String viewCreationMethod,
                         String... eventHandlers) {
      this.presenter = new ClassNameModel(presenter);
      this.isMultiple = isMultiple;
      this.isShell = isShell;
      this.viewClass = new ClassNameModel(viewClass);
      this.viewInterface = new ClassNameModel(viewInterface);
      this.viewCreationMethod = viewCreationMethod;
      Arrays.stream(eventHandlers)
            .forEach(eventHandler -> handledEvents.add(eventHandler));
    }

    public ClassNameModel getPresenter() {
      return presenter;
    }

    public List<String> getHandledEvents() {
      return handledEvents;
    }

    public boolean isMultiple() {
      return "true".equals(isMultiple);
    }

    public String getIsMultiple() {
      return isMultiple;
    }

    public ClassNameModel getViewClass() {
      return viewClass;
    }

    public ClassNameModel getViewInterface() {
      return viewInterface;
    }

    public String getViewCreationMethod() {
      return viewCreationMethod;
    }

    public boolean isShell() {
      return "true".equals(isShell);
    }

    public String getIsShell() {
      return isShell;
    }
  }
}
