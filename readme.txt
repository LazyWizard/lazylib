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
its classes are called, there is no harm in leaving it tagged in the launcher
at all times even if your currently active mods don't require it.


 Compiling from source:
------------------------
If you want to compile LazyLib yourself, the source can be found inside
jars/LazyLib.jar (most modern archive programs can open jars).
LazyLib only requires starfarer.api.jar and lwjgl_util.jar in the classpath.


(readme last updated 2013-04-19)