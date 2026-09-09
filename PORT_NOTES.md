# Create Gears — 1.21.1 NeoForge → 1.20.1 Forge 移植笔记

> 本文记录把本模组从 **NeoForge 1.21.1 / Create 6.0.6** 移植到 **Forge 1.20.1 / Create 0.5.1** 过程中踩过的坑与实际解法。供日后维护或二次移植参考。
> 移植结论：**构建、datagen、服务器启动、客户端进世界、齿轮模型与旋转、半轴视觉均正常。**

---

## 1. 目标版本矩阵

| 项目 | 值 |
|---|---|
| Minecraft | 1.20.1 |
| Forge | 47.1.3 |
| Create | 0.5.1.j-55（`com.simibubi.create:create-1.20.1`） |
| Flywheel | 0.6.11-13（`com.jozufozu.flywheel:flywheel-forge-1.20.1`） |
| Registrate | MC1.20-1.3.3（`com.tterrag.registrate:Registrate`） |
| Java | 17 |
| Mappings | official 1.20.1 |
| Ponder | **不引**（0.5.1 内置于 Create；源码已明确不用 Ponder 教程） |

`gradle.properties` 中对应：
```properties
minecraft_version = 1.20.1
forge_version = 47.1.3
create_version = 0.5.1.j-55
registrate_version = MC1.20-1.3.3
flywheel_version = 0.6.11-13
java_version = 17
mappings_channel = official
mappings_version = 1.20.1
```

---

## 2. 改动文件清单

### 构建层
- `gradle.properties` — 版本矩阵（见上）
- `settings.gradle` — pluginManagement 加 `maven.minecraftforge.net`
- `build.gradle` — NeoForge `moddev` 插件 → ForgeGradle `[6.0,6.2)`；`minecraft{}` + runs；依赖重写；`processResources` 替换 `META-INF/mods.toml`
- `src/main/resources/META-INF/mods.toml` — 由 `neoforge.mods.toml` 改名并改为 Forge 格式

### 源码
- `Gears.java` — `@Mod` 构造器、DeferredRegister/RegistryObject、resourceLocation、删 `defaultCreativeTab`、加 `GearsStressProvider.register()` 调用
- `regitration/GearsTiles.java` — `.visual(...)` → `.instance(SingleRotatingInstance::new, false)`；删 flywheel visual import
- `regitration/GearsBlocks.java` — model generator import 改 Forge 包；`modLoc` 改 `ResourceLocation` 构造；删 4 处 `.transform(GearsStressProvider.fixed(...))`
- `regitration/GearsStressProvider.java` — 改为 `BlockStressValues.registerProvider(modid, IStressValueProvider)`
- `blocks/GearBlock.java` — `VoxelShaper` 改 `create.foundation.utility` 包；`modLoc` 构造
- `blocks/HalfShaftGearBlock.java` — `VoxelShaper` 改包
- `util/GenericUtils.java`、`util/ShapeUtils.java` — `ResourceLocation`/`AllShapes` 适配
- `tiles/GearTile.java` — 不变（同包同构造器）

### 删除
- `tiles/HalfShaftGearVisual.java` — 0.5.1 无 Flywheel 1.x 体系，改为模型+blockstate 还原半轴

### 资源（黑块修复）
- `assets/creategears/models/block/half_shaft_gear.json`
- `assets/creategears/models/block/large_gear.json`
- `assets/creategears/models/block/large_half_shaft_gear.json`

---

## 3. 关键坑点（按调试顺序）

