#!/bin/bash
# Run water flow program in the background and shows the current processes
# Rea Keebine
# 19 September 2020

echo running water flow program in the background...
java -cp bin FlowApp largesample_in.txt &
echo showing current processes...
top -H