# JavaFX 3D Editor - Architecture Documentation

## Overview

Tento dokument popisuje architekturní refaktor JavaFX 3D editoru z v1.0 na v2.0.

## Problémy v Původní Architektuře (v1.0)

### 1. God Class Problem
**GUI.java** obsahoval všechnu logiku:
- UI rendering
- 3D transformace
- Event handling
- File I/O (neimplementováno)
- Object management

```
GUI.java (1000+ lines)
├── State (60+ fields)
├── UI initialization
├── Event handlers inline
├── Business logic mixed
└── File operations (stub)
```

### 2. Slabá Separation of Concerns
- View logika = Business logika
- Event handlers přímo v handle() metodě
- Hardcoded hodnoty rozptýleny po kódu
- Nejednotný systém pro ID management

### 3. Missing Features
- Move tlačítko: Deklarováno, ale nefunkční
- Scale tlačítko: Deklarováno, ale nefunkční
- Save/Load: Pouze kostry, žádná implementace

### 4. Error Handling
- Nulové error handling
- Catch blocks jsou prázdné nebo ignorují exception
- Žádné validace parametrů
- Crash bez informativní zprávy

### 5. Thread Safety
```java
private static int nextId = 1;  // Race condition!

public CustomBox(Color color) {
    this.id = nextId;           // Thread-unsafe
    nextId++;
    // ...
}
```

### 6. Event Handler Management
```java
scene.setOnMousePressed(event1 -> { ... });  // Přepsáno vícekrát
scene.setOnMouseDragged(event2 -> { ... });  // Ani se nečistí
```

## Nová Architektura (v2.0)

### Layer-based Design

```
┌─────────────────────────────────────────────────┐
│              Presentation Layer                 │
│ GUIv2.java - UI Components, Event wiring       │
└────────────────┬────────────────────────────────┘
                 │ uses
┌─────────────────────────────────────────────────┐
│             Handler Layer                       │
│ TransformEventHandler - Event routing          │
└────────────────┬────────────────────────────────┘
                 │ uses
┌─────────────────────────────────────────────────┐
│          Service Layer (Business Logic)         │
│ ├─ Scene3DManager                              │
│ ├─ TransformService                            │
│ └─ SceneIOService                              │
└────────────────┬────────────────────────────────┘
                 │ uses
┌─────────────────────────────────────────────────┐
│            Component Layer                      │
│ ├─ CustomBox                                   │
│ ├─ CustomSphere                                │
│ ├─ CustomCylinder                              │
└────────────────┬────────────────────────────────┘
                 │ extends
┌─────────────────────────────────────────────────┐
│          JavaFX Shape3D Classes                 │
└─────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────┐
│          Utility Layer                          │
│ ├─ Logger - Strukturované logování            │
│ ├─ AppConfig - Centralizovaná konfigurace     │
│ └─ ExtensionGroup - Extended Group functionality
└─────────────────────────────────────────────────┘
```

### Key Principles

#### 1. Single Responsibility
- **GUIv2**: Pouze UI rendering
- **TransformService**: Pouze transformace
- **Scene3DManager**: Pouze správa objektů
- **SceneIOService**: Pouze I/O operace

#### 2. Dependency Injection
```java
public class TransformEventHandler {
    private final TransformService transformService;
    private final Scene3DManager scene3DManager;
    
    public TransformEventHandler(
        TransformService transformService,
        Scene3DManager scene3DManager) {
        // Není nutné vytářet - jsou injektovány
        this.transformService = transformService;
        this.scene3DManager = scene3DManager;
    }
}
```

#### 3. Configuration Management
```java
// Místo hardcoded hodnot
// OLD: private static final float WIDTH = 1280;
// NEW: AppConfig.WINDOW_WIDTH

// Všechny konstanty na jednom místě
AppConfig.WINDOW_WIDTH
AppConfig.BUTTON_CUBE_Y
AppConfig.SPHERE_RADIUS
// atd.
```

