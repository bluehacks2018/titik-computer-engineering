"""Example module for model definition"""
from sqlalchemy import Column, Integer, String
from .declarative_base import Base

class Sample(Base):
    """Sample class model definition"""
    __tablename__ = 'sample'
    id = Column(Integer, primary_key=True)
    name = Column(String)
    fullname = Column(String)
    password = Column(String)

    def __repr__(self):
        return "<Sample(id='{}', name='{}', fullname='{}', password='{}')>".format(
            self.id,
            self.name,
            self.fullname,
            self.password
        )
        