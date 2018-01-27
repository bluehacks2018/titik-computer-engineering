"""Sample module for resources regarding [Sample]"""
import json
import falcon
import requests

from db import User

# Sample Resource
class UserResource(object):
    def on_get(self, req, res, user_email):
        ### Sample result configuration
        res.status = falcon.HTTP_200  # This is the default status
        user = self.session.query(User).filter_by(email=user_email).first()
        res.body = json.dumps({ 'user': user.email })
