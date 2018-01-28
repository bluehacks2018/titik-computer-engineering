"""Sample module for resources regarding [Sample]"""
import json
import falcon


from db import User

class LoginResource(object):
    def on_post(self, req, res):
        # Example x-www-form-urlencoded
        if req.stream:
            requestBody = json.load(req.stream)
        elif req.params:
            if req.params.email and req.params.password:
                requestBody = req.params
                
        if requestBody:
            email = requestBody.get('email')
            password = requestBody.get('password')

            user = self.session.query(User).filter_by(
                    email=email,
                    password=password
                ).first()

            if user:
                res.status = falcon.HTTP_200
                res.body = json.dumps({ 'user': user.email })
            else:
                res.status = falcon.HTTP_500
        else:
            res.status = falcon.HTTP_400
