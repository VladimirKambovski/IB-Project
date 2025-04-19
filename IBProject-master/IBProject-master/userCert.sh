#!/bin/bash

if [[ $# -eq 0 ]]; then
  echo "Usage: $0 <name>"
  exit 1
fi

name=$1

# Generate CSR
openssl req -new -newkey rsa:4096 -nodes -keyout "${name}.key" -out "${name}.csr"

# Sign CSR with root CA and create certificate
openssl x509 -req -CA rootCA.crt -CAkey rootCA.key -in "${name}.csr" -out "${name}.crt" -days 365 -CAcreateserial

# Create PKCS#12 file containing private key and certificate
openssl pkcs12 -export -out "${name}.p12" -name "${name}" -inkey "${name}.key" -in "${name}.crt"
