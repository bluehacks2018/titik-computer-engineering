"""Example module for model definition"""
from sqlalchemy import Column, Integer, String
from .declarative_base import Base

class CourseItem(Base):
    __tablename__ = 'course_item'

    id = Column(Integer, primary_key=True)
    firebase_url = Column(String, unique=True)
    title = Column(String)
    language = Column(String)
    owner = Column(String)
    filesize = Column(String)
    category = Column(String)
    rating = Column(Integer, default=0)
    rating_weight = Column(Integer, default=0)

    # Languages:
    #   English
    #   Tagalog
    #   Cebuano

    def __repr__(self):
        return "<Course_Item(id='{}', firebase_url='{}', title='{}', language='{}', owner='{}', filesize='{}')>".format(
            self.id,
            self.firebase_url,
            self.title,
            self.language,
            self.owner,
            self.filesize
        )