### 3.1 构建环境（Windows）
- **JDK 路径**：`D:\Minecraft\Java\17.0.7-windows-x64`（父目录 `D:\Minecraft\Java\` 是仓库，**不是** JDK 根）。`java -version` 能跑但 gradlew 报 `invalid JAVA_HOME`，因 PATH 指到了正确子目录而 JAVA_HOME 指向错误的父目录。
- **GRADLE_USER_HOME 只读**：系统 `GRADLE_USER_HOME=E:\.gradle`，但 `E:` 盘根对当前用户只读，wrapper 无法建锁文件。解决：运行 gradle 时覆盖为项目内 `.gradle`。
  ```powershell
  $env:JAVA_HOME = "D:\Minecraft\Java\17.0.7-windows-x64"
  $env:GRADLE_USER_HOME = "E:\Resources\Create-Gears\.gradle"
  ```
- **网络**：PowerShell/curl 用 Windows Schannel 报 `SEC_E_NO_CREDENTIALS`（TLS 层拿不到凭证），但 **Gradle/JVM 用自己独立的 TLS 栈，正常**。所以 maven 下载没问题，别被 PowerShell 的报错骗了。

### 3.2 依赖坐标与传递（最重要）
- **Create 1.20.1 不在 `maven.createmod.net`**：该仓库 `create-1.20.1` 目录**只有 Create 6.x**，没有 0.5.x。正确来源是 **`modmaven.dev`** → `https://modmaven.dev`。
- **Create 的 pom 是聚合 pom（`<packaging>pom</packaging>`）**：真正的 mod jar 是 classifier `slim`（`create-1.20.1-0.5.1.j-55-slim.jar`），所以坐标**必须带 `:slim`**。
- **`transitive = false` 必须在 `fg.deobf(...)` 括号内**，而不是 `implementation(...) { exclude group: }`：
  - Create 聚合 pom 把 `registrate/flywheel/jei/curios/cc.tweaked` 列为 runtime 依赖。
  - ForgeGradle 6 的 deobf 会**逐个去映射这些传递依赖**，`exclude group:` 对 `__obfuscated` 配置**不生效**，于是报 `Could not find cc.tweaked...` 和 `_mapped_official_1.20.1 找不到`。
  - `transitive = false` 才真正掐断解析。
- 正确依赖块（已写入 build.gradle）：
  ```groovy
  implementation(fg.deobf("com.simibubi.create:create-1.20.1:0.5.1.j-55:slim") { transitive = false })
  implementation fg.deobf("com.tterrag.registrate:Registrate:MC1.20-1.3.3")
  compileOnly fg.deobf("com.jozufozu.flywheel:flywheel-forge-1.20.1:0.6.11-13")
  runtimeOnly fg.deobf("com.jozufozu.flywheel:flywheel-forge-1.20.1:0.6.11-13")
  ```
- Registrate/Flywheel 因 `transitive=false` 被掐断，但源码直接 `import com.tterrag.registrate.*`，所以必须显式声明 Registrate（compile 需要）。

### 3.3 mods.toml（NeoForge → Forge）
- 文件从 `META-INF/neoforge.mods.toml` 改名 `META-INF/mods.toml`。
- 依赖字段：NeoForge 用 `type = "required"`，**Forge 1.20.1 用 `mandatory = true`**。用 `type` 会报 `Missing required field mandatory in dependency`。
- `loaderVersion` 保持 `[1,)`（不要写成从 Forge 版本开始）。
- 各依赖：`forge`(非 neoforge)、minecraft `[1.20.1,1.21)`、create `[0.5.1,)`、flywheel `[0.6.0,0.7)`。

### 3.4 @Mod 构造器（NeoForge → Forge）
- NeoForge 1.21.1 支持 `public Gears(IEventBus modBus, ModContainer container)` 构造注入；**Forge 1.20.1 不支持**，会报 `NoSuchMethodException: Gears.<init>()`。
- Forge 1.20.1 用**无参构造器** + `FMLJavaModLoadingContext.get().getModEventBus()` 拿总线：
  ```java
  public Gears() {
      IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
      TAB_REGISTER.register(modBus);
      REGISTRATE.registerEventListeners(modBus);
      ...
  }
  ```

