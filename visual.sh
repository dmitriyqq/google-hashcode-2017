#!/bin/sh

in="./in/$1.in"
out="./out/$1.out"

echo $in
echo $out

java -jar ./visual/out/artifacts/visual_jar/visual.jar "$in" "$out"
