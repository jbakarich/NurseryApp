# system imports
import os
import cherrypy
import json

# local imports
from tool import SQLAlchemyTool
from plugin import SQLAlchemyPlugin
from base import Base
import models
import datetime
import time


PATH = os.path.abspath(os.path.join(os.path.dirname(__file__)))
STATIC = os.path.join(PATH, 'static')


class Root(object):

    @property
    def db(self):
        return cherrypy.request.db

    @cherrypy.expose
    def CheckLogin(self, **kwargs):
        print "\n\nstarting login"
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        results = self.db.query(models.User).all()
        response = {}
        for x in results:
            curUser = x.toDict()
            if(curUser['username'] == b['username'] and curUser['pin'] == b['password']):
                response = {
                    "name": curUser['firstname'],
                    "id": curUser['id']
                }
                if curUser['isAdmin']:
                    response['isAdmin'] = "True"
                else:
                    response['isAdmin'] = "False"
        if len(response) is 0:
            response = {
                "name": "invalid"
            }
        print("Returning login:")
        print json.dumps(response, indent=4)
        print "\n\n"
        return json.dumps(response, indent=2)

    @cherrypy.expose
    def AddUser(self, **kwargs):
        print "adding user"
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        # print b
        b['phone'] = int(b['phone'])
        if b['isAdmin'] == "True":
            isAdmin = True
        else:
            isAdmin = False
        self.db.add(models.User(
            firstname=b['firstname'],
            isAdmin=isAdmin,
            lastname=b['lastname'],
            childname=b['childname'],
            username=b['username'],
            address1=b['address1'],
            address2=b['address2'],
            phone=b['phone'],
            email=b['email'],
            pin=1234
        ))
        self.db.commit()
        print "We commited!\n"
        return json.dumps({"added": "Successful"}, indent=2)

    @cherrypy.expose
    def AdminHome(self, **kwargs):
        print "\n\nGetting admin home."

        allParents = self.db.query(models.User)

        toReturn = {
            "children": [],
        }
        for x in allParents:
            print "newuser"
            newuser = x.toDict()
            if newuser['isAdmin'] is True:
                continue
            newobj = {
                "username": newuser['username']
            }

            attendenceRecords = self.db.query(models.Attendance)
            lastDate = 0
            for y in attendenceRecords:
                print "were attendence"
                lastDate = 0
                print y.toDict()
                print newuser['username']
                if y.toDict()['user'] == newuser['username']:
                    print y.toDict()['date']
                    if lastDate < y.toDict()['date']:
                        lastDate = y.toDict()['date']
            newobj['lastcheckin'] = lastDate

            toReturn["children"].append(newobj)

        print "and this is what we're returning:\n{}".format(json.dumps(toReturn, indent=2))
        return json.dumps(toReturn, indent=4)

    @cherrypy.expose
    def RequestProfile(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        parents = self.db.query(models.User)
        for x in parents:
            print "\nTHIS:\n"
            print x.toDict()
            if x.toDict()['username'] == b['name']:
                print "\n\nFOUND HIM\n\n"
                parent = x
                break
        return json.dumps(parent.toDict(), indent=4)

    @cherrypy.expose
    def CheckIn(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        print "\nUser checking in: "
        print b
        parents = self.db.query(models.User)
        for x in parents:
            if x.toDict()['username'] == b['name']:
                print "\n\nthere:"
                print time.mktime(datetime.datetime.now().timetuple())
                self.db.add(models.Attendance(
                    username=x.toDict()['username'],
                    userid=x.toDict()["id"],
                    date=int(time.mktime(datetime.datetime.now().timetuple())),
                    checkin=int(time.mktime(datetime.datetime.now().timetuple())),
                    checkout=0
                ))
                self.db.commit()
                print "success"
                return "success"
        print "Error"
        return "error"

    @cherrypy.expose
    def CheckOut(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        print "\nUser checking out: "
        print b
        parents = self.db.query(models.User)
        for x in parents:
            if x.toDict()['username'] == b['name']:
                parentId = x.toDict()['id']
        if parentId is None:
            return "error, parent not found"
        records = self.db.query(models.Attendance)
        for y in records:
            if y.toDict()['userid'] == parentId:
                y.CheckOut = datetime.time()
                print "Attendence logged"
                return "Attendence logged"
        print "error finding record"
        return "error finding record"


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
