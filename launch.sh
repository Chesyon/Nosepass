#!/bin/bash
set -e

granian --interface asgi sky_compass.api:app &
./nosepass-1.0.0/bin/nosepass
