# LazyLib #
[<img src="https://img.shields.io/badge/documentation-stable-blue">](https://lazywizard.github.io/lazylib/ "Documentation") [<img src="https://img.shields.io/github/downloads/LazyWizard/lazylib/total">](https://github.com/lazywizard/lazylib/releases "GitHub all releases") [<img src="https://img.shields.io/github/downloads/LazyWizard/lazylib/latest/total">](https://github.com/lazywizard/lazylib/releases/latest "GitHub release (latest by date)")

LazyLib is not a regular mod. It doesn't add any ships, weapons or factions. In fact, it doesn't change anything at all in your game! All it does is make writing code for mods easier. It contains methods to deal with otherwise complicated tasks like collision detection and formatted sector messages, among many other things.

Some mods may require LazyLib to run. If you are ever running a mod and get an error along the lines of 'Imported class "org.lazywizard.lazylib.<whatever>" could not be loaded', then this mod is required. Just make sure you have the latest version installed and that it is tagged in the launcher and everything should work.


### Installation ###
This is installed the same way as a normal mod. Simply extract the zip into your mods folder and tag it in the launcher. As this mod does nothing until its methods are called, there is no harm in leaving it tagged in the launcher at all times even if your currently active mods don't require it.


### Instruction for modders ###
For modders who wish to use LazyLib's methods, all you need to do is add _mods/LazyLib/jars/LazyLib.jar_ as a dependency in your project, the same way you did for _starsector-core/starsector.api.jar_.

If you use Kotlin, you'll also want to include _mods/LazyLib/jars/LazyLib-Kotlin.jar_, which includes extension methods for the base LazyLib classes as well as bundles the [kotlinx.coroutines ](https://kotlinlang.org/docs/coroutines-guide.html)library.

**Do not include the Kotlin runtime or the kotlinx.coroutines library in your own mod's jar, as they will conflict with the versions included in LazyLib.**


### Why use LazyLib? ###
Say you're writing custom shipsystem AI and need to get all visible enemies within 5000 su of your ship. Without LazyLib you would need to write code that iterates over all ships on the battle map, check their distance from your ship, ensure they aren't covered by the fog of war, filter out allies and hulks, etc. All of this would create massive clutter in your code and make your mod very difficult to maintain.

With LazyLib, the above can be done in one line _(AIUtils.getNearbyEnemies(ship, 5000f))_. This is just one of dozens of methods contained in this library, all designed to make complicated modding tasks as painless as possible.


### Why a utility mod? ###
In the past this mod was a simple jar bundled with other mods. However, once there were multiple versions of LazyLib floating around compatibility issues started to arise. Starsector will use the first version of the jar it finds, even if there is a newer version used by another mod. So if mod A requires a collision detection algorithm that was added in a recent release, it would have a compile error if mod B (containing an old version of this library) was loaded first.

As a utility mod there is only one copy of the jar shared between all mods, and it is up to the player to keep it up-to-date (meaning other mods won't need to release a patch every time there is a new LazyLib version).


### A note to modders ###
Since this is a utility mod, all you need to do is let your users know they need this mod downloaded and tagged in the launcher. Starsector will do the rest. For modders who use an IDE, you will want to add _mods/LazyLib/jars/LazyLib.jar_ as a library in your project (the same as you did for _starsector-core/starfarer.api.jar_).

Features should remain stable now that this library has reached version 1.0. If I absolutely need to remove something, it will remain under the @Deprecated tag until the next major Starsector release comes out (as you would be rewriting portions of your code at that point anyway).

Every method and class in this mod is fully documented (~4,000 lines of documentation as of October 2024). Documentation can be found in javadoc.zip in the main mod folder (open index.html). The source is included in the jar if you wish to see how things were done, or can be found online at https://github.com/LazyWizard/lazylib. Modders have my permission to borrow any code they want from LazyLib.


### Compiling from source ###
If you want to compile LazyLib yourself, the source can be found inside jars/LazyLib.jar (most modern archive programs can open jars, as they are essentially zip files). The [online repository](https://gtihub.com/lazywizard/lazylib) also provides the source, along with an IntelliJ IDEA project that can be used to compile the project.

LazyLib will require starfarer.api.jar, lwjgl.jar, lwjgl_util.jar, json.jar and log4j-1.2.9.jar in the classpath to compile - all of these can be found in Starsector's starsector-core folder. LazyLib-Kotlin will require the Kotlin standard library as well as kotlinx.coroutines. LazyLib-Console, which provides commands used to test various LazyLib features, requires lw_Console.jar from the Console Commands mod - however, you can exclude this artifact when building as it isn't necessary for LazyLib's functionality.


### Contributing to this mod ###
If you wish to contribute to LazyLib, the project repository can be found at https://github.com/LazyWizard/lazylib. All of my projects use Git for revision control. [Pull requests](https://docs.github.com/en/pull-requests) are preferred, but if you want direct write access just PM me (include a link to your GitHub account) and I'll add you to the repository as a contributor.

There are a few things you should keep in mind when contributing:
 * Any class ending with 'Utils' should only contain static methods and shouldn't be instantiatable.
 * Use human-readable method names. If there's a proper name that describes a method's functionality but nobody outside of that field will know it, use a more generic, descriptive name. Many modders are still in their teens, so assume that level of education.
 * Build upon the API, don't replace it - most of what this library does uses the existing API methods, not custom framework.
 * LazyLib is intended to sit in the background and do nothing until a mod needs it. There should be no EveryFrameScripts, SpawnPointPlugins, or EveryFrameCombatPlugins in this mod. The only overhead outside of method calls should be the classes stored in memory, if possible.
