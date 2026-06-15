# Nosepass
Discord bot frontend for [sky-compass](https://github.com/Chesyon/sky-compass).

## Running Nosepass
There are two primary methods of running Nosepass: through the Docker image or manually. Regardless of the approach you choose to take, you will need to create a Discord app and acquire a token. Discord's developer documentation explains how to do this, so I will not do so here. Follow the  of the *Creating an app* section of the [*Building your first Discord Bot*](https://docs.discord.com/developers/quick-start/getting-started#step-1-creating-an-app) guide if you haven't done this before.

### Docker (Recommended)
Install Docker Engine through their [setup guide](https://docs.docker.com/engine/install/).
```
docker pull ghcr.io/chesyon/nosepass
docker run -e BOT_TOKEN=[YOUR_TOKEN_HERE] ghcr.io/chesyon/nosepass
```

### Manual (Untested, not recommended)
Nosepass *should* be able to run anywhere you can run Python and Java. Specific instructions for Ubuntu-based Linux distros will be available when I can be bothered to check them; PRs are welcome for any other OS-specific instructions!
#### Dependencies
- JDK 21 (newer versions may work but are untested)
- Python 3.10 or newer
	- pip is additionally required but should be installed by default
- PyPI packages (you may wish to install these inside of a venv):
	- sky-compass with API dependencies: `pip install sky-compass[api]`
	- A Python HTTP server package capable of ASGI; I recommend Granian, but Uvicorn should work fine as well.
- Nosepass repository: `git clone https://github.com/Chesyon/Nosepass`, or download and extract source code from GitHub
#### Running
1. Run the HTTP server (varies by implementation; with Granian, use `granian --interface asgi sky_compass.api:app`)
  * If using a remote sky-compass server, instead set the `SKYCOMPASS_URL` environment variable (`export SKYCOMPASS_URL=[YOUR_URL_HERE]`)
2. With the HTTP server still running, run Nosepass with gradlew: `BOT_TOKEN=[YOUR_TOKEN_HERE] ./gradlew(.bat) run`
## License
Nosepass is licensed under the MIT License.
