#!/usr/bin/env bash
# Builds the tracker and the peer, then starts a local demo:
# 1 tracker + 3 peers (A shares demo.txt, B and C download it piece by piece).
#
# Usage: demo/run-demo.sh [--manual]
#   default   the peers' GUIs are driven automatically (reproducible recording)
#   --manual  just open the windows, you click yourself
#
# Window layout targets a 1512x982 screen; override with SCREEN_W / SCREEN_H.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
WORK="$ROOT/demo/work"
AUTO="--auto"
[[ "${1:-}" == "--manual" ]] && AUTO=""
W=${SCREEN_W:-1512}
H=${SCREEN_H:-982}

make -s -C "$ROOT/tracker" >/dev/null
make -s -C "$ROOT/peer" >/dev/null

# Fresh working directories: each peer resolves ../config.ini, ../seed, ../log.txt from its build/ dir
[[ -d "$WORK" ]] && rm -r "$WORK"
mkdir -p "$WORK/tracker"
cp "$ROOT/tracker/build/tracker" "$ROOT/tracker/config.ini" "$WORK/tracker/"
port=12222
for p in A B C; do
  mkdir -p "$WORK/peer$p/build" "$WORK/peer$p/seed"
  cp "$ROOT"/peer/build/*.class "$WORK/peer$p/build/"
  javac -nowarn -cp "$WORK/peer$p/build" -d "$WORK/peer$p/build" "$ROOT"/demo/Demo*.java
  printf "tracker-address=127.0.0.1\ntracker-port=10000\nopen-port=%s\n" "$port" > "$WORK/peer$p/config.ini"
  port=$((port + 1))
done

# The shared file lives outside seed/: the original peer crashes when it serves a
# file that was already in seed/ at startup (FileManager.getBuffermapToString).
mkdir -p "$WORK/peerA/share"
for i in $(seq -w 1 139); do
  echo "Line $i: The quick brown fox jumps over the lazy dog - P2P demo payload."
done > "$WORK/peerA/share/demo.txt"

pids=()
cleanup() { kill "${pids[@]}" 2>/dev/null || true; }
trap cleanup EXIT INT TERM

# `script` gives the tracker a pty so its stdout is not block-buffered
(cd "$WORK/tracker" && exec script -q -F tracker.out ./tracker >/dev/null) &
pids+=($!)
sleep 1

TOP=30
TRACKER_H=$(( (H - TOP) / 5 ))
COL_W=$(( W / 3 ))
PEER_Y=$(( TOP + TRACKER_H ))
PEER_H=$(( H - PEER_Y ))

(cd "$WORK/peerA/build" && exec java DemoLog "Tracker (C) - port 10000" ../../tracker/tracker.out 0 $TOP "$W" $TRACKER_H) &
pids+=($!)
(cd "$WORK/peerA/build" && exec java DemoPeer A seed  0              $PEER_Y $COL_W $PEER_H 4000  $AUTO) &
pids+=($!)
(cd "$WORK/peerB/build" && exec java DemoPeer B leech $COL_W         $PEER_Y $COL_W $PEER_H 10000 $AUTO) &
pids+=($!)
(cd "$WORK/peerC/build" && exec java DemoPeer C leech $((COL_W * 2)) $PEER_Y $COL_W $PEER_H 12000 $AUTO) &
pids+=($!)

echo "Demo running (work dir: $WORK). Press Ctrl+C to stop."
wait
