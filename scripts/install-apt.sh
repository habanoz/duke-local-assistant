#!/usr/bin/env bash

set -e

# Add Yarn's package repository to the system
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | sudo apt-key add -
echo "deb https://dl.yarnpkg.com/debian/ stable main" | sudo tee /etc/apt/sources.list.d/yarn.list

# Add NodeJS's package repository to the system
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -

# Install the requirements
sudo apt update;
sudo apt install yarn -y;
sudo apt install nodejs -y;

echo ""
echo "YARN $(yarn --version) installed."
echo "NODEJS $(node --version) installed."
echo "DOTNET $(dotnet --version) installed."
