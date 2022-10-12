'''plantuml

@startuml

hide circle
hide empty methods
hide empty attributes

' classes
class User{
name
bio
}

class ProfileDatabase{
}

class Profile{
email
password
dateCreated
}

class SongDatabase{
}


class Song{
id
name
artist
fileLocation
}

class Playlist{
duration
name
}

' associations
User "1" -down- "*" Playlist : \t created \t
User "1" -left- "*" Song : \t uploads \t\t
User "1" - "*" User : follows \t

ProfileDatabase "1" -down- "*" User : \t stores \t
ProfileDatabase "1" -left- "*" Profile : \t stores \t

Playlist "1" -up- "*" Song : contains \t

SongDatabase "1" - "*" Song : \t contains \t\t

@enduml


@startuml

actor User
participant loginPageView
control profileDatabase

@enduml

@startuml

class test{
}

class tester{
}

test "1" - "*" tester : \t contains \t\t


@enduml