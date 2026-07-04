# RefLib
A feature-rich application for handling reference images throughout your artistic journey

## Features
### Store reference images while checking for duplicates
- Drag-and-drop images from the file explorer
- Using the SIFT algorithm, new reference images are checked against the references that are already stored
- Accept or reject the new reference image after checking for duplicates
- Planned: Drag-and-drop from the web
- Planned: Compare image data (like resolution) when deciding which of the duplicates to keep
- Planned: Set tags on new reference images

![Demo of the upload page](UploadDemo.png)

### Plan practice session
- Freely plan a practice session using the stored reference images
- Timed refs: Choose the number of references and the duration of each reference
- Untimed refs: Choose the number of references
- Break: Put a break between sections of the practice session
- Planned: Filter on reference tags to limit which images are included
- Planned: Save multiple different practice sequences

![Demo of Planning page](PlanningDemo.png)

### Practice session
- Randomly display references from the stored images
- Practice session goes through the steps defined during practice planning
- Remaining time and remaining reference images are always displayed
- Pause practice session
- Skip to next image

![Demo of the Practice page](PracticeDemo.png)

### Planned: Browse stored reference images
- Planned: Browse through all stored reference images
- Planned: Filter images based on tags

### Planned: Tags
- Planned: Filter images used in the practice session based on reference tags
- Planned: Filter images displayed when browsing based on reference tags

### Planned: Pop out reference image
- Planned: Pop out to a transparent window that can be dragged around freely
- Planned: Image controls to help compare the artwork with the reference
  - Image opacity
  - Image rotation and scaling
  - Edge detection filter (to highlight outer edges of the reference subject)

## Roadmap
### Bugs
- Upload dragboard behaves strange sometimes (at least when running from VSCode on Wayland)
  - Sometimes empty dragboard
  - Sometimes takes image in clipboard instead of dragboard
  - Throws a lot of exceptions
### Improvements
- Adress TODOs
- Properly implement navigation in the app
- Find a good way to test the session part of Practice
- Avoid the dependency on libgtk-x11-2.0.so if possible
  - Can be done by manually building javacv-platform with the env var HEADLESS="yes"
- I would ideally like Practice to rely on Ref instead of MatchableRef. Just for abstraction's sake
- Look into the stack guard warning
- Be more dynamic about displayed image size
- Make the app prettier 
- Take metadata rotations into account
- Build guide
- Handle descriptor matching on other threads
- Proper error handling
  - Descriptive error types
  - Pop-ups
  - Code responding to errors
### Home page
- ~~Links to the other pages~~
- Stats?
### Practice page
- Ensure that terminology is consistent (e.g. practice/session, ~~ref/pose/image~~)
- Provide helpful filtering options when planning practice
- ~~Display the total amount of practice time when planning practice~~
- ~~Display ref images at random~~
- ~~Show ref #~~
- Practice settings
  - Practice sequences
    - ~~Step types~~
      - ~~timed images~~
      - ~~untimed images~~
      - ~~breaks~~
    - Add a minutes field to duration
    - ~~Edit practice sequence~~
    - Rename sequence
    - Reorder sequences
    - ~~Save multiple different practice sequences~~
    - ~~Delete a practice sequence~~
  - ~~Allow or disallow duplicates~~
    - ~~Restore list after each session~~
  - Filter on tags
- Page to review images from session
  - ~~Show drawn images~~
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
- Upload images from
  - ~~Local files~~
  - Browser
- Check new images against images in library to avoid duplicates
  - ~~SIFT~~
    - Or a faster algorithm if I ever find one that works as well
  - Store SIFT descriptors of images in library (only needs to compute descriptors for new image)
  - ~~Handle the fact that SIFT is not flip-robust~~
    - ~~Can the descriptors be flipped so they only need to be computed once?~~
    - ~~Or is it necessary to compute descriptors for the image as well as the double-sided image?~~
  - ~~Display 5? most similar images~~
  - Let user compare and keep the image they prefer (e.g. higer res)
- Also check duplicate names I guess
### Browse page
- Browse collection based on tags
### Storage
- Let user define location of images
- Appdata with info about images
  - ~~Location~~
  - Tags
  - SIFT descriptors
- Allow separately fetching Ref path or descriptors (match on ID)
- Export database