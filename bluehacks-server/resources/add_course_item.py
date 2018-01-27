import json
import falcon
import requests

from db import CourseItem
from db import bucket

class AddCourseItemResource(object):
    def on_post(self, req, res):
        # Example x-www-form-urlencoded
        if req.stream:
            requestBody = json.load(req.stream)
                
        if requestBody:
            course_item = CourseItem(
                firebase_url = requestBody.get('firebase_url'),
                title = bucket.get_blob(requestBody.get('firebase_url')).name,
                language = requestBody.get('language'),
                owner = requestBody.get('owner'),
                filesize = str(bucket.get_blob(requestBody.get('firebase_url')).size)
            )
            
            self.session.add(course_item)
            self.session.commit()

            course_item = self.session.query(CourseItem).filter_by(firebase_url=requestBody.get('firebase_url')).first()

            if course_item:
                res.status = falcon.HTTP_200
                res.body = json.dumps({
                    'firebase_url': course_item.firebase_url,
                    'title': course_item.title,
                    'language': course_item.language,
                    'owner': course_item.owner,
                    'filesize': course_item.filesize
                })
            else:
                res.status = falcon.HTTP_500
        else:
            res.status = falcon.HTTP_400
        
