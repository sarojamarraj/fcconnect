#!/bin/sh

# source /dev/stdin < <(gpg -o- data.gpg)


if [ $# -lt 1 ]; then
  echo "usage: `basename "${0}"` host"
  exit
fi

HOST=$1
BASEDIR=$(dirname "$0")


${BASEDIR}/set-password-username dalslighting "${FREIGHTCOM_dalslighting}" $HOST
${BASEDIR}/set-password-username ChequesPlus "${FREIGHTCOM_ChequesPlus}" $HOST
${BASEDIR}/set-password-username sdaudlin "${FREIGHTCOM_sdaudlin}"   $HOST
${BASEDIR}/set-password-username spears1 "${FREIGHTCOM_spears1}"  $HOST

${BASEDIR}/set-password-username admin "${FREIGHTCOM_admin}" $HOST
${BASEDIR}/set-password-username peppertree "${FREIGHTCOM_peppertree}" $HOST

