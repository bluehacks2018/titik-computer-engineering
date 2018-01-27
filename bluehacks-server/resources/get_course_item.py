import json
import falcon
import requests

from db import CourseItem
from db import bucket

class GetCourseItemResource(object):
    def on_post(self, req, res):
        if req.stream:
            requestBody = json.load(req.stream)
                
        if requestBody:
            firebase_url = requestBody.get('firebase_url')
            metadata = bucket.get_blob(firebase_url).metadata

            res.status = falcon.HTTP_200

            res.body = json.dumps({
                'firebase_url': '/' + firebase_url,
                'metadata': metadata
            })
        else:
            res.status = falcon.HTTP_400