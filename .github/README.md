# BattleScape

This is the open side of the BattleScape RSPS. Due to the private core, to fully test any changes, you'll need to request access to the beta server were you can freely deploy your changes as often as needed.

## Getting Started

These instructions will get you a copy of the project up on your local machine for development purposes.

### Prerequisites

[Visual Studio Code](https://code.visualstudio.com) is recommended, and will be the IDE used in the installation guide.

Required extensions:
- Java Extension Pack
- Lombok Annotations Support for VS Code

Recommended extensions:
- Bracket Pair Colorizer
- GitLens — Git supercharged
- Trello Viewer (if you've been given access to BattleScape's Trello).

Creating your own fork:
- Select `Fork` in the top-right of this page.
- Once it finishes, you'll be taken to your forked copy of our repo.
- Select `Clone or download` and copy the url, you'll need this to start installing.

### Installing

- Create your own fork, as described in prerequisites.
- Open Visual Studio Code.
- Install the extensions mentioned in prerequisites through the sidebar.
- Go to View -> Command Pallete...
- Type in `Git: Clone` and select it.
- Paste the url you copied earlier from `Clone or download` and hit enter.
- Choose the location you'd like to create tthe repository at, such as your desktop. The directory that will be created can be renamed or moved later.
- Select open when asked if you'd like to open the cloned repository.

## Running/Debugging

Autocompletion and error checking against the core will work, but you won't be able to deploy a test server on your local machine. You can request access to the beta server where you can upload your classes to test and debug them thoroughly.

There is an ongoing effort to port a large amount of the closed repo over to the open repo to provide better access to what can be worked on, but there are currently many limitations to what can be changed due to what is publicly available. The reason for the closed repo is to limit access to some core classes to prevent anyone from simply creating their own server from BattleScape instead of contributing to it.

## Getting the Latest Changes

Your forked repo won't automatically fetch updates made to the official repo.

Setup:
- Go to `View -> Command Palette...`.
- Type in `Git: Add Remote` and select it.
- Type in `BattleScape-Server` for the name and hit enter.
- Type in `https://github.com/Palidino/BattleScape-Server.git` for the url and hit enter.
- Go to `View -> Command Palette...`.
- Type in `Git: Fetch From All Remotes`.

Pulling any changes from the official repo:
- From `Source Ccontrol` in the sidebar, select the three dots on the right-side of source control git (`More Actions...`).
- Select `Pull from...`.
- Select `BattleScape-Server`.
- Select `BattleScape-Server/master`.

## Contributing

Once you've completed change(s), push them into your forked repo.
- Go to `Source Control` in the sidebar.
- Enter a message briefly describing the changes.
- Select the checkmark (`Commit`).
- Select the `Synchronize Changes`, which saves your changes into your forked repo.

To request your changes to be added to the official repo, visit your forked repo on GitHub. Select `New pull request`, and then submit your changes from there.
