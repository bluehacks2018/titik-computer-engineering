import json
import falcon

from db import User

class RegisterResource(object):
    def on_post(self, req, res):
        # Example x-www-form-urlencoded
        if req.stream:
            requestBody = json.load(req.stream)
                
        if requestBody:
            name = requestBody.get('name')
            email = requestBody.get('email')
            password = requestBody.get('password')

            user = User(
                name=name, 
                email=email,
                password=password
            )
            
            self.session.add(user)
            self.session.commit()

            user = self.session.query(User).filter_by(email=email, password=password).first()

            if user:
                res.status = falcon.HTTP_200
                res.body = json.dumps({ 'user': user.email })
            else:
                res.status = falcon.HTTP_500
        else:
            res.status = falcon.HTTP_400
