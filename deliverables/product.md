# product

# Team21 - LotSpot!

#### Q1: What are you planning to build?

We are planning to build an android application that finds available public parking locations. There are two parts of the application that we are going to build, one side is the users
end, which will be an android app that displays a map with parking lots. The parking lot will be green, yellow or red indicating how many available parking spots are available.
The other side of the applications is a central server, the server will receive updated information from parking locations and update their local database so the users have 
real time accuracy of how many lots are available.

Concrete example of a common use case: User is on vacation with a rental car, unfamiliar with the area they do not want to waste time driving around searching for a spot,
they open the application and can see all available parking spots near their location.
On the other end, a new parking lot opens and they want to bring in new clients, rather on relying on people to visually see the new lot and post many signs they sign up with
the application and their lot becomes easy to locate for all users.



#### Q2: Who are your target users?

Our target users are drivers that are comming downtown for sort of event. These people are trying to find parking during high traffic and finding a spot can seriously 
become an issue. Additionaly, events are not exclusive to large public events. A regular downtown friday is busy enough to cause issues for club goers trying to find parking.
Our personas provide better clarity for what kind of demographic we are trying to hit, however, they are all commuters trying to find parking during a busy event day.

// ** TODO ADD PERONA'S **

#### Q3: Why would your users choose your product? What are they using today to solve their problem/need?

Our product incorporates an element not seen by other similar applications/tools. It shows the number of lots available. This will not be available for every lot as 
the parking garage themselves must integrate some system of keeping track of available lots, in that limitation it will function just as well as other parking garage
finding applications. This also allows users to make more informed decisions, with information not readily available by other parking applications.
Some similar products people use today to find parking are [ParkMe Parking](https://itunes.apple.com/ca/app/parkme-parking/id417605484?mt=8), 
which is an application on the app store that helps you locate parking garages, and also be able to reserve spots. Another similar webb application [Parkopedia](http://en.parkopedia.ca/parking/locations/toronto_on_canada_dpz83dffmxp/?arriving=201702061230&leaving=201702061430),
is a web application that just finds parking garages and prices. Our application will have similar functionality as there will be a map interface with pinned lots, however
our applications will show the number of available spots to save the user's time and money, they do not have to go to a garage to find that there are no free lots.

Parking lot owners would have a large incentive to attract customers. Our application will advertise their location with an easy to find interface. As opposed to making signs
to show drivers their parking garage/lot. 

----

### Highlights

During our team meetings we considered several alternatives, including:

- Parking share application that allowed users to share their parking spot with others.
	- We decided not to go this route after speaking to our TA about the idea in Lab. They mentioned it was not an original concept, and that we should try to avoid sharing type of applications, the market is oversaturated. From this idea though we focused our sights on parking garages.

- Application to help those with ALS to navigate their phones with an eye tracker.
	- Idea was scrapped because we were not familiar enough with the hardware and tools that would be involved in making the application.

- Book Sharing application that keeps track of books users have shared with one another as well as a community aspect that has groups of users sharing books with each other at random.
	- We chose not to go with idea because most of the app would be CRUD (Create, Remove, Update, Delete).

After our second meeting during our lab we bounced our ideas off the TA he gave us some feedback and from his feedback that the scope of our application was good, that we would be
allowed to emulate car lots for the purpose of showcasing the application and that it was original. We chose to go with our parking lot idea idea. We also think it solves a
problem that several of us have experienced. The problem of finding a parking lot that is not full during peak hours or when there are events going about such as a Blue Jay's game.
It saves the user's time, it allows the user to discover new information compared to similar applications on the market that allows users to make a more informed decision, 
and fulfills a useful common purpose.

#### MVP features

Phone Application:

- map
	- google map
	- search addresses and location
- View all parking lots as pins on a map.
	- color code pins (Green = lot has an available parking spot, Red = no free parking spots).
- Able to select a parking spot to get additional information (symbols used for each attribute).
	- Address
	- Rates (cost)
	- Spots remaining
- Search
	- using google map

Server Side:

- Parking lot side application
	- keep track of the number of available lots
	- send update to server whenever there is a change
	- get information from sensors


Additional Features (done after meeting MVP features):

- Search
	- filter parking lots by price
	- filter parking lots by additional information (such as height limit, accessibility ect)

- Recomended Lot
	- input what hours you wish to park your car and recieve the best fitting parking lots (on compatibility)

- Additional Parking Lot Informaiton
	- Add information to parking locations that can be displayed and searched on
	- Accessibility
	- Height limit
	- Overnight parking
	- Hours of operation


#### Application Mockup:

Main
![alt tag](https://github.com/csc301-winter-2017/project-team-21/blob/master/deliverables/main.jpg)

Navigation
![alt tag](https://github.com/csc301-winter-2017/project-team-21/blob/master/deliverables/navigation.jpg)

Mini Info
![alt tag](https://github.com/csc301-winter-2017/project-team-21/blob/master/deliverables/miniInfo.jpg)

Details
![alt tag](https://github.com/csc301-winter-2017/project-team-21/blob/master/deliverables/details.jpg)



