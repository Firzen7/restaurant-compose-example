Understanding the basics of Dagger Hilt
---------------------------------------

Let's analyze the three most important concepts and their corresponding annotations that we're required to work with to enable automatic DI in our project:

 - Injection
 - Modules
 - Components

Let's start with injection!

Injection
---------

Dagger Hilt needs to know the type of instances we want it to provide us with. When we discussed manual constructor injection, we initially wanted ExampleViewModel to be injected wherever we needed it, and we used a DependencyContainer class for that.

If we want Dagger Hilt to inject instances of a class somewhere, we must first declare a variable of that type and annotate it with the @Inject annotation.

Let's say that inside the main() function used for the manual DI example, we no longer want to use manual DI to get an instance of ExampleViewModel. Instead, we want Dagger to instantiate this class. That's why we will annotate the ExampleViewModel variable with the Java @Inject annotation and refrain from instantiating the ViewModel class by ourselves. Dagger Hilt should do that for us now:

    import javax.inject.Inject

    @Inject
    val vm: ExampleViewModel
    fun main() {
        vm.doSomething()
    }

Now, for Dagger Hilt to know how to provide us with an instance of the ExampleViewModel class, we must also add the @Inject annotation to the dependencies of ExampleViewModel so that Dagger knows how to instantiate the ViewModel class.

Since the dependencies of ExampleViewModel are inside the constructor (from when we used manual constructor injection), we can directly add the @Inject annotation to constructor:

    class ExampleViewModel @Inject constructor(private val repo: Repository) {
        fun doSomething() { repo.use() }
    }

Now, Dagger Hilt also needs to know how to inject the dependencies of ExampleViewModel, more precisely the Repository class.

Let's consider that Repository has only one dependency, a Retrofit constructor variable. For Dagger to know how to inject a Repository class, we must annotate its constructor with @Inject as well:

    class Repository @Inject constructor(val retrofit: Retrofit){
        fun use() { retrofit.baseUrl() }
    }
    
Until now, we got away with @Inject annotations because we had access to the classes and dependencies that we were trying to inject, but now, how can Dagger know how to provide us with a Retrofit instance? We have no way of tapping inside the Retrofit class and annotating its constructor with @Inject, since it's in an external library.

To instruct Dagger on how to provide us with specific dependencies, let's learn a bit about modules!

Modules
-------

**Modules** are classes annotated with `@Module` that allow us to instruct Dagger Hilt on how to provide dependencies. For example, we need Dagger Hilt to provide us with a `Retrofit` instance in our `Repository`, so we could define a `DataModule` class that tells Dagger Hilt how to do so:

    @Module
    object DataModule {
        @Provides
        fun provideRetrofit() : Retrofit {
            return Retrofit.Builder().baseUrl("some_url").build()
        }
    }

Now, Dagger Hilt knows how to provide us with all the dependencies our `ExampleViewModel needs` (its direct `Repository` dependency and `RepositoryRetrofit` dependency). Yet, Dagger will complain that it needs a component class in which the module we've created must be installed.

Let's have a brief look at components next!

Components
----------

**Components** are interfaces that represent the container for a certain set of dependencies. A component takes in modules and makes sure that the injection of its dependencies happens with respect to a certain lifecycle.

For our example with the `ExampleViewModel`, `Repository`, and `Retrofit` dependencies, let's say that we create a component that manages the creation for these dependencies.

With Dagger Hilt, you can define a component with the `@DefineComponent` annotation:

    @DefineComponent()
    interface MyCustomComponent(…) { /* component build code */ }

Afterward, we could install our DataModule in this component:

    @Module
    @InstallIn(MyCustomComponent::class)
    object DataModule {
        @Provides
        fun provideRetrofit(): Retrofit { […] }
    }

In practice though, the process of defining and building a component is more complex than that. This is because a component must scope its dependencies to a certain lifetime scope (such as the lifetime of the application) and have a pre-existent parent component.

Luckily, Hilt provides components for us out of the box. Such predefined components allow us to install modules in them and to scope dependencies to their corresponding lifetime scope.

Some of the most important predefined components are as follows


 - `SingletonComponent`: Allows us to scope dependencies to the lifetime of the application, as singletons, by annotating them with the `@Singleton` annotation. Every time a dependency annotated with `@Singleton` is requested, Dagger will provide the same instance.

 - `ActivityComponent`: Allows us to scope dependencies to the lifetime of an `Activity`, with the `@ActivityScoped` annotation. If the `Activity` is recreated, a new instance of the dependency will be provided.

 - `ActivityRetainedComponent`: Allows us to scope dependencies to the lifetime of an `Activity`, surpassing its recreation upon orientation change, with the `@ActivityRetainedScoped` annotation. If the `Activity` is recreated upon orientation change, the same instance of the dependency is provided.

 - `ViewModelComponent`: Allows us to scope dependencies to the lifetime of a `ViewModel`, with the `@ViewModelScoped` annotation.

 Important
 ---------
 
 Under the hood, Hilt uses Dagger. Dagger 2 is separate library. Here is comparison: https://medium.com/@manishkumar_75473/dagger-2-vs-hilt-in-android-a-comparison-of-dependency-injection-in-same-codebase-5da68e787ea7
