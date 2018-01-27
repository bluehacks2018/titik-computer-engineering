"""Example module for model definition"""
from sqlalchemy import Column, Integer, String
from .declarative_base import Base

class Playlist(Base):
    __tablename__ = 'playlist'
    id = Column(Integer, primary_key=True)
    name = Column(String)
    user_email = Column(String)
    firebase_url = Column(String)

    def __repr__(self):
        return "<Playlist(id='{}',  user_email='{}', firebase_url='{}')>".format(
            self.id,
            self.user_email,
            self.firebase_url
        )