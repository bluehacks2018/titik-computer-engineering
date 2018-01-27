"""Example module for model definition"""
from sqlalchemy import Column, Integer, String
from .declarative_base import Base

class User(Base):
    """Sample class model definition"""
    __tablename__ = 'user'

    id = Column(Integer, primary_key=True)
    name = Column(String)
    email = Column(String, unique=True)
    password = Column(String)

    def __repr__(self):
        return "<Sample(id='{}', name='{}', email='{}', password='{}')>".format(
            self.id,
            self.name,
            self.email,
            self.password
        )
        