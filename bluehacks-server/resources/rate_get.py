import json
import falcon
import requests

from db import CourseItem, Rating
from db import bucket
class GetRateResource(object):
    def on_post(self, req, res):
        if req.stream:
            requestBody = json.load(req.stream)

        if requestBody:
            firebase_url = requestBody.get('firebase_url')
            course_item = self.session.query(CourseItem).filter_by(firebase_url=firebase_url).first()

            if course_item:
                res.status = falcon.HTTP_200
                res.body = json.dumps({
                    'rating': course_item.rating,
                    'rating_weight': course_item.rating_weight
                })
            else:
                res.status = falcon.HTTP_500
        else:
            res.status = falcon.HTTP_400