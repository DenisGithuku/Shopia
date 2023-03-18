Shopia is an online repository for different products on sale. Built on Android, it boasts feature-rich set of APIs and design patterns to achieve a modern look and appeal to the user.

### Features
* Eye catching. The design system offers an attractive interface that is sticky to the user's mind
* Navigable. Use of universal cues makes it easier to use the app without much struggle.
* Perfomant. The app updates the data only at specific times taking into account of user's device health.

### Installation
The app is built on top of compose ui version `1.3.3` and compose compiler `1.4.2`. Make sure your Android enviroment supports Kotlin `1.8.0`.

### Architecture
The app follows Modern Android Development practives. Relying on a set of jetpack libraries to simplity development.

The app is offline first meaning users can still get data without worrying about internet connectivity. When the device is idle, the app syncs with remote apis to ensure the most recent data is shown to the user. This achieves many benefits among them better device health and bandwidth saving.

The app leverages the following design patterns.
- Declarative UI - uses [jetpack compose](https://developer.android.com/jetpack/compose) to simplify ui development. 
- Clean Architecture - combining on Single Source of Truth (SSoT), separation of concerns , [Unidirectional Data Flow](https://developer.android.com/jetpack/compose/architecture) among others.
- Test Driven Development (TDD) - methods are tested before implementation to ensure correctness and reliability.
- [Model View ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - data is driven from viewmodel which manages the business logic while ui components are responsible for ui logic.

### Modules
The app has the following modules:
##### :app 
-- serves as the entry point into the application. Also pools all the modules together.

#### :core_data
-- maintains all the common business logic needed by other modules.

#### :core_design
-- contains all the related logic to achieve a universal design pattern.

#### :feature_product
-- aggregates list of products on sale.

#### :feature_auth
-- handles all the logic related to authentication.

more to come...

### Interface
##### Auth - Login

<img src ="https://user-images.githubusercontent.com/47632042/226111885-ffc3df97-1be4-407c-a6b6-d2ada433efb2.png" width = "30%" height = "30%">

#### Product list

<img src = "https://user-images.githubusercontent.com/47632042/226111974-d56de010-9ad8-410c-b9f4-b040f747b257.png" width = "30%" height = "30%">

#### Product Details

<img src = "https://user-images.githubusercontent.com/47632042/226112108-fec2dfa7-e761-4699-a090-a07da4f6ec94.png" width = "30%" height = "30%">

