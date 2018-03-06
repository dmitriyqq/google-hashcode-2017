#!/bin/sh

in="./in/$1.in"
out="./out/$1.out"

echo $in
echo $out

./hashcode "$in" "$out"
