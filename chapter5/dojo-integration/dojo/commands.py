# Here you can create play commands that are specific to the module, and extend existing commands

import urllib
import time
import tarfile
import os
import sys
import getopt
import shutil
import modulesrepo

MODULE = 'dojo'

# Commands that are specific to your module

COMMANDS = ['dojo:download', 'dojo:compile', 'dojo:copy', 'dojo:clean']

dojoVersion = "1.6.0"
dojoProfile = "play"

def execute(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")

    global dojoVersion
    global dojoProfile

    dojoVersion = app.readConf("dojo.version")

    try:
        optlist, args = getopt.getopt(args, '', ['version=', 'profile='])
        for o, a in optlist:
            if o in ('--version'):
                dojoVersion = a
            if o in ('--profile'):
                dojoProfile = a
    except getopt.GetoptError, err:
        print "~ %s" % str(err)
        print "~ "
        sys.exit(-1)

    if command == "dojo:download":
        dojoDownload()
    if command == "dojo:compile":
        dojoCompile()
    if command == "dojo:copy":
        dojoCopy()
    if command == "dojo:clean":
        dojoClean()

def getDirectory():
   global dojoVersion
   return "dojo-release-" + dojoVersion + "-src"

def getFile():
   return getDirectory() + ".zip"

def getUrl():
    global dojoVersion
    return "http://download.dojotoolkit.org/release-" + dojoVersion + "/" + getFile()

def dojoCompile():
    dir = "dojo/%s/util/buildscripts" % getDirectory()
    os.chdir(dir)
    os.chmod("build.sh", 0755)
    os.system("./build.sh profileFile=../../../../conf/dojo-profile-%s.js action=release" % dojoProfile)

def dojoClean():
    dir = "dojo/%s/util/buildscripts" % getDirectory()
    os.chdir(dir)
    os.system("./build.sh action=clean" % dojoProfile)

def dojoCopy():
    src = "dojo/%s/release/dojo/" % getDirectory()
    dst = "public/javascripts/dojo"
    print "Removing current dojo compiled code at %s" % dst
    shutil.rmtree(dst)
    print "Copying dojo %s over to public/ directory" % dojoVersion
    shutil.copytree(src, dst)

def dojoDownload():

    file = getFile()

    if not os.path.exists("dojo"):
        os.mkdir("dojo")

    if not os.path.exists("dojo/" + file):
        modulesrepo.Downloader().retrieve(getUrl(), "dojo/" + file)
    else:
        print "Archive already downloaded. Please delete to force new download or specifiy another version"

    if not os.path.exists("dojo/" + getDirectory()):
        print "Unpacking " + file + " into dojo/"
        modulesrepo.Unzip().extract("dojo/" + file, "dojo/")
    else:
        print "Archive already unpacked. Please delete to force new extraction"

