import json
import falcon
import requests

from db import Playlist
from db import bucket

class PlaylistResource(object):
    def on_get(self, req, res):
        user_course_items = self.session.query(Playlist).all()
        data = []
        for playlist in user_course_items:
            if len(data) > 0:
                for i in range(0,len(data)):
                    entry = data[i]
                    if entry.get('name') == playlist.name and entry.get('owner') == playlist.user_email:
                        print('Entry Found, Adding {}'.format(playlist.firebase_url))
                        entry.get('firebase_data').append({
                            'url': playlist.firebase_url,
                            'metadata': bucket.get_blob(playlist.firebase_url).metadata,
                            'size': int(bucket.get_blob(playlist.firebase_url).size/1024/1024)
                        })
                    elif data.index(entry) == len(data)-1:
                        print('Creating Entry, Adding {}'.format(playlist.firebase_url))
                        data.append({
                            'name': playlist.name,
                            'owner': playlist.user_email,
                            'firebase_data': [
                                {
                                    'url': playlist.firebase_url,
                                    'metadata': bucket.get_blob(playlist.firebase_url).metadata,
                                    'size': int(bucket.get_blob(playlist.firebase_url).size/1024/1024)
                                }
                            ]
                        })
                        i+=1
            else:
                data.append({
                    'name': playlist.name,
                    'owner': playlist.user_email,
                    'firebase_data': [{
                            'url': playlist.firebase_url,
                            'metadata': bucket.get_blob(playlist.firebase_url).metadata,
                            'size': int(bucket.get_blob(playlist.firebase_url).size/1024/1024)
                        }
                    ]
                })
            
            res.status = falcon.HTTP_200
            res.body = json.dumps({
                'data': data
            })
        # else:
        #     res.status = falcon.HTTP_500

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
        
