#!/bin/bash
basePath=../../..
systemPath=$basePath/system
rlGluePath=$systemPath/rl-glue/RL-Glue
PYTHONPATH=$rlGluePath/Python:./src python -c "import org.rlcommunity.rlglue.codec.util.AgentLoader" RandomAgent