import json
import falcon
import requests

from db import CourseItem, Rating
from db import bucket

class RateResource(object):
    def on_post(self, req, res):
        if req.stream:
            requestBody = json.load(req.stream)
                
        if requestBody:
            firebase_url = requestBody.get('firebase_url')
            user_email = requestBody.get('user_email')
            add_rating = requestBody.get('rating')

            rating = self.session.query(Rating).filter_by(firebase_url=firebase_url, user_email=user_email)

            if not rating:
                rating = Rating(
                    user_email=user_email,
                    firebase_url=firebase_url,
                    rating=add_rating
                )
                self.session.add(rating)
                self.session.commit()

                rating = self.session.query(Rating).filter_by(firebase_url=firebase_url, user_email=user_email)
                course_item = self.session.query(CourseItem).filter_by(firebase_url=firebase_url).first()

                if course_item and rating:
                    course_item.rating = str((
                            (float(course_item.rating)*float(course_item.rating_weight)) + float(add_rating)
                        ) / (float(rating_weight)+1))
                    course_item.rating_weight += 1
                    self.session.commit()
                    
                    res.status = falcon.HTTP_200
                    res.body = json.dumps({
                        'rating': course_item.rating,
                        'rating_weight': course_item.rating_weight
                    })
                else:
                    res.status = falcon.HTTP_500
            else:
                res.status = falcon.HTTP_500
        else:
            res.status = falcon.HTTP_400
        
