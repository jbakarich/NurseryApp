
from sqlalchemy.sql import text
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

engine = create_engine("sqlite:///:memory:")
Base = declarative_base()


class Person(Base):

    def __init__(self, *args, **kwargs):
        super(Person, self).__init__(*args, **kwargs)

    __tablename__ = "person"
    id = Column(String(), primary_key=True)
    firstName = Column(String())
    lastName = Column(String())
    age = Column(Int())
    phoneNumber = Column(Int())
    address = Column(String())


class Payment(Base):

    def __init__(self, *args, **kwargs):
        super(Payment, self).__init__(*args, **kwargs)

    __tablename__ = "payment"
    id = Column(String(), primary_key=True)
    payee = Column(Person())
    paymentDate = Column(Date())
    paymentAmount = Column(Int())







class User
    foreignKey = pin
    pinkey



class pin
    Key
    password
