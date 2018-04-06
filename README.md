# MVP4G 2 (Beta)

## Preface
With the next version of GWT (GWT 3) and the new J2CL transpiller, there will be major changes in the GWT developmemt. For example: JSNI and generators, besides other things, will be gone. Removing generators will cause the current implementation of mvp4g to stop working.

To prepare MVP4G for GWT 3, the GWT generator has to be replaced by an annotation processor and most of the dependencies of MVP4G have to be removed.

So we started a new project, calles MVP4G2 and rewrite MVP4G from scratch. MVP4G2 will work with GWT 2 + 3 and will bring major changes. In opposite to the first version of MVP4G, MVP4G2 will not use generators, JSNI or the widget system. Instead it will use Elemental 2 (only for the history implementation) and APT.

Moving to APT will cause changes, related to the options offered by APT. Using the ideas of MVP4G, MVP4G 2 will try to take as much as possible of the principles and features to the next version.

Keep in mind, MVP4G2 helps you to structure your application. It does not offer a widget system nor does it force you to use a particular one. MVP4G2 will work with native GWT widgets, GXT, Elemental (1 + 2), Elemeto, etc.

Currently, most of the features of mvp4g, which can be used with APT, are implemented (except the multi module feature). A complete list of the things already added in MVP4G2 can be found [here](https://github.com/mvp4g/mvp4g2/wiki/Comparision:-Mvp4g-vs.-Mvp4g2)

There are already a lot of exsiting esamples. You can take a look at the mvp4g2 example, that uses APT instead of generators: [https://github.com/mvp4g/mvp4g2-examples](https://github.com/mvp4g/mvp4g2-examples)

To speed up settung up a MVP4G2 project you can use the [MVP4G2 Boot Starter Project Generator](http://www.mvp4g.org/gwt-boot-starter-mvp4g2/GwtBootStarterMvp4g2.html). The project generator will generate a Maven Project which can be imported to your preferred and is ready to use. Here you find some notes about the project generator: [Mvp4g2 Project Generator](https://github.com/mvp4g/mvp4g2/wiki/Mvp4g2-Project-Generator).

**Remember:** This is a beta version and the documentation is still in progress.

## MVP4G2
GWT is a very powerful framework that allows you to build efficient applications, especially if you follow the best practices described by Ray Ryan at Google IO 2009:

- Stateless Services
- Dependency Injection
- Event Bus
- Model View Presenter
- Place Service

(see [https://www.youtube.com/watch?v=PDuhR18-EdM for the video](https://www.youtube.com/watch?v=PDuhR18-EdM) or [http://extgwt-mvp4g-gae.blogspot.com/2009/10/gwt-app-architecture-best-practices.html](http://extgwt-mvp4g-gae.blogspot.com/2009/10/gwt-app-architecture-best-practices.html) for the text, thanks to Araik Minosian.)
However, following these best practices is not always easy and you can end up with a project with a lot of boilerplate code that is hard to manage.

On the other hand the native GWT implementation has some drawbacks.

- the navigation conformation does not allow a server call to check if a side is vaild
- Place management can be hard for complex UI

That's why Mvp4g offers a solution following these best practices

 - Event Bus
 - Model View Presenter
 - Place Service

 using simple mechanisms that only need a few lines of code and a few annotations.

And, keep in mind, mvp4g2 has not dependecy to GWT, therefore it does not know the Element- or Widget-classes. To keep the framwork free of stuff to handle browser elements, these things are part of the developer job. You can use mvp4g2 with any widget framework you like. It will work with native GWT >= 2.8.0, GXT, Elemental 1+2, Elemento or any other widget library. And, from the thinks we know today, it will work with J2CL/GWT 3!

**Important Note:** MVP4G2 requires Java 8!

This is all you need to create an event bus with four events in mvp4g2:
```
@EventBus(shellPresenter = ShellPresenter.class) 
public interface CompanyEventBus
    extends EventBus {     
         
        @Event        
        public void goToEdit(CompanyBean company);          
        
        @Event        
        public void goToDisplay(CompanyBean company);          
        
        @Event         
        public void createCompany(CompanyBean newBean);          
        
        @Event         
        public void deleteCompany(CompanyBean newBean); 
        
}
```
All you have to do to handle an event in your presenter / handler, is:
* create a method annotated with @EventHandler
* make sure that the method name looks like this: 'on' + [eventName with first letter capitalize]
* use the same signature

For example, to handle the ```goToEdit(CompanyBean company)``` event, use this code inside the handler / presenter:
```
       @EventHandler
       public void onGoToEdit(CompanyBean company) {
           .... 
       }
```
and to handle the ```deleteCompany(CompanyBean newBean)``` just create a method with this code:
```
       @EventHandler
       public void onDeleteCompany(CompanyBean company) {
           .... 
       }
```
To fire an event, call ```eventBus.goToDisplay(myCompanyBean);``` inside you presenter or handler.

Mvp4g2 makes sure, that all handlers / presenters, that implemet a event handler for the goToDisplay-event will be called!

Of course, the old style of binding a presenter / handler to an event still works!
Using the old style of binding an event to handlers, all you need to do is to create an event bus and use the handlers-attribute inside the @Event-annotation.
Here an example of a event bus with four events:
```
@EventBus(shellPresenter = ShellPresenter.class) 
public interface CompanyEventBus
    extends EventBus {     
         
         @Event(handlers = CompanyEditPresenter.class)        
         public void goToEdit(CompanyBean company);          
         
         @Event(handlers = CompanyDisplayPresenter.class)         
         public void goToDisplay(CompanyBean company);          
         
         @Event(handlers = { CompanyListPresenter.class, CompanyDisplayPresenter.class })         
         public void companyCreated(CompanyBean newBean);          
         
         @Event(handlers = CompanyListPresenter.class)         
         public void companyDeleted(CompanyBean newBean);
          
}
```
To handle the ```goToEdit(CompanyBean company)``` event, just create a method in the handler / presenter that looks like this:
```
       public void onGoToEdit(CompanyBean company) {
           .... 
       }
```

#### Here are some of the key features of mvp4g2
Eventbus:
- create an event bus using a few annotations and one centralized interface where you can easily manage your events
- control your event flow thanks to event filtering
- see what your application does thanks to event logging
- send events only to presenters / handlers that already handled a event thanks to passive event feature
- activate and deactivate presenters and handlers thanks to the activate- and deactivate feature
- have the same control of user's navigation as the GWT Activities/Place architecture thanks to navigation event feature

MVP:
- create a presenter and inject a view with one annotation
- support for multiple instances of the same presenter
- easily implement the Reverse MVP (or View Delegate) pattern thanks to Reverse View feature (MVP4G2 requires the use of the view delegate pattern)
- easily control your presenter thanks to onBeforeEvent
- add an event handling method to the event bus with one annotation

History Management/Place Service:
- convert any event to history token thanks to a simple history converters mechanism

Not only does Mvp4g2 help you follow the best practices, it also provides mechanisms to build fast applications:
- support for lazy loading: build your presenters/views only when you need them.
- Useless presenters/views are automatically removed.

To understand how the framework works, you can look at the documentation, the [tutorials](https://github.com/mvp4g/mvp4g2/wiki/1.-Tutorials-and-Examples), the [examples](https://github.com/mvp4g/mvp4g2-examples) or the [mvp4g blog](http://mvp4g.blogspot.de/).



Mvp4g has been successfully used on several commercial projects im the past, [take a look at a few of them](https://github.com/mvp4g/mvp4g/wiki/1.-Tutorials-and-Examples). You can also read and post feedback here: [Mvp4g forum](https://groups.google.com/forum/#!forum/mvp4g).

To communicate with the developers of MVP4G2 directly feel free to use the [MVP4G Gitter room](https://gitter.im/mvp4g/mvp4g).

Any comments or ideas to improve the framework are more than welcome. If you want to help us to improve and contribute to this project, feel free to do so.

To ensure quality, library code is covered by JUnit tests.

