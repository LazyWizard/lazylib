Mod thread: http://fractalsoftworks.com/forum/index.php?topic=5444.0

 Introduction:
---------------
LazyLib is not a regular mod. It doesn't add any ships, weapons or factions.
In fact, it doesn't change anything at all in your game! All it does is make
writing code for mods easier. It contains classes to deal with otherwise
complicated tasks like collision detection and formatted sector messages,
among many other things.

Some mods may require LazyLib to run. If you are ever running a mod and get
an error about not finding a class in 'org.lazywizard.lazylib.<whatever>',
then this utility mod is required. Just make sure you have the latest version
installed and that it is tagged in the launcher and everything should work.

All classes that end in "Utils" are utility classes and cannot be instantiated.


 Installation:
---------------
This is installed the same way as a normal mod. Simply extract the zip into
your mods folder and tag it in the launcher. As this mod does nothing until
its methods are called, there is no harm in leaving it tagged in the launcher
at all times even if your currently active mods don't require it.


 Using this library as a modder:
---------------------
For modders who wish to use LazyLib's methods, all you need to do is add
_mods/LazyLib/jars/LazyLib.jar_ as a dependency in your project.

If you use Kotlin, you'll also want to include
_mods/LazyLib/jars/LazyLib-Kotlin.jar_, which includes extension methods for
the base LazyLib classes as well as bundles the kotlinx.coroutines library.

**Do not include the Kotlin runtime or the kotlinx.coroutines library in your
mod's jar, as they will conflict with the versions included with LazyLib.**


 Compiling from source:
------------------------
If you want to compile LazyLib yourself, the source can be found inside
jars/LazyLib.jar (most modern archive programs can open jars). The repo
provides an IntelliJ IDEA project that can be used to compile the project.

LazyLib will require starfarer.api.jar, lwjgl.jar, lwjgl_util.jar, json.jar
and log4j-1.2.9.jar in the classpath to compile. LazyLib-Kotlin will require
the Kotlin standard library as well as kotlinx.coroutines.


 Contributing to this mod:
---------------------------
If you wish to contribute to LazyLib, the project repository can be found at
https://bitbucket.org/LazyWizard/lazylib. All of my projects use Mercurial for
revision control. Pull requests are preferred, but if you want direct write
access just PM me (include a link to your Bitbucket account) and I'll add you
to the repository.

There are a few goals you should keep in mind when contributing:
 * Any class ending with 'Utils' should only contain static methods and
   shouldn't be instantiatable.
 * Use human-readable method names. If there's a proper name that describes a
   method's functionality but nobody outside of that field will know it, use a
   more generic, descriptive name. Many modders are still in their teens, so
   assume that level of education.
 * Build upon the API, don't replace it - most of what this library does uses
   the existing API methods, not custom framework.
 * LazyLib is intended to sit in the background and do nothing until a mod
   needs it. There should be no EveryFrameScripts, SpawnPointPlugins, or
   EveryFrameCombatPlugins in this mod. The only overhead should be the classes
   stored in memory, if possible.

(readme last updated 2014-01-01)