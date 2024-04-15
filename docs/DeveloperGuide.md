---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

<div style="page-break-after: always;"></div>

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the application.

Below is a quick overview of the main components and how they interact with each other.<br><br>

**Main components of the architecture**

**`Main`** (consisting of the classes [`Main`](https://github.com/AY2324S2-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2324S2-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the application launch and shut down.
* On application launch, it initializes the other components in the correct sequence, and connects them up with each other.
* On shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the application's work is done by the following four components:

* [**`UI`**](#ui-component): The User Interface of the application.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the application data in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.<br><br>

**How the architecture components interact with each other**

The ***Sequence Diagram*** below shows how the architecture components interact with one another for the scenario where the user issues the command `/delete ; name : Poochie`:

<img src="images/ArchitectureSequenceDiagram.png" width="750" />

Each of the four main components (also shown in the diagram above):

* Defines its *API* in an `interface` with the same name as the component.
* Implements its functionality using a concrete `{Component Name} Manager` class which follows the corresponding API interface mentioned in the previous point.


For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside components from being coupled to the implementation of a component), as illustrated in the (partial) class diagram below:

<img src="images/ComponentManagers.png" width="300" />

<div style="page-break-after: always;"></div>

### UI component

The ***API*** of this component is specified in [`Ui.java`](https://github.com/AY2324S2-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/address/ui/Ui.java).

Below is a class diagram of the `UI` component:

<br>

![Structure of the UI Component](images/UiClassDiagram.png)


The UI consists of a `MainWindow` component which itself is made up of sub-components e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these UI components, including the `MainWindow` component, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.
The `UI` component uses the JavaFX UI framework. The layout of these UI parts are defined in matching `.fxml` files that are located in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2324S2-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) component is specified in [`MainWindow.fxml`](https://github.com/AY2324S2-CS2103T-W10-2/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component:

* Executes user commands using the `Logic` component.
* Listens for changes to `Model` data so that the UI can be updated with the modified data.
* Keeps a reference to the `Logic` component, because the `UI` component relies on the `Logic` component to execute commands.
* Depends on some classes in the `Model` component, as it displays the `Person` object residing in the `Model`.

<div style="page-break-after: always;"></div>

### Logic component

The ***API*** of this component is specified in [`Logic.java`](https://github.com/AY2324S2-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/address/logic/Logic.java).

Below is a (partial) class diagram of the `Logic` component:

<br>

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("/delete ; name : Poochie")` API call as an example.

![Interactions Inside the Logic Component for the `/delete ; name : Poochie` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.
</div>

Execution lifecycle of the `Logic` component:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g. `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g. `DeleteCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Illustrated below are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
1. When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g. `AddCommandParser`). `XYZCommandParser` uses the other classes as shown above to parse the user command and creates an `XYZCommand` object (e.g. `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
2. All `XYZCommandParser` classes (e.g. `AddCommandParser`, `DeleteCommandParser` etc.) inherit from the `Parser` interface so that they can be treated similarly where possible (e.g. during testing).

<div style="page-break-after: always;"></div>

### Model component

The ***API*** of the `Model` component is specified in [`Model.java`](https://github.com/AY2324S2-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/address/model/Model.java).

Below is a class diagram of the `Model` component:

<br>

<img src="images/ModelClassDiagram.png" width="700" />

The `Model` component:

* Stores different states of `AddressBook` inside `VersionedAddressBook`.
* Stores all data from PoochPlanner (i.e. all `Person` objects which are contained in a `UniquePersonList` object).
* Stores the currently "selected" `Person` objects (e.g. results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` object that can be "observed" (e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list changes).
* Stores a `UserPref` object that represents the user’s preferences. This is exposed to outsiders as a `ReadOnlyUserPref` object.
* Does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components).

<div style="page-break-after: always;"></div>

### Storage component

The ***API*** of this component is specified in [`Storage.java`](https://github.com/AY2324S2-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/address/storage/Storage.java).

Below is a class diagram of the `Storage` component:

<br>

<img src="images/StorageClassDiagram.png" width="6000" />

The `Storage` component:

* Saves both PoochPlanner data and user preference data in JSON format, which is read during the bootup of PoochPlanner.
* Inherits from both `AddressBookStorage` and `UserPrefStorage` interfaces, which means that it can be treated as either one (if the functionality of only one interface is needed).
* Depends on some classes in the `Model` component (because the `Storage` component's job is to save and retrieve objects that belong to the `Model`).

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

<div style="page-break-after: always;"></div>

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

[//]: # (@@author chiageng)
### Add feature

#### Overview

The `add-XYZ` command enables users to add a new contact to PoochPlanner.

The following sequence diagram models the interactions between the different components of PoochPlanner for the execution of the `add-person` command:

![Add Sequence Diagram](images/AddCommandSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The implementation for adding `Person`, `Staff`, `Supplier`, and `Maintainer` are similar and only differ in their accepted attributes. `XYZ` can refer to either `person`, `staff`, `supplier`, or `maintainer`. 
</div>

#### Details

1. The user inputs the command to add a new contact.
2. An `AddCommandParser` object invokes its `parse` method which parses the user input.
3. An `AddCommand` object is created.
4. The `AddCommandParser` object returns the `AddCommand` object.
5. A `LogicManager` object invokes the `execute` method of the `AddCommand` object.
6. The `execute` method of the `AddCommand` object invokes the `addPerson` method of its `Model` argument to create a new contact with a new `Person` object.
7. The `execute` method of the `AddCommand` object returns a `CommandResult` object which stores the data regarding the completion of the `add-XYZ` command.

#### Example Usage
1. The user launches the application.
2. The user inputs `/add-person ; name : John Doe ; phone : 98765432 ; email : johnd@example.com ; address : 311, Clementi Ave 2, #02-25` into the CLI.
3. The contact card for the person named "John Doe" is created. This change should be reflected in the contacts list in PoochPlanner.

[//]: # (@@author yleeyilin)

<div style="page-break-after: always;"></div>

### Edit feature

#### Overview

The `edit-XYZ` command enables users to modify specified field(s) of an existing contact in PoochPlanner.

The following sequence diagram models the interactions between the different components of PoochPlanner for the execution of the `edit-XYZ` command:

![Edit Sequence Diagram](images/EditCommandSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The implementation for editing a `Person`, `Staff`, `Supplier`, and `Maintainer` are similar and only differ in their accepted attributes. `XYZ` can refer to either `person`, `staff`, `supplier`, or `maintainer`. 
</div>

#### Details

1. The user inputs the command to edit an existing contact by first stating the name of the contact they want to edit. This is followed by specifying the respective fields and new values that the user wants to modify.
2. An `EditCommandParser` object invokes its `parse` method which parses the user input and creates an `EditPersonDescriptor` object which contains the new values to be edited for the specified contact.
3. An `EditCommand` object is created with the name of the contact to edit and the `EditPersonDescriptor` object.
4. The `EditCommandParser` returns the `EditCommand` object.
5. A `LogicManager` object invokes the `execute` method of the `EditCommand` object.
6. The `execute` method of the `EditCommand` objects finds the specified contact by its name. The `execute` method then calls the `createEditedPerson` method of the `EditCommand` object which creates a new `Person` object that contains the updated values of the contact.
7. The `execute` method of the `EditCommand` object invokes the `setPerson` method of its `Model` argument to replace the specified contact with the new `Person` object.
8. The `execute` method of the `EditCommand` object invokes the `updateFilteredPersonList` method of its `Model` argument to update the view of PoochPlanner to show all contacts.
9. The `execute` method of the `EditCommand` object returns a `CommandResult` object which stores the data regarding the completion of the `edit-XYZ` command.

#### Example Usage
1. The user launches the application.
2. The user inputs `/edit-person ; name : Alice Tan ; field : { phone : 9990520 ; email : impooch@gmail12.com }` into the CLI.
3. The contact card for the person named "Alice Tan" has its `phone` and `email` fields updated respectively. This change should be reflected in the contacts list in PoochPlanner.

**Aspect: How to implement the edit command**

* **Alternative 1 (current choice)**: Create four distinct edit commands for the four contact types (`Person`, `Staff`, `Maintainer`, `Supplier`).
    * Pros: More user-friendly since users will be less prone to error that involves trying to edit a field that does not exist for the specific contact type.
    * Cons: Steeper learning curve for users due to the greater number of commands.

* **Alternative 2**: Use only one edit command across all classes by using a dynamic edit parser. The dynamic edit parser will internally route to the correct edit command to handle the modification of different contact types separately.
    * Pros: Much simpler suite of features for users, which makes it easier for users to start using PoochPlanner.
    * Cons: Complex to implement since the checking of the contact type must be done at the point of parsing by the dynamic edit parser. However, doing so will violate the intended abstracted implementation of MVC (Model-View-Controller) as the model will have to be accessible from within the parser class in order for the type checking to be done. 

<div style="page-break-after: always;"></div>

### Search feature

#### Overview

The `search` command enables users to find contacts in PoochPlanner that match the input search query.

The following sequence diagram models the interactions between the different components of PoochPlanner for the execution of the `search` command:

<img src="images/SearchCommandSequenceDiagram.png" width="900" />

#### Details

1. The user inputs the command to search for contacts with the specified search query.
2. A `SearchCommandParser` object invokes its `parse` method which parses the user input by storing the prefixes and their respective values in an `ArgumentMultimap` object, and using this object to create an instance of `KeywordPredicate`.
3. The `SearchCommandParser` object then creates a `SearchCommand` object containing the aforementioned `KeywordPredicate` object.
4. A `LogicManager` object invokes the `execute` method of the `SearchCommand` object.
5. The `execute` method of the `SearchCommand` object invokes the `updateFilteredPersonList` method of its `Model` argument, taking in the `KeywordPredicate` object as a parameter to filter and update the view of PoochPlanner.
6. The `execute` method of the `SearchCommand` object returns a `CommandResult` object which stores the data regarding the completion of the `search` command.

#### Example Usage

1. The user launches the application.
2. The user inputs `/search ; name : Poochie` into the CLI.
3. PoochPlanner is updated to display all contact cards with contacts containing "Poochie" in their name.

**Aspect: How to implement the search command**

* **Alternative 1 (current choice)**: Accept multiple search fields in the search query.
  * Pros: More user-friendly as users can search using multiple fields at once, allowing for a more targeted search.
  * Cons: More prone to errors due to the broader search scope over multiple fields.

* **Alternative 2**: Only accept one field in the search query.
  * Pros: Less prone to errors due to the stricter search only over one field.
  * Cons: Less user-friendly since users will not be able to search using multiple fields at once.

<div style="page-break-after: always;"></div>

### Delete feature

#### Overview

The `delete` command enables users to delete a specific contact from PoochPlanner.

The following sequence diagram models the interactions between the different components of PoochPlanner for the
execution of the `delete` command:

<img src="images/DeleteSequenceDiagram.png" width="900" />

#### Details

1. The user inputs the command to delete a contact by stating the name of the contact they want to delete.
2. A `DeleteCommandParser` object invokes its `parse` method which parses the user input by storing the prefixes and their respective values as an `ArgumentMultimap` object.
3. A `DeleteCommand` object is created with the name of the contact to delete.
4. The `DeleteCommandParser` object returns the `DeleteCommand` object.
5. A `LogicManager` object invokes the `execute` method of the `DeleteCommand` object.
6. The `execute` method of the `DeleteCommand` object invokes the `deletePerson` method of its `Model` argument which removes the specified contact from its `addressBook` property.
7. The `execute` method of `DeleteCommand` returns a `CommandResult` object which stores the data regarding the completion of the `delete` command.

#### Example Usage

1. The user launches the application.
2. The user inputs `/delete ; name : Poochie` into the CLI.
3. The contact with the name "Poochie" will be deleted from PoochPlanner. This change should be reflected in the contacts list in PoochPlanner.

**Aspect: How to implement delete command**

* **Alternative 1 (current choice)**: Accept multiple name fields, where only the last name field will be taken.
  * Pros: More user-friendly. Should users make a mistake, they can easily append another name field without deleting the previous name field entered.
  * Cons: Less rigorous validation check on entered names as users may not intentionally enter a second name field.

* **Alternative 2**: Accept only one name field.
  * Pros: Less prone to possible errors due to stricter validation checks on name fields.
  * Cons: Less user-friendly since users will have to put in more effort to fix their commands.

<div style="page-break-after: always;"></div>

### Rate feature

#### Overview

The `rate` command enables users to rate a specific contact in PoochPlanner.

The following sequence diagram models the interactions between the different components of PoochPlanner for the
execution of the `rate` command:

<img src="images/RateCommandSequenceDiagram.png" width="900" />

#### Details

1. The user inputs the command to add a rating to a specific contact by first stating the name of the
   contact they want to rate. This is followed by the rating to be given to the user.
2. A `RateCommandParser` object invokes its `parse` method which parses the user input by storing the name and its
   prefix in an `ArgumentMultimap` object.
3. A `RateCommand` object is created with the parsed name and rating.
4. A `LogicManager` object invokes the `execute` method of the `RateCommand` object.
5. The `execute` method of the `RateCommand` object invokes the `findByName` method of its `Model` argument to find the
   contact with the specified name.
6. The `execute` method of the `RateCommand` object invokes the `setPerson` method of its `Model` argument to set the contact
   in the existing contacts list to the new `Person` object which has been edited by the `execute` method of the `RateCommand` object.
7. The `execute` method of the `RateCommand` object invokes the `updateFilteredPersonList` method of its `Model` argument to
   update the view of PoochPlanner to show all contacts.
8. The `execute` method of the `RateCommand` objects returns a `CommandResult` object which stores the data regarding the
   completion of the `rate` command.

#### Example Usage

1. The user launches the application.
2. The user inputs `/rate ; name : Poochie ; rating : 5` into the CLI.
3. The contact with the name "Poochie" is given a rating of "5". This change should be reflected in the contacts list in PoochPlanner.

**Aspect: How to store rating field in Person class and subclasses**

* **Alternative 1 (current choice)**: Add the rating field to all four constructors (`Person`, `Staff`, `Maintainer`, `Supplier`).
  * Pros: Leverages inheritance, thus reducing repeated code and adheres to OOP.
  * Cons: Changing the constructors of the four classes is a tedious task.

* **Alternative 2**: Add the rating field to the parent person constructor and use a setter to set new ratings.
  * Pros: Much simpler implementation that will require less refactoring of code.
  * Cons: Violates OOP, specifically encapsulation as the other classes would be able to manipulate the
    ratings of `Person` objects directly.

<div style="page-break-after: always;"></div>

[//]: # (@@author yleeyilin)

### Pin and Unpin features

#### Overview

The `pin` and `unpin` commands enable users to pin and unpin any existing contacts in PoochPlanner.

The following sequence diagram models the interactions between the different components of PoochPlanner for the execution of the `pin` command:

![Pin Sequence Diagram](images/PinCommandSequenceDiagram.png)

The following sequence diagram models the interactions between the different components of PoochPlanner for the execution of the `unpin` command:

![Unpin Sequence Diagram](images/UnpinCommandSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The implementation for pinning and unpinning `Person`, `Staff`, `Supplier`, `Maintainer` are the same. The `pin` and `unpin` commands are also implemented similarly as seen in the sequence diagrams above. 
</div>

#### Details

1. The user inputs the command to pin/unpin a specified contact by stating the target name of the contact that they want to pin/unpin.
2. A `PinCommandParser`/`UnpinCommandParser` object invokes its `parse` method which parses the user input by storing the prefixes and their respective values as an `ArgumentMultimap` object.
3. A `PinCommand`/`UnpinCommand` object is created with the name of the contact to pin/unpin.
4. The `PinCommandParser`/`UnpinCommandParser` object returns the `PinCommand`/`UnpinCommand` object.
5. A `LogicManager` object invokes the `execute` method of the `PinCommand`/`UnpinCommand` object.
6. The `execute` method of the `PinCommand`/`UnpinCommand` object finds the specified contact by its name. The `updateToPinned`/`updateToUnpinned` method of the found `Person` object creates a new `Person` object that contains the updated `pin` boolean of the contact.
7. The `execute` method of the `PinCommand`/`UnpinCommand` object invokes the `setPerson` method of its `Model` argument to replace the specified contact with the new `Person` object.
8. The `execute` method of the `PinCommand`/`UnpinCommand` object invokes the `updatePinnedPersonList` method of its `Model` argument to update the view of PoochPlanner to show all contacts.
9. The `execute` method of the `PinCommand`/`UnpinCommand` object returns a `CommandResult` object which stores the data regarding the completion of the `pin`/`unpin` command.

#### Example Usage

1. The user launches the application.
2. The user inputs `/pin ; name : Alice Tan` or `/unpin ; name : Alice Tan` into the CLI.
3. The contact card for the contact named "Alice Tan" is now pinned/unpinned. This change should be reflected in the contacts list in PoochPlanner.

**Aspect: How to implement pin/unpin command**

* **Alternative 1 (current choice)**: Accept multiple name fields, where only the last name field will be taken.
  * Pros: More user-friendly. Should users make a mistake, they can easily append another name field without deleting the previous name field entered.
  * Cons: Less rigorous validation check on name as users may not intentionally enter a second name field.

* **Alternative 2**: Accept only one name field.
  * Pros: Less prone to possible errors due to stricter validation checks on name fields.
  * Cons: Less user-friendly since users will have to put in more effort to fix their commands.

<div style="page-break-after: always;"></div>

### Sort feature

#### Overview

The `sort` command enables users to sort contacts in PoochPlanner by a contact field.

The following sequence diagram models the interactions between the different components of PoochPlanner for the execution of the `sort` command:

<img src="images/SortCommandSequenceDiagram.png" width="900" />

#### Details

1. The user inputs the command to sort contacts with the target field.
2. A `SortCommandParser` object invokes its `parse` method which parses the user input through `ArgumentMultimap` and `mapName`, creating a new `Prefix` object.
3. The `SortCommandParser` object then creates a new `SortCommand` object with the target `prefix`, returning this object.
4. A `LogicManager` object invokes the `execute` method of the `SortCommand` object.
5. The `execute` method of the `SortCommand` object invokes the `updateSortedPersonList` method of its `Model` argument with the target `prefix` to update the view of PoochPlanner to sort all contacts by the target field.
6. The `execute` method of the `SortCommand` object returns a `CommandResult` object which stores the data regarding the completion of the `sort` command.

#### Example Usage

1. The user launches the application.
2. The user inputs `/sort ; field : phone` into the CLI.
3. PoochPlanner is updated to sort all the contact cards by phone number in ascending order. This change should be reflected in the contacts list in PoochPlanner.

**Aspect: How to implement sort command**

* **Alternative 1 (current choice)**: Sorts only by ascending order, alphabetically and numerically (except ratings, which are sorted in descending order).
  * Pros: Straightforward to input command. Users can just key in the field they want to sort by without having to indicate whether to sort either in ascending or descending order.
  * Cons: Less flexible in sorting as later alphabets and larger values will take longer to find.

* **Alternative 2**: Sorts by both ascending and descending order depending on user indication.
  * Pros: More flexible in sorting for users to sort in either way to find what they need.
  * Cons: Longer command, another field required to specify either ascending or descending sorting order.

<div style="page-break-after: always;"></div>

[//]: # (@@author jannaleong)

### Note feature

#### Overview

The `note` command enables users to add notes to existing contacts in PoochPlanner.

The following sequence diagram models the interactions between the different components of PoochPlanner for the execution of the `note` command.

<img src="images/NoteCommandSequenceDiagram.png" width="900" />

#### Details

1. The user inputs the command to add a note to a specified contact by first stating the name of the contact they want to add a note to. This is followed by the note and an optional deadline prefixes and their respective values.
2. A `NoteCommandParser` object invokes its `parse` method which parses the user input by storing the prefixes and their respective values as an `ArgumentMultimap` object.
3. A `NoteCommand` object is created with the parsed name, note and optional deadline field.
4. The `NoteCommandParser` object returns the `NoteCommand` object.
5. A `LogicManager` object invokes the `execute` method of the `NoteCommand` object. 
6. The `execute` method of the `NoteCommand` object invokes the `findByName` method of its `Model` argument to find the person with the specified name. 
7. The `execute` method of the `NoteCommand` object invokes the `setPerson` method of its `Model` argument to set the person in the existing contacts list to the new `Person` object which has been edited by the `execute` method of the `NoteCommand` object. 
8. The `execute` method of the `NoteCommand` object invokes the `updateFilteredPersonList` method of its `Model` argument to update the view of PoochPlanner to show all contacts. 
9. The `execute` method of the `NoteCommand` object returns a `CommandResult` object which stores the data regarding the completion of the `note` command.

#### Example Usage

1. The user launches the application.
2. The user inputs `/note ; name : Janna ; note : get kibble` into the CLI.
3. The given note will be added to the description of the contact with the given name. This change should be reflected in the contacts list in PoochPlanner.

**Aspect: How to store note field in Person class and subclasses**

* **Alternative 1 (current choice)**: Add note field to all four constructors (`Person`, `Staff`, `Maintainer`, `Supplier`).
    * Pros: Leverages inheritance, thus reducing repeated code and adheres to OOP.
    * Cons: Changing the constructors of the four classes is a tedious task.

* **Alternative 2**: Add note field to the parent person constructor and use a setter to set new notes.
    * Pros: Much simpler implementation that will require less refactoring of code.
    * Cons: Violates OOP, specifically encapsulation as the other classes would be able to manipulate the
  inner details of `Person` objects directly.

<div style="page-break-after: always;"></div>

[//]: # (@@author)

[//]: # (@@author chiageng)
### Undo and redo features

#### Overview

The `undo` and `redo` commands enable users to undo and redo previous actions made in PoochPlanner.

<br>

As the implementation of the `undo` and `redo` commands are more complicated, the mechanism is fully explained with the corresponding implementation as described below.

#### Implementation

The undo/redo mechanism is facilitated by the `VersionedAddressBook` class. It extends the `AddressBook` class with an additional undo/redo history, stored internally with the `addressBookStateList` and `currentStatePointer` properties. Additionally, it supports the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. A `VersionedAddressBook` object will be initialized with an initial AddressBook state, with the `currentStatePointer` pointer pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes the `/delete ; name : Poochie` command to delete the contact named "Poochie" from PoochPlanner. The `delete` command calls the `Model#commitAddressBook()` method, causing the modified state of the address book after the `/delete ; name : Poochie` command executes to be saved in the `addressBookStateList` property, and the `currentStatePointer` pointer is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `/add-person ; name : John …​` to add a new contact named "John". The `add-person` command also calls the `Model#commitAddressBook()` method, causing another modified address book state to be saved into the `addressBookStateList` property.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails during its execution, it will not call the `Model#commitAddressBook()` method, and consequently the address book state will not be saved into the `addressBookStateList` property.

</div>

Step 4. The user now decides that adding John was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call the `Model#undoAddressBook()` method, which will shift the `currentStatePointer` pointer once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` pointer is at index 0 (pointing to the initial address book state), then there are no previous address book states to restore. The `undo` command calls the `Model#canUndoAddressBook()` method to check if this is the case. If so, it will return an error to the user rather
than attempt to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for the `UndoCommand` object should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls the `Model#redoAddressBook()` method, which shifts the `currentStatePointer` pointer once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` pointer is at index `addressBookStateList.size() - 1` (pointing to the latest address book state), then there are no undone address book states to restore. The `redo` command calls the `Model#canRedoAddressBook()` method to check if this is the case. If so, it will return an error to the user rather than attempt to perform the redo.

</div>

Step 5. The user then decides to execute the `list` command. Commands that do not modify the address book, such as `list`, will not call the `Model#commitAddressBook()` method. Thus, the `addressBookStateList` property remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes the `clear` command, which calls the `Model#commitAddressBook()` method. Since the `currentStatePointer` pointer is not pointing at the end of the `addressBookStateList` property, all address book states after the `currentStatePointer` property will be purged (reason: It no longer makes sense to redo the `/add-person ; name : John...` command). This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

### Design considerations:

#### Aspect: How undo and redo executes:

**Alternative 1 (current choice)**: Save snapshots of the entire address book in individual states (store a revision history).
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

**Alternative 2**: Implement inverse commands (commands that are antagonistic in nature).
* Pros: Significant reduction in memory overhead.
* Cons: Not all commands have an inverse (e.g. the `sort` command is not a bijection and hence no single function exists as its inverse).

[//]: # (@@author jannaleong)

### Help feature

#### Overview

The `help` command enables users to view help for all commands.

The following sequence diagram models the interactions between the different components of PoochPlanner for the execution of the `help` command:

<img src="images/HelpCommandSequenceDiagram.png" width="900" />

#### Details

1. The user inputs the command to view help for a specific command. This is followed by the command field specifying the command they want to view help for.
2. A `HelpCommandParser` object invokes its `parse` method which parses the user input by storing the prefix of its respective values as an `ArgumentMultimap` object.
3. A `HelpCommand` object is created with the command type that was specified in the command field.
4. The `HelpCommandParser` object returns the `HelpCommand` object.
5. A `LogicManager` object invokes the `execute` method of the `HelpCommand` object.
6. The `execute` method of the `HelpCommand` object returns a `CommandResult` object which stores the data regarding the completion of the `help` command.

#### Example Usage

1. The user launches the application.
2. The user inputs `/help ; command : delete` into the CLI.
3. Help for the `delete` command will be displayed.

**Aspect: How to display different help command windows**

* **Alternative 1 (current choice)**: Use only one help window class to display different help messages for different commands. Different content is displayed by passing in different strings.
  * Pros: Code is made much more concise.
  * Cons: Lengthy if-else statements are required to display the correct string.

* **Alternative 2**: Create a different help window class for each type of command.
  * Pros: All details relating to a single command is within its own file. Can be perceived as neater.
  * Cons: Highly repetitive code. Even small mistakes made would have to be fixed in over ten windows.

<div style="page-break-after: always;"></div>

### Remind feature

#### Overview

The `remind` command enables users to view all contacts with note deadlines from today onwards.

The following sequence diagram models the interactions between the different components of PoochPlanner for the execution of the `remind` command:

![Remind Sequence Diagram](images/RemindCommandSequenceDiagram.png)

#### Details

1. The user inputs the command to view reminders.
2. A `RemindCommand` object is created.
3. A `LogicManager` object invokes the `execute` method of the `RemindCommand` object.
4. The `execute` method of the `RemindCommand` object invokes the `updateFilteredPersonList` method of its `Model` argument to update the view of the application to show contacts
   with note deadlines from today onwards.
5. The `execute` method of the `RemindCommand` object returns a `CommandResult` object which stores the data regarding the completion of the `remind` command.

#### Example Usage

1. The user launches the application.
2. The user inputs `/remind` into the CLI.
3. Contacts that have deadline notes from today onwards will be displayed.

<div style="page-break-after: always;"></div>

### Clear feature

#### Overview

The `clear` command enables users to remove all existing contacts from PoochPlanner.

The following sequence diagram models the interactions between the different components of PoochPlanner for the execution of the `clear` command:

![Clear Sequence Diagram](images/ClearCommandSequenceDiagram.png)

#### Details

1. The user inputs the command to clear all contacts.
2. A `LogicManager` object invokes the `execute` method of a `ClearCommand` object.
3. The `execute` method of the `ClearCommand` object invokes the `setAddressBook` method of its `Model` argument with a new `AddressBook` object which contains an empty `UniquePersonList` property.
4. The `execute` method of the `ClearCommand` object returns a `CommandResult` object which stores the data regarding the completion of the `clear` command.

#### Example Usage

1. The user launches the application.
2. The user inputs `/clear` into the CLI.
3. The data in PoochPlanner is emptied.

<div style="page-break-after: always;"></div>

### List feature

#### Overview

The `list` command enables users to view all existing contacts from PoochPlanner.

The following sequence diagram models the interactions between the different components of PoochPlanner for the execution of the `list` command:

![List Sequence Diagram](images/ListCommandSequenceDiagram.png)

#### Details

1. The user inputs the command to list all contacts.
2. A `LogicManager` object invokes the `execute` method of a `ListCommand` object.
3. The `execute` method of the `ListCommand` object invokes the `updateFilteredPersonList` method of its `Model` argument to update the view of the application to show all contacts.
4. The `execute` method of the `ListCommand` object returns a `CommandResult` object which stores the data regarding the completion of the `list` command.

#### Example Usage

1. The user launches the application.
2. The user inputs `/list` into the CLI.
3. All contacts in PoochPlanner are displayed.

<div style="page-break-after: always;"></div>

[//]: # (@@author)

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

<div style="page-break-after: always;"></div>

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Dog cafe owners who need to manage a team of staff, F&B vendors, and a dog maintenance team.
* Users who prefer typing to other forms of input and who are comfortable using CLI applications.

**Value proposition**: PoochPlanner is a desktop application to track details of various groups (`Person`, `Supplier`, `Maintainer`, `Staff`) that dog cafe owners have to regularly interact with. The app is optimized for use using a Command Line Interface (CLI) while still encompassing a user-friendly Graphical User Interface (GUI).

<div style="page-break-after: always;"></div>

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                | I want to …​                                                          | So that I can…​                                                                             |
|----------|------------------------|-----------------------------------------------------------------------|---------------------------------------------------------------------------------------------|
| `* * *`  | well connected user    | add new contacts to my contacts list                                  | have the contacts of new acquaintances in my contacts list                                  |
| `* * *`  | cafe owner user        | edit my contacts in my contacts list                                  | update contact information such as the new phone number of my contacts                      |
| `* * *`  | cafe owner user        | delete contacts                                                       | remove outdated contacts such as retrenched staff                                           |
| `* * *`  | well connected user    | search through my long list of contacts by different specified fields | find my contacts efficiently                                                                |
| `* * *`  | first-time user        | get help about what commands to use                                   | easily know how to navigate the system                                                      |
| `* *`    | profit-maximising user | sort vendors in ascending order of price                              | view the vendors selling the cheapest products easily                                       |
| `* *`    | careless user          | undo my commands                                                      | revert my accidental commands easily                                                        |
| `* *`    | careless user          | redo my commands                                                      | revert my accidental undo commands easily                                                   |
| `* *`    | well connected user    | pin my contacts in my contacts list                                   | easily view important contacts                                                              |
| `* *`    | well connected user    | unpin my contacts in my contacts list                                 | remove my less important contacts from the top of my list                                   |
| `* *`    | profit-maximising user | rate the efficiency of contacts                                       | view the efficiency of my contacts easily and only conduct business with efficient contacts |
| `* *`    | forgetful user         | note down all details about my contacts                               | track and remember important details and deadlines easily                                   |
| `* *`    | forgetful user         | be reminded of my deadlines                                           | complete all my tasks on time                                                               |

<div style="page-break-after: always;"></div>

### Use cases

[//]: # (@@author yleeyilin)
**System**: `PoochPlanner`

**Use case**: `UC01 - Adding a contact`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 3, a new contact is added into the contacts list.`

**MSS**:

1.  User requests to add the contact of a contact.
2.  PoochPlanner updates the contacts list.
3.  PoochPlanner confirms the successful addition.

    Use case ends.

**Extensions**:

* 1a. PoochPlanner detects a missing field in the entered input.
   * 1a1. PoochPlanner displays the error message.
   * 1a2. User re-enters a new command with the required field.
   * Steps 1a1 - 1a2 are repeated until the input entered is correct.
   * Use case resumes from step 2.

* 1b. PoochPlanner detects a duplicate name entry.
   * 1b1. PoochPlanner displays the error message.
   * 1b2. User re-enters a new command with another name.
   * Steps 1b1 - 1b2 are repeated until there are no duplicate entries in the input.
   * Use case resumes from step 2.

* 1c. PoochPlanner detects an invalid address format.
  * 1c1. PoochPlanner displays the error message.
  * 1c2. User re-enters a new command with a correct address format.
  * Steps 1c1 - 1c2 are repeated until there is no error with the input.
  * Use case resumes from step 2.

* 1d. PoochPlanner detects an invalid email format.
   * 1d1. PoochPlanner displays the error message.
   * 1d2. User re-enters a new command with a correct email format.
   * Steps 1d1 - 1d2 are repeated until there is no error with the input.
   * Use case resumes from step 2.

* 1e. PoochPlanner detects an invalid input for employment.
  * 1e1. PoochPlanner displays the error message.
  * 1e2. User re-enters a new command with correct input for employment.
  * Steps 1e1 - 1e2 are repeated until there is no error with the input.
  * Use case resumes from step 2.

* 1f. PoochPlanner detects an invalid phone format.
    * 1f1. PoochPlanner displays the error message.
    * 1f2. User re-enters a new command with a correct phone format.
    * Steps 1f1 - 1f2 are repeated until there are no errors with the input.
    * Use case resumes from step 2.
   
* 1g. PoochPlanner detects an invalid salary format.
    * 1g1. PoochPlanner displays the error message.
    * 1g2. User re-enters a new command with a correct price format.
    * Steps 1g1 - 1g2 are repeated until there are no errors in input.
    * Use case resumes from step 2.

* 1h. PoochPlanner detects an invalid price format.
    * 1h1. PoochPlanner displays the error message.
    * 1h2. User re-enters a new command with a correct price format.
    * Steps 1h1 - 1h2 are repeated until there are no errors with the input.
    * Use case resumes from step 2.

* 1i. PoochPlanner detects an invalid note format.
  * 1i1. PoochPlanner displays the error message.
  * 1i2. User re-enters a new command with a correct note format.
  * Steps 1i1 - 1i2 are repeated until there are no errors with the input.
  * Use case resumes from step 2.

* 1j. PoochPlanner detects an invalid rating format.
  * 1j1. PoochPlanner displays the error message.
  * 1j2. User re-enters a new command with a correct rating format.
  * Steps 1j1 - 1j2 are repeated until there are no errors with the input.
  * Use case resumes from step 2.

* 1k. PoochPlanner detects an invalid product format.
  * 1k1. PoochPlanner displays the error message.
  * 1k2. User re-enters a new command with a correct product format.
  * Steps 1k1 - 1k2 are repeated until there are no errors with the input.
  * Use case resumes from step 2.

* 1l. PoochPlanner detects an invalid commission format.
  * 1l1. PoochPlanner displays the error message.
  * 1l2. User re-enters a new command with a correct commission format.
  * Steps 1l1 - 1l2 are repeated until there are no errors with the input.
  * Use case resumes from step 2.

* 1m. PoochPlanner detects an invalid skill format.
  * 1m1. PoochPlanner displays the error message.
  * 1m2. User re-enters a new command with a correct skill format.
  * Steps 1m1 - 1m2 are repeated until there are no errors with the input.
  * Use case resumes from step 2.

* 1n. PoochPlanner detects an invalid name format.
  * 1n1. PoochPlanner displays the error message.
  * 1n2. User re-enters a new command with a correct name format.
  * Steps 1n1 - 1n2 are repeated until there are no errors with the input.
  * Use case resumes from step 2.

[//]: # (@@author)

[//]: # (@@author yleeyilin)

<div style="page-break-after: always;"></div>

**System**: `PoochPlanner`

**Use case**: `UC02 - Editing a contact`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 3, the contact is successfully edited in the contacts list.`

**MSS**:

1.  User requests to edit the field of a contact.
2.  PoochPlanner updates the field of specified contact.
3.  PoochPlanner confirms the successful edit.

    Use case ends.

**Extensions**:

* 1a. PoochPlanner detects a missing name field in the entered input.
   * 1a1. PoochPlanner displays the error message.
   * 1a2. User re-enters a new command with the name field.
   * Steps 1a1 - 1a2 are repeated until the input entered is correct.
   * Use case resumes from step 2.

* 1b. PoochPlanner is unable to find the contact.
   * 1b1. PoochPlanner displays the error message.
   * 1b2. User re-enters a new command with another name.
   * Steps 1b1 - 1b2 are repeated until the input references a contact that exists in PoochPlanner.
   * Use case resumes from step 2.

* 1c. PoochPlanner detects an unknown input for employment.
  * 1c1. PoochPlanner displays the error message.
  * 1c2. User re-enters a new command with correct input for employment.
  * Steps 1c1 - 1c2 are repeated until there are no errors with the input.
  * Use case resumes from step 2.

* 1d. PoochPlanner detects empty field in the entered input.
   * 1d1. PoochPlanner displays the error message.
   * 1d2. User re-enters a new command and specifies the field(s) to edit.
   * Steps 1d1 - 1d2 are repeated until a valid field is specified.
   * Use case resumes from step 2.

* 1e. User specifies an invalid field.
   * 1e1. PoochPlanner displays the error message.
   * 1e2. User re-enters a new command with a correct field format.
   * Steps 1e1 - 1e2 are repeated until a valid field is specified.
   * Use case resumes from step 2.

* 1f. PoochPlanner detects an invalid email format.
    * 1f1. PoochPlanner displays the error message.
    * 1f2. User re-enters a new command with a correct email format.
    * Steps 1f1 - 1f2 are repeated until there are no errors with the input.
    * Use case resumes from step 2.

* 1g. PoochPlanner detects an invalid phone format.
    * 1g1. PoochPlanner displays the error message.
    * 1g2. User re-enters a new command with a correct phone format.
    * Steps 1g1 - 1g2 are repeated until there are no errors with the input.
    * Use case resumes from step 2.
   
* 1h. PoochPlanner detects an invalid salary format.
    * 1h1. PoochPlanner displays the error message.
    * 1h2. User re-enters a new command with a correct salary format.
    * Steps 1h1 - 1h2 are repeated until there are no errors with the input.
    * Use case resumes from step 2.

* 1i. PoochPlanner detects an invalid price format.
    * 1i1. PoochPlanner displays the error message.
    * 1i2. User re-enters a new command with a correct price format.
    * Steps 1i1 - 1i2 are repeated until there are no errors with the input.
    * Use case resumes from step 2.

* 1j. PoochPlanner detects an invalid address format.
  * 1j1. PoochPlanner displays the error message.
  * 1j2. User re-enters a new command with a correct address format.
  * Steps 1j1 - 1j2 are repeated until there are no errors with the input.
  * Use case resumes from step 2.

* 1k. PoochPlanner detects an invalid commission format.
  * 1k1. PoochPlanner displays the error message.
  * 1k2. User re-enters a new command with a correct commission format.
  * Steps 1k1 - 1k2 are repeated until there are no errors with the input.
  * Use case resumes from step 2.

* 1l. PoochPlanner detects an invalid product format.
  * 1l1. PoochPlanner displays the error message.
  * 1l2. User re-enters a new command with a correct product format.
  * Steps 1l1 - 1l2 are repeated until there are no errors with the input.
  * Use case resumes from step 2.

* 1m. PoochPlanner detects an invalid name format.
  * 1m1. PoochPlanner displays the error message.
  * 1m2. User re-enters a new command with a correct name format.
  * Steps 1m1 - 1m2 are repeated until there are no errors with the input.
  * Use case resumes from step 2.

* 1n. PoochPlanner detects an invalid skill format.
  * 1n1. PoochPlanner displays the error message.
  * 1n2. User re-enters a new command with a correct skill format.
  * Steps 1n1 - 1n2 are repeated until there are no errors with the input.
  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

[//]: # (@@author)

[//]: # (@@author yleeyilin)

**System**: `PoochPlanner`

**Use case**: `UC03 - Searching for a contact`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 3, the contacts list is filtered successfully.`

**MSS**:

1.  User requests to search for the contact of a person with a keyword for a specified field.
2.  PoochPlanner confirms successful search.
3.  PoochPlanner returns the filtered list of contacts that matches the keyword as specified by the user.

    Use case ends.

**Extensions**:

* 1a. PoochPlanner detects a missing field in the entered input.
   * 1a1. PoochPlanner displays the error message.
   * 1a2. User re-enters a new command with a specified field.
   * Steps 1a1 - 1a2 are repeated until a valid field is entered by the user.
   * Use case resumes from step 2.

* 1b. PoochPlanner detects duplicate fields in the entered input.
  * 1b1. PoochPlanner displays the error message.
  * 1b2. User re-enters a new command with a specified field.
  * Steps 1b1 - 1b2 are repeated until the command does not contain any duplicate fields.
  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

[//]: # (@@author)

[//]: # (@@author yleeyilin)

**System**: `PoochPlanner`

**Use case**: `UC04 - Deleting a contact`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 3, the contact is deleted from the contacts list.`

**MSS**:

1.  User requests to delete a contact.
2.  PoochPlanner removes the contact and updates the contacts list.
3.  PoochPlanner confirms the successful deletion.

    Use case ends.

**Extensions**:

* 1a. PoochPlanner detects a missing name field in the entered input.
  * 1a1. PoochPlanner displays the error message.
  * 1a2. User re-enters a new command with the name field.
  * Steps 1a1 - 1a2 are repeated until the input entered is correct.
  * Use case resumes from step 2.

* 1b. PoochPlanner is unable to find the contact.
  * 1b1. PoochPlanner displays the error message.
  * 1b2. User re-enters a new command with another name.
  * Steps 1b1 - 1b2 are repeated until the input name matches a contact name that exists in PoochPlanner.
  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

[//]: # (@@author)

**System**: `PoochPlanner`

**Use case**: `UC05 - Rating a contact`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 3, a rating for the contact is updated successfully in the contacts list.`

**MSS**:

1.  User requests to rate a contact with the specified rating.
2.  PoochPlanner updates the contact rating with the rating provided.
3.  PoochPlanner confirms the successful rating of the contact.

    Use case ends.

**Extensions**:

* 1a. PoochPlanner detects a missing name in the entered input.
   * 1a1. PoochPlanner displays the error message.
   * 1a2. User re-enters a new command with a specified name.
   * Steps 1a1 - 1a2 are repeated until a valid name is input by the User.
   * Use case resumes from step 2.

* 1b. PoochPlanner detects an invalid name in the entered input.
   * 1b1. PoochPlanner displays the error message.
   * 1b2. User re-enters a new command with a specified name.
   * Steps 1b1 - 1b2 are repeated until a valid name is input by the User.
   * Use case resumes from step 2.

* 1c. PoochPlanner detects a missing rating in the entered input.
   * 1c1. PoochPlanner displays the error message.
   * 1c2. User re-enters a new command with a new rating value.
   * Steps 1c1 - 1c2 are repeated until the rating provided is an integer between 0 and 5 inclusive.
   * Use case resumes from step 2.

* 1d. PoochPlanner detects an invalid rating in the entered input.
   * 1d1. PoochPlanner displays the error message.
   * 1d2. User re-enters a new command with a new rating value.
   * Steps 1d1 - 1d2 are repeated until the rating provided is an integer between 0 and 5 inclusive.
   * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

[//]: # (@@author yleeyilin)

**System**: `PoochPlanner`

**Use case**: `UC06 - Pinning a contact`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 3, the user has successfully pinned the contact.`

**MSS**:

1. User requests to pin a contact.
2. The specified contact is pinned successfully.
3. PoochPlanner displayed the contacts list with the pinned contacts at the top.

   Use case ends.

* 1a. PoochPlanner detects a missing name field in the entered input.
  * 1a1. PoochPlanner displays the error message.
  * 1a2. User re-enters a new command with a specified name field.
  * Steps 1a1 - 1a2 are repeated until the input entered is correct.
  * Use case resumes from step 2.

* 1b. PoochPlanner detects an invalid name field in the entered input.
  * 1b1. PoochPlanner displays the error message.
  * 1a2. User re-enters a new command with a specified name field.
  * Steps 1b1 - 1b2 are repeated until the input entered is correct.
  * Use case resumes from step 2.

* 1c. PoochPlanner fails to find the person.
  * 1c1. PoochPlanner displays the error message.
  * 1c2. User re-enters a new command with another name.
  * Steps 1c1 - 1c2 are repeated until the input name matches a contact name that exists in PoochPlanner.
  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

[//]: # (@@author)

[//]: # (@@author yleeyilin)

**System**: `PoochPlanner`

**Use case**: `UC07 - Unpinning a contact`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 3, the user has successfully unpinned the contact.`

**MSS**:

1. User requests to unpin a contact.
2. The specified contact is unpinned successfully.
3. PoochPlanner updates the contacts list with the remaining pinned contacts at the top.

   Use case ends.

**Extensions**:

* 1a. PoochPlanner detects a missing name field in the entered input.
  * 1a1. PoochPlanner displays the error message.
  * 1a2. User re-enters a new command with a specified name field.
  * Steps 1a1 - 1a2 are repeated until the input entered is correct.
  * Use case resumes from step 2.

* 1b. PoochPlanner detects an invalid name field in the entered input.
  * 1b1. PoochPlanner displays the error message.
  * 1b2. User re-enters a new command with a specified name field.
  * Steps 1b1 - 1b2 are repeated until the input entered is correct.
  * Use case resumes from step 2.

* 1c. PoochPlanner fails to find the person.
  * 1c1. PoochPlanner displays the error message.
  * 1c2. User re-enters a new command with another name.
  * Steps 1c1 - 1c2 are repeated until the input name matches a contact name that exists in PoochPlanner.
  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

[//]: # (@@author)

**System**: `PoochPlanner`

**Use case**: `UC08 - Sorting the contacts list`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 3, the user has successfully sorted the contacts list by a specified field.`

**MSS**:

1.  User requests to sort PoochPlanner by a specified field.
2.  PoochPlanner updates the contacts list in the sorted order.
3.  PoochPlanner confirms that the contacts list has been successfully sorted.

    Use case ends.

**Extensions**:

* 1a. PoochPlanner detects a missing field in the entered input.
  * 1a1. PoochPlanner displays the error message.
  * 1a2. User re-enters a new command with a specified name.
  * Steps 1a1 - 1a2 are repeated until a valid name is input by the User.
  * Use case resumes from step 2.

* 1b. PoochPlanner detects an invalid field in the entered input.
  * 1b1. PoochPlanner displays the error message.
  * 1b2. User re-enters a new command with a specified name.
  * Steps 1b1 - 1b2 are repeated until a valid field is input by the User.
  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

[//]: # (@@author jannaleong)

**System**: `PoochPlanner`

**Use case**: `UC09 - Adding a note to a contact`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 3, a note for the contact specified is updated successfully in the contacts list.`

**MSS**:

1.  User requests to add a note to the contact.
2.  PoochPlanner updates the contact with the specified note.
3.  PoochPlanner confirms that the note has been successfully added.

    Use case ends.

**Extensions**:

* 1a. PoochPlanner detects a missing name in the entered input.
  * 1a1. PoochPlanner displays the error message.
  * 1a2. User re-enters a new command with name value.
  * Steps 1a1 - 1a2 are repeated until a valid name value is input by the user.
  * Use case resumes from step 2.

* 1b. PoochPlanner detects an invalid name in the entered input.
  * 1b1. PoochPlanner displays the error message.
  * 1b2. User re-enters a new command with a new name value.
  * Steps 1b1 - 1b2 are repeated until a valid name value is input by the user.
  * Use case resumes from step 2.

* 1c. PoochPlanner detects a missing note in the entered input.
  * 1c1. PoochPlanner displays the error message.
  * 1c2. User re-enters a new command with a note value.
  * Steps 1c1 - 1c2 are repeated until the note value is provided (non-null/non-empty).
  * Use case resumes from step 2.

* 1d. PoochPlanner detects an invalid note in the entered input.
  * 1d1. PoochPlanner displays the error message.
  * 1d2. User re-enters a new command with a new note value.
  * Steps 1d1 - 1d2 are repeated until the note provided is valid (non-null/non-empty).
  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

**System**: `PoochPlanner`

**Use case**: `UC10 - Adding a deadline note to a contact`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 3, a note with a deadline for the specified contact will be updated successfully in the contacts list.`

**MSS**:

1.  User requests to add a deadline note to the contact.
2.  PoochPlanner updates the contact with the specified deadline note.
3.  PoochPlanner confirms that the deadline note has been successfully added.

    Use case ends.

**Extensions**:

* 1a. PoochPlanner detects a missing name in the entered input.
  * 1a1. PoochPlanner displays the error message.
  * 1a2. User re-enters a new command with a name value.
  * Steps 1a1 - 1a2 are repeated until a name is input by the user.
  * Use case resumes from step 2.

* 1b. PoochPlanner detects an invalid name in the entered input.
  * 1b1. PoochPlanner displays the error message.
  * 1b2. User re-enters a new command with a new name value.
  * Steps 1b1 - 1b2 are repeated until a valid name is input by the user.
  * Use case resumes from step 2.

* 1c. PoochPlanner detects a missing note in the entered input.
  * 1c1. PoochPlanner displays the error message.
  * 1c2. User re-enters a new command with a note value.
  * Steps 1c1 - 1c2 are repeated until the a note value is provided (non-null/non-empty).
  * Use case resumes from step 2.

* 1d. PoochPlanner detects an invalid note in the entered input.
  * 1d1. PoochPlanner displays the error message.
  * 1d2. User re-enters a new command with a new note value.
  * Steps 1d1 - 1d2 are repeated until the note provided is valid (non-null/non-empty).
  * Use case resumes from step 2.

* 1e. PoochPlanner detects an invalid deadline in the entered input.
  * 1e1. PoochPlanner displays the error message.
  * 1e2. User re-enters a new command with a new deadline value.
  * Steps 1e1 - 1e2 are repeated until the deadline provided is valid (non-null/non-empty).
  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

[//]: # (@@author)

[//]: # (@@author chiageng)

**System**: `PoochPlanner`

**Use case**: `UC11 - Undoing a command`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 2, the user has successfully reverted back to the previous command.`

**MSS**:

1.  User requests to undo a previous command.
2.  PoochPlanner retrieves a previous record of the address book data.

    Use case ends.

**Extensions**:

* 1a. PoochPlanner detects no previous record of the address book data.
    * 1a1. PoochPlanner displays the error message.
    * Use case ends.

[//]: # (@@author)

<div style="page-break-after: always;"></div>

[//]: # (@@author chiageng)

**System**: `PoochPlanner`

**Use case**: `UC12 - Redoing a command`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 2, the user has successfully reverted back the undo command.`

**MSS**:

1.  User requests to redo a previous command.
2.  PoochPlanner retrieves a future record of the address book data.

    Use case ends.

**Extensions**:

* 1a. PoochPlanner detects no future record of the address book data.
    * 1a1. PoochPlanner displays the error message.
    * Use case ends.

[//]: # (@@author)

<div style="page-break-after: always;"></div>

[//]: # (@@author jannaleong)

**System**: `PoochPlanner`

**Use case**: `UC13 - Viewing help`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 2, the help window for the corresponding command pops up.`

**MSS**:

1.  User requests to get help about a command.
2.  PoochPlanner displays help details relating to this command.

    Use case ends.

**Extensions**:

* 1a. User requests help for an invalid command (a command that is not offered by PoochPlanner).
  * 1a1. PoochPlanner displays the error message.
  * 1a2. User re-enters a new command and request to learn about a new command.
  * Steps 1a1 - 1a2 are repeated until a valid command is entered by the user.
  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

**System**: `PoochPlanner`

**Use case**: `UC14 - Viewing reminders for the contacts list`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 2, contacts will be displayed only if their note deadlines are on or after today's date.`

**MSS**:

1.  User requests to receive reminders.
2.  PoochPlanner displays all relevant contacts.

    Use case ends.

[//]: # (@@author)

**System**: `PoochPlanner`

**Use case**: `UC15 - Clearing the contacts list`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 3, the user has successfully cleared the contacts list.`

**MSS**:

1.  User requests to clear the data in the contacts list.
2.  PoochPlanner updates the data in the contacts list.
3.  PoochPlanner confirms that the data in the contacts list has been cleared.

    Use case ends.

<div style="page-break-after: always;"></div>

**System**: `PoochPlanner`

**Use case**: `UC16 - Listing all contacts`

**Actor**: `User`

**Guarantee**: `If MSS reaches step 3, the user has successfully listed all the contacts.`

**MSS**:

1.  User requests to list all contacts.
2.  PoochPlanner displays all relevant contacts.
3.  PoochPlanner confirms that all relevant contacts has been successfully listed.

    Use case ends.

<div style="page-break-after: always;"></div>


### Non-Functional Requirements

1. PoochPlanner needs to be compatible across major operating systems, including Windows, macOS, and Linux, supporting only Java 11.
2. User-managed transactions and budgets should be locally saved and backed up, ensuring restoration in subsequent sessions unless data integrity is compromised.
3. Thorough documentation of all non-private methods is essential to ensure the maintainability of the codebase.
4. PoochPlanner should function completely offline. 
5. PoochPlanner should be able to hold up to 1000 contacts without a noticeable sluggishness in performance for typical usage. 
6. A user with above average typing speed should be able to accomplish most of the tasks faster using commands than using the mouse. 
7. All code snippets presented in the developer guide shall follow a consistent coding style and formatting, adhering to the module's coding standards and best practices. 
8. The developer guide shall undergo regular content audits, with outdated or deprecated information flagged for removal or revision, and new features or updates documented within one week of release. 
9. The system should respond within 2 seconds. 
10. The data should be stored locally and should not be accessible from other devices due to privacy issues.
    
<div style="page-break-after: always;"></div>

### Glossary

* **PoochPlanner**: An address book CLI software that stores contacts.
* **Contact**: A contact that is stored in PoochPlanner.
* **Supplier**: External suppliers that sell the logistics required for the sustenance of dog cafe operations, for example dog food, to the dog cafe owners at a fixed price.
* **Staff**: Employees of the dog cafe that handle the running of the cafe.
* **Maintainer**: Specialized external workers that take special care of and maintain the dogs.
* **CLI**: Command Line Interface
* **GUI**: Graphical User Interface
* **MSS**: Main Success Scenario
* **JSON**: JavaScript Object Notation
* **API**: Application Programming Interface

<div style="page-break-after: always;"></div>

## **Appendix: Planned Enhancements**

1. Enhance commands to be space-insensitive
   1. Currently, we do not allow for incorrect spacings in commands. 
   2. `/add-person ; name : Person1 ;phone :98883888;address:Pooch Street 32 ; email : impooch@gmail.com`.
   3. The above example will be considered as invalid since there is no spacing before the `phone` prefix and before the `address` prefix. The lack of spacing causes `phone` and `address` to not be parsed as valid prefixes.
   4. We plan to extend PoochPlanner to accept alternative possible inputs with varied spacings to cater fast typists as varied spacings are likely to occur due to typing errors

2. Enhance commands to fix multiple white spacings in the user's input
   1. Currently, we do not have any checker to verify if there are multiple white spacings in the user's input. 
   2. We take any input values `John Doe` with multiple number of spacings as different inputs.
   3. We plan to parse all inputs to remove additional spacings to cater fast typists as additional spacings are likely to occur due to typing errors.

3. Enhance prices to allow for decimal places
   1. Currently, we do not allow prices to have decimal places.
   2. We plan to allow decimal places for prices to allow for greater flexibility in recording prices.

4. Enhance salaries to allow for storage in different units
   1. Currently, we only allow storing hourly salaries with the unit `/hr`.
   2. We plan to allow for more flexible units such as `/day`, `/month` and `/event`.

5. Enhance validation on input fields for search command
   1. Currently, we do not have any validation on input fields such as salary and phone in search commands.
   2. If users insert a random word in the salary field, the execution will not throw any error.
   3. We plan to do validation checks on all fields to ensure that users are inserting the correct type of value in the field.

6. Enhance post-search status
   1. Currently, after a search command, the contact book will only display the filtered contacts list.
   2. Execution of delete, pin, unpin, undo and redo will not return to the full contacts list.
   3. We plan to enhance the commands by returning to the full list after every command execution.

7. Enhance commissions to allow for decimal places
   1. Currently, we do not allow commissions to have decimal places.
   2. We plan to allow decimal places for commissions to allow for greater flexibility in recording commissions.

8. Enhance commissions to allow for storage in different units
   1. Currently, we only allow storing hourly commissions with the unit `/hr`.
   2. We plan to allow for more flexible units such as `/day`, `/month` and `/event`.

9. Enhance phone number storage
   1. Currently, we only allow users to add one phone number to one contact.
   2. We plan to allow users to add more than one phone number to allow for greater flexibility in storing contacts.

10. Enhance undo command upon pinning or unpinning
    1. Currently, when using pin command two or more times, calling undo once will not revert the pin operation. This is similar for unpin since they both share the same implementation.
    2. We plan to allow users to use undo only once to undo all repeated and consecutive pin/unpin attempts.

<div style="page-break-after: always;"></div>

## **Appendix: Instructions for manual testing**

Below are instructions to test the app manually. Before each test, run `/clear` to reset the data in PoochPlanner.
Also, take caution when copying the commands to the input box as our commands are space sensitive. Line breaks may result
in spaces being omitted.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the `[CS2103T-W10-2][PoochPlanner].jar` file and copy it into an empty folder

   2. Double-click the `[CS2103T-W10-2][PoochPlanner].jar` file.<br>
       Expected: Shows the GUI with an empty contacts list.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the `[CS2103T-W10-2][PoochPlanner].jar` file.<br>
       Expected: The most recent window size and location is retained.

<div style="page-break-after: always;"></div>

[//]: # (@@author chiageng)

### Adding a contact

1. Adding a `Person` contact

   1. Prerequisites: The specified name of the contact must not already exist in the contacts list.

   2. Test case: `/add-person ; name : Person1 ; phone : 98883888 ; address : Pooch Street 32 ; email : impooch@gmail.com`<br>
     Expected: Adds a person named "Person1" into the contacts list. Details of the added contact is shown in the status message.

2. Adding a `Staff` contact

   1. Prerequisites: The specified name of the contact must not already exist in the contacts list.

   2. Test case: `/add-staff ; name : Staff1 ; phone : 98765435 ; address : Poochie Street 21 ; email : ilovecatstoo@gmail.com ; salary : $50/hr ; employment : part-time`<br>
     Expected: Adds a staff named "Staff1" into the contacts list. Details of the added contact is shown in the status message.

3. Adding a `Supplier` contact

   1. Prerequisites: The specified name of the contact must not already exist in the contacts list.

   2. Test case: `/add-supplier ; name : Supplier1 ; phone : 98673098 ; address : Meow Street 24 ; email : ilovewombatstoo@gmail.com ; product : kibble ; price : $98/bag`<br>
     Expected: Adds a supplier named "Supplier1" into the contacts list. Details of the added contact is shown in the status message.

4. Adding a `Maintainer` contact

   1. Prerequisites: The specified name of the contact must not already exist in the contacts list.

   2. Test case: ` /add-maintainer ; name : Maintainer1 ; phone : 98765435 ; address : Poochie Street 24 ; email : ihelppooches@gmail.com ; skill : trainer ; commission : $60/hr`<br>
     Expected: Adds a maintainer named "Maintainer1" into the contacts list. Details of the added contact is shown in the status message.

[//]: # (@@author yleeyilin)

<div style="page-break-after: always;"></div>

### Editing a contact

1. Editing a `Person` contact

   1. Prerequisites: The contact to be edited must already exist and should have been added as a `Person` type. You can run the following command to add in a `Person` to edit:<br>
      `/add-person ; name : Person1 ; phone : 98883888 ; address : Pooch Street 32 ; email : impooch@gmail.com`
   
   2. Test case: `/edit-person ; name : Person1 ; field : { phone : 99820520 }`<br>
      Expected: The phone field of contact named "Person1" is edited to `99820520`. Details of the edited contact is shown in the status message.

   3. Test case: `/edit-person ; name : Person1 ; field : { address : Pooch Street 31 }`<br>
      Expected: The address field of contact named "Person1" is edited to `Pooch Street 31`. Details of the edited contact is shown in the status message.

   4. Test case: `/edit-person ; name : Person1 ; field : { phone : 99990520 ; email : impooch@gmail13.com }`<br>
      Expected: The phone and email field of contact named "Person1" is edited to `99990520` and `impooch@gmail13.com` respectively. Details of the edited contact is shown in the status message.

2. Editing a `Staff` contact

   1. Prerequisites: The contact to be edited must already exist and should have been added as a `Staff` type. You can run the following command to add in a `Staff` to edit:<br>
      `/add-staff ; name : Staff1 ; phone : 98765435 ; address : Poochie Street 21 ; email : ilovecatstoo@gmail.com ; salary : $50/hr ; employment : part-time`

   2. Test case: `/edit-staff ; name : Staff1 ; field : { phone : 99820520 }`<br>
      Expected: The phone field of contact named "Staff1" is edited to `99820520`. Details of the edited contact is shown in the status message.

   3. Test case: `/edit-staff ; name : Staff1 ; field : { salary : $55/hr }`<br>
      Expected: The salary field of contact named "Staff1" is edited to `$55/hr`. Details of the edited contact is shown in the status message.

   4. Test case: `/edit-staff ; name : Staff1 ; field : { employment : full-time }`<br>
      Expected: The employment field of contact named "Staff1" is edited to `full-time`. Details of the edited contact is shown in the status message.

   5. Test case: `/edit-staff ; name : Staff1 ; field : { salary : $40/hr ; employment : part-time }`<br>
      Expected: The salary and employment field of contact named "Staff1" is edited to `40/hr` and `part-time` respectively. Details of the edited contact is shown in the status message.

3. Editing a `Supplier` contact

   1. Prerequisites: The contact to be edited must already exist and should have been added as a `Supplier` type. You can run the following command to add in a `Supplier` to edit:<br>
      `/add-supplier ; name : Supplier1 ; phone : 98673098 ; address : Meow Street 24 ; email : ilovewombatstoo@gmail.com ; product : kibble ; price : $98/bag`

   2. Test case: `/edit-supplier ; name : Supplier1 ; field : { phone : 9994555 }`<br>
      Expected: The phone field of contact named "Supplier1" is edited to `9994555`. Details of the edited contact is shown in the status message.

   3. Test case: `/edit-supplier ; name : Supplier1 ; field : { product : dogdiapers }`<br>
      Expected: The product field of contact named "Supplier1" is edited to `dogdiapers`. Details of the edited contact is shown in the status message.

   4. Test case: `/edit-supplier ; name : Supplier1 ; field : { price : $10/bag }`<br>
      Expected: The price field of contact named "Supplier1" is edited to `$10/bag`. Details of the edited contact is shown in the status message.

   5. Test case: `/edit-supplier ; name : Supplier1 ; field : { product : kibbles ; price : $75/bag }`<br>
      Expected: The product and price field of contact named "Supplier1" is edited to `kibbles` and `$75/bag` respectively. Details of the edited contact is shown in the status message.

4. Editing a `Maintainer` contact

   1. Prerequisites: The contact to be edited must already exist and should have been added as a `Maintainer` type. You can run the following command to add in a `Maintainer` to edit:<br>
      `/add-maintainer ; name : Maintainer1  ; phone : 98765435 ; address : Poochie Street 24 ; email : ihelppooches@gmail.com ; skill : trainer ; commission : $60/hr`

   2. Test case: `/edit-maintainer ; name : Maintainer1 ; field : { phone : 84444555 }`<br>
      Expected: The phone field of contact named "Maintainer1" is edited to `84444555`. Details of the edited contact is shown in the status message.

   3. Test case: `/edit-maintainer ; name : Maintainer1 ; field : { commission : $10/hr }`<br>
      Expected: The commission field of contact named "Maintainer1" is edited to `$10/hr`. Details of the edited contact is shown in the status message.

   4. Test case: `/edit-maintainer ; name : Maintainer1 ; field : { skill : cleaner }`<br>
      Expected: The skill field of contact named "Maintainer1" is edited to `cleaner`. Details of the edited contact is shown in the status message.

   5. Test case: `/edit-maintainer ; name : Maintainer1 ; field : { commission : $12/hr ; skill : janitor }`<br>
      Expected: The commission and skill field of contact named "Maintainer1" is edited to `$12/hr` and `janitor` respectively. Details of the edited contact is shown in the status message.

<div style="page-break-after: always;"></div>

[//]: # (@@author)

### Searching a contact

1. Searching contacts by name

   1. Prerequisites: The contact list must already have some contacts for testing purposes. You may run the following commands to help in testing:<br>
     `/add-person ; name : Poochie ; phone : 12345678 ; address : Pooch Street 32 ; email : impoochie@gmail.com`<br>
     `/add-person ; name : John Doe ; phone : 88888888 ; address : Pooch Street 32 ; email : imjohndoe@gmail.com`<br>
     `/add-person ; name : John ; phone : 23452345 ; address : Pooch Street 32 ; email : imjohn@gmail.com`

   2. Test case: `/search ; name : John`<br>
     Expected: Displays contacts with the names "John" and "John Doe".

2. Searching contacts by phone number

   1. Prerequisites: The contact list must already have some contacts for testing purposes. You may run the following commands to help in testing:<br>
     `/add-person ; name : Poochie ; phone : 12345678 ; address : Pooch Street 32 ; email : impoochie@gmail.com`<br>
     `/add-person ; name : John Doe ; phone : 8888888 ; address : Pooch Street 32 ; email : imjohndoe@gmail.com`<br>
     `/add-person ; name : John ; phone : 23452345 ; address : Pooch Street 32 ; email : imjohn@gmail.com`<br>

   2. Test case: `/search ; phone : 12345678`<br>
     Expected: Displays only one contact named "Poochie" with the phone number `12345678`.

<div style="page-break-after: always;"></div>

### Deleting a contact

1. Deleting a contact while all contacts are being shown

   1. Prerequisites: Only **one** contact with the name **_Poochie_** should exist in PoochPlanner. If not, run the following command to ensure add **_Poochie_** into PoochPlanner. PoochPlanner does not accept duplicate names so there will not be an instance where there is more than one contact with the name **_Poochie_** that exists in the contacts list.<br>
      `/add-person ; name : Poochie ; phone : 98883888 ; address : Pooch Street 32 ; email : impoochie@gmail.com`

   2. Test case: `/delete ; name : Poochie`<br>
      Expected: Contact named **_Poochie_** is deleted from the list. Contact type and name of the deleted contact is shown in the status message. Timestamp in the status bar is updated.

   3. Test case: `/delete ; name : Moochie`<br>
      Expected: No contact is deleted. Error details shown in the status message. Status bar remains the same.

   4. Test case: `/delete`<br>
      Expected: No contact is deleted. Error details shown in the status message. Status bar remains the same.

   5. Other incorrect delete commands to try: `/delete`, `delete ; name :`<br>
      Expected: Similar to previous.

<div style="page-break-after: always;"></div>

### Rating a contact

1. Rating a contact while all contacts are being shown

   1. Prerequisites: Only **one** contact with the name **_Poochie_** should exist in PoochPlanner. If not, run the following command to ensure add **_Poochie_** into PoochPlanner. PoochPlanner does not accept duplicate names so there will not be an instance where there is more than one contact with the name **_Poochie_** that exists in the contacts list.<br>
     `/add-person ; name : Poochie ; phone : 98883888 ; address : Pooch Street 32 ; email : impoochie@gmail.com`

   2. Test case: `/rate ; name : Poochie ; rating : 5`<br>
     Expected: Contact named **_Poochie_** is updated with a rating of 5. Contact type and name of the rated contact is shown in the status message. Timestamp in the status bar is updated.

   3. Test case: `/rate ; name : Moochie ; rating : 5`<br>
     Expected: No contact is rated. Error details shown in the status message. Status bar remains the same.

   4. Test case: `/rate ; name : Poochie ; rating : 6`<br>
     Expected: No contact is rated. Error details shown in the status message. Status bar remains the same.

<div style="page-break-after: always;"></div>

[//]: # (@@author yleeyilin)

### Pinning a contact
  
1. Pinning a contact while all contacts are being shown
  
   1. Prerequisites: Only **one** contact with the name **_Poochie_** should exist in PoochPlanner. If not, run the following command to ensure add **_Poochie_** into PoochPlanner. PoochPlanner does not accept duplicate names so there will not be an instance where there is more than one contact with the name **_Poochie_** that exists in the contacts list.<br>
      `/add-person ; name : Poochie ; phone : 98883888 ; address : Pooch Street 32 ; email : impoochie@gmail.com`
  
   2. Test case: `/pin ; name : Poochie`<br>
      Expected: Contact named **_Poochie_** is pinned at the top of the contact list.

### Unpinning a contact
  
1. Unpinning a contact while all contacts are being shown
  
   1. Prerequisites: Only **one** contact with the name **_Poochie_** should exist in PoochPlanner. If not, run the following command to ensure add **_Poochie_** into PoochPlanner. PoochPlanner does not accept duplicate names so there will not be an instance where there is more than one contact with the name **_Poochie_** that exists in the contacts list.<br>
      `/add-person ; name : Poochie ; phone : 98883888 ; address : Pooch Street 32 ; email : impoochie@gmail.com`<br>
      `/pin ; name : Poochie`
  
   2. Test case: `/unpin ; name : Poochie`<br>
      Expected: Contact named **_Poochie_** is no longer pinned at the top of the contact list.

<div style="page-break-after: always;"></div>

[//]: # (@@author)

### Sorting contacts list

1. Sorting contacts by name

   1. Prerequisites: The contacts list must have some contacts for testing purposes. You may run the following commands first to help in testing:<br>
     `/add-person ; name : Poochie ; phone : 12345678 ; address : Pooch Street 32 ; email : impoochie@gmail.com`<br>
     `/add-person ; name : John Doe ; phone : 88888888 ; address : Pooch Street 32 ; email : imjohndoe@gmail.com`<br>
     `/add-person ; name : John ; phone : 23452345 ; address : Pooch Street 32 ; email : imjohn@gmail.com`<br>

   2. Test case: `/sort ; field : name`<br>
     Expected: Displays all contacts sorted by name in ascending order.

2. Sorting contacts by phone number

   1. Prerequisites: The contacts list must have some contacts for testing purposes. You may run the following commands first to help in testing:<br>
     `/add-person ; name : Poochie ; phone : 12345678 ; address : Pooch Street 32 ; email : impoochie@gmail.com`<br>
     `/add-person ; name : John Doe ; phone : 88888888 ; address : Pooch Street 32 ; email : imjohndoe@gmail.com`<br>
     `/add-person ; name : John ; phone : 23452345 ; address : Pooch Street 32 ; email : imjohn@gmail.com`

   2. Test case: `/sort ; field : phone`<br>
     Expected: Displays all contacts sorted by phone number in ascending order.

<div style="page-break-after: always;"></div>

[//]: # (@@author jannaleong)

### Adding a note to a contact
  
1. Adding a note (no deadline) to a contact
  
   1. Prerequisites: The contact to add a note to must already exist. This contact can be of `Person`/`Supplier`/`Staff`/`Maintainer` type. You can run the following command to add a contact:<br>
      `/add-person ; name : Poochie ; phone : 98883888 ; address : Pooch Street 32 ; email : impoochie@gmail.com`
        
   2. Test case:<br>`/note ; name : Poochie ; note : get kibble`<br>
      Expected: Adds a note to a contact named **_Poochie_**.

2. Adding a note (with deadline) to a contact
  
   1. Prerequisites: The contact to add a note to must already exist. This contact can be of `Person`/`Supplier`/`Staff`/`Maintainer` type. You can run the following command to add a contact:<br>
      `/add-person ; name : Poochie ; phone : 98883888 ; address : Pooch Street 32 ; email : impoochie@gmail.com`
        
   2. Test case: `/note ; name : Poochie ; note : get kibble ; deadline : 2024-10-10`<br>
      Expected: Adds a note with deadline to a contact named **_Poochie_**.

<div style="page-break-after: always;"></div>

[//]: # (@@author)

[//]: # (@@author chiageng)

### Undoing a command
  
1. Undoing a command that modifies the contacts list
  
   1. Prerequisites: The previous command must have modified the contacts list. You may run the following command first to modify the contact book:<br>
      `/add-person ; name : Poochie ; phone : 98883888 ; address : Pooch Street 32 ; email : impoochie@gmail.com`
        
   2. Test case: `/undo`<br>
      Expected: Reverts the changes in the contacts list to just before executing the `add-person` command.

2. Undoing a command that does not modify the contacts list
   
   1.  Prerequisites: The previous command must not have made any modifications to the contacts list. You may run the following two commands, whereby the second command does not modify the contacts list:<br>
      `/add-person ; name : Poochie ; phone : 98883888 ; address : Pooch Street 32 ; email : impoochie@gmail.com`<br>
      `/search ; name : Poochie`
   
   2. Test case: `/undo`<br>
      Expected: In this case, as no modifications were made directly to the contacts list upon performing the `search` command, the `undo` command reverts back the changes to just before the `add-person` is executed.

[//]: # (@@author)

<div style="page-break-after: always;"></div>

[//]: # (@@author chiageng)

### Redoing a command
1. Redoing an undo command
  
   1. Prerequisites: There must have been at least one undo command executed. You may run the following command before testing:<br>
      `/add-person ; name : Poochie ; phone : 98883888 ; address : Pooch Street 32 ; email : impoochie@gmail.com`<br>
      `/undo`
  
   2. Test case: `/redo`<br>
      Expected: Reverts the changes caused by the `undo` command to just right after `add-person` command is executed.

[//]: # (@@author)

<div style="page-break-after: always;"></div>

[//]: # (@@author jannaleong)

### Viewing reminders
  
1. Viewing a reminder
  
   1. Prerequisites: There must be a contact with a note that has a deadline on or after today's date. You may run the following commands to add such a contact:<br>
      `/add-person ; name : Poochie ; phone : 98883888 ; address : Pooch Street 32 ; email : impoochie@gmail.com`<br>
      `/note ; name : Poochie ; note : get kibble ; deadline : 2024-10-10`
        
   2. Test case: `/remind`<br>
      Expected: Displays the contact named **_Poochie_** with the note deadline after today (note: if there are other contacts in the contacts list with notes that have deadlines on or after today's date, they will also appear).

<div style="page-break-after: always;"></div>

### Viewing help

1. Viewing help

   1. Test case: `/help ; command : delete`<br>
      Expected: Displays help details for the delete command.

<div style="page-break-after: always;"></div>

[//]: # (@@author)

[//]: # (@@author chiageng)

### Appendix : Effort
#### Project Overview
Our project aimed to enhance the functionality of a contact management system, building upon the foundation laid by AB3 (Address Book 3). Key improvements included accommodating multiple types of contacts, refining command formats for user-friendliness, introducing dynamic search and sorting capabilities, implementing note and reminder features, integrating pin and unpin functionalities, and incorporating undo and redo functionalities. These enhancements aimed to provide users with a more intuitive and efficient contact management experience.

#### Difficulty Level and Challenges Faced
The project faced significant challenges due to its complexity and the need to seamlessly integrate new features with the existing AB3 framework. One major challenge was accommodating multiple types of contacts (`Person`, `Staff`, `Maintainer`, `Supplier`) while ensuring compatibility with the original AB3 data model and commands. This required thorough understanding of the project structure and meticulous modification of existing components, particularly the `JsonAdaptedPerson` classes.

Additionally, redesigning command formats and implementing new features such as dynamic search, sorting, note/reminder functionalities, and pin/unpin features demanded careful planning and detailed implementation. Adapting the undo and redo features from AB4 to fit within the AB3 framework posed another challenge, as it necessitated significant modifications to `ModelManager` and command execution flow while ensuring backward compatibility.

#### Effort Required
The effort required for the project was substantial, spanning analysis, design, development, testing, and documentation phases. The multidisciplinary team invested significant time and resources in understanding AB3's architecture, identifying areas for enhancement, and implementing new features while ensuring compatibility and stability. Agile methodologies were employed to iteratively address challenges and incorporate stakeholder feedback, resulting in an efficient developmental process.

#### Achievements
Despite the challenges, the project achieved several milestones that significantly enhanced the contact management system's functionality and user experience. Key achievements included:

1. Successful integration of multiple contact types, providing users with greater flexibility and organizational capabilities.
2. Redesigning command formats for improved intuitiveness and ease of use, enhancing user interaction.
3. Implementation of dynamic search and sorting functionalities, empowering users to efficiently navigate and manage their contacts.
4. Introduction of note and reminder features, enabling users to add context and schedule tasks associated with contacts.
5. Seamless integration of pin and unpin functionalities, allowing users to prioritize contacts.
6. Seamless integration of undo and redo functionalities, allowing users to navigate between different states of their contacts list, improving data integrity.


#### Effort Saved Through Reuse:
Approximately 10% of the project effort was saved through strategic reuse of existing components and libraries. Notably, the redesign of command formats leveraged insights from previous projects and industry best practices, streamlining development and ensuring consistency. Additionally, adapting the undo and redo features from AB4 involved reusing core concepts and methodologies, significantly reducing implementation complexity and effort.

In summary, the project's successful implementation of advanced features within the AB3 framework demonstrates our team's proficiency in software development and problem-solving. Despite the inherent challenges, our strategic approach to reuse and adaptation resulted in a robust and feature-rich contact management system that meets the evolving needs of users.

<div style="page-break-after: always;"></div>

### Acknowledgements
1. PoochPlanner is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org/).
2. The undo and redo features (including the design and UML diagrams) was inspired and reused with minimal changes from [SE-addressbook](https://se-education.org/addressbook-level4/DeveloperGuide.html#undo-redo-feature).

