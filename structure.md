# Navigation Structure of Suplink

> This document will describe the structure of the app.

- <a href="#navOver" style="text-decoration:none">Overview</a>
- <a href="#navFrd" style="text-decoration:none">Friendlist</a>
- <a href="#navDis" style="text-decoration:none">Discover</a>
- <a href="#navMe" style="text-decoration:none">Me</a>



## <span id="navOver">Overview</span>

```mermaid
graph TD
start[Start] --> |Install app| launch(Launch App)
launch --> login(Log in)
login --> friendlist[Friend List]
login --> nav(Bottom Nav Bar)
nav -->friendlist
nav --> discover[Discover]
nav --> me[Me]
```



## <span id="navFrd">Friendlist</span>

```mermaid
graph TD
friends[Friend List] -->ntf(Notifications)
friends -->map(Map)
friends --> search(Search)
friends -->chat(Chat Page)

```

```mermaid
graph TD

chat --> |Friend send a message| getMessage(Get Message)
chat(Chat Page) --> |input text & click send| message(Send Text)
chat -->chatMenu{Options}
chatMenu --> pic{Options}
chatMenu --> location{Map2}
chatMenu --> profile{Profile}
pic --> camera(Camera)
pic --> album(Album)
camera -->send1(Send Pic)
album -->send1
location --> |send current location/search a place| send2(Send Location)
profile -->chat2(Chat Page)
profile -->post(Friend's All Posts)
```


```mermaid
graph TD
search[Search] --> email(By Email)
search --> phone(By Phone Number)
email --> |Input keyword & Pick User|profile(User Profile)
phone --> |Input keyword & Pick User|profile
```

```mermaid
graph TD
map(Map) --> |If No Location Service| turnOn(Turn on service)
map --> nearby(Search Nearby User)
map --> post(Nearby Post)
map --> site(Current Location)
map --> check(Check location)
map --> list(Location List)
map --> |if opened by message from chat| location(Show location)
location --> |Click the pin| add(Add to My Places)

nearby --> profile(User Profile)
post --> showPost(Post)
list --> showList(Checked points)
list --> showPlaces(Show My Places)
showPlaces --> showPlace(Guide to the place)


```

```mermaid
graph TD
profile(User Profile) --> post(All User's Post)
profile --> |If friends| chat(Chat Page)
profile --> |If not friends| request(Send Request)
profile --> |if have received a request| accept(Add as friends)
```

```mermaid
graph TD
ntf(Notifications) --> request(Friend Request)
request --> profile(Profile)
ntf --> message(Chat Message)
message --> chat(Chat Page)

```



## <span id="navDis">Discover</span>

```mermaid
graph TD
discover[Discover] --> |Choose a channel|channels(All Posts)
channels --> newPost(New Post)
channels --> |Press and hold|favorite(Add post to favorite)
newPost --> fill(Input text)
newPost --> |not required|pic(Insert pic)
newPost --> |not required|location(Add Location)
newPost --> |If no location service| service(Turn on service)
fill --> submit(Submit)
pic --> submit
location --> submit

channels --> |click on post|post(Post Content)
post --> like(Like the post)
post --> |If you liked the post before|dislike(Dislike)

```



## <span id="navMe">Me</span>

```mermaid
graph TD
me(Me) --> edit(Edit)
me --> options{Options}
options --> posts(All Posts)
options --> favorites(Favorite Post)
options --> logout(Log Out)
logout --> login(Login Page)
edit --> |satisfy format|fill(Fill in text information)
edit --> |not required|avator(Choose picture as Avatar)
fill --> save(Save)
avator --> save

```





<div style="text-align:right">
    <a href="#navOver" style="text-decoration:none"><button>Back to top</button></a>
</div>