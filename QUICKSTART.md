# Quick Start Guide - JavaFX 3D Editor v2.0

## 1. První spuštění

### Požadavky
- Java 17+
- JavaFX SDK 22+
- GSON 2.10.1+

### Setup (30 sekund)
1. Stáhni JavaFX: https://gluonhq.com/products/javafx/
2. Stáhni GSON: https://github.com/google/gson/releases
3. Přidej do IDE (viz README.md)
4. Spusť `javafx3DV2.view.GUIv2`

## 2. Základní Operace

### Přidání Objektu
```
1. Klikni na 🔷 (Cube), ⚪ (Sphere) nebo 🔸 (Cylinder)
2. Objekt se automaticky přidá do scény
3. Vidíš ho v seznamu vpravo
```

### Transformace Objektu

#### Rotace
```
1. Vyber Rotate tool (otočené kolo)
2. KLIKNI a TÁHNI myší nad objektem
3. Pohyb myši = rotace objektu
```

#### Posun
```
1. Vyber Move tool (šipka)
2. KLIKNI a TÁHNI myší nad objektem
3. Pohyb myši = posun objektu
```

#### Škálování
```
1. Vyber Scale tool (zvětšovací sklo)
2. Otáčej SCROLL KOLEČKO
3. Kolečko nahoru = zvětšení
4. Kolečko dolů = zmenšení
```

#### Zoom (vždycky)
```
- Kdykoli: SCROLL KOLEČKO = přiblížení/oddálení kámery
```

### Výběr Objektu
```
- Klikni na položku v seznamu vpravo
- Při transformaci se změní vybraný objekt
```

### Smazání/Reset
```
- Edit → Clear Scene = vymazat vše
- Edit → Reset Transform = resetovat transformace
```

### Uložení Scény
```
- File → Save → vyber lokaci + jméno
- Soubor se uloží jako .j3d
```

### Načtení Scény
```
- File → Open → vyber .j3d soubor
- Scéna se obnoví se všemi objekty a transformacemi
```

## 3. Praktické Příklady

### Příklad 1: Vytvoření Jednoduché Scény
```
1. Přidej 3 kostky:    [Add Cube] → [Add Cube] → [Add Cube]
2. Vyber 1. kostku:    Klikni v seznamu na "Cube 1"
3. Rotuj:              Vyber Rotate → drag myší
4. Posuň:              Vyber Move → drag myší
5. Opakuj pro ostatní
6. Ulož:               File → Save
```

### Příklad 2: Vytvoření Pyramidy
```
1. Přidej kouli:       [Add Sphere]
2. Přidej 4 kostky:    [Add Cube] × 4
3. Umisti kostky       Move tool → drag myší
4. Ulož:               File → Save
```

### Příklad 3: Načtení a Úprava
```
1. Otevři soubor:      File → Open → vyber .j3d
2. Vyber objekt:       Klikni v seznamu
3. Uprav:              Rotate/Move/Scale
4. Ulož:               File → Save
```

## 4. Tipy a Triky

### Tip 1: Přesná Rotace
- Pomalý pohyb myší = přesnější rotace
- Rychlý pohyb = větší rotace

### Tip 2: Středování Scény
- Objekty se vytvoří uprostřed scény
- Můžeš je poposouvat Move toolem

### Tip 3: Zoom
- SCROLL kolečko = zoom kámery (vždycky)
- Scale tool + SCROLL = zvětšenie objektu

### Tip 4: Backups Before Saving
- Ulož nový soubor pod jiným jménem
- File → Save →NewName.j3d

### Tip 5: Debugging
- Konzole ukazuje DEBUG zprávy
- Logger slouží k troubleshootingu

## 5. Klávesnice Zkratky

V2.0 není klávesnica zkratek implementovaná.
Plánováno pro v2.1:
- Ctrl+S = Save
- Ctrl+O = Open
- Ctrl+Z = Undo
- Ctrl+Y = Redo

## 6. Oftý Problémy

### Problém: Model se nezobrazí
```
Řešení:
- Zkontroluj že někde není mimo viditelný prostor
- Move tool → drag model do středu
- Zkusit SCROLL kolečko pro zoom
```

### Problém: Transformace se nemění
```
Řešení:
- Zkontrolej výběr v seznamu (vpravo)
- Klikni na zvolený model
- Zkusit jiný tool (Move/Rotate/Scale)
```

### Problém: obrazy chybí
```
Řešení:
- Zkontroluj /images folder má soubory:
  cube3D.png, sphere3D.png, cylinder3D.png,
  move3D.png, rotate3D.png, scale3D.png
- Přidej ikony ručně nebo text se zobrazí místo nich
```

### Problém: Soubor se neuloží
```
Řešení:
- Zkontrolej že máš právo zápisu v adresáři
- Zkusit jiný adresář
- Exception bude vidět v konzoli
```

## 7. Upgrade z v1.0

Pokud máš starou v1.0:

### Co se Změnilo
- GUIv2.java = nová třída (stará GUI.java zůstane pro kompatibilitu)
- Move a Scale JSOU nyní implementovány
- Save/Load FUNGUJE (JSON formát)
- Lepší error handling

### Co je Kompatibilní
- Obrázky stejné (cube3D.png, atd.)
- CSS stylesheet stejný
- CustomBox/Sphere/Cylinder API kompatibilní

### Co NENÍ Kompatibilní
- Staré .ser soubory (Java serialization)
- Spusť GUIv2 místo GUI
- Změny v ID generování (nyní thread-safe)

### Migrace Dat
```
// Staré objekty nejdou přímo použít
// Musíš je v GUIv2 znovu vytvořit
// Ale save/load bude pracovat s novým formátem
```

## 8. Další Zdroje

- README.md - Detailní dokumentace
- ARCHITECTURE.md - Technické detaily
- Kód - Studuj javafx3DV2.view.GUIv2

## 9. Kontakt & Podpora

### Debug Mode
- Logger.debug() vidíš v konzoli
- AppConfig.DEBUG_MODE = true pro víc detailů

### Error Messages
- Chyby se zobrazují v dialogech
- Exception stacktrace jsou v konzoli

### Příště (v2.1)
- Undo/Redo
- Klávesniče zkratky
- Vlastní textury
- Export do OBJ/STL
