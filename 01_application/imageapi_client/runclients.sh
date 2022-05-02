#! /bin/zsh

if [ $# -lt 2 ]
  then
    echo "Error: Valid server URL and client name required"
    echo "Example: http://server client_1 or https://server client_1"
    return
fi
echo "Got Server URL: $1"
SERVICE_URL="${1}/validate" 
CLIENT_NAME="${2}"
echo "Got Service URL: $SERVICE_URL"
while :
do
	echo "Submitting API Call; hit [CTRL+C] to stop!"
    ./imageapiclient "${SERVICE_URL}" "${CLIENT_NAME}" "image-1.png"
    sleep 5s
done
#./runclients.sh https://imageapi.malcolm.io Client_1
#./imageapiclient "https://imageapi.malcolm.io/validate" "Malcolm-GO" "image-1.png"
