#!/bin/bash

# Define log file path
LOG_FILE="/var/log/vminit.log"

# Redirect stdout and stderr to the log file
exec > >(tee -a "$LOG_FILE") 2>&1

sudo apt-get update
sudo apt-get install -y ca-certificates curl 

# Installs Docker
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo $VERSION_CODENAME) stable" | sudo tee /etc/apt/sources.list.d/docker.list
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io

# Installs Azure CLI
curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash

# Login to azure via identity
az login --identity --allow-no-subscription

sudo apt-get install -y certbot

sudo certbot certonly --standalone -d learniverseconnect.norwayeast.cloudapp.azure.com --non-interactive --agree-tos --email tobiagra@stud.ntnu.no

DB_PASSWORD=$(sudo az keyvault secret show --name DB-Password --vault-name LC-Key-Vault-Test --query value -o tsv)
DB_USERNAME=$(sudo az keyvault secret show --name DB-Username --vault-name LC-Key-Vault-Test --query value -o tsv)
EMAIL_PASSWORD=$(sudo az keyvault secret show --name Email-Password --vault-name LC-Key-Vault-Test --query value -o tsv)
EMAIL_USERNAME=$(sudo az keyvault secret show --name Email-Username --vault-name LC-Key-Vault-Test --query value -o tsv)
JWT=$(sudo az keyvault secret show --name JWT --vault-name LC-Key-Vault-Test --query value -o tsv)

sudo az acr login --name learniverseconnect

sudo docker network create lc-net

sudo docker pull learniverseconnect.azurecr.io/lc-backend:latest
sudo docker run --restart unless-stopped -d --name lc-backend -e DATABASE_URL=lc-database.mysql.database.azure.com -e DATABASE_USERNAME="${DB_USERNAME}" -e JWTSECRET="${JWT}" -e EMAIL_USERNAME="${EMAIL_USERNAME}" -e EMAIL_PASSWORD="${EMAIL_PASSWORD}" -p 8080:8080 --network lc-net learniverseconnect.azurecr.io/lc-backend:latest

sudo docker pull learniverseconnect.azurecr.io/lc-frontend:latest
sudo docker run --restart unless-stopped -d --name lc-frontend -v /etc/letsencrypt:/etc/letsencrypt:ro --network lc-net -p 80:80 -p 443:443 learniverseconnect.azurecr.io/lc-frontend:latest