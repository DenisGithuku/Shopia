<p align="center">
<img src = "https://user-images.githubusercontent.com/47632042/227858138-b5d8ef8e-c564-4b28-93ad-e9561bdf8bec.svg" width="50%" height="50%" />
</p>

# About Shopia

Shopia is an Mobile app that hooks to an online e-commerce repository. Built on Android, it boasts feature-rich set of APIs and design patterns to achieve a modern look and appeal to the user.

### Features
* Eye catching. The design system offers an attractive interface that is sticky to the user's mind
* Navigable. Use of universal cues makes it easier to use the app without much struggle.
* Performant. The app updates the data only at specific times taking into account of user's device health.

### Installation
The app is built on top of compose ui version `1.3.3` and compose compiler `1.4.2`. Make sure your Android environment supports Kotlin `1.8.0`.

To run the app, first clone the [repository](https://github.com/DenisGithuku/Shopia). Go ahead and run `./gradlew assembleDebug` to build the debug variant of the application. To install it on a physical device or running emulator, run `./gradlew installDebug`

### Architecture
The app follows Modern Android Development practices. Relying on a set of jetpack libraries to simplicity development.

The Unidirectional-Data-Flow pattern was used as this is beneficial to think of since different components are separated according to the work they perform. The app is split into the following layers:

<p align="center">
<img src = "https://raw.githubusercontent.com/ImangazalievM/CleanArchitectureManifest/master/images/CleanArchitectureLayers.png" width = "60%" height="60%"/>
</p>

- **Data layer**:- Handles all data fetching related logic.Consolidates the different sources of data (local and remote) using the [repository pattern](https://www.kodeco.com/24509368-repository-pattern-with-jetpack-compose).
- **Domain layer**:- Defines the abstraction between data and ui layers. Has interfaces that abstract implementation in the data layer. Also contains [use case](https://developer.android.com/topic/architecture/domain-layer#use-cases-kotlin) definitions required for common interactions.
- **UI/Presentation layer**:- Contains all ui components and classes that handle ui state management. ViewModels are placed here since the app employs the [Model-View-ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) pattern

### Caching
The app is offline-first meaning users can still get data without worrying about internet connectivity. When the device is idle, the app syncs with remote apis to ensure the most recent data is shown to the user. This achieves many benefits among them improved device health and bandwidth saving.

### Design patterns
The app leverages the following design patterns.
- **Dependency injection** - constructor injection is used with the various required dependencies coming from central modules constructed with Hilt. This makes it easier to provide those dependencies during runtime and ensures that only single instances of those required dependencies are provided in the entire application lifecycle, by using the singleton pattern.
- **Declarative UI** - uses [jetpack compose](https://developer.android.com/jetpack/compose) to simplify ui development. The benefit is it's easy to think of the ui from a data perspective.
- **Clean Architecture** - combining on Single Source of Truth (SSoT), separation of concerns , [Unidirectional Data Flow](https://developer.android.com/jetpack/compose/architecture) among others, the implementation is easier to understand.
- **Test Driven Development (TDD)** - methods are tested before implementation to ensure correctness and reliability.
- **[Model View ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)** - data is driven from viewmodel which manages the business logic while ui components are responsible for ui logic.

### Modules
To achieve a clear separation of concerns, the app leverages the power of multi-modular architecture. This leaves room for the app to scale. It also reduces build time since modules can be built in a parallel manner.
To attain this the app has the following modules:

![image](https://user-images.githubusercontent.com/47632042/229034883-1179d378-ab4b-418c-89de-b9862608e9f7.png)

##### :app 
-- serves as the entry point into the application. Also pools all the modules together.

#### :core_nav
-- central point for navigation implementation.

#### :core_data
-- maintains all the common business logic needed by other modules.

#### :core_design
-- consolidates logic required to achieve a seamless universal design. Contains all the palettes used in a central location.

#### :feature_product
-- aggregates list of products on sale, product details, pricing, rating etc

#### :feature_auth
-- handles all the logic related to authentication.

#### :feature_user
-- contains all user related operations, ie user profile information.

#### :feature_cart
-- the user can see their cart, add or remove items etc

### Interface
##### Auth - Login

<img src ="https://github.com/user-attachments/assets/8d6b4ad9-5a0b-4d56-ae81-afe582a6f86a" width = "30%" height = "30%">

#### Products
<p align = "center">
<img src = "https://github.com/user-attachments/assets/df85f27c-e195-4815-a79a-7c5929fb2dd5" width = "30%" height = "30%">
&nbsp; &nbsp; &nbsp; &nbsp;
<img src = "https://github.com/user-attachments/assets/386a950d-28a7-4807-95bd-a178844bf151" width = "30%" height = "30%">
</p>

#### Cart
<p align = "center">
<img src = "https://github.com/user-attachments/assets/4b5a6637-a150-4ccd-82fe-0d0482e50de7" width = "30%" height = "30%">
&nbsp; &nbsp; &nbsp; &nbsp;
<img src = "https://github.com/user-attachments/assets/cc67a419-f2be-47e7-a724-07eaff2ce8a6" width = "30%" height = "30%">
</p>

#### Profile
<img src = "https://github.com/user-attachments/assets/ce0b8a8d-d3f9-4795-b82b-3a6c0c60facf" width = "30%" height = "30%">

