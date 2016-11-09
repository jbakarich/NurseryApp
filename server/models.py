from sqlalchemy import Column, ForeignKey
from sqlalchemy.types import String, Integer, Date, Boolean
from sqlalchemy.orm import relationship
from server import Base


class Entry(Base):

    __tablename__ = 'name'

    id = Column(Integer, primary_key=True)
    firstName = Column(String())
    lastName = Column(String())
    age = Column(String())


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
        return data


class Payment(Base):

    __tablename__ = "payment"

    id = Column(Integer, primary_key=True)
    user = relationship("User", back_populates="paymentHistory")
    user_id = Column(Integer, ForeignKey('user.id'))

    amount = Column(Integer())
    date = Column(Date())
    isPaid = Column(Boolean())


class Attendance(Base):

    __tablename__ = "attendance"

    id = Column(Integer, primary_key=True)
    user = relationship("User", back_populates="attendanceHistory")
    user_id = Column(Integer, ForeignKey('user.id'))

    date = Column(Date())
    attended = Column(Boolean())

