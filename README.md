# RefLib

## About??

## Roadmap
### Bugs
- Figure out the "requires transitive" warning
- Where are all the tests, you lazy bum??
- Only show debug button when run in debug mode
### Improvements
- Is it bad that `elapsedSeconds` in the model is updated by the timer every ms?
### Home page
- Links to the other pages I guess??
- Funny stats?
- Idk it's just the home page
### Practice page
- Ensure that terminology is consistent (e.g. practice vs session)
- Provide helpful filtering options when planning practice
- Display the total amount of practice time when planning practice
- ~~Display ref images at random~~
- ~~Show pose #~~
- Practice settings
  - Practice sequences
    - ~~Step types~~
      - ~~timed images~~
      - ~~untimed images~~
      - ~~breaks~~
    - Edit practice sequence
    - Save multiple different practice sequences
    - Delete a practice sequence
  - ~~Allow or disallow duplicates~~
    - ~~Restore list after each session~~
  - Filter on tags
  - Exclude NSFW
- ~~Page to review images from session~~
  - ~~Option to copy image~~
  - Option to bulk delete
  - Option to bulk tag
  - Option to display image information
  - (Maybe) Remember previous sessions (delayed review)
- ~~Pause session~~
- ~~Skip image~~
- Mark image (for later deleting / editing / etc.)
- (Maybe) One-handed viewing tools (for the traditional people to hold the paper against the screen)
  - Quick access to filters like edge detection and threshold (easier to see ref through paper)
  - One handed transformations (zoom, rotation, panning)
- In case an image has disappeared, warn user
  - Option to remind next time RefLib is opened
- (Maybe) would it be possible to "pop out" a ref in a transparent window to let user drag ref on top of drawing program without copy-pasting? If so, add this in practice, review and browse
### Upload page
- Check new images against images in library to avoid duplicates
  - SIFT
    - Or a faster algorithm if I ever find one that works as well
  - Store SIFT descriptors of images in library (only needs to compute descriptors for new image)
  - Display 5? most similar images
  - Let user compare and keep the image they prefer (e.g. higer res)
- Also check duplicate names I guess
### Browse page
- Browse collection based on tags
### RefLib settings
- Option to remove all mentions of "NSFW"
### Storage
- Let user define location of images
- Appdata with info about images
  - Location
  - Tags
  - SIFT descriptors
- Export data