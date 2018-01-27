import json
import falcon
import requests

from db import Rating, User
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

        print(filteredSims.head().index.values)
        res.status = falcon.HTTP_200
        res.body = json.dumps({ 'suggestions': filteredSims.head().index.values.tolist() })