Movie Script Test

Given a movie script in the format described below, the application will have to extract the movie
settings, the characters and the dialogues, identifying the characters that appeared in each
setting, and counting the dialogue words for each character.

Movie settings

Scene headings have one line, all upper­case chars, no indentation, and begin with "EXT. ",
"INT. " or "INT./EXT. ". Setting names in scene headings are separated by " ­ ". Only consider
the first setting name and ignore the prefix. Settings with the same name are considered the
same setting.

Ex1:

INT./EXT. LUKE'S SPEEDER ­ DESERT WASTELAND ­ TRAVELING ­ DAY

Results into "LUKE'S SPEEDER".

Ex2:

INT. LUKE'S X­WING FIGHTER ­ COCKPIT ­ TRAVELING

Results into "LUKE'S X­WING FIGHTER".
Movie characters and dialogues
Dialogue headings have one line, all upper­case chars, indented by 22 space chars, with the
character name. Character names may contain space chars in the middle. Characters with the
same name are considered the same character. Dialogues are indented by 10 space chars.

Dialogue descriptions are indented by 15 space chars.

Words for the character dialogues should be counted, case insensitive, ignoring punctuation

marks and dialogue descriptions.

Ex1:

THREEPIO

I see, sir.

LUKE

Uh, you can call me Luke.

THREEPIO

I see, sir Luke.

LUKE

(laughing)

Just Luke.

Results:

character name: "THREEPIO"

counted words: "i" (2 times), "see" (2 times), "sir" (2 times), "luke" (1 time).

character name: "LUKE"

counted words: "luke" (2 times), "uh" (1 time), "you" (1 time), "can" (1 time), "call" (1 time), "me"

(1 time), "just" (1 time).

Ex2:

THREEPIO

Artoo­Detoo! It's you! It's you!

Results:

character name: "THREEPIO"

counted words: "it’s" (2 times), "you" (2 times), "artoo" (1 time), "detoo" (1 time).

Dialogue word counts should be summed for all the dialogues of each character in all the
settings.
