from sqlalchemy import create_engine, Column, Integer, String, Sequence
from models import Person


def addEntry(session, newEntry):
    # print("\nwere here:\n{}\n".format(newEntry))
    # should be newEntry.firstName amd newEntry.lastName
    newUser = Person(firstName='firstName', lastName='lastName')
    session.add(newUser)
    session.commit()
    # print "A user has been added"


def getPerson(session, name, isFirst):
    results = session.query(Person)
    return results.query(Person).filter(name == Person.firstName).first()

