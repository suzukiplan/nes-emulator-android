# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             nes-core

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/jni.cpp
             src/main/cpp/AndroidAudioFairy.cpp
             src/main/cpp/AndroidGamepadFairy.cpp
             src/main/cpp/AndroidVideoFairy.cpp
             src/main/cpp/Cycloa/src/emulator/Cartridge.cpp
             src/main/cpp/Cycloa/src/emulator/file/NesFile.h
             src/main/cpp/Cycloa/src/emulator/file/NesFile.cpp
             src/main/cpp/Cycloa/src/emulator/Processor.cpp
             src/main/cpp/Cycloa/src/emulator/Audio.cpp
             src/main/cpp/Cycloa/src/emulator/AudioChannel.h
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper4.cpp
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper1.h
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper23.h
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper2.cpp
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper3.cpp
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper1.cpp
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper0.cpp
             src/main/cpp/Cycloa/src/emulator/mapper/board/VRC4.cpp
             src/main/cpp/Cycloa/src/emulator/mapper/board/VRC4.h
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper2.h
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper25.h
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper21.h
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper3.h
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper25.cpp
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper4.h
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper0.h
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper23.cpp
             src/main/cpp/Cycloa/src/emulator/mapper/Mapper21.cpp
             src/main/cpp/Cycloa/src/emulator/Video.cpp
             src/main/cpp/Cycloa/src/emulator/VirtualMachine.h
             src/main/cpp/Cycloa/src/emulator/fairy/VideoFairy.h
             src/main/cpp/Cycloa/src/emulator/fairy/FairyConstantValues.cpp
             src/main/cpp/Cycloa/src/emulator/fairy/AudioFairy.h
             src/main/cpp/Cycloa/src/emulator/fairy/GamepadFairy.h
             src/main/cpp/Cycloa/src/emulator/VirtualMachine.cpp
             src/main/cpp/Cycloa/src/emulator/exception/EmulatorException.cpp
             src/main/cpp/Cycloa/src/emulator/exception/EmulatorException.h )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log)

find_library( # Sets the name of the path variable.
              graphic-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              jnigraphics)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       nes-core

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       ${graphic-lib})