#### 4. Structured Logging
```java
// OLD: System.out.println("Selected Object: " + selectedShape);
// NEW: Logger.info("Selected shape: " + shape.getClass().getSimpleName());

Logger.debug("Debug info");     // Detaily pro vývoj
Logger.info("Normal flow");     // Běžné operace
Logger.warn("Potential issue"); // Upozornění
Logger.error("Error", ex);      // Chyby s stacktrace
```

## Klíčové Komponenty

### 1. Scene3DManager

**Odpovědnost**: Správa všech 3D objektů a jejich životního cyklu

```java
Scene3DManager manager = new Scene3DManager(sceneGroup);

// Přidání objektů
CustomBox cube = manager.addCube(Color.RED);
CustomSphere sphere = manager.addSphere(Color.BLUE);

// Správa
ObservableList<Shape3D> objects = manager.getObjects();
manager.removeObject(cube);
manager.clearScene();

// Hledání
Shape3D found = manager.findObjectById(5);

// ID management
manager.resetIdCounters(); // Pro nový projekt
```

**Výhody**:
- Centralizovaný přístup k objektům
- ObservableList umožňuje data binding
- Bezpečný life-cycle management

### 2. TransformService

**Odpovědnost**: Efektivní aplikace transformace na vybrané objekty

```java
TransformService service = new TransformService();

// Výběr objektu
service.setSelectedShape(shape);
if (service.hasSelectedShape()) {
    // Transformace
    service.startTransform(mouseX, mouseY);
    service.rotateShape(currentX, currentY);
    service.moveShape(currentX, currentY);
    service.scaleShape(1.1);
    
    // Reset
    service.resetTransforms();
}
```

**Výhody**:
- Oddělení transformační logiky od UI
- Jednoduchoe testování
- Snadné přidání nových transformací

### 3. SceneIOService

**Odpovědnost**: Serializace a deserializace 3D scén

```java
// Uložení
List<Shape3D> objects = manager.getObjects();
SceneIOService.saveScene(objects, "scene.j3d");

// Načtení
List<Shape3D> loaded = SceneIOService.loadScene("scene.j3d");
```

**JSON Formát**:
```json
{
  "version": "1.0",
  "timestamp": 1710777600000,
  "shapes": [
    {
      "type": "BOX",
      "id": 1,
      "color": "#CCCCCC",
      "x": 0.0, "y": 0.0, "z": 0.0,
      "scaleX": 1.0, "scaleY": 1.0, "scaleZ": 1.0,
      "rotation": 0.0,
      "width": 100.0, "height": 100.0, "depth": 100.0
    }
  ]
}
```

**Výhody**:
- Lidský čitelný formát
- Bez závislostí na Java serialization
- Snadný import/export

### 4. TransformEventHandler

**Odpovědnost**: Centralizované spravování event handlerů

Rozděluje event handling do diskrétních metod:

```java
// Mouse events
getRotateMouseDragHandler()
getMoveMouseDragHandler()
getTransformStartHandler()

// Scroll events
getScrollZoomHandler()
getScaleScrollHandler()
```

**Výhody**:
- Event logika oddělena od UI
- Snadné testování
- Snadné debugování

## Refaktoring CustomBox/Sphere/Cylinder

### Problém v v1.0
```java
private static int nextId = 1;  // Race condition
public CustomBox(Color color) {
    this.id = nextId++;          // Thread-unsafe!
}
public void setID(int id) {
    this.id = id;                // Setter pro immutable field?
}
```

### Řešení v v2.0
```java
private final int id;           // Immutable

// Thread-safe ID generation
private static int generateId() {
    synchronized (ID_LOCK) {
        return nextId++;
    }
}

// Zbavit se setID() - užitečné
public int getID() { ... }  // Pouze gettery
```

## Thread Safety Implementace

### ID Generation
```java
private static final Object ID_LOCK = new Object();

private static int generateId() {
    synchronized (ID_LOCK) {
        return nextId++;  // Nyní bezpečné
    }
}
```

