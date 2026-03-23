# JavaFX 3D Editor - v2.0

Pokročilý 3D editor postavený na JavaFX s podporou tvorby, editace a ukládání 3D scén.

## Nové Funkce v v2.0

### Architekturní Vylepšení
- **Separation of Concerns**: Zcela nová architektura s oddělením View, Service a Handler vrstev
- **Dependency Injection**: Services jsou injektovány místo global state
- **Event Handler Management**: Centralizované spravování event handlerů
- **Configuration Management**: Všechny hardcoded hodnoty v `AppConfig`

### Implementované Funkce
- ✅ **Přidávání objektů**: Kostky, koule a válce
- ✅ **Transformace objektů**:
  - Rotace (myš + drag)
  - Posun (myš + drag)
  - Škálování (mousewheel)
  - Reset transformací
- ✅ **3D Kamera**:
  - Zoom (mousewheel)
  - Automatické centrování scény
- ✅ **Save/Load Funkce**:
  - Ukládání do JSON formátu
  - Zachování všech transformací
  - Přístupný formát pro externích import
- ✅ **Správa Objektů**:
  - Seznam objektů v ListView
  - Výběr objektu kliknutím
  - Mazání scény
  - Reset ID counterů
- ✅ **Error Handling**:
  - Try-catch pro všechny IO operace
  - User-friendly error dialogy
  - Null checks na kritických místech
- ✅ **Logging**:
  - Strukturované logování s úrovněmi
  - Debug, Info, Warn, Error levely
  - Timestamps na všech zprávách

### Bezpečnost a Robustnost
- **Thread-safe ID Management**: Synchronized ID generator
- **Null Validation**: Všechny public metody validují parametry
- **Exception Handling**: Comprehensive error handling
- **Resource Management**: Správné čištění event handlerů

## Project Struktura

```
src/
├── javafx3D/
│   ├── view/              # GUI komponenty
│   ├── controller/        # FXML controllers
│   ├── model/             # Data modely
│   ├── components/        # CustomBox, CustomSphere, CustomCylinder
│   ├── service/           # Business logic
│   │   ├── Scene3DManager.java      # Správa 3D scény
│   │   ├── TransformService.java    # Transformace objektů
│   │   └── SceneIOService.java      # Save/Load funcionalita
│   ├── handler/           # Event handlery
│   ├── utils/             # Utility třídy (Logger, ExtensionGroup)
│   └── config/            # Aplikační konfigurace
├── javafx3DV2/
│   └── view/
│       └── GUIv2.java     # Nová refaktorovaná GUI

css/
└── stylesheet.css

images/
├── cube3D.png
├── sphere3D.png
├── cylinder3D.png
├── move3D.png
├── rotate3D.png
└── scale3D.png
```

## Instalace a Setup

### Požadavky
- Java 17 nebo novější
- JavaFX SDK 22+
- Maven (doporučeno) nebo Gradle/Ant

### Krok 1: Stažení Závislostí

#### JavaFX SDK
1. Stáhni JavaFX SDK z https://gluonhq.com/products/javafx/
2. Rozbal do vhodného místa (např. `C:\javafx-sdk-22`)

#### GSON Library (pro Save/Load)
```bash
# Pokud používáš Maven, přidej do pom.xml:
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>

# Nebo stáhni JAR ručně z: https://github.com/google/gson
```

### Krok 2: Konfigurace v IDE

#### IntelliJ IDEA
1. File → Project Structure → Libraries
2. Přidej novou knihovnu (+ button)
3. Vyber JavaFX SDK
4. Apply changes
5. Přidej GSON JAR do knihoven

#### Eclipse
1. Project → Properties → Java Build Path → Libraries
2. Add External JARs
3. Vyber JavaFX lib folder
4. Přidej gson-2.10.1.jar

#### NetBeans
1. Tools → Libraries
2. New Library - jmenuj "JavaFX"
3. Add JAR/Folder → vyber javafx-sdk/lib
4. Stejně pro GSON

### Krok 3: Spuštění

#### Option A: IDE
- Pravý klik na `javafx3DV2.view.GUIv2` → Run

