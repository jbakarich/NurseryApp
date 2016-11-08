import os
import cherrypy
import json
import random
import string

from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, desc, ForeignKey
from sqlalchemy.types import String, Integer, Date, Boolean
from sqlalchemy.orm import relationship

from tool import SQLAlchemyTool
from plugin import SQLAlchemyPlugin


PATH = os.path.abspath(os.path.join(os.path.dirname(__file__)))
STATIC = os.path.join(PATH, 'static')
Base = declarative_base()


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
    isAdmin = Column(String())

    firstname = Column(String())
    lastname = Column(String())
    childname = Column(String())
    phone = Column(Integer())
    address = Column(String())
    email = Column(String())
    attendanceHistory = relationship("Attendance", back_populates="user")
    paymentHistory = relationship("Payment", back_populates="user")


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


class Root(object):

    @property
    def db(self):
        return cherrypy.request.db

    @cherrypy.expose
    def CheckLogin(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        results = self.db.query(User).filter(User.username == b['username']).filter(User.pin == int(b['password']))
        response = {}
        for x in results:
            if "name" not in results:
                print "nope"
                continue
            response = {
                "name": x['firstname']
            }
            if x['isAdmin']:
                response['type'] = "admin"
            else:
                response['type'] = "parent"
        if response is None:
            response = {
                "name": "invalid",
                "type": "invalid"
            }
        print json.dumps(response, indent=4)
        return json.dumps(response, indent=4)

    @cherrypy.expose
    def AddUser(self, **kwargs):
        print "we're here"
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        self.db.add(User(
            firstname=b['firstname'],
            lastname=b['lastname'],
            childname=b['childname'],
            username=b['username'],
            address=b['address'],
            phone=b['phone'],
            email=b['email'],
            pin=1234
        ))
        self.db.commit()
        print "We commited!"
        print b

    def CreateNewUser(self, **kwargs):
        self.db.add(Entry(firstName=kwargs['first_name'], lastName=kwargs['last_name'], age=kwargs['age']))
        self.db.commit()

    @cherrypy.expose
    def enter_name(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        self.db.add(User(username=b['username']))
        self.db.commit()
        return json.dumps({"message": "User created successfully!"}, indent=4)

    @cherrypy.expose
    def get_table(self, **kwargs):
        print "\n\n{}\n".format(kwargs)
        data = []
        if 'filterCount' in kwargs:
            results = self.db.query(Entry)
            for i in range(0, int(kwargs['filterCount'])):
                filterBy = kwargs['filters[{}][filterBy]'.format(i)]
                filterOf = kwargs['filters[{}][filterOf]'.format(i)]
                filterValue = kwargs['filters[{}][filterValue]'.format(i)]

                if filterOf == "firstName":
                    filterOf = Entry.firstName
                elif filterOf == "lastName":
                    filterOf = Entry.lastName
                else:
                    filterOf = Entry.age

                print "\nFiltering {}".format(filterOf)
                print "\nwith {}".format(filterBy)
                print "by {}\n".format(filterValue)
                if filterBy == "filtercontains":
                    print "\n{}\n".format(results)
                    results = results.filter(Entry.firstName.in_(filterValue))
                    print "\n{}\n".format(results)
                if filterBy == "filteris":
                    results.filter(filterOf == filterValue)
                if filterBy == "filterstartswith":
                    results.filter(filterOf.startsWith(filterValue))
                if filterBy == "filterendswith":
                    results.filter(filterOf.endsWitch(filterValue))
            for j in results:
                data.append({"firstName": j.firstName, "lastName": j.lastName, "age": j.age})
            return json.dumps(data, indent=4)

        elif 'sort' in kwargs:
            if kwargs['direction'] == 'decreasing':
                if kwargs['sort'] == 'firstName':
                    for entry in self.db.query(Entry).order_by(desc(Entry.firstName)).all():
                        data.append({"firstName": entry.firstName, "lastName": entry.lastName, "age": entry.age})
                elif kwargs['sort'] == 'lastName':
                    for entry in self.db.query(Entry).order_by(desc(Entry.lastName)).all():
                        data.append({"firstName": entry.firstName, "lastName": entry.lastName, "age": entry.age})
                else:
                    for entry in self.db.query(Entry).order_by(desc(Entry.age)).all():
                        data.append({"firstName": entry.firstName, "lastName": entry.lastName, "age": entry.age})
            else:
                if kwargs['sort'] == 'firstName':
                    for entry in self.db.query(Entry).order_by(Entry.firstName).all():
                        data.append({"firstName": entry.firstName, "lastName": entry.lastName, "age": entry.age})
                elif kwargs['sort'] == 'lastName':
                    for entry in self.db.query(Entry).order_by(Entry.lastName).all():
                        data.append({"firstName": entry.firstName, "lastName": entry.lastName, "age": entry.age})
                else:
                    for entry in self.db.query(Entry).order_by(Entry.age).all():
                        data.append({"firstName": entry.firstName, "lastName": entry.lastName, "age": entry.age})
            return json.dumps(data, indent=4)
        else:
            for entry in self.db.query(Entry).all():
                data.append({"firstName": entry.firstName, "lastName": entry.lastName, "age": entry.age})
        return json.dumps(data, indent=4)

    @cherrypy.expose
    def generate_random(self, **kwargs):
        for x in range(0, 50):
            self.db.add(Entry(
                firstName=''.join(random.choice(string.lowercase) for i in range(7)),
                lastName=''.join(random.choice(string.lowercase) for i in range(7)),
                age=random.randint(0, 100)
            ))
        self.db.commit()

    @cherrypy.expose
    def delete_entries(self, **kwargs):
        for i in self.db.query(Entry).all():
            self.db.delete(i)
        self.db.commit()


def get_cp_config():
    return {
        '/': {
            'tools.db.on': True,
            'tools.staticdir.on': True,
            'tools.staticdir.dir': STATIC,
            'tools.staticdir.index': 'index.html',
        },
    }


def runserver(config):
    cherrypy.tools.db = SQLAlchemyTool()
    cherrypy.tree.mount(Root(), '/', config)
    dbfile = os.path.join(PATH, 'log.db')
    if not os.path.exists(dbfile):
        open(dbfile, 'w+').close()

    sqlalchemy_plugin = SQLAlchemyPlugin(
        cherrypy.engine, Base, 'sqlite:///%s' % (dbfile),
        echo=True
    )
    sqlalchemy_plugin.subscribe()
    sqlalchemy_plugin.create()
    cherrypy.server.socket_host = "0.0.0.0"
    cherrypy.engine.start()
    cherrypy.engine.block()

if __name__ == "__main__":
    runserver(get_cp_config())
else:
    cherrypy.config.update({'environment': 'embedded'})
    application = cherrypy.Application(
        Root(), script_name=None, config=get_cp_config())
