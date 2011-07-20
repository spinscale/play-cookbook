#!/bin/bash

set -xe

PLAY_PATH="/Users/alexanderreelsen/Downloads/play-1.2.1"

$PLAY_PATH/play deps
$PLAY_PATH/play auto-test

cd test-result
(
echo "<html><head><title>Test results</title></head><body><ul>"

for i in *.failed.html ; do
        if [ -e $i ] ; then
                echo "<li><a href=\"$i\">$i</li>"
        fi
done

echo "</ul><p><ul>"

for i in *.passed.html ; do
        if [ -e $i ] ; then
                echo "<li><a href=\"$i\">$i</li>"
        fi
done

echo "</ul></body></html>"
) > index.html

if [ -e result.failed ] ; then
  return -1
fi

cd -

