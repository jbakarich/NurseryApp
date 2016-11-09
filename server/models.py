from sqlalchemy import Column, ForeignKey
from sqlalchemy.types import String, Integer, Date, Boolean
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
    attendanceHistory = relationship("Attendance", back_populates="user")
    paymentHistory = relationship("Payment", back_populates="user")

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
            "email": self.email
        }
        data['attendenceRecords'] = []
        for x in self.attendanceHistory:
            data['attendenceRecords'].append(x.toDict())
        data['paymentRecords'] = []
        for x in self.paymentHistory:
            data['paymentRecords'].append(x.toDict())
        return data


class Payment(Base):

    __tablename__ = "payment"

    id = Column(Integer, primary_key=True)
    user = relationship("User", back_populates="paymentHistory")
    user_id = Column(Integer, ForeignKey('user.id'))

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
    user = relationship("User", back_populates="attendanceHistory")
    user_id = Column(Integer, ForeignKey('user.id'))

    date = Column(Date())
    attended = Column(Boolean())

    def toDict(self):
        data = {
            "id": self.id,
            "user": self.user,
            "user_id": self.user_id,
            "date": self.date,
            "attended": self.attended
        }
        return data
