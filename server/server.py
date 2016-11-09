# system imports
import os
import cherrypy
import json

# local imports
from tool import SQLAlchemyTool
from plugin import SQLAlchemyPlugin
from base import Base
import models


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
            print json.dumps(curUser, indent=2)
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
        print b
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

    @cherrypy.expose
    def DatabaseUpdate(self, **kwargs):
        print "\n\nSomeone asked for a database update"
        rawData = cherrypy.request.body.read(int(cherrypy.request.headers['Content-Length']))
        b = json.loads(rawData)
        print "\nThis is what we recived in the request:\n{}".format(json.dumps(b, indent=2))
        results = self.db.query(models.User).filter(models.User.id == b['id'])
        print "\nresults = \n{}".format(results)
        # print "\nresults length: {}".format(len(results))
        for x in results:
            print "\nfor x: {}\n".format(x)
            foundUser = x

        print "\ntoDict method:\n{}".format(foundUser.toDict())
        res = foundUser.toDict()
        print "\nEnd of results\n"
        toReturn = {
            "isAdmin": res['isAdmin'],
            "FirstName": res['firstname'],
            "LastName": res['lastname'],
            "UserName": res['username'],
            "ChildName": res['childname'],
            "Phone": res['phone'],
            "Email": res['email'],
            "Address1": res['address1'],
            "Address2": res['address2']
        }
        if not res['isAdmin']:
            for date in res['attendenceHistory']:
                toReturn['AttendenceRecords'].append({
                    "DateIn": date['intime'],
                    "DateOut": date['outtime']
                })
            for payment in res['paymentHistory']:
                toReturn['PaymentRecords'].append({
                    "Date": payment['date'],
                    "Amount": date['amount']
                })
        else:
            allParents = self.db.query(models.User).filter(models.User.isAdmin == False)
            toReturn["parents"] = []
            for parent in allParents:
                toReturn['parents'].append(parent.toDict())
        print "and this is what we're returning:\n{}".format(json.dumps(toReturn, indent=2))
        return json.dumps(toReturn, indent=4)


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