### Event Handler Cleanup
```java
// OLD: Handler se přepisuje bez čištění
scene.setOnMousePressed(event1 -> {});
scene.setOnMousePressed(event2 -> {});  // Prvý je pryč

// NEW: Explicitně se čistí
mainScene.setOnMousePressed(null);
mainScene.setOnMouseDragged(null);
mainScene.setOnScroll(null);
mainScene.setOnMousePressed(newHandler);
```

## Dependency Injection Pattern

### Inicializace v GUIv2
```java
private void initializeServices() {
    extension = new ExtensionGroup();
    root = new Group();
    
    scene3DManager = new Scene3DManager(extension);
    transformService = new TransformService();
    eventHandler = new TransformEventHandler(
        transformService,           // Injected
        scene3DManager             // Injected
    );
}
```

### Výhody
- Slabé vazby (loose coupling)
- Snadná testovatelnost
- Snadná záměna implementací

## Configuration Best Practices

### Centralizace
```java
// Před: Hardcoded po celém projektu
x = 20, y = 40, width = 1280...

// Teď: Jednotné místo
AppConfig.BUTTON_X_OFFSET
AppConfig.BUTTON_CUBE_Y
AppConfig.WINDOW_WIDTH
```

### Výhody
- Jednoduchá personalizace
- Konzistence
- Jednoduchý theming

## Error Handling Strategy

### Try-Catch Blok v IO Operacích
```java
try {
    SceneIOService.saveScene(objects, filePath);
    showInfoDialog("Success", "Scene saved");
} catch (IOException e) {
    Logger.error("Save failed", e);
    showErrorDialog("Save Error", e.getMessage());
}
```

### Null Validation
```java
public void setColor(Color color) {
    if (color == null) {
        throw new IllegalArgumentException("Color cannot be null");
    }
    this.color = color;
}
```

## Testing Strategy

### Unit Testing Services
```java
@Test
void testAddCube() {
    Scene3DManager manager = new Scene3DManager(new Group());
    CustomBox cube = manager.addCube(Color.RED);
    
    assertEquals(1, manager.getObjectCount());
    assertNotNull(cube);
}
```

### Event Handler Testing
```java
@Test
void testRotateShape() {
    TransformService service = new TransformService();
    CustomBox box = new CustomBox(Color.RED);
    service.setSelectedShape(box);
    
    service.startTransform(0, 0);
    service.rotateShape(10, 10);
    
    assertNotEquals(0, box.getRotate());
}
```

## Porovnání s v1.0

### Metrika: Cyclomatic Complexity

```
GUI.java (v1.0):
- Class: ~100+
- handle() method: ~50+
- File operations: Error handling absent

GUIv2.java (v2.0):
- Class: ~5
- Each method: ~10-15
- All covered with try-catch
```

### Metrika: Koherence

```
v1.0: Low cohesion (transform + IO + UI = 1 class)
v2.0: High cohesion (each class = 1 responsibility)
```

### Metrika: Coupling

```
v1.0: High coupling (direct dependencies)
v2.0: Low coupling (dependency injection)
```

## Future Improvements

### Undo/Redo
```java
public interface Command {
    void execute();
    void undo();
}

class AddObjectCommand implements Command { }
class TransformCommand implements Command { }

class CommandHistory {
    List<Command> undoStack;
    List<Command> redoStack;
}
```

### Copy/Paste
```java
class ClipboardService {
    void copy(Shape3D shape);
    Shape3D paste();
}
```

### Scene Graph Optimization
```java
class SceneGraphOptimizer {
    void createOctree();
    void culledRendering();
}
```

## Conclusion

Nová architektura v2.0:
- ✅ Lépe testovatelná
- ✅ Lépe udržovatelná
- ✅ Lépe rozšiřovatelná
- ✅ Bezpečnější
- ✅ Robustnější

Investice v čistým kódu se vyplatí dlouhodobě!
