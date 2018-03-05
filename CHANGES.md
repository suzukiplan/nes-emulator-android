# CHANGES

## Version 1.5.1

- added a method: `NESKey#setCode`

## Version 1.5.0 (destructive)

- describe javadoc
- renamed class: `Logger` -> `NESLogger`
- added save/load state interface (NOTE: interface only)

## Version 1.4.0 (destructive)

- changed the audio capture interface
- added an audio capture service for reading from InputStream
- close check on the setOnCaptureAudioListener

## Version 1.3.1

- execute first enqueue when the first tick called
- add audio capture interface

## Version 1.3.0 (destructive)

- changed the specification of key code

## Version 1.2.3

- added: `NESView#multipleTicks`
- update test code

## Version 1.2.2

- make null safe NESView methods
- bugfix: `NESView#capture` often break

## Version 1.2.1

- added NESView.capture method
- added NESView.reset method

## Version 1.2.0

- bugfix: buffer overwrite every sound buffer copy
- change `NESView#load` return code to `boolean` from `void`

## Version 1.1.3

- update license info in bintray

## Version 1.1.2

- bugfix: correct copy sound buffer procedure

## Version 1.1.1

- bugfix: invalid copy size (it does not become obvious)
- implement key input to the test module

## Version 1.1.0 (destructive)

- destructive changed: NESView#tick (needs specify key codes)
- added NESKey class (key code calculator)

## Version 1.0.0

first release

