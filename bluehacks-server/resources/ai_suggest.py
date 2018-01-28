import json
import falcon
import requests

from db import Rating, User, Playlist
from db import bucket

import pandas as pd

class SuggestResource(object):
    def on_get(self, req, res):
        user_email = req.params['user_email']
        data_arr = []
        for rating in self.session.query(Rating).all():
            row_data = []
            row_data.append(rating.user_email)
            row_data.append(rating.firebase_url)
            row_data.append(int(rating.rating))
            data_arr.append(row_data)

        df = pd.DataFrame(data_arr, columns=['user_email', 'firebase_url', 'rating'])
        # df = pd.read_csv('training_data.csv')
        userRatings = df.pivot_table(index=['user_email'],columns=['firebase_url'],values='rating')
        corrMatrix = userRatings.corr()

        myRatings = userRatings.loc[user_email].dropna()
        simCandidates = pd.Series()

        for i in range(0, len(myRatings.index)):
            # Retrieve similar movies to this one that I rated
            sims = corrMatrix[myRatings.index[i]].dropna()
            # Now scale its similarity by how well I rated this movie
            sims = sims.map(lambda x: x * myRatings[i])
            # Add the score to the list of similarity candidates
            simCandidates = simCandidates.append(sims)
            
        #Glance at our results so far:
        simCandidates.sort_values(inplace = True, ascending = False)
        simCandidates = simCandidates.groupby(simCandidates.index).sum()
        simCandidates.sort_values(inplace = True, ascending = False)
        filteredSims = simCandidates.drop(myRatings.index)

        suggestions = filteredSims.head().index.values.tolist()
        # res.status = falcon.HTTP_200
        # res.body = json.dumps({ 'suggestions': filteredSims.head().index.values.tolist() })

        user_course_items = self.session.query(Playlist).filter(Playlist.firebase_url.in_(list(suggestions))).all()
        user_course_items_others = self.session.query(Playlist).filter(Playlist.firebase_url.notin_(list(suggestions))).all()

        user_course_items.extend(user_course_items_others)

        data = []
        for playlist in user_course_items:
            if len(data) > 0:
                for i in range(0,len(data)):
                    entry = data[i]
                    print(entry)
                    if entry.get('name') == playlist.name and entry.get('owner') == playlist.user_email:
                        entry.get('firebase_data').append({
                            'url': playlist.firebase_url,
                            'metadata': bucket.get_blob(playlist.firebase_url).metadata,
                            'size': int(bucket.get_blob(playlist.firebase_url).size/1024/1024)
                        })
                    elif data.index(entry) == len(data)-1 and entry.get('name') != playlist.name:
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