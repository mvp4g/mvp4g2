# MVP4G 2 (Beta)

## Preface
With the next version of GWT (GWT 3) and the new J2CL transpiller, there will be major changes. For example: JSNI and generators, besides other things, will be gone. Removing generators will cause the current implementation of mvp4g to stop working.

To prepare MVP4G for GWT 3, the GWT generator has to be replaced by an annotation processor and most of the dependencies of MVP4G have to be removed. MVP4G2 will work with GWT 2 + 3. 

MVP4G 2 will be the second edition of MVP4G and will have major changes.

In opposite to the first version of MVP4G, MVP4G2 will not use generators, JSNI or the widget system. Instead it will use Elemental 2 and APT.

Moving to APT will cause changes, related to the options offered by APT. Using the ideas of MVP4G, MVP4G 2 will try to take as much as possible of the principles and features to the next version.

Currently, the basic event handling and place management is implemented.

You can take a look at the mvp4g2 example, that uses APT instead of generators: [https://github.com/mvp4g/mvp4g2-examples](https://github.com/mvp4g/mvp4g2-examples)

**Remember:** This is only a beta version.

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

- the navigation conformation does not allow a server call to check if a sid is vaild
- Place management can be hard for complex UI

That's why Mvp4g offers a solution to following these best practices

 - Event Bus
 - Model View Presenter
 - Place Service

 using simple mechanisms that only need a few lines of code and a few annotations.

This is all you need to create an event bus with four events:
```
@Events(startPresenter = CompanyListPresenter.class, module = CompanyModule.class) 
public interface CompanyEventBus extends EventBus {          
        @Event( handlers = CompanyEditPresenter.class )        
        public void goToEdit(CompanyBean company);          
        
        @Event( handlers = CompanyDisplayPresenter.class )         
        public void goToDisplay(CompanyBean company);          
        
        @Event( handlers = { CompanyListPresenter.class, CompanyDisplayPresenter.class } )         
        public void companyCreated(CompanyBean newBean);          
        
        @Event( handlers = CompanyListPresenter.class )         
        public void companyDeleted(CompanyBean newBean); 
}
```
To handle a event, just create a method in your presenter.
For example, to handle the ```goToEdit(CompanyBean company)``` event, use this code inside the handler / presenter:
```
       public void onGoToEdit(CompanyBean company) {
           .... 
       }
```
**_NEW!_**
Starting with this version there is another way to add a handler / presenter method as a event handler to the event bus. In this case you do not have to use the ```handler```-attibute of the event annotation.
Using the new @EventHandler annotation (The EventHandler annotation of mvp4g version 1 is now represented with @Handler) the event bus looks like this:
```
@Events(startPresenter = CompanyListPresenter.class, module = CompanyModule.class) 
public interface CompanyEventBus extends EventBus {          
        @Event        
        public void goToEdit(CompanyBean company);          
        
        @Event         
        public void goToDisplay(CompanyBean company);          
        
        @Event         
        public void companyCreated(CompanyBean newBean);          
        
        @Event        
        public void companyDeleted(CompanyBean newBean); 
}
```
To handle the ```goToEdit(CompanyBean company)``` event, just annotate the event handling method in the handler / presenter with @EventHandler, add a 'on' to the event name, change the first letter to uppercase and use the same signuture:
```
       @EventHandler
       public void onGoToEdit(CompanyBean company) {
           .... 
       }
```
Eventbus:
- create an event bus using a few annotations and one centralized interface where you can easily manage your events
- control your event flow thanks to event filtering, event logs, passive event
- have the same control of user's navigation as the GWT Activities/Place architecture thanks to Navigation Event

MVP:
- create a presenter and inject a view with one annotation
- support for multiple instances of the same presenter (**not yet implemented, but planned**)
- easily implement the Reverse MVP (or View Delegate) pattern thanks to Reverse View feature (MVP4G2 requires the use of the view delegate pattern)
- easily control your presenter thanks to onBeforeEvent

History Management/Place Service:
- convert any event to history token thanks to simple history converters

Not only does Mvp4g help you follow the best practices, it also provides mechanisms to build fast applications:
- support for lazy loading: build your presenters/views only when you need them. Useless presenters/views are also automatically removed.

To understand how the framework works, you can look at the documentation, the [tutorials](https://github.com/mvp4g/mvp4g2/wiki/1.-Tutorials-and-Examples) or the [examples](https://github.com/mvp4g/mvp4g2-examples).

Mvp4g has been successfully used on several commercial projects im the past, [take a look at a few of them](https://github.com/mvp4g/mvp4g/wiki/1.-Tutorials-and-Examples). You can also read and post feedback on the official [GWT marketplace](http://www.gwtmarketplace.com/#mvp4g) or [Mvp4g forum](https://groups.google.com/forum/#!forum/mvp4g).

To communicate with the developers of MVP4G2 directly feel free to use the [MVP4G Gitter room](https://gitter.im/mvp4g/mvp4g).

Any comments or ideas to improve the framework are more than welcome. If you want to help us to improve and contribute to this project, feel free to do so.

To ensure quality, library code is covered by JUnit tests.

