# CHANGES

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

