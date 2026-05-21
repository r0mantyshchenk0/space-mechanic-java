# Space Mechanic

**Space Mechanic** is a simple 2D game created as a semestral project in Java.

The game takes place on a damaged spaceship. The player controls a mechanic who moves through two parts of the ship, collects items, crafts repair modules, unlocks obstacles and repairs important ship systems.

## Game goal

The game contains two levels.

### Level 1 - Engineering Deck

The goal of the first level is to create the **Power Module**.

```text
Wire + Battery -> Power Module
```

After creating the module, the player can unlock the obstacle and activate the elevator to the second level.

### Level 2 - Control Deck

The goal of the second level is to create the **Engine Core**.

```text
Metal Plate + Fuel Cell -> Engine Core
```

After creating the Engine Core, the player can remove the final obstacle and finish the game.

## Controls

| Key | Action |
|---|---|
| W, A, S, D | Move player |
| E | Interact with object |
| I | Show / hide inventory |
| C | Show / hide crafting menu |
| ENTER | Craft item in crafting menu |
| ESC | Pause / continue |
| F5 | Save game state |
| F9 | Load game state |

## Game mechanics

The game contains the main mechanics needed to complete both levels:

- player movement
- collisions with walls and locked doors
- item collection
- inventory
- crafting
- chests with items
- terminal with hints
- repairable objects
- elevator between levels
- HUD with player information
- pause and win screens
- saving and loading game state
- basic logging of important events

The inventory is empty when starting a new game, so the player has to complete the game flow from the beginning.

## Saving and loading

The game can save and load the basic game state.

Saving is done with:

```text
F5
```

Loading is done with:

```text
F9
```

The save file is called:

```text
savegame.txt
```

The saved state contains:

- current level
- player position
- inventory items
- repaired objects

Saving and loading are handled by the `SaveManager` class.

## External level files

Levels are loaded from external text files:

```text
src/main/resources/levels/level1.txt
src/main/resources/levels/level2.txt
```

Each line describes one object in this format:

```text
TYPE,x,y,width,height,extra
```

Example:

```text
CHEST,500,300,40,40,Wire
LOCKED_DOOR,895,205,40,70,Power Module
ELEVATOR,745,250,45,60
```

Supported object types:

| Type | Meaning |
|---|---|
| SPARE_PART | Collectible spare part |
| TOOL | Collectible tool |
| CHEST | Chest with an item |
| WALL | Collision obstacle |
| LOCKED_DOOR | Locked obstacle |
| ELEVATOR | Transition between levels |
| TERMINAL | Terminal with a hint |
| ENGINE | Repairable system |
| GENERATOR | Repairable system |
| DOOR | Repairable door system |

## Project structure

The project is divided into several packages:

```text
cz.cvut.fel.pjv.spacemechanic
```

| Package | Description |
|---|---|
| main | Game startup, game loop and game state |
| input | Keyboard input handling |
| level | Level loading and game logic management |
| model | Game objects, player, inventory and items |
| collision | Collision detection |
| ui | HUD, inventory, crafting menu and game screens |
| save | Saving and loading game state |

## Technical documentation

The project is written in Java 21 and uses Maven.

The graphical part is implemented with Swing / Java AWT.

Main classes:

- `Game` initializes the main parts of the game and stores the current game state.
- `GamePanel` contains the game loop and regular rendering.
- `InputHandler` handles keyboard input.
- `LevelManager` manages levels, objects, interactions, crafting, repairs and the win condition.
- `UIManager` renders the HUD, inventory, crafting menu, pause screen and win screen.
- `Inventory` stores items collected by the player.
- `CollisionManager` checks collisions between the player and objects.
- `SaveManager` saves and loads the basic game state.

The base class for most objects in the game is `GameObject`. Specific objects such as `Chest`, `Wall`, `Elevator`, `LockedDoor`, `Item` and `RepairableObject` are based on it.

`RepairableObject` is a common base class for objects that can be repaired, for example `Engine`, `Generator` and `DoorSystem`.

## Threads

The game loop runs in a separate thread inside `GamePanel`.

The loop regularly updates the game logic and repaints the game panel.

## Logging

Logging is implemented using:

```java
java.util.logging.Logger
```

The game logs important events such as:

- game initialization
- game state changes
- saving game state
- loading game state

Logging can be disabled with the launch parameter:

```text
-Dlogging=false
```

## JavaDoc

The source code contains JavaDoc comments for main classes and important methods.

JavaDoc comments are mainly added to parts that contain the basic logic of the project:

- main game classes
- level management
- keyboard input
- user interface
- inventory
- collisions
- repairable objects
- saving and loading
- tests

HTML documentation was not generated as part of the submission, because it was not required. It can still be generated using Maven:

```bash
mvn javadoc:javadoc
```

## Tests

The project contains unit tests using JUnit 5.

The tests check:

- inventory add/remove logic
- item collection
- tool collection
- object repair
- level loading
- crafting in the first level
- crafting in the second level
- crafting without required items
- initial level manager state

Run tests with:

```bash
mvn test
```

The graphical part is tested manually, because rendering and keyboard interaction are not suitable for simple unit tests.

## Network communication

The project does not use network communication. The game runs locally as a desktop application and does not contain a client-server architecture.

Because of that, no communication protocol is needed.

## Running the project

The project is a Maven project.

First, open the folder that contains `pom.xml`.

Run the game:

```bash
mvn exec:java
```

Run tests:

```bash
mvn test
```

The project can also be started directly in IntelliJ IDEA by running the main class:

```text
cz.cvut.fel.pjv.spacemechanic.main.Game
```

## Project status

The game is functional and contains a complete basic playthrough.

The player can complete the first level, create the Power Module, unlock the way to the elevator, move to the second level, create the Engine Core and finish the game.

## Author

Roman Tyshchenko  
CTU FEE