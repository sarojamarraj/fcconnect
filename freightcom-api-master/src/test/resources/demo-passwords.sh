#!/bin/sh

if [ $# -lt 2 ]; then
  echo "usage: `basename "${0}"` host"
  exit
fi

HOST=$1

${BASEDIR}/set-password-username dalslighting freightdemo1 $HOST
${BASEDIR}/set-password-username ChequesPlus freightdemo1 $HOST
${BASEDIR}/set-password-username sdaudlin freightdemo1   $HOST
${BASEDIR}/set-password-username spears1 freightdemo1 $HOST
${BASEDIR}/set-user-fields-username $HOST spears1 firstname=Spears



