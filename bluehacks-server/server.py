"""Application module file"""
import falcon

from resources import RateResource, GetRateResource, SuggestResource, SampleResource,LoginResource, RegisterResource, UserResource, AddCourseItemResource, PlaylistResource, GetCourseItemResource
from db import SQLAlchemySessionManager, Session

# falcon.API instances are callable WSGI apps
APP = falcon.API(middleware=[
    SQLAlchemySessionManager(Session),
])

# Settings
APP.req_options.auto_parse_form_urlencoded = True # set x-www-form-urlencoded to be available via req.params

# Resources
LOGIN = LoginResource()
REGISTER = RegisterResource()
USER = UserResource()
COURSE_ITEM = AddCourseItemResource()
GET_COURSE_ITEM = GetCourseItemResource()
PLAYLIST = PlaylistResource()
SUGGEST = SuggestResource()
RATE = RateResource()
GET_RATE = GetRateResource()

# Routes
APP.add_route('/login', LOGIN)
APP.add_route('/register', REGISTER)
APP.add_route('/user/{user_email}', USER)
APP.add_route('/user', USER)
APP.add_route('/course_item/get', GET_COURSE_ITEM)
APP.add_route('/course_item', COURSE_ITEM)
APP.add_route('/playlist', PLAYLIST)
APP.add_route('/suggest', SUGGEST)
APP.add_route('/rate/get', GET_RATE)
APP.add_route('/rate', RATE)