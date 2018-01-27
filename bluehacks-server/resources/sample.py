"""Sample module for resources regarding [Sample]"""
import json
import falcon

from db import Sample

# Sample Resource
class SampleResource(object):
    """Class definition, contains definition on request handling"""
    def on_get(self, req, res):
        # Test route

        ### External API Call Example
        # root = 'https://reqres.in'
        # route = '/api/users?page=2'

        # test_data = requests.get(root + route)
        # test_data = test_data.json()
        # res.body = json.dumps(test_data)
        
        ### Sample atabase add to table with model=Sample ###
        # sample_test = Sample(name='sample', fullname='Sample Name', password='samplepass')
        # self.session.add(sample_test)
        # self.session.commit()

        ### Sample result configuration
        res.status = falcon.HTTP_200  # This is the default status
        res.body = json.dumps({'status': 'finished'})

    def on_post(self, req, res):
        # Example x-www-form-urlencoded (requires app.req_options.auto_parse_form_urlencoded = True)
        # request_body = req.params
        # earthquakeData = req.get_param_as_list('earthquake_data')

        # Example raw/json
        # requestBody  = json.load(req.stream)
        # earthquakeData = requestBody.get('earthquake_data')

        json_data = {'sample_key': 'sample_value'}

        res.body = json.dumps(json_data)
        res.status = falcon.HTTP_200
