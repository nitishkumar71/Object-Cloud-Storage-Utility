sudo add-apt-repository -y ppa:webupd8team/java
apt-get update
rm -rf /var/lib/apt/lists/*
rm -rf /var/cache/oracle-jdk8-installer


# Create an environment variable for the correct distribution
export CLOUD_SDK_REPO="cloud-sdk-$(lsb_release -c -s)"

# Add the Cloud SDK distribution URI as a package source
echo "deb http://packages.cloud.google.com/apt $CLOUD_SDK_REPO main" | sudo tee -a /etc/apt/sources.list.d/google-cloud-sdk.list

# Import the Google Cloud Platform public key
curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -

# Update the package list and install the Cloud SDK
sudo apt-get update && sudo apt-get install google-cloud-sdk

# provide your personal access for gcloud on development environment
gcloud auth application-default login

# provide secret json for service account
export GOOGLE_APPLICATION_CREDENTIALS=/path/to/my/key.json

sudo pip install awscli==1.11.178
aws configure