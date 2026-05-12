# Space Mechanic

**Space Mechanic** je jednoduchý 2D herní engine vytvořený jako semestrální projekt v Javě.

Hra se odehrává na poškozené vesmírné stanici. Hráč ovládá technika, který musí projít dvě části stanice, sbírat součástky, vyrábět opravné moduly, odstraňovat překážky a nakonec opravit hlavní systém stanice.

## Cíl hry

Hra má dva levely:

### Level 1 - Engineering Deck

Cílem prvního levelu je vyrobit **Power Module**.

Potřebné součástky:

- Wire
- Battery

Recept:

```text
Wire + Battery -> Power Module
```

Po vytvoření modulu může hráč odemknout zablokovaný panel a aktivovat výtah do druhého levelu.

### Level 2 - Control Deck

Cílem druhého levelu je vyrobit **Engine Core**.

Potřebné součástky:

- Metal Plate
- Fuel Cell

Recept:

```text
Metal Plate + Fuel Cell -> Engine Core
```

Po vytvoření Engine Core hráč odstraní finální překážku a dokončí hru.

## Ovládání

| Klávesa | Akce |
|---|---|
| W | Pohyb nahoru |
| A | Pohyb doleva |
| S | Pohyb dolů |
| D | Pohyb doprava |
| E | Interakce s objektem |
| C | Crafting aktuálního receptu |
| ESC | Pauza / pokračování |

## Herní mechaniky

### Inventář

Hráč má inventář, do kterého se ukládají sebrané předměty a vyrobené moduly.

Inventář se zobrazuje v levém HUD panelu.

Po dokončení hry se obsah inventáře uloží do souboru:

```text
inventory_save.txt
```

### Sběr předmětů

Součástky jsou rozmístěné v mapě jako interaktivní objekty.

Po sebrání zmizí z mapy a přidají se do inventáře.

Příklady předmětů:

- Wire
- Battery
- Metal Plate
- Fuel Cell
- Wrench

### Crafting

Crafting se provádí klávesou `C`.

Každý level má vlastní recept:

```text
Level 1: Wire + Battery -> Power Module
Level 2: Metal Plate + Fuel Cell -> Engine Core
```

Pokud hráč nemá potřebné součástky, hra zobrazí zprávu v HUD panelu.

### Překážky a zamčené dveře

Ve hře existují překážky a zamčené dveře.

Zamčené dveře blokují přístup do důležité části levelu.

Hráč je může odstranit pouze pomocí správného vyrobeného modulu.

Příklad:

```text
Power Module -> odemkne překážku v Levelu 1
Engine Core -> odemkne finální překážku v Levelu 2
```

### Výtah

Výtah slouží k přechodu mezi levely.

V Levelu 1 je výtah uzamčený, dokud hráč nesplní cíl levelu.

Po použití Power Module se výtah aktivuje a hráč může přejít do Levelu 2.

### Terminál

Ve hře je také lodní terminál.

Hráč s ním může interagovat pomocí klávesy `E`.

Terminál zobrazuje nápovědu, která je definovaná v externím level souboru.

## Externí level soubory

Levely jsou načítány z externích textových souborů:

```text
src/main/resources/levels/level1.txt
src/main/resources/levels/level2.txt
```

Každý řádek souboru popisuje jeden objekt v levelu.

Příklad:

```text
SPARE_PART,455,210,24,24,Wire
LOCKED_DOOR,895,205,40,70,Power Module
ELEVATOR,745,250,45,60
TERMINAL,990,190,36,36,Hint: craft Power Module from Wire and Battery.
```

Obecný formát:

```text
TYPE,x,y,width,height,extra
```

Podporované typy objektů:

| Typ | Význam |
|---|---|
| SPARE_PART | Sebratelná součástka |
| TOOL | Sebratelný nástroj |
| WALL | Neviditelná kolizní překážka |
| LOCKED_DOOR | Zamčená překážka |
| ELEVATOR | Přechod mezi levely |
| TERMINAL | Terminál s nápovědou |
| ENGINE | Opravitelný systém |
| GENERATOR | Opravitelný systém |
| DOOR | Opravitelný dveřní systém |

## Struktura projektu

Projekt je rozdělen do několika balíčků:

```text
cz.cvut.fel.pjv.spacemechanic
```

| Balíček | Popis |
|---|---|
| main | Spuštění hry, herní smyčka, stav hry |
| input | Zpracování klávesnice |
| level | Načítání levelů, správa objektů, přepínání levelů |
| model | Herní objekty, hráč, inventář, dveře, výtah, terminál |
| collision | Kolize mezi hráčem a objekty |
| ui | HUD, menu, pause screen, win screen |

## Důležité třídy

### Game

Hlavní třída hry.

Obsahuje aktuální stav hry, `LevelManager`, `InputHandler`, `GamePanel` a `UIManager`.

### GamePanel

Swing panel, který spouští herní smyčku a vykresluje hru.

### LevelManager

Spravuje aktuální level, hráče a seznam objektů.

Načítá levely z externích souborů, řeší interakce, crafting, výtah, překážky a podmínku výhry.

### Player

Reprezentuje hráče.

Obsahuje pohyb, pozici, životy a vykreslení hráče.

### Inventory

Ukládá sebrané předměty a vyrobené moduly.

### Item, SparePart, ToolItem

Třídy pro sebratelné předměty.

### LockedDoor

Překážka, kterou lze odstranit pouze pomocí správného modulu.

### Elevator

Objekt pro přechod mezi levely.

### ShipTerminal

Terminál, který zobrazuje hráči nápovědu.

### UIManager

Vykresluje uživatelské rozhraní, inventář, zprávy, stav oprav a obrazovky hry.

## Jak spustit projekt

Projekt lze spustit v IntelliJ IDEA.

Hlavní třída:

```text
cz.cvut.fel.pjv.spacemechanic.main.Game
```

Projekt používá Java Swing pro grafické rozhraní.

## Implementované funkce

- 2D herní smyčka
- Pohyb hráče
- GUI / HUD
- Inventář
- Sběr předmětů
- Crafting systém
- Zamčené dveře
- Kolizní překážky
- Výtah mezi levely
- Dva levely
- Načítání levelů z externích souborů
- Terminál s nápovědou
- Uložení inventáře do souboru
- Stav výhry
