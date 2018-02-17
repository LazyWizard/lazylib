# LazyLib #
LazyLib is not a regular mod. It doesn't add any ships, weapons or factions. In fact, it doesn't change anything at all in your game! All it does is make writing code for mods easier. It contains methods to deal with otherwise complicated tasks like collision detection and formatted sector messages, among many other things.

Some mods may require LazyLib to run. If you are ever running a mod and get an error along the lines of 'Imported class "org.lazywizard.lazylib.<whatever>" could not be loaded', then this mod is required. Just make sure you have the latest version installed and that it is tagged in the launcher and everything should work.


### Installation ###
This is installed the same way as a normal mod. Simply extract the zip into your mods folder and tag it in the launcher. As this mod does nothing until its classes are called, there is no harm in leaving it tagged in the launcher at all times even if your currently active mods don't require it.


### Why use LazyLib? ###
Say you're writing custom shipsystem AI and need to get all visible enemies within 5000 su of your ship. Without LazyLib you would need to write code that iterates over all ships on the battle map, check their distance from your ship, ensure they aren't covered by the fog of war, filter out allies and hulks, etc. All of this would create massive clutter in your code and make your mod very difficult to maintain.

With LazyLib, the above can be done in one line _(AIUtils.getNearbyEnemies(ship, 5000f))_. This is just one of dozens of methods contained in this library, all designed to make complicated modding tasks as painless as possible.


### Why a utility mod? ###
In the past this mod was a simple jar bundled with other mods. However, once there were multiple versions of LazyLib floating around compatibility issues started to arise. Starsector will use the first version of the jar it finds, even if there is a newer version used by another mod. So if mod A requires a collision detection algorithm that was added in a recent release, it would have a compile error if mod B (containing an old version of this library) was loaded first.

As a utility mod there is only one copy of the jar shared between all mods, and it is up to the player to keep it up-to-date (meaning other mods won't need to release a patch every time there is a new LazyLib version).


### A note to modders ###
Since this is a utility mod, all you need to do is let your users know they need this mod downloaded and tagged in the launcher. Starsector will do the rest. For modders who use an IDE, you will want to add mods/LazyLib/jars/LazyLib.jar as a library in your project (the same as you did for starfarer.api.jar).

If your mod used the old, bundled version of this library, you should remove LazyLib.jar from your mod folder and mod_info.json, as well as delete data/scripts/plugins/LazyLibPlugin.java if you have it.

Features should remain stable now that this library has reached version 1.0. If I absolutely need to remove something, it will remain under the @Deprecated tag until the next major Starsector release comes out (as you would be rewriting portions of your code at that point anyway).

Every method and class in this mod is fully documented (~2,000 lines of documentation as of December 2013). Documentation can be found in javadoc.zip in the main mod folder (open index.html). The source is included in the jar if you wish to see how things were done. Modders have my permission to borrow any code they want from LazyLib.


### Contributing to this mod ###
If you wish to contribute to LazyLib, feel free. All of my projects use Mercurial for revision control. [Pull requests](https://confluence.atlassian.com/display/BITBUCKET/Fork+a+Repo,+Compare+Code,+and+Create+a+Pull+Request) are preferred, but if you want direct write access just PM me (include a link to your Bitbucket account) and I'll add you to the repository.

There are a few things you should keep in mind when contributing:
 * Any class ending with 'Utils' should only contain static methods and shouldn't be instantiatable.
 * Use human-readable method names. If there's a proper name that describes a method's functionality but nobody outside of that field will know it, use a more generic, descriptive name. Many modders are still in their teens, so assume that level of education.
 * Build upon the API, don't replace it - most of what this library does uses the existing API methods, not custom framework.
 * LazyLib is intended to sit in the background and do nothing until a mod needs it. There should be no EveryFrameScripts, SpawnPointPlugins, or EveryFrameCombatPlugins in this mod. The only overhead outside of method calls should be the classes stored in memory, if possible.
