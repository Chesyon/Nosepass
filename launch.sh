#!/bin/bash
set -e

export SKYCOMPASS_URL='http://localhost:8000'

granian --interface asgi sky_compass.api:app &
./nosepass-1.0.0/bin/nosepass
