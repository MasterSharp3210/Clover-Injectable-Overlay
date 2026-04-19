# Clover Injectable Overlay 
This is a (Clover-Minecraft-Client)[https://github.com/MasterSharp3210/Clover-Minecraft-Client] Add-In. This is my first Injectable Program i ever created. **Only for Windows**

# Some Informations
Supported OS: **Windows**

Target hProc: **javaw.exe**

Target huint: **Minecraft 1.8.9 Forge**

Languages: 
**Java** (Minecraft Class)

**C++** (DLL File)

**C#** (Injector Program)

**C** (Kernel + WinAPI)

# Explanation
The Heart of the Client is written in Java likes a normal mod with a special `public static void Inject` which specify the injection progress loading other internal classes using `private static void StartClient`. After this, the mod classes will run as normal mod parts. The important block is written in C and C++. This **D**ynamic**L**ink**L**ibrary, use WinAPI and Kernel32 to decompress the .jar mod and introduce these into Minecraft (*javaw.exe*)...
The Injector written in C#, load the .dll as a module and Forge Loader accept the mod class with some -jvmArgs like JNI/JNA.

I admit, the C++ part is coded under the help of Gemini AI. So if you want to report an issue about the DLL, i think i won't be able to help you :((

# I hope you enjoy
This is a Developer Sperimental Program which uses DLL Process Modifications. Only expert devs can rate this. If you want to download this file you have the full responsibility of your actions. Enjoy!
