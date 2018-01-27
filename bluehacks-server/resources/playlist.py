import json
import falcon
import requests

from db import Playlist
from db import bucket

class PlaylistResource(object):
    def on_get(self, req, res):
        if req.params['name'] and req.params['user_email']:
            name = req.params['name']
            user_email = req.params['user_email']
            user_course_items = self.session.query(Playlist).filter_by(name=name, user_email=user_email).all()
            print(user_course_items)

            if user_course_items:
                url_array = []
                for course in user_course_items:
                    url_array.append({
                        'url': '/' + course.firebase_url,
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
            name = requestBody.get('name')
            firebase_url = requestBody.get('firebase_url')
            user_email = requestBody.get('user_email')

            playlist_entry = self.session.query(Playlist).filter_by(name=name, user_email=user_email)

            if not playlist_entry:
                playlist_entry = Playlist(
                    name=name,
                    user_email=user_email,
                    firebase_url=firebase_url
                )
                self.session.add(playlist_entry)
                self.session.commit()

                playlist_entry = self.session.query(Playlist).filter_by(name=name, user_email=user_email)

                if playlist_entry:
                    res.status = falcon.HTTP_200
                    res.body = json.dumps({
                        'playlist_entry': playlist_entry.firebase_url
                    })
                else:
                    res.status = falcon.HTTP_500
            else:
                res.status = falcon.HTTP_500
        else:
            res.status = falcon.HTTP_400
        
