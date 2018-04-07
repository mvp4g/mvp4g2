# MVP4G2 Project Generator

## Motivation

Setting up a Mvp4g2 project can be hard. There are a lot of things to do before it is possible to run the project and
check wheather it is working or not. To improve the set up process, we created a
[Mvp4g2 project generator](http://www.mvp4g.org/gwt-boot-starter-mvp4g2/GwtBootStarterMvp4g2.html). Once you have
configurated and generated the project, you will get a zip file containing the ready to import Mvp4g2 project.

Keep in mind, Mvp4g2 helps you to structure your application. Navigation, history and confirmation will work. It is not
a widget library. So, the views are nearly empty. It is up to you to implement the views.

**Important note:** Mvp4g2 requires Java 8!

**Note:**

The implementation of the generator uses Mvp4g2, GXT and is based on GWT 2.8.2! The generated project requires in this
version GWT 2.8.2 and Java 8. The generated Mvp4g2 source code will work with J2CL / GWT 3 once all dependies to GWT
2.8.2 are replaced. You have to choose a widget set, that will work with J2CL / GWT 3 to be compatible with J2CL / GWT 3!

**The generator nor Mvp4g2 will make any source code compatible with J2CL / GWT 3.**

## Configuration

### Project Meta Data

In this area the Group Id, the Artifact Id and GWT version of the project is defined.

![Project Meta Data](https://github.com/mvp4g/mvp4g2/blob/master/etc/wiki/images/ProjectMetaData.png)

#### Group Id

The group id is used as group id inside the POM. Also it will be used as the name of the package. The group id can not
be empty. You can enter letters [a - z], numbers [0 - 9] and dots [.].

#### Artifact Id

The artifact id is used as artifact id inside the POM. It is also used as the name of the GWT module. The group id can
not be empty. You can enter letters [a - z, A - Z] and numbers [0 - 9].

#### GWT Version

The GWT version of the generated project. Currently the generator supports only GWT 2.8.2.

#### Widget Set

The widget library that will be used for the code generation. At the moment the Mvp4g2 Example generator supports:

- Elemento

- native GWT widgets

- GXT GPL 4.0.0

**Note:** The native GWT and the GXT GPL 4.0.0 widget sets will not work with J2CL / GWT 3!

### Application Meta Data

In this area the features of Mvp4g2 for generated the project are defined.

![Project Meta Data](https://github.com/mvp4g/mvp4g2/blob/master/etc/wiki/images/ApplicationMetaData.png)

#### Application Loader

If checked, a [Application Loader](https://github.com/mvp4g/mvp4g2/wiki/02.-Application-Loader) will be added to the project.

#### Debug Support

If checked, the [Debug](https://github.com/mvp4g/mvp4g2/wiki/03.-Defining-an-Event-Bus#logs) annotation will be
generated inside the event bus.

#### History Support

If checked, everything needed for [history handling](https://github.com/mvp4g/mvp4g2/wiki/05.-Browser-History-Support)
will be generated.

#### History on Start

If checked, the application will handle a [history token on start](https://github.com/mvp4g/mvp4g2/wiki/05.-Browser-History-Support#history-on-start) (book mark).

### Screen Meta Data

In this area the screens are defined. A screen in mvp4g2 is a combination of a presenter and a view. There are three
predefined screens called search, list and detail.

![Project Meta Data](https://github.com/mvp4g/mvp4g2/blob/master/etc/wiki/images/ScreenMetaData.png)

There are three buttons:

- the first buttons adds another screen

- the second button edits the selected screen

- the third button removes the selected screen.

In case you add or edit a screen, a window will open:

![Add or edit screen data](https://github.com/mvp4g/mvp4g2/blob/master/etc/wiki/images/PresenterDataWindow.png)

Using this window, you can:

* name the screen: this name will be used as package name, the goto event, the presenter- and view-name, etc
* history name (optional): if entered, this name will be used as token instead the event name
* view creation method:
  * view is created by framework: the instance of the view will be created by the framwork (using 'new')
  * view is created by presenter: the instance of the view has to be created by the developer
* enable navigation confirmation
