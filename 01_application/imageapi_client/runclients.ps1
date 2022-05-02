param (
    [parameter(Mandatory=$true)] [String]$serviceURL,
	[parameter(Mandatory=$true)] [String]$tenantName
)

echo "Got Server URL: $serviceURL"
$SERVICE_URL="$serviceURL/validate" 
$CLIENT_NAME=$tenantName
echo "Got Service URL: $SERVICE_URL"
echo "Got Tenant: $CLIENT_NAME"

Do {
    ./imageapiclient "${SERVICE_URL}" "${CLIENT_NAME}" "image-1.png"
    
	Start-Sleep -s 5
}
while ($true)