"""Example module for model definition"""
from sqlalchemy import Column, Integer, String
from .declarative_base import Base

class Rating(Base):
    __tablename__ = 'rating'
    id = Column(Integer, primary_key=True)
    user_email = Column(String)
    firebase_url = Column(String)
    rating = Column(String)

    # Languages:
    #   English
    #   Tagalog
    #   Cebuano

    def __repr__(self):
        return "<Course_Item(id='{}',  user_email='{}', firebase_url='{}')>".format(
            self.id,
            self.user_email,
            self.firebase_url
        )