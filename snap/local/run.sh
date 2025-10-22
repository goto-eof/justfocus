#!/bin/bash

exec "$SNAP/bin/java" \
    -Dsun.java2d.opengl=true \
    -Dsun.java2d.xrender=true \
    -Djdk.gtk.version=3 \
    -jar "$SNAP/jar/just-focus.jar" "$@"