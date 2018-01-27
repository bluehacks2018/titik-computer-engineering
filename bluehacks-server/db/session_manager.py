from sqlalchemy import create_engine
from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import scoped_session
from sqlalchemy.orm import sessionmaker

from .models import Base, Sample
### DB Initialization ###

db_info = {
    'engine': 'postgresql',
    'username': 'bluehacks',
    'password': 'password',
    'host': 'localhost',
    'port': '5433',
    'db_name': 'bluehacks_db'
}

engine = create_engine(
    '{engine}://{username}:{password}@{host}:{port}/{db_name}'.format(**db_info),
    echo=True
)

# Create Table
Base.metadata.create_all(engine)

### Start Session Manager ###
session_factory = sessionmaker(bind=engine)
Session = scoped_session(session_factory)

class SQLAlchemySessionManager(object):
    def __init__(self, Session):
        self.Session = Session

    def process_resource(self, req, resp, resource, params):
        resource.session = self.Session()

    def process_response(self, req, resp, resource, req_succeeded):
        if hasattr(resource, 'session'):
            if not req_succeeded:
                resource.session.rollback()
            Session.remove()