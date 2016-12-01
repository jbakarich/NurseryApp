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
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        results = self.db.query(models.User).all()
        response = {}
        AdminExist = False
        for x in results:
            curUser = x.toDict()
            if curUser['isAdmin'] is True:
                AdminExist = True
            if(curUser['username'] == b['username'] and curUser['pin'] == b['password']):
                response = {
                    "name": curUser['username'],
                    "id": curUser['id']
                }
                if curUser['isAdmin']:
                    response['isAdmin'] = "True"
                else:
                    response['isAdmin'] = "False"
        if AdminExist is False:
            self.db.add(models.User(
                firstname="admin",
                isAdmin=True,
                lastname="admin",
                childname="admin",
                username='admin',
                address1='address1',
                address2='address2',
                phone=1234567890,
                email='email',
                pin=1234,
                creationdate=int(time.mktime(datetime.datetime.now().timetuple()))
            ))
        self.db.commit()
        if len(response) is 0:
            response = {
                "name": "invalid"
            }
        return json.dumps(response, indent=2)

    @cherrypy.expose
    def AddUser(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
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
            pin=1234,
            creationdate=int(time.mktime(datetime.datetime.now().timetuple()))
        ))
        self.db.commit()
        return json.dumps({"added": "Successful"}, indent=2)

    @cherrypy.expose
    def EditProfile(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        print "we got this:"
        print json.dumps(b, indent=2)
        allParents = self.db.query(models.User)
        for x in allParents:
            if x.toDict()['username'] == b['username']:
                if x.toDict()['pin'] != b['password']:
                    return json.dumps({"badpassword": "badpassword"}, indent=4)
                if b['firstname'] != "":
                    x.firstname = b['firstname']
                if b['lastname'] != "":
                    x.lastname = b['lastname']
                if b['childname'] != "":
                    x.childname = b['childname']
                if b['phone'] != "":
                    x.phone = b['phone']
                if b['address1'] != "":
                    x.address1 = b['address1']
                if b['address2'] != "":
                    x.address2 = b['address2']
                if b['email'] != "":
                    x.email = b['email']
                return json.dumps({"success": "success"}, indent=4)
        return json.dumps({"error": "error"}, indent=4)

    @cherrypy.expose
    def AddActivity(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        self.db.add(models.Activity(
            name=b['name'],
            time=b['time']
        ))
        self.db.commit()
        return json.dumps({"success": "success"}, indent=4)

    @cherrypy.expose
    def AdminHome(self, **kwargs):
        allParents = self.db.query(models.User)
        attendenceRecords = self.db.query(models.Attendance)
        toReturn = {
            "children": [],
        }
        for x in allParents:
            newuser = x.toDict()
            if newuser['isAdmin'] is True:
                continue
            newobj = {
                "username": newuser['username']
            }

            lastDate = 0
            for y in attendenceRecords:
                if y.toDict()['user'] == newuser['username']:
                    if lastDate < y.toDict()['date']:
                        lastDate = y.toDict()['date']
            newobj['lastcheckin'] = lastDate
            toReturn["children"].append(newobj)
        print json.dumps(toReturn, indent=4)
        return json.dumps(toReturn, indent=4)

    @cherrypy.expose
    def ParentHome(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        attendenceRecords = self.db.query(models.Attendance)
        activityRecords = self.db.query(models.Activity)
        lastCheckin = 0
        for x in attendenceRecords:
            if b['name'] == x.toDict()['user']:
                if lastCheckin < x.toDict()['checkout']:
                    lastCheckin = x.toDict()['checkout']
        latestActivity = "naptime"
        activityTime = 9999999999
        curTime = time.mktime(datetime.datetime.now().timetuple())
        print "\n\nstart activity loop:\n"
        for y in activityRecords:
            rec = y.toDict()
            print "checking activity:"
            print json.dumps(y.toDict())
            if rec['time'] > curTime and rec['time'] < activityTime:
                activityTime = rec['time']
                latestActivity = rec['name']

        toReturn = {
            "name": latestActivity,
            "time": activityTime,
            "lastcheckin": lastCheckin
        }
        print json.dumps(toReturn, indent=2)
        return json.dumps(toReturn, indent=4)

    @cherrypy.expose
    def RequestProfile(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        parents = self.db.query(models.User)
        newObj = {}
        for x in parents:
            if x.toDict()['username'] == b['name']:
                newObj = {
                    "user": x.toDict()
                }
                break
        if "user" not in newObj:
            return json.dumps({"error": "error"}, indent=4)
        attendance = self.db.query(models.Attendance)
        newObj['attendencerecords'] = []
        lastDate = 0
        for y in attendance:
            if y.toDict()['user'] == newObj['user']['username']:
                if lastDate < y.toDict()['date']:
                    lastDate = y.toDict()['date']
        newObj['lastcheckin'] = lastDate

        return json.dumps(newObj, indent=4)

    @cherrypy.expose
    def getPhone(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        parents = self.db.query(models.User)
        for x in parents:
            if x.toDict()['username'] == b['name']:
                return json.dumps({"phonenumber": x.toDict()['phone']}, indent=4)
        return json.dumps({"failure": "failure"}, indent=4)

    @cherrypy.expose
    def CheckIn(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        parents = self.db.query(models.User)
        for x in parents:
            if x.toDict()['username'] == b['name']:
                self.db.add(models.Attendance(
                    username=x.toDict()['username'],
                    userid=x.toDict()["id"],
                    date=int(time.mktime(datetime.datetime.now().timetuple())),
                    checkin=int(time.mktime(datetime.datetime.now().timetuple())),
                    checkout=0,
                    ischeckedin=True
                ))
                self.db.commit()
                return "success"
        return "error"

    @cherrypy.expose
    def CheckOut(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        parents = self.db.query(models.User)
        for x in parents:
            if x.toDict()['username'] == b['name']:
                parentId = x.toDict()['id']
        if parentId is None:
            return "error, parent not found"
        records = self.db.query(models.Attendance)
        for y in records:
            if y.toDict()['ischeckedin'] is True and y.toDict()['user'] == b['name']:
                y.checkout = int(time.mktime(datetime.datetime.now().timetuple()))
                y.ischeckedin = False
                return "Attendence logged"
        return "error finding record"

    @cherrypy.expose
    def PasswordChange(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        parents = self.db.query(models.User)
        for x in parents:
            if x.toDict()['username'] == b['name']:
                print "found correct parent"

                if x.toDict()['pin'] == b['oldpassword']:
                    x.pin = b['password']
                    return json.dumps({"success": "success"}, indent=2)
        return json.dumps({"failure": "failure"}, indent=2)

    @cherrypy.expose
    def GetAttendence(self, **kwargs):
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        dates = self.db.query(models.Attendance)
        datesArr = {
            "data": []
        }
        print "\n\nwe got:\n{}".format(b)
        for x in dates:
            print "comapring {} to {}".format(b['username'], x.toDict()['user'])
            if b["username"] == x.toDict()["user"]:
                print "here"
                datesArr['data'].append({
                    "checkinTime": x.toDict()['checkin'],
                    "checkoutTime": x.toDict()['checkout']
                })
        print "we are returning:"
        print json.dumps(datesArr, indent=4)
        return json.dumps(datesArr, indent=4)




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
