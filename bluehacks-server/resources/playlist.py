import json
import falcon
import requests

from db import Playlist
from db import bucket

class PlaylistResource(object):
    def on_get(self, req, res):
        if req.params['user_email']:
            user_email = req.params['user_email']
            user_course_items = self.session.query(Playlist).filter_by(user_email=user_email).all()
            print(user_course_items)

            if user_course_items:
                url_array = []
                for course in user_course_items:
                    url_array.append({
                        'url': course.firebase_url,
                        'metadata': bucket.get_blob(course.firebase_url).metadata
                    })

                res.status = falcon.HTTP_200
                res.body = json.dumps({
                    'playlist': url_array
                })
            else:
                res.status = falcon.HTTP_500
        else:
            res.status = falcon.HTTP_400

    def on_post(self, req, res):
        if req.stream:
            requestBody = json.load(req.stream)
                
        if requestBody:
            firebase_url = requestBody.get('firebase_url')
            user_email = requestBody.get('user_email')

            playlist_entry = self.session.query(Playlist).filter_by(firebase_url=firebase_url, user_email=user_email)

            if not playlist_entry:
                playlist_entry = Playlist(
                    user_email=user_email,
                    firebase_url=firebase_url
                )
                self.session.add(playlist_entry)
                self.session.commit()

                playlist_entry = self.session.query(Playlist).filter_by(firebase_url=firebase_url, user_email=user_email)

                if playlist_entry:
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
        
