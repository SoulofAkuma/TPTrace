This plugin traces every teleport affecting you. E.g. you or someone else executes a command that leads to a teleport or the game teleports you when you do a certain action. These commands can also be from other plugins such as a warp management plugin etc. . The plugin will create a long list of past locations your teleports have originated from (whenever you die your death coordinate will also be added to that list).

To list these locations use `/backlist [optional number of elements] [optional clear]`. The default number of elements is equal to the number of elements in the list with a maximum of 10. If a number of steps and the keyword `clear` is given, the given number of elements will be deleted from the end of the list, instead of being printed.

To go back a certain amount of steps use `/back [optional number of steps]`. The default number of steps is 1. After you've gone back, the element is removed from the list.

If you encounter any bugs feel free to contact me or post them as an issue on the GitHub site of the plugin.
