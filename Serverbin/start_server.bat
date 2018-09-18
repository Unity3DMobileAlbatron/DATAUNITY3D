del xdb\xdb.inuse
del xdb\trace.log

call replace.vbs
> config/role/serveridmap.data echo 0
java -Xdebug -Xrunjdwp:transport=dt_socket,address=1718,server=y,suspend=n -jar gsd.jar gsd.config.xml

pause