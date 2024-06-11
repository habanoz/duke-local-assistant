#!/usr/bin/env bash

set -e

# Install the requirements
brew install yarn;
brew install nodejs;

echo ""
echo "YARN $(yarn --version) installed."
echo "NODEJS $(node --version) installed."
