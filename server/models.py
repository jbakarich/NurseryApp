from sqlalchemy import Column, ForeignKey
from sqlalchemy.types import String, Integer, Date, Boolean, DateTime
from sqlalchemy.orm import relationship
from base import Base


class User(Base):

    __tablename__ = 'user'

    id = Column(Integer, primary_key=True)
    username = Column(String())
    pin = Column(String())
    isAdmin = Column(Boolean())

    firstname = Column(String())
    lastname = Column(String())
    childname = Column(String())
    phone = Column(Integer())
    address1 = Column(String())
    address2 = Column(String())
    email = Column(String())
    creationdate = Column(String())

    def toDict(self):
        data = {
            "id": self.id,
            "username": self.username,
            "pin": self.pin,
            "isAdmin": self.isAdmin,
            "firstname": self.firstname,
            "lastname": self.lastname,
            "childname": self.childname,
            "phone": self.phone,
            "address1": self.address1,
            "address2": self.address2,
            "email": self.email,
            "creationdate": self.creationdate
        }
        return data


class Payment(Base):

    __tablename__ = "payment"

    id = Column(Integer, primary_key=True)
    username = Column(String())
    userid = Column(Integer())
    amount = Column(Integer())
    date = Column(Date())
    isPaid = Column(Boolean())

    def toDict(self):
        data = {
            "id": self.id,
            "user": self.user,
            "user_id": self.user_id,
            "amount": self.amount,
            "date": self.date,
            "isPaid": self.isPaid
        }
        return data


class Attendance(Base):

    __tablename__ = "attendance"

    id = Column(Integer, primary_key=True)
    username = Column(String())
    userid = Column(String())

    date = Column(Integer())
    checkin = Column(Integer())
    checkout = Column(Integer())

    def toDict(self):
        data = {
            "id": self.id,
            "user": self.username,
            "userid": self.userid,
            "date": self.date,
            "checkin": self.checkin,
            "checkout": self.checkout
        }
        return data
