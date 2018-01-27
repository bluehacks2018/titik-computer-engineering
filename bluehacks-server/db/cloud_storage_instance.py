# Imports the Google Cloud client library
from google.cloud import storage
import os

os.environ['GOOGLE_APPLICATION_CREDENTIALS']='bluehacks2018-47b156a193b0.json'

# Instantiates a client
storage_client = storage.Client()

# The name for the new bucket
bucket_name = 'bluehacks2018.appspot.com'

# Creates the new bucket
bucket = storage_client.get_bucket(bucket_name)

print('Bucket {} accessed'.format(bucket.name))