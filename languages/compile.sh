#!/bin/sh

for i in *.properties; do
  native2ascii -encoding UTF-8 $i ../src/main/resources/$i
done