### 3.5 runData 崩溃的 mixin refmap
- datagen 加载 Create 时崩 `Mixin apply failed create.mixins.json: EntityMixin ... could not find any targets`。
- 在每个 run（client/server/data）配置加：
  ```groovy
  property 'mixin.env.remapRefMap', 'true'
  property 'mixin.env.refMapRemappingFile', "${projectDir}/build/createSrgToMcp/output.srg"
  ```

### 3.6 渲染黑块（核心视觉 bug）
- **现象**：功能正常（能转、能传动），但齿轮部分面（齿的侧面/内部）显示黑色色块。
- **根因**：`half_shaft_gear.json`/`large_gear.json`/`large_half_shaft_gear.json` 是从 1.21.1 带过来的，齿轮元素 UV（如 `[7,7.5,16,9]`）按 1.21.1 的 `cogwheel.png`/`large_cogwheel.png` 布局设计，而 **Create 0.5.1 的这两张纹理布局不同**（官方模型 UV 是 `[7,8,16,9.5]`、`[0,13,15,14.5]`），UV 采样到纹理空白/黑色区域 → 黑块。
- **不是换 instance 类能解决**：`SingleRotatingInstance.getModel()` = `getRotatingMaterial().getModel(blockState)`，是从 blockstate 的 BakedModel 读的，instance 类（SingleRotatingInstance vs BracketedKineticBlockEntityInstance）对小齿轮 `getModel()` 完全相同。
- **修复**：用 Create 0.5.1 官方 `cogwheel_shaftless.json`/`large_cogwheel_shaftless.json` 的齿轮元素（UV 正确），再拼上自己的半轴（Axis）元素，重写 3 个模型文件。`gear.json` 用 parent `cogwheel_shaftless` 本身就是官方模型，未出问题。
- 半轴（Axis）元素需要纹理 `#0`(`create:block/axis`)、`#3`(`create:block/axis_top`)——这俩在 0.5.1 存在。
- 注意：子代理结论指出 `SHAFTLESS_COGWHEEL`/`SHAFTLESS_LARGE_COGWHEEL` 是 Flywheel `PartialModel`（Crafter/FlapDisplay 用），**不是**给普通 cogwheel 渲染用的，别直接套。

### 3.7 其他
- 模型 generator 包名：`net.neoforged.neoforge.client.model.generators.*` → `net.minecraftforge.client.model.generators.*`。
- Catnip（`net.createmod.catnip.*`）在 0.5.1 **不存在**：`VoxelShaper`/`Iterate` 改 `com.simibubi.create.foundation.utility.*`。
- `ResourceLocation.fromNamespaceAndPath`（1.21）→ `new ResourceLocation(ns, path)`（1.20.1）。

---

## 4. 常用构建命令

```powershell
# 先把环境和 gradle home 设好（见 3.1）
$env:JAVA_HOME = "D:\Minecraft\Java\17.0.7-windows-x64"
$env:GRADLE_USER_HOME = "E:\Resources\Create-Gears\.gradle"

.\gradlew.bat build          # 编译 + 打包 + reobf
.\gradlew.bat runData        # datagen（重新生成 recipe/loot/advancement/blockstate/lang）
.\gradlew.bat runServer      # 无头服务器（首次需同意 run/eula.txt 的 eula=true）
.\gradlew.bat runClient      # 客户端窗口
```

---

## 5. 需要你手动做的事

- **EULA**：`run/eula.txt` 首次启动需把 `eula=false` 改为 `eula=true`（服务器才会继续加载）。
- **游戏内验证**：客户端放方块、接动力源看旋转、看半轴视觉；服务器端测试逻辑无需额外操作。

---

## 6. 遗留（可选清理）

- `src/main/resources/assets/creategears/templates/models/block/` — datagen 模板占位符（`$$NAMESPACE$$`/`$$PATH$$`），非必需，删与否不影响。
- `assets/creategears/textures/block/simple_gearshift.png` — 疑似旧资产，未使用，可清。
- 这两类不影响功能，留作以后判断。
