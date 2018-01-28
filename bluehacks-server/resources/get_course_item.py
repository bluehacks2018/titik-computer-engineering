import json
import falcon
import requests

from db import CourseItem
from db import bucket

class GetCourseItemResource(object):
    def on_get(self, req, res):
        courses = self.session.query(CourseItem).all()

        courses_list = []
        for course in courses:
            courses_row = []
            courses_row.append(course.firebase_url)
            courses_row.append(course.title)
            courses_row.append(course.language)
            courses_row.append(course.owner)
            courses_row.append(course.filesize)
            courses_row.append(course.category)
            courses_row.append(course.rating)
            courses_row.append(course.rating_weight)
            
            course_list.append(course_row)

        res.body = json.dumps({ 'courses': courses_list })
    def on_post(self, req, res):
        if req.stream:
            requestBody = json.load(req.stream)
                
        if requestBody:
            firebase_url = requestBody.get('firebase_url')
            firebase_url = requestBody.get('language')
            metadata = bucket.get_blob(firebase_url).metadata

            res.status = falcon.HTTP_200

            res.body = json.dumps({
                'firebase_url': '/' + firebase_url,
                'metadata': metadata
            })
        else:
            res.status = falcon.HTTP_400