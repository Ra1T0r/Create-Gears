# Create Gears

**Shaftless gears for [Create](https://github.com/Creators-of-Create/Create)!**

A port of *Create: Gears n' Kinetics* — adds shaftless gear blocks to Create: cogwheels without the shaft, available for both the small and large cogwheel sizes, plus half-shaft variants.

---

## Versions

This repository hosts **two parallel versions**, kept on separate branches so each can be developed and released independently:

| MC / Loader | Create | Branch | Status |
|---|---|---|---|
| Minecraft **1.21.1** / NeoForge | Create **6.0.6** | [`forge-1.21.1`](https://github.com/Ra1T0r/Create-Gears/tree/forge-1.21.1) (\`master\`) | Origin |
| Minecraft **1.20.1** / Forge | Create **0.5.1.j** | [`forge-1.20.1`](https://github.com/Ra1T0r/Create-Gears/tree/forge-1.20.1) | Ported |

> Both branches share the same mod version (`2.0.0`); tags are distinguished by the MC version, e.g. `v2.0.0-mc1.21.1` / `v2.0.0-mc1.20.1`.

### Switching versions

```bash
git checkout forge-1.21.1    # MC 1.21.1 / NeoForge / Create 6
git checkout forge-1.20.1    # MC 1.20.1 / Forge / Create 0.5.1
```

### Dependencies

| Version | Forge/NeoForge | Create | Flywheel | Registrate | Java |
|---|---|---|---|---|---|
| 1.21.1 (NeoForge) | 21.1.201 | 6.0.6-98 | 1.0.4 | MC1.21-1.3.0+62 | 21 |
| 1.20.1 (Forge) | 47.1.3 | 0.5.1.j-55 | 0.6.11-13 | MC1.20-1.3.3 | 17 |

---

## Blocks

- **Gear** — shaftless cogwheel (small)
- **Large Gear** — shaftless large cogwheel
- **Half Shaft Gear** — small cogwheel with a single half-shaft
- **Large Half Shaft Gear** — large cogwheel with a single half-shaft

All are pure relays: they transmit rotation but produce no stress (0 impact / 0 capacity). Gears have **no bracket behaviour** (shaftless), unlike Create's stock cogwheels.

---

## Building

Requirements: the JDK listed in the table, plus [Git](https://git-scm.com/) is bundled via the Gradle wrapper (no global Gradle install needed).

```bash
# MC 1.20.1 / Forge build
git checkout forge-1.20.1
gradlew build                    # builds the jar (uses wrapper)

# MC 1.21.1 / NeoForge build
git checkout forge-1.21.1
gradlew build
```

The built jar lands in `build/libs/` (e.g. `creategears-2.0.0.jar`).

---

## Development / Porting Notes

See [`PORT_NOTES.md`](PORT_NOTES.md) for a detailed log of porting this mod from **NeoForge 1.21.1 / Create 6** to **Forge 1.20.1 / Create 0.5.1** — including the build-env gotchas, dependency coordinates (`modmaven.dev`, `:slim` + `transitive=false`), `@Mod` no-arg constructor, mixin refmap, and the rendering "black patch" UV fix.

---

## License

[MIT](LICENSE)
