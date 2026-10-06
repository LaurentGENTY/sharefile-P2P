#!/usr/bin/env bash
# Records the automatic demo (macOS only) and converts it to media/demo.mp4 and media/demo.gif.
# Needs the Screen Recording permission for the terminal running it, and ffmpeg.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DURATION=${DURATION:-30}
TMP="$(mktemp -d)"
mkdir -p "$ROOT/media"

"$ROOT/demo/run-demo.sh" &
demo_pid=$!
# Start capturing only once every demo window is open (build + JVM startup take a few seconds)
until [[ $(pgrep -f "DemoPeer|DemoLog" | wc -l) -ge 4 ]]; do sleep 0.5; done
sleep 2
# Capture only the demo area, below the menu bar
screencapture -v -x -V "$DURATION" -R "0,30,${SCREEN_W:-1512},$(( ${SCREEN_H:-982} - 30 ))" "$TMP/demo.mov"
kill "$demo_pid" 2>/dev/null || true
wait "$demo_pid" 2>/dev/null || true

ffmpeg -y -loglevel error -i "$TMP/demo.mov" \
  -vf "scale=1512:-2,fps=30" -c:v libx264 -pix_fmt yuv420p -crf 23 -movflags +faststart \
  "$ROOT/media/demo.mp4"
ffmpeg -y -loglevel error -i "$TMP/demo.mov" \
  -vf "fps=10,scale=1000:-1:flags=lanczos,split[a][b];[a]palettegen=max_colors=128[p];[b][p]paletteuse=dither=bayer" \
  "$ROOT/media/demo.gif"

echo "Written: media/demo.mp4 ($(du -h "$ROOT/media/demo.mp4" | cut -f1)), media/demo.gif ($(du -h "$ROOT/media/demo.gif" | cut -f1))"