#### Option B: Command Line
```bash
# S Mavenm
mvn javafx:run

# S Ant
ant run

# Ručně s JVM args
java --module-path /path/to/javafx-sdk/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp ".:gson-2.10.1.jar" \
     javafx3DV2.view.GUIv2
```

## Použití

### Přidávání Objektů
1. Klikni na ikonu Cube, Sphere nebo Cylinder v toolbaru
2. Objekt se vytvoří na scéně a přidá do seznamu

### Transformace
1. **Rotace**: Vyber rotate tool, klikni + drag myší
2. **Posun**: Vyber move tool, klikni + drag myší
3. **Škálování**: Vyber scale tool, scroll kololo myši
4. **Zoom**: Scroll kololo myši (kdykoli)

### Výběr Objektů
- Klikni na objekt v ListView na pravé straně
- Vybraný objekt bude transformován

### Ukládání a Načítání
- **File → Save**: Uloží scénu do .j3d souboru (JSON formát)
- **File → Open**: Načte dříve uloženou scénu
- **Edit → Clear Scene**: Vymaže všechny objekty

## Klíčové Třídy

### Scene3DManager
Spravuje všechny 3D objekty v scéně:
```java
Scene3DManager manager = new Scene3DManager(sceneGroup);
CustomBox cube = manager.addCube(Color.LIGHTGREY);
manager.removeObject(cube);
manager.saveScene(); // Interně volá SceneIOService
```

### TransformService
Zpracovává všechny transformace:
```java
TransformService service = new TransformService();
service.setSelectedShape(selectedObject);
service.rotateShape(mouseX, mouseY);
service.scaleShape(1.1); // Scale faktor
```

### SceneIOService
Saves/loads scény v JSON:
```java
// Uložení
List<Shape3D> objects = manager.getObjects();
SceneIOService.saveScene(objects, "myScene.j3d");

// Načtení
var loadedObjects = SceneIOService.loadScene("myScene.j3d");
```

### Logger
Všechny třídy používají Logger:
```java
Logger.debug("Debug zpráva");
Logger.info("Info zpráva");
Logger.warn("Warningová zpráva");
Logger.error("Chyba", exception);
```

## Porovnání: v1.0 vs v2.0

| Feature | v1.0 | v2.0 |
|---------|------|------|
| Move funkcionalita | ❌ | ✅ |
| Scale funcionalita | ❌ | ✅ |
| Save/Load scén | Incompleted stub | ✅ Plně implementováno |
| Architektura | Monolitická | Service-based |
| Error handling | Žádný | Comprehensive |
| Logging | System.out | Strukturované |
| Thread-safety | Ne | Ano (ID sync) |
| Null checks | Ne | Ano |
| Event cleanup | Ne | Ano |
| Configuration | Hardcoded | AppConfig |

## Ostatní Vylepšení

1. **CustomBox, CustomSphere, CustomCylinder**:
   - Thread-safe ID generation
   - Lepší metody: `setPosition()`, `getPosition()`, `setColor()`
   - Sériování s serialVersionUID
   - ToString() pro debugging

2. **ExtensionGroup**:
   - Zachován, ale nyní léčeji používán

3. **Error Messages**:
   - User-friendly dialogy
   - Detailní logging pro debugging
   - Konzise error reporting

## Plán Budoucího Vývoje

- [ ] Undo/Redo funcionalita
- [ ] Clipboard operace (Copy/Paste objektů)
- [ ] Vlastní textury a materiály
- [ ] Export do 3D formátů (OBJ, STL)
- [ ] Animační engine
- [ ] Collaboration mode (více uživatelů)

## Troubleshooting

### "Module not found: com.google.gson"
- Přidej GSON JAR do library path
- V IDE: Project Structure → Libraries → Add JAR

### "Image not found"
- Zkontroluj `/images` folder v src
- Obrázky by měly být: cube3D.png, sphere3D.png, cylinder3D.png, etc.

## Autoři a Licence

Autor: cepel
Licence: Viz LICENSE soubor

---

**Verze**: 2.0
**Poslední aktualizace**: 2026-03-18
**Stav**: Produkce ready (bez Undo/Redo